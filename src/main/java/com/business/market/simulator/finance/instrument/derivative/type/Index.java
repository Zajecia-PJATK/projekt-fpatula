package com.business.market.simulator.finance.instrument.derivative.type;

import com.business.market.simulator.finance.instrument.derivative.Derivative;
import com.business.market.simulator.finance.transaction.MarketTransaction;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
public class Index extends Derivative {
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
