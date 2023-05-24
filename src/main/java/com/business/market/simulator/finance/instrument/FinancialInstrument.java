package com.business.market.simulator.finance.instrument;

import com.business.market.simulator.finance.owner.Owner;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity(name = "financial_instruments")
public class FinancialInstrument {
    @Id
    private long instrumentId;
    @Enumerated(EnumType.STRING)
    private InstrumentType type;
    @ManyToOne
    private Owner owningCompany;
    @OneToMany
    private List<ActiveInstrument> marketActiveInstruments;
}
