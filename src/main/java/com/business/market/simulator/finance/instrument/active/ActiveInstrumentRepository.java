package com.business.market.simulator.finance.instrument.active;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface ActiveInstrumentRepository extends JpaRepository<ActiveInstrument, Long> {
    @Query("Select a1.askPrice from active_instruments a1 where a1.financialInstrument.instrumentId = :instrumentId and a1.type = 'SHARE' and CAST(a1.askPrice as double) = (select MIN(CAST(a2.askPrice as double)) from active_instruments a2 where a2.financialInstrument.instrumentId = :instrumentId and a2.type = 'SHARE')")
    BigDecimal getMinAskPriceByFinancialInstrumentId(@Param("instrumentId") Long financialInstrumentId);

}
