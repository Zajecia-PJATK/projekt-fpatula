package com.business.market.simulator.finance.owner;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Owner {
    @Id
    private long ownerId;
    @Enumerated(EnumType.STRING)
    private OwnerType ownerType;
}
