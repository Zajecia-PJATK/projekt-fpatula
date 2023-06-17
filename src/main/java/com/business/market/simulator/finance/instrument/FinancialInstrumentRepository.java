package com.business.market.simulator.finance.instrument;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FinancialInstrumentRepository extends JpaRepository<FinancialInstrument, Long> {
    FinancialInstrument findBySymbol(String symbol);
    List<FinancialInstrument> findAllByTypeEquals(InstrumentType type);
}
