package com.business.market.simulator.finance.owner;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@Entity(name = "owners")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Owner {
    @Id
    private long ownerId;
    @Column(nullable = false, unique = true)
    private String ownerName;
    @Enumerated(EnumType.STRING)
    private OwnerType ownerType;
}
