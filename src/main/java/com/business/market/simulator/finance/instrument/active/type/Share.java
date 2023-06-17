package com.business.market.simulator.finance.instrument.active.type;

import com.business.market.simulator.finance.instrument.InstrumentType;
import com.business.market.simulator.finance.instrument.active.ActiveInstrument;
import com.business.market.simulator.finance.instrument.aspect.Tradeable;
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
    public boolean isBuyable() {
        return true;
    }

    @Override
    public boolean isSellable() {
        return true;
    }


    @Override
    public int compareTo(Tradeable other) {
        if (other instanceof Share otherShare) {
            return this.askPrice.compareTo(otherShare.getAskPrice());
        } else {
            return 0;
        }
    }
}
