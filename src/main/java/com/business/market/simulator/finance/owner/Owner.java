package com.business.market.simulator.finance.owner;

import com.business.market.simulator.finance.instrument.FinancialInstrument;
import com.business.market.simulator.finance.transaction.entity.LegalEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity(name = "owners")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Owner implements LegalEntity {
    @Id
    @GeneratedValue
    private long ownerId;
    @Column(nullable = false, unique = true)
    private String ownerName;
    @Enumerated(EnumType.STRING)
    private OwnerType ownerType;
    @OneToMany(cascade = {CascadeType.PERSIST,CascadeType.PERSIST})
    private List<FinancialInstrument> ownerFinancialInstruments = new ArrayList<>();

    @Override
    public String getEntityId() {
        return "O" + getOwnerId();
    }
}
