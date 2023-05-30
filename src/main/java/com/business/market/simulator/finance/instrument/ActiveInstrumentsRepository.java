package com.business.market.simulator.finance.instrument;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ActiveInstrumentsRepository extends JpaRepository<ActiveInstrument, Long> {
}
