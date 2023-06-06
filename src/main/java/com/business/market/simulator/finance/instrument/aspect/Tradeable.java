package com.business.market.simulator.finance.instrument.aspect;

import java.math.BigDecimal;

public interface Tradeable extends Buyable, Sellable {
    default BigDecimal getCurrentMarketValue(){
        return BigDecimal.ZERO;
    }
}
