package com.business.market.simulator.finance.transaction;


import com.business.market.simulator.finance.instrument.active.ActiveInstrument;
import com.business.market.simulator.user.User;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity(name = "market_transactions")
public class MarketTransaction {
    @Id
    private Long transactionId;
    @ManyToOne
    private ActiveInstrument targetInstrument;
    private Timestamp timestamp;
    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(
            name = "transaction_participants",
            joinColumns = @JoinColumn(name = "transactionId"),
            inverseJoinColumns = @JoinColumn(name = "userId")
    )
    private Set<User> transactionParticipants = new HashSet<>();
}
