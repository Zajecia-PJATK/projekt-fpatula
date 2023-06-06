package com.business.market.simulator.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByUsername(String username);
    boolean existsByUsername(String username);

}
