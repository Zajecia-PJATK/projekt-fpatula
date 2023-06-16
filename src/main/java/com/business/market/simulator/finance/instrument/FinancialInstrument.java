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
    @ManyToOne
    private Owner owningCompany;
    @OneToMany
    private List<ActiveInstrument> marketActiveInstruments = new ArrayList<>();

    public void addActiveInstrument(ActiveInstrument activeInstrument) {
        marketActiveInstruments.add(activeInstrument);
        activeInstrument.setFinancialInstrument(this);
    }
}
