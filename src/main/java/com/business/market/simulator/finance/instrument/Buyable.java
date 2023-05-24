package com.business.market.simulator.finance.instrument;

import com.business.market.simulator.finance.transaction.MarketTransaction;

public interface Buyable {
    public MarketTransaction buyInstrument();
}
