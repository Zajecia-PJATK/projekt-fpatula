package com.business.market.simulator.finance.fund;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Fund {
    @Id
    private Long fundId;

}
