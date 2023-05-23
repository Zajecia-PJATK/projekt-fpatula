package com.business.market.simulator.finance.instrument;

import com.business.market.simulator.user.User;
import com.business.market.simulator.utils.BigDecimalToStringConverter;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@SecondaryTable(name = ActiveInstrument.TABLE_NAME)
public abstract class ActiveInstrument extends FinancialInstrument implements Tradeable {
    static final String TABLE_NAME = "active_instruments";
    @Convert(converter = BigDecimalToStringConverter.class)
    private BigDecimal askPrice;
    @ManyToOne
    private User currentInstrumentOwner;
}
