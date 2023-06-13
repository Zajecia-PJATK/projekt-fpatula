package com.business.market.simulator.finance.instrument.derivative.type;

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
    public Index() {
        super();
        this.setDerivativeType(DerivativeType.INDEX);
    }
    @ElementCollection
    private Set<Long> instrumentsIds;
}
