package com.business.market.simulator.finance;

import com.business.market.simulator.finance.instrument.FinanceOperationException;
import com.business.market.simulator.finance.instrument.active.ActiveInstrument;
import com.business.market.simulator.finance.instrument.active.ActiveInstrumentService;
import com.business.market.simulator.finance.instrument.active.type.Share;
import com.business.market.simulator.finance.instrument.active.type.TreasuryBond;
import com.business.market.simulator.finance.instrument.aspect.Tradeable;
import com.business.market.simulator.finance.instrument.derivative.Derivative;
import com.business.market.simulator.finance.instrument.derivative.DerivativeRepository;
import com.business.market.simulator.finance.instrument.derivative.type.Index;
import com.business.market.simulator.finance.simulation.MarketSimulationService;
import com.business.market.simulator.user.User;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Service
@Setter(onMethod_ = {@Autowired})
public class FinanceService {

    private ActiveInstrumentService activeInstrumentService;
    private IndexHelper indexHelper;

    public BigDecimal getMarketValue(Tradeable tradeable) {
        if (tradeable instanceof ActiveInstrument) {
            switch (((ActiveInstrument) tradeable).getType()) {
                case SHARE -> {
                    return activeInstrumentService.getLowestAskPrice((Share) tradeable);
                }
                case TREASURY_BOND -> {
                    return activeInstrumentService.getTreasuryBondValue((TreasuryBond) tradeable);
                }
                default -> {
                    return BigDecimal.ZERO;
                }
            }
        } else if (tradeable instanceof Derivative) {
            switch (((Derivative) tradeable).getDerivativeType()) {
                case INDEX -> {
                    return indexHelper.getIndexValue((Index) tradeable);
                }
                default -> {
                    return BigDecimal.ZERO;
                }
            }
        } else {
            return BigDecimal.ZERO;
        }
    }

    public Tradeable sellInstrument(Tradeable tradeable, User user) throws FinanceOperationException {
        return sellInstrument(tradeable, user, BigDecimal.ONE, MarketSimulationService.getCurrentSimulationTimestamp());
    }

    public Tradeable sellInstrument(Tradeable tradeable, User user, BigDecimal price) throws FinanceOperationException {
        return sellInstrument(tradeable, user, price, MarketSimulationService.getCurrentSimulationTimestamp());
    }

    public Tradeable sellInstrument(Tradeable tradeable, User user, BigDecimal price, Timestamp currentTimestamp) throws FinanceOperationException {
        if (!MarketSimulationService.isMarketOpen(currentTimestamp)) {
            throw new FinanceOperationException("Market is closed");
        }
        if (tradeable instanceof ActiveInstrument) {
            switch (((ActiveInstrument) tradeable).getType()) {
                case SHARE -> {
                    return activeInstrumentService.sellShare((Share) tradeable, user, price);
                }
                case TREASURY_BOND -> {
                    return activeInstrumentService.sellTreasuryBond((TreasuryBond) tradeable, user, currentTimestamp);
                }
                default -> {
                    return null;
                }
            }
        } else if (tradeable instanceof Derivative) {
            switch (((Derivative) tradeable).getDerivativeType()) {
                case INDEX -> {
                    return null;
                }
                default -> {
                    return null;
                }
            }
        } else {
            return null;
        }
    }

    public Tradeable buyInstrument(Tradeable tradeable, User user) throws FinanceOperationException {
        return buyInstrument(tradeable, user, 1.0);
    }

    public Tradeable buyInstrument(Tradeable tradeable, User user, double priceIncreaseFactor) throws FinanceOperationException {
        return buyInstrument(tradeable, user, priceIncreaseFactor, MarketSimulationService.getCurrentSimulationTimestamp());
    }

    public Tradeable buyInstrument(Tradeable tradeable, User user, double priceIncreaseFactor, Timestamp currentTimestamp) throws FinanceOperationException {
        if (!MarketSimulationService.isMarketOpen(currentTimestamp)) {
            throw new FinanceOperationException("Market is closed");
        }
        if (tradeable instanceof ActiveInstrument) {
            switch (((ActiveInstrument) tradeable).getType()) {
                case SHARE -> {
                    return activeInstrumentService.buyShare((Share) tradeable, user, currentTimestamp, priceIncreaseFactor);
                }
                case TREASURY_BOND -> {
                    return activeInstrumentService.buyTreasuryBond((TreasuryBond) tradeable, user, currentTimestamp);
                }
                default -> {
                    return null;
                }
            }
        } else if (tradeable instanceof Derivative) {
            switch (((Derivative) tradeable).getDerivativeType()) {
                case INDEX -> {
                    return null;
                }
                default -> {
                    return null;
                }
            }
        } else {
            return null;
        }
    }


    @Component
    static class IndexHelper {
        private final DerivativeRepository derivativeRepository;

        @Autowired
        public IndexHelper(DerivativeRepository derivativeRepository) {
            this.derivativeRepository = derivativeRepository;
        }

        //TODO implement this
        public BigDecimal getIndexValue(Index index) {
            return BigDecimal.ZERO;
        }
    }

    @Component
    static class FundHelper {

    }
}
