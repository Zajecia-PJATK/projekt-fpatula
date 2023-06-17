package com.business.market.simulator.finance.transaction;

import com.business.market.simulator.finance.instrument.FinancialInstrument;
import com.business.market.simulator.finance.instrument.active.ActiveInstrument;
import com.business.market.simulator.finance.simulation.MarketSimulationService;
import com.business.market.simulator.finance.transaction.entity.LegalEntity;
import com.business.market.simulator.user.User;
import jakarta.transaction.Transactional;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Setter(onMethod_ = {@Autowired})
@Service
public class MarketTransactionService {
    private MarketTransactionRepository marketTransactionRepository;

    public BigDecimal getMarketCloseValueFromDaysBefore(FinancialInstrument financialInstrument, int days) {
        return marketTransactionRepository.getMarketCloseValueDaysBefore(financialInstrument.getInstrumentId(), MarketSimulationService.getCurrentSimulationTimestamp(), days);
    }

    public long getMarketVolumeByFinancialInstrumentInDaysRange(FinancialInstrument financialInstrument, int days) {
        return marketTransactionRepository.countMarketTransactionByTransactionTimestampAfterDays(financialInstrument.getInstrumentId(), MarketSimulationService.getCurrentSimulationTimestamp(), days);
    }

    @Transactional
    public MarketTransaction createMarketTransaction(ActiveInstrument activeInstrument, Timestamp transactionTimestamp, LegalEntity buyer, LegalEntity seller, BigDecimal transactionValue) {
        MarketTransaction marketTransaction = new MarketTransaction();
        marketTransaction.setTransactionTimestamp(transactionTimestamp);
        marketTransaction.setTransactionValue(transactionValue);
        marketTransaction.setBuyerId(buyer.getEntityId());
        marketTransaction.setSellerId(seller.getEntityId());
        if (buyer instanceof User) {
            marketTransaction.addParticipant((User) buyer);
        }
        if (seller instanceof User) {
            marketTransaction.addParticipant((User) seller);
        }
        marketTransaction.addTargetInstrument(activeInstrument);
        return marketTransactionRepository.save(marketTransaction);
    }
}
