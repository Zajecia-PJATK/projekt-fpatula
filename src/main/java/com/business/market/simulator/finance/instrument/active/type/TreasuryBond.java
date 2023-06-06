package com.business.market.simulator.finance.instrument.active.type;

import com.business.market.simulator.finance.instrument.InstrumentType;
import com.business.market.simulator.finance.instrument.active.ActiveInstrument;
import com.business.market.simulator.finance.transaction.MarketTransaction;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
public class TreasuryBond extends ActiveInstrument {

    private double interest;

    private int termInMonths;

    private Timestamp dateSold;

    public TreasuryBond(){
        super();
        setType(InstrumentType.TREASURY_BOND);
    }

    @Override
    public MarketTransaction buyInstrument() {
        return null;
    }

    @Override
    public MarketTransaction sellInstrument() {
        return null;
    }

    @Override
    public BigDecimal getCurrentMarketValue() {
        return super.getCurrentMarketValue();
    }
}
