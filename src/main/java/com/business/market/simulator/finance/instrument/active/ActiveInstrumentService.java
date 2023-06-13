package com.business.market.simulator.finance.instrument.active;

import com.business.market.simulator.finance.instrument.FinancialInstrument;
import com.business.market.simulator.finance.instrument.InstrumentType;
import com.business.market.simulator.finance.instrument.active.type.Share;
import com.business.market.simulator.finance.instrument.active.type.TreasuryBond;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@Setter(onMethod_ = {@Autowired})
public class ActiveInstrumentService {
    ActiveInstrumentRepository activeInstrumentRepository;

    public TreasuryBond createTreasuryBond(FinancialInstrument financialInstrument, double interest, int termInMonths) throws ActiveInstrumentException {
        if (!financialInstrument.getType().equals(InstrumentType.TREASURY_BOND)) {
            throw new ActiveInstrumentException("Instrument is not a treasury bond type");
        }
        TreasuryBond treasuryBond = new TreasuryBond();
        treasuryBond.setInterest(interest);
        treasuryBond.setTermInMonths(termInMonths);
        financialInstrument.addActiveInstrument(treasuryBond);
        return activeInstrumentRepository.save(treasuryBond);
    }

    public List<TreasuryBond> createTreasuryBonds(FinancialInstrument financialInstrument, double interest, int termInMonths, int quantity) throws ActiveInstrumentException {
        List<TreasuryBond> treasuryBonds = new ArrayList<>();
        for (int i = 0; i < quantity; i++) {
            treasuryBonds.add(createTreasuryBond(financialInstrument, interest, termInMonths));
        }
        return treasuryBonds;
    }

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

    public BigDecimal getShareCurrentValueByFinancialInstrument(FinancialInstrument financialInstrument) {
        return activeInstrumentRepository.getMinAskPriceByFinancialInstrumentId(financialInstrument.getInstrumentId());
    }
}
