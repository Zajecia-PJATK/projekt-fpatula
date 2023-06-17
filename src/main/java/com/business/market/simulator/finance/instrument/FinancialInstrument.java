package com.business.market.simulator.finance.instrument;

import com.business.market.simulator.finance.instrument.active.ActiveInstrument;
import com.business.market.simulator.finance.owner.Owner;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity(name = "financial_instruments")
public class FinancialInstrument {
    @Id
    @GeneratedValue
    private long instrumentId;
    @Column(nullable = false, unique = true)
    private String symbol;
    @Enumerated(EnumType.STRING)
    private InstrumentType type;
    @Enumerated(EnumType.STRING)
    private Sector sector;
    @ManyToOne(cascade = {CascadeType.MERGE})
    private Owner owningCompany;
    @OneToMany
    private List<ActiveInstrument> marketActiveInstruments = new ArrayList<>();

    public void addActiveInstrument(ActiveInstrument activeInstrument) {
        marketActiveInstruments.add(activeInstrument);
        activeInstrument.setFinancialInstrument(this);
    }

    public void addOwner(Owner owner) {
        owner.getOwnerFinancialInstruments().add(this);
        owningCompany = owner;
    }
}
