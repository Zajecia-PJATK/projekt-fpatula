package com.business.market.simulator.finance.instrument;

import com.business.market.simulator.finance.transaction.MarketTransaction;
import com.business.market.simulator.user.User;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Data
@Entity(name = "active_instruments")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class ActiveInstrument implements Tradeable {
    @Id
    private Long activeInstrumentId;
    @Enumerated(EnumType.STRING)
    private InstrumentType type;
    @ManyToOne
    private FinancialInstrument financialInstrument;
    @ManyToOne
    private User currentInstrumentOwner;
    @OneToMany
    private Set<MarketTransaction> instrumentTransactions;
}
