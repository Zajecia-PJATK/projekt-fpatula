package com.business.market.simulator.finance.owner;

import com.business.market.simulator.finance.instrument.FinancialInstrument;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity(name = "owners")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Owner {
    @Id
    @GeneratedValue
    private long ownerId;
    @Column(nullable = false, unique = true)
    private String ownerName;
    @Enumerated(EnumType.STRING)
    private OwnerType ownerType;
    @OneToMany
    private List<FinancialInstrument> ownerFinancialInstruments = new ArrayList<>();
}
