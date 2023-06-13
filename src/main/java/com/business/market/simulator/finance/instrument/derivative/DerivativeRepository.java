package com.business.market.simulator.finance.instrument.derivative;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DerivativeRepository extends JpaRepository<Derivative, Long> {
}
