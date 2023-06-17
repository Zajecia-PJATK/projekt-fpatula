package com.business.market.simulator.user;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    @EntityGraph(attributePaths = {"ownedInstruments", "userTransactions"})
    User findUserDetailedByUsername(String username);

    boolean existsByUsername(String username);

    boolean deleteUserByUserId(Long userId);
    @EntityGraph(attributePaths = {"ownedInstruments", "userTransactions"})
    List<User> findByUsernameContaining(String namePart);
}
