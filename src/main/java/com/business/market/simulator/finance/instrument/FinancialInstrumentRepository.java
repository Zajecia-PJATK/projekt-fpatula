package com.business.market.simulator.finance.instrument;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FinancialInstrumentRepository extends JpaRepository<FinancialInstrument, Long> {
}
