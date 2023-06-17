package com.business.market.simulator.finance.simulation;

import com.business.market.simulator.finance.FinanceService;
import com.business.market.simulator.finance.instrument.FinanceOperationException;
import com.business.market.simulator.finance.instrument.active.ActiveInstrument;
import com.business.market.simulator.finance.instrument.active.ActiveInstrumentService;
import com.business.market.simulator.finance.instrument.active.type.Share;
import com.business.market.simulator.finance.instrument.active.type.TreasuryBond;
import com.business.market.simulator.finance.instrument.aspect.Tradeable;
import com.business.market.simulator.user.User;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.business.market.simulator.finance.simulation.MarketSimulationService.increaseTimestampByMinutes;
import static java.lang.Thread.sleep;

@Service
public class MarketAlgorithmService {
    protected final static int minutesPerMarketRound = 10;
    private final static int actionsPerRound = 10;

    private final static double[] treasuryBondOperationChances = new double[]{0.1, 0.4};
    private final static Random random = new Random();
    private final static double[] shareSellFactors = new double[]{0.7, 1.4};
    private final static double priceIncreaseFactor = 1.5;
    @Getter
    @Setter
    private static double simulationsSpeed = 1.0;
    @Setter(onMethod_ = @Autowired)
    private FinanceService financeService;
    @Setter(onMethod_ = @Autowired)
    private ActiveInstrumentService activeInstrumentService;

    private int generateRandomInt(int min, int max) {
        return random.nextInt(min, max + 1);
    }

    private double generateRandomDouble(double min, double max) {
        return random.nextDouble(min, max + 0.01);
    }

    public Map<User, Integer> createUserOperations(Collection<User> users) throws SimulationException {
        int usersSize = users.size();
        if (usersSize < 1) {
            throw new SimulationException("Error while loading simulation users");
        }
        Map<User, Integer> usersOperations = new HashMap<>(users.size());
        for (User user : users) {
            usersOperations.put(user, actionsPerRound);
        }
        return usersOperations;
    }

    private Map<String, List<Share>> getSharesGrouped(Collection<ActiveInstrument> activeInstruments) {
        List<Share> shares = activeInstruments.stream().filter(activeInstrument -> activeInstrument instanceof Share).map(activeInstrument -> (Share) activeInstrument).toList();
        return getActiveInstrumentsGroupedBySymbol(shares);
    }

    private Map<String, List<TreasuryBond>> getTreasuryBondGrouped(Collection<ActiveInstrument> activeInstruments) {
        List<TreasuryBond> treasuryBonds = activeInstruments.stream().filter(activeInstrument -> activeInstrument instanceof TreasuryBond).map(activeInstrument -> (TreasuryBond) activeInstrument).toList();
        return getActiveInstrumentsGroupedBySymbol(treasuryBonds);
    }

    private <T extends ActiveInstrument> Map<String, List<T>> getActiveInstrumentsGroupedBySymbol(Collection<T> activeInstruments) {
        return activeInstruments.stream().collect(Collectors.groupingBy(activeInstrument -> activeInstrument.getFinancialInstrument().getSymbol()));
    }

    private void performHoldAction(User selectedUser, Map<User, Integer> usersOperations) {
        int remainingOperations = usersOperations.get(selectedUser);
        usersOperations.put(selectedUser, --remainingOperations);
    }

    private void performSellActions(User selectedUser, Collection<ActiveInstrument> userActiveInstruments, Map<User, Integer> usersOperations) {
        int remainingOperations = usersOperations.get(selectedUser);
        double activeInstrumentsChoose = random.nextDouble();
        PriorityQueue<ActiveInstrument> instrumentsQueue = new PriorityQueue<>(Tradeable::compareTo);
        if (activeInstrumentsChoose < treasuryBondOperationChances[0]) {
            Map<String, List<TreasuryBond>> treasuryBondGrouped = getTreasuryBondGrouped(userActiveInstruments);
            if (!treasuryBondGrouped.isEmpty()) {
                String[] bondKeys = treasuryBondGrouped.keySet().toArray(new String[0]);
                int chosenInstrumentIndex = random.nextInt(bondKeys.length);
                instrumentsQueue.addAll(treasuryBondGrouped.get(bondKeys[chosenInstrumentIndex]));
            }
        } else {
            Map<String, List<Share>> sharesGrouped = getSharesGrouped(userActiveInstruments);
            if (!sharesGrouped.isEmpty()){
                String[] shareKeys = sharesGrouped.keySet().toArray(new String[0]);
                int chosenInstrumentIndex = random.nextInt(shareKeys.length);
                instrumentsQueue.addAll(sharesGrouped.get(shareKeys[chosenInstrumentIndex]));
            }
        }
        while (instrumentsQueue.peek() != null && remainingOperations > 0) {
            try {
                ActiveInstrument instrumentToSell = instrumentsQueue.poll();
                if (instrumentToSell instanceof Share) {
                    financeService.sellInstrument(instrumentToSell, selectedUser, ((Share) instrumentToSell).getAskPrice().multiply(BigDecimal.valueOf(generateRandomDouble(shareSellFactors[0], shareSellFactors[1]))));
                } else if (instrumentToSell instanceof TreasuryBond) {
                    financeService.sellInstrument(instrumentToSell, selectedUser);
                }
                --remainingOperations;
            } catch (FinanceOperationException financeOperationException) {
                System.err.println(financeOperationException.getMessage());
            }
        }
        usersOperations.put(selectedUser, --remainingOperations);

    }

    private void performBuyActions(User selectedUser, Collection<ActiveInstrument> buyableInstruments, Map<User, Integer> usersOperations) {
        double activeInstrumentsChoose = random.nextDouble();
        int remainingOperations = usersOperations.get(selectedUser);
        PriorityQueue<ActiveInstrument> instrumentsQueue = new PriorityQueue<>(Tradeable::compareTo);

        if (activeInstrumentsChoose < treasuryBondOperationChances[1]) {
            Map<String, List<TreasuryBond>> treasuryBondGrouped = getTreasuryBondGrouped(buyableInstruments);
            if (!treasuryBondGrouped.isEmpty()){
                String[] bondKeys = treasuryBondGrouped.keySet().toArray(new String[0]);
                int chosenInstrumentIndex = random.nextInt(bondKeys.length);
                instrumentsQueue.addAll(treasuryBondGrouped.get(bondKeys[chosenInstrumentIndex]));
            }
        } else {
            Map<String, List<Share>> sharesGrouped = getSharesGrouped(buyableInstruments);
            if (!sharesGrouped.isEmpty()){
                String[] shareKeys = sharesGrouped.keySet().toArray(new String[0]);
                int chosenInstrumentIndex = random.nextInt(shareKeys.length);
                instrumentsQueue.addAll(sharesGrouped.get(shareKeys[chosenInstrumentIndex]));
            }
        }
        while (instrumentsQueue.peek() != null && remainingOperations > 0) {
            try {
                ActiveInstrument instrumentToBuy = instrumentsQueue.poll();
                if (instrumentToBuy instanceof Share) {
                    financeService.buyInstrument(instrumentToBuy, selectedUser, priceIncreaseFactor);
                } else if (instrumentToBuy instanceof TreasuryBond) {
                    financeService.buyInstrument(instrumentToBuy, selectedUser);
                }
                --remainingOperations;
            } catch (FinanceOperationException financeOperationException) {
                System.err.println(financeOperationException.getMessage());
            }
        }
        usersOperations.put(selectedUser, remainingOperations);
    }

    @Transactional
    public void performTradersActions(Map<User, Integer> usersOperations) {
        while (!usersOperations.isEmpty()) {
            User[] userKeys = usersOperations.keySet().toArray(new User[0]);
            User selectedUser = userKeys[random.nextInt(userKeys.length)];
            int remainingOperations = usersOperations.get(selectedUser);

            double action = random.nextDouble();
            MarketAlgorithmService.MarketStrategy randomMarketStrategy = new MarketAlgorithmService.MarketStrategy() {
                @Override
                public MarketOperationChances getChances(Random random) {
                    double sellChance = random.nextDouble();
                    double buyChance = random.nextDouble(sellChance + 0.01, 1.01);
                    return new MarketOperationChances(sellChance, buyChance);
                }
            };

            MarketAlgorithmService.MarketStrategy.MarketOperationChances marketOperationChances = randomMarketStrategy.getChances(random);
            if (marketOperationChances.sellChance() <= action && marketOperationChances.buyChance() <= action) {
                performHoldAction(selectedUser, usersOperations);
            } else {
                List<ActiveInstrument> userActiveInstruments = selectedUser.getOwnedInstruments();
                if (!userActiveInstruments.isEmpty() && action < marketOperationChances.sellChance()) {
                    performSellActions(selectedUser, userActiveInstruments, usersOperations);
                } else if (action < marketOperationChances.buyChance()) {
                    List<ActiveInstrument> buyableInstruments = activeInstrumentService.getInstrumentsBuyableByUser(selectedUser.getUserId());
                    performBuyActions(selectedUser, buyableInstruments, usersOperations);
                }
            }
            if (remainingOperations == 0) {
                usersOperations.remove(selectedUser);
            }
        }
    }

    public void updateMarket() {
        increaseTimestampByMinutes(minutesPerMarketRound);
        try {
            sleep((long) (5000 / simulationsSpeed));
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
            throw new RuntimeException(new SimulationException("Unexpected simulation interruption"));
        }
    }

    public void forwardMarketClosedTime() {
        increaseTimestampByMinutes(minutesPerMarketRound * 2);
    }

    interface MarketStrategy {
        MarketOperationChances getChances(Random random);

        record MarketOperationChances(double sellChance, double buyChance) { }
    }
}
