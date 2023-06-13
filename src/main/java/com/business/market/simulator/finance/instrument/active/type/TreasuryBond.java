package com.business.market.simulator.finance.instrument.active.type;

import com.business.market.simulator.finance.instrument.InstrumentType;
import com.business.market.simulator.finance.instrument.active.ActiveInstrument;
import com.business.market.simulator.utils.BigDecimalToStringConverter;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
public class TreasuryBond extends ActiveInstrument {
    @Convert(converter = BigDecimalToStringConverter.class)
    private BigDecimal initialContractValue;

    private double interest;

    private int termInMonths;

    private Timestamp dateSold;

    public TreasuryBond() {
        super();
        setType(InstrumentType.TREASURY_BOND);
    }
}
