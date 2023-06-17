package com.business.market.simulator.finance.instrument.active;

import com.business.market.simulator.finance.instrument.FinanceOperationException;
import com.business.market.simulator.finance.instrument.FinancialInstrument;
import com.business.market.simulator.finance.instrument.InstrumentType;
import com.business.market.simulator.finance.instrument.active.type.Share;
import com.business.market.simulator.finance.instrument.active.type.TreasuryBond;
import com.business.market.simulator.finance.simulation.MarketSimulationService;
import com.business.market.simulator.finance.transaction.MarketTransactionService;
import com.business.market.simulator.finance.transaction.entity.LegalEntity;
import com.business.market.simulator.user.User;
import jakarta.transaction.Transactional;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Service
@Setter(onMethod_ = {@Autowired})
public class ActiveInstrumentService {
    private ActiveInstrumentRepository activeInstrumentRepository;
    private MarketTransactionService marketTransactionService;

    public List<ActiveInstrument> getActiveInstruments() {
        return activeInstrumentRepository.findAll();
    }

    @Transactional
    public TreasuryBond createTreasuryBond(FinancialInstrument financialInstrument, BigDecimal initialContractValue, double interest, int termInMonths) throws ActiveInstrumentException {
        if (!financialInstrument.getType().equals(InstrumentType.TREASURY_BOND)) {
            throw new ActiveInstrumentException("Instrument is not a treasury bond type");
        }
        TreasuryBond treasuryBond = new TreasuryBond();
        treasuryBond.setInitialContractValue(initialContractValue);
        treasuryBond.setOverallInterest(interest);
        treasuryBond.setTermInMonths(termInMonths);
        financialInstrument.addActiveInstrument(treasuryBond);
        return activeInstrumentRepository.save(treasuryBond);
    }

    public List<TreasuryBond> createTreasuryBonds(FinancialInstrument financialInstrument, BigDecimal initialContractValue, double interest, int termInMonths, int quantity) throws ActiveInstrumentException {
        List<TreasuryBond> treasuryBonds = new ArrayList<>();
        for (int i = 0; i < quantity; i++) {
            treasuryBonds.add(createTreasuryBond(financialInstrument, initialContractValue,  interest, termInMonths));
        }
        return treasuryBonds;
    }

    @Transactional
    public Share createShare(FinancialInstrument financialInstrument, BigDecimal initialPrice) throws ActiveInstrumentException {
        if (!financialInstrument.getType().equals(InstrumentType.SHARE)) {
            throw new ActiveInstrumentException("Instrument is not a share type");
        }
        Share share = new Share();
        share.setAskPrice(initialPrice);
        financialInstrument.addActiveInstrument(share);
        return activeInstrumentRepository.save(share);
    }

    public List<Share> createShares(FinancialInstrument financialInstrument, BigDecimal initialPrice, int quantity) throws ActiveInstrumentException {
        List<Share> shares = new ArrayList<>();
        for (int i = 0; i < quantity; i++) {
            shares.add(createShare(financialInstrument, initialPrice));
        }
        return shares;
    }

    public List<ActiveInstrument> updateActiveInstruments(Collection<ActiveInstrument> activeInstruments) {
        return activeInstrumentRepository.saveAllAndFlush(activeInstruments);
    }

    public List<ActiveInstrument> getUsersActiveInstruments(Collection<Long> userIds) {
        return activeInstrumentRepository.findAllByCurrentInstrumentOwnerIdIn(userIds);
    }


    public List<ActiveInstrument> getUserActiveInstruments(Long userId) {
        return activeInstrumentRepository.findAllByCurrentInstrumentOwner(userId);
    }

    public List<ActiveInstrument> getInstrumentsBuyableByUser(Long userId) {
        return activeInstrumentRepository.findAllBuyableInstrumentsByUser(userId);
    }

    public BigDecimal getLowestAskPrice(Share share) {
        return activeInstrumentRepository.getMinAskPriceByFinancialInstrumentId(share.getFinancialInstrument().getInstrumentId());
    }
    public Share getShareToBuyByUserBySymbol(String symbol, User user){
        return activeInstrumentRepository.findLowestPriceShareBySymbolAndCurrentInstrumentOwnerIdNotEquals(symbol, user.getUserId());
    }
    public TreasuryBond getTreasuryBondToBuyByUserBySymbol(String symbol){
        return activeInstrumentRepository.findNotSoldTreasuryBondBySymbol(symbol);
    }

    @Transactional
    public Share buyShare(Share share, User buyer, Timestamp transactionTimestamp, double priceIncreaseFactor) throws FinanceOperationException {
        LegalEntity sellerEntity;
        User shareOwner = share.getCurrentInstrumentOwner();
        User.UserOperations buyerOperations = buyer.new UserOperations();
        BigDecimal askPrice = share.getAskPrice();
        if (buyerOperations.canWithdraw(askPrice)) {
            throw new FinanceOperationException("Balance too low for operation");
        }
        if (Objects.isNull(shareOwner)) {
            sellerEntity = share.getFinancialInstrument().getOwningCompany();
        } else {
            sellerEntity = shareOwner;
            User.UserOperations currentOwnerOperations = shareOwner.new UserOperations();
            currentOwnerOperations.addToBalance(askPrice);
        }
        buyerOperations.withdrawBalance(askPrice);
        share.changeCurrentInstrumentOwner(buyer);
        share.setAskPrice(askPrice.multiply(BigDecimal.valueOf(priceIncreaseFactor)));
        marketTransactionService.createMarketTransaction(share, transactionTimestamp, buyer, sellerEntity, askPrice);
        return activeInstrumentRepository.save(share);
    }

    @Transactional
    public Share sellShare(Share share, User user, BigDecimal price) throws FinanceOperationException {
        if (!share.getCurrentInstrumentOwner().equalsUserId(user)) {
            throw new FinanceOperationException("Invalid share owner");
        }
        if (Objects.isNull(price) || price.signum() <= 0) {
            throw new FinanceOperationException("Invalid share price");
        }
        share.setAskPrice(price);
        return activeInstrumentRepository.save(share);
    }

    @Transactional
    public TreasuryBond buyTreasuryBond(TreasuryBond treasuryBond, User buyer, Timestamp transactionTimestamp) throws FinanceOperationException {
        if (treasuryBond.isBuyable()) {
            throw new FinanceOperationException("Can't buy already contracted treasury bond");
        }
        User.UserOperations buyerOperations = buyer.new UserOperations();
        BigDecimal initialContractValue = treasuryBond.getInitialContractValue();
        if (!buyerOperations.canWithdraw(initialContractValue)) {
            throw new FinanceOperationException("Balance too low for operation");
        }
        buyerOperations.withdrawBalance(initialContractValue);
        treasuryBond.changeCurrentInstrumentOwner(buyer);
        treasuryBond.setDateSold(transactionTimestamp);
        marketTransactionService.createMarketTransaction(treasuryBond, transactionTimestamp, buyer, treasuryBond.getCurrentInstrumentOwner(), initialContractValue);
        return activeInstrumentRepository.save(treasuryBond);
    }

    @Transactional
    public TreasuryBond sellTreasuryBond(TreasuryBond treasuryBond, User seller, Timestamp transactionTimestamp) throws FinanceOperationException {
        if (!treasuryBond.getCurrentInstrumentOwner().equalsUserId(seller)) {
            throw new FinanceOperationException("Invalid treasury bond owner");
        }
        BigDecimal treasuryBondValue = getTreasuryBondValue(treasuryBond);
        User.UserOperations sellerOperations = seller.new UserOperations();
        sellerOperations.addToBalance(treasuryBondValue);
        treasuryBond.setCurrentInstrumentOwner(null);
        marketTransactionService.createMarketTransaction(treasuryBond, transactionTimestamp, treasuryBond.getCurrentInstrumentOwner(), seller, treasuryBondValue);
        return activeInstrumentRepository.save(treasuryBond);
    }

    public BigDecimal getShareCurrentValueByFinancialInstrument(FinancialInstrument financialInstrument) {
        return activeInstrumentRepository.getMinAskPriceByFinancialInstrumentId(financialInstrument.getInstrumentId());
    }

    public BigDecimal getTreasuryBondValue(TreasuryBond treasuryBond) {
        BigDecimal treasuryBondValue = treasuryBond.getInitialContractValue();
        if (getContractPassedPeriodInMonths(treasuryBond) >= treasuryBond.getTermInMonths()) {
            treasuryBondValue = treasuryBondValue.add(
                    treasuryBondValue
                            .scaleByPowerOfTen(-2)
                            .multiply(BigDecimal.valueOf(treasuryBond.getOverallInterest()))
            );
        }
        return treasuryBondValue;
    }

    private long getContractPassedPeriodInMonths(TreasuryBond treasuryBond) {
        LocalDateTime dateSold = treasuryBond.getDateSold().toLocalDateTime();
        LocalDateTime now = MarketSimulationService.getCurrentSimulationTimestamp().toLocalDateTime();
        return ChronoUnit.MONTHS.between(now, dateSold);
    }
}
