package com.business.market.simulator.finance.instrument;

import com.business.market.simulator.finance.transaction.MarketTransaction;
import com.business.market.simulator.utils.BigDecimalToStringConverter;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
public class Share extends ActiveInstrument {
    @Convert(converter = BigDecimalToStringConverter.class)
    private BigDecimal askPrice;

    public Share() {
        super();
        this.setType(InstrumentType.SHARE);
    }

    @Override
    public MarketTransaction buyInstrument() {
        return null;
    }

    @Override
    public MarketTransaction sellInstrument() {
        return null;
    }
}
