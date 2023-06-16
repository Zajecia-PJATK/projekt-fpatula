package com.business.market.simulator.finance.simulation;

import com.business.market.simulator.finance.instrument.FinancialInstrumentService;
import com.business.market.simulator.finance.instrument.active.ActiveInstrumentService;
import com.business.market.simulator.finance.owner.OwnerService;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Setter(onMethod_ = @Autowired)
@Component
public class SimulationDataInitializer {

    OwnerService ownerService;
    FinancialInstrumentService financialInstrumentService;
    ActiveInstrumentService activeInstrumentService;

    @Transactional
    @PostConstruct
    public void initializeSimulationData() {

    }
}
