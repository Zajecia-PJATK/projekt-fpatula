package com.business.market.simulator.finance.instrument;

import com.business.market.simulator.finance.owner.Owner;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity(name="financial_instruments")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class FinancialInstrument {
    @Id
    private long instrumentId;
    private double startingValue;
    @Enumerated(EnumType.STRING)
    private InstrumentType type;
    @ManyToOne
    private Owner owningCompany;
}
