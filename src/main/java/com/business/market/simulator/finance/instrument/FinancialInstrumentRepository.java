package com.business.market.simulator.finance.instrument;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FinancialInstrumentRepository extends JpaRepository<FinancialInstrument, Long> {
    @Override
    List<FinancialInstrument> findAll(Sort sort);
}
