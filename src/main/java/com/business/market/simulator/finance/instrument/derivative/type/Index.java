package com.business.market.simulator.finance.instrument.derivative.type;

import com.business.market.simulator.finance.instrument.aspect.Tradeable;
import com.business.market.simulator.finance.instrument.derivative.Derivative;
import com.business.market.simulator.finance.instrument.derivative.DerivativeType;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class Index extends Derivative {
    @ElementCollection
    private Set<Long> instrumentsIds;

    public Index() {
        super();
        this.setDerivativeType(DerivativeType.INDEX);
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
    public int compareTo(Tradeable o) {
        return 0;
    }
}
