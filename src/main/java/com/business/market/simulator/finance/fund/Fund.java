package com.business.market.simulator.finance.fund;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import lombok.Data;

@Data
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Fund {
    @Id
    private Long fundId;
    private String fundName;
    //TODO add fund participants and what is needed

}
