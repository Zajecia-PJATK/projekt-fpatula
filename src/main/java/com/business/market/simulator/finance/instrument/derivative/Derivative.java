package com.business.market.simulator.finance.instrument.derivative;

import com.business.market.simulator.finance.instrument.aspect.Tradeable;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import lombok.Data;

@Data
@Entity(name = "derivatives")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Derivative implements Tradeable {
    @Id
    private Long derivativeId;


}
