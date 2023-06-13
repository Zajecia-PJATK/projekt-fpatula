package com.business.market.simulator.finance.transaction;

import com.business.market.simulator.finance.instrument.FinancialInstrument;
import com.business.market.simulator.finance.simulation.MarketSimulationService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

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
}
