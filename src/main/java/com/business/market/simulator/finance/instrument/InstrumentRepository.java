package com.business.market.simulator.finance.instrument;

import org.springframework.data.jpa.repository.JpaRepository;

public interface InstrumentRepository extends JpaRepository<ActiveInstrument, Long> {
}
