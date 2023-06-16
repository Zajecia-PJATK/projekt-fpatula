package com.business.market.simulator.finance.instrument.active;

import com.business.market.simulator.finance.instrument.FinancialInstrument;
import com.business.market.simulator.finance.instrument.InstrumentType;
import com.business.market.simulator.finance.instrument.aspect.Tradeable;
import com.business.market.simulator.finance.transaction.MarketTransaction;
import com.business.market.simulator.user.User;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity(name = "active_instruments")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class ActiveInstrument implements Tradeable {
    @Id
    @GeneratedValue
    private Long activeInstrumentId;
    @Enumerated(EnumType.STRING)
    private InstrumentType type;
    @ManyToOne
    private FinancialInstrument financialInstrument;
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private User currentInstrumentOwner;
    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private List<MarketTransaction> instrumentTransactions = new ArrayList<>();

    public void changeCurrentInstrumentOwner(User newOwner) {
        currentInstrumentOwner.getOwnedInstruments().remove(this);
        currentInstrumentOwner = newOwner;
        newOwner.getOwnedInstruments().add(this);
    }
    public void addTransaction(MarketTransaction marketTransaction){
        instrumentTransactions.add(marketTransaction);
    }
}
