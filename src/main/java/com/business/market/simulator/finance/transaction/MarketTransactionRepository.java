package com.business.market.simulator.finance.transaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.sql.Timestamp;

public interface MarketTransactionRepository extends JpaRepository<MarketTransaction, Long> {
    @Query("Select m.transactionValue from market_transactions m inner join active_instruments a where a.financialInstrument.instrumentId = :instrumentId and m.transactionTimestamp = (select max(mt.transactionTimestamp) from market_transactions mt inner join active_instruments ai where ai.financialInstrument.instrumentId = :instrumentId and date_trunc(DAY,mt) <= dataadd(DAY,-1*:daysBefore,date_trunc(DAY,:currentTime)))")
    BigDecimal getMarketCloseValueDaysBefore(@Param("instrumentId") Long financialInstrumentId, @Param("currentTime") Timestamp timestamp, @Param("daysBefore") int days);

    @Query("select count(m) from market_transactions m inner join active_instruments a where a.financialInstrument.instrumentId = :instrumentId and date_trunc(DAY,m.transactionTimestamp) >= dataadd(DAY,-1*:daysBefore,date_trunc(DAY,:currentTime))")
    long countMarketTransactionByTransactionTimestampAfterDays(@Param("instrumentId") Long financialInstrumentId, @Param("currentTime") Timestamp timestamp, @Param("daysBefore") int days);
}
