package com.business.market.simulator.finance.instrument;

import jakarta.persistence.Entity;

@Entity
public class Share extends ActiveInstrument{
    public Share() {
        super();
        this.setType(InstrumentType.SHARE);
    }
}
