package com.business.market.simulator.finance.instrument;

import com.business.market.simulator.finance.instrument.active.ActiveInstrumentRepository;
import com.business.market.simulator.finance.instrument.active.ActiveInstrumentService;
import com.business.market.simulator.finance.owner.Owner;
import com.business.market.simulator.finance.transaction.MarketTransactionService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Setter(onMethod_ = {@Autowired})
@Service
public class FinancialInstrumentService {
    private ActiveInstrumentRepository activeInstrumentRepository;
    private FinancialInstrumentRepository financialInstrumentRepository;
    private ActiveInstrumentService activeInstrumentService;
    private MarketTransactionService marketTransactionService;

    public FinancialInstrument createFinancialInstrument(Owner owner, InstrumentType type, String symbol, Sector sector) {
        FinancialInstrument financialInstrument = new FinancialInstrument();
        financialInstrument.setOwningCompany(owner);
        financialInstrument.setType(type);
        financialInstrument.setSymbol(symbol);
        financialInstrument.setSector(sector);
        return financialInstrumentRepository.save(financialInstrument);
    }

    public long getFinancialInstrumentVolume(FinancialInstrument financialInstrument, int periodInDays) {
        return marketTransactionService.getMarketVolumeByFinancialInstrumentInDaysRange(financialInstrument, periodInDays);
    }
    public List<FinancialInstrument> getAllByType(InstrumentType instrumentType){
        return financialInstrumentRepository.findAllByTypeEquals(instrumentType);
    }

    public List<FinancialInstrument> getTreasuryBond(InstrumentType instrumentType){
        return financialInstrumentRepository.findAllByTypeEquals(instrumentType);
    }

    public double calculateShareChange(FinancialInstrument financialInstrument, int periodInDays) {
        double currentShareValue = activeInstrumentService.getShareCurrentValueByFinancialInstrument(financialInstrument).doubleValue();
        BigDecimal marketCloseValueDayBefore = marketTransactionService.getMarketCloseValueFromDaysBefore(financialInstrument, periodInDays);
        if (Objects.isNull(marketCloseValueDayBefore)) {
            return 0.0;
        }
        return currentShareValue - marketCloseValueDayBefore.doubleValue();
    }

    public Double getFinancialInstrumentChange(FinancialInstrument financialInstrument, int periodInDays) {
        switch (financialInstrument.getType()) {
            case SHARE -> {
                return calculateShareChange(financialInstrument, periodInDays);
            }
            case TREASURY_BOND -> {
                return 0.0;
            }
            case INDEX -> {
                return 0.0;
            }
            default -> {
                return 0.0;
            }
        }
    }
}
