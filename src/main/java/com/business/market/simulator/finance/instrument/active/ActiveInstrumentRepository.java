package com.business.market.simulator.finance.instrument.active;

import com.business.market.simulator.finance.instrument.active.type.Share;
import com.business.market.simulator.finance.instrument.active.type.TreasuryBond;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

public interface ActiveInstrumentRepository extends JpaRepository<ActiveInstrument, Long> {
    @Query("Select a1.askPrice from active_instruments a1 where a1.financialInstrument.instrumentId = :instrumentId and a1.type = 'SHARE' and CAST(a1.askPrice as double) = (select MIN(CAST(a2.askPrice as double)) from active_instruments a2 where a2.financialInstrument.instrumentId = :instrumentId and a2.type = 'SHARE')")
    BigDecimal getMinAskPriceByFinancialInstrumentId(@Param("instrumentId") Long financialInstrumentId);

    @Query("select a from active_instruments a where a.currentInstrumentOwner.userId in :ids")
    List<ActiveInstrument> findAllByCurrentInstrumentOwnerIdIn(@Param("ids") Collection<Long> userIds);

    @Query("select a from active_instruments a where a.currentInstrumentOwner.userId = :id")
    List<ActiveInstrument> findAllByCurrentInstrumentOwner(@Param("id") Long userId);

    @Query("select a from active_instruments a where (a.currentInstrumentOwner.userId <> :id and a.type='SHARE') or (a.type = 'TREASURY_BOND' and a.dateSold is null)")
    List<ActiveInstrument> findAllBuyableInstrumentsByUser(@Param("id") Long userId);

    @Query("select a1 from active_instruments a1 where a1.financialInstrument.symbol = :symbol and a1.currentInstrumentOwner.userId <> :uid and CAST(a1.askPrice as double) = (select MIN(CAST(a2.askPrice as double)) from active_instruments a2 where a2.financialInstrument.instrumentId = :instrumentId and a2.type = 'SHARE' and a2.currentInstrumentOwner.userId <> :uid )")
    Share findLowestPriceShareBySymbolAndCurrentInstrumentOwnerIdNotEquals(@Param("symbol") String symbol, @Param("uid")Long userId);
    @Query("select a from active_instruments a where a.financialInstrument.symbol = :symbol and a.type = 'TREASURY_BOND' and a.dateSold is null")
    TreasuryBond findNotSoldTreasuryBondBySymbol(@Param("symbol") String symbol);

}
