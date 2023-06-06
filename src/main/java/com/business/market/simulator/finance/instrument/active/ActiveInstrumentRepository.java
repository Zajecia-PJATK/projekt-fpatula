package com.business.market.simulator.finance.instrument.active;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ActiveInstrumentRepository extends JpaRepository<ActiveInstrument, Long> {
    @Query("Select max(Share.askPrice) from active_instruments a where a.financialInstrument = ?1 and a.type = 'SHARE'")
    String getMaxAskPriceByFinancialInstrumentId(Long financialInstrumentId);
}
