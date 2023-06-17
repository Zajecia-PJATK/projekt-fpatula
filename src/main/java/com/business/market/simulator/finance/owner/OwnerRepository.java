package com.business.market.simulator.finance.owner;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OwnerRepository extends JpaRepository<Owner, Long> {
    @Override
    @EntityGraph(attributePaths = {"ownerFinancialInstruments"},type = EntityGraph.EntityGraphType.LOAD)
    List<Owner> findAll();
}
