package com.business.market.simulator.finance.instrument.derivative;

import com.business.market.simulator.finance.instrument.aspect.Tradeable;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity(name = "derivatives")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Derivative implements Tradeable {
    @Id
    @GeneratedValue
    private Long derivativeId;
    private String symbol;
    @Enumerated(EnumType.STRING)
    private DerivativeType derivativeType;
}
