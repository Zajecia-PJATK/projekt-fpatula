package com.business.market.simulator.user;

import com.business.market.simulator.finance.FinanceService;
import com.business.market.simulator.finance.instrument.active.ActiveInstrument;
import com.business.market.simulator.finance.transaction.MarketTransaction;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Setter(onMethod_ = {@Autowired})
@Service
public class UserService {
    private UserRepository userRepository;
    private FinanceService financeService;

    public boolean userExists(String username) {
        return userRepository.existsByUsername(username);
    }

    public User persistUser(User user) {
        return userRepository.save(user);
    }

    public User removeUser(User user) {
        user.setDeleted(true);
        return userRepository.save(user);
    }

    public User getUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    public BigDecimal getBalance(User user) {
        return user.getBalance();
    }

    public BigDecimal getPortfolioValue(User user) {
        List<ActiveInstrument> ownedInstruments = user.getOwnedInstruments();
        return ownedInstruments.parallelStream().map(ownedInstrument -> financeService.getMarketValue(ownedInstrument)).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<MarketTransaction> userTransactions(User user) {
        return user.getUserTransactions();
    }

    public List<User> getUsersContaining(String containingString) {
        return userRepository.findByUsernameContaining(containingString);
    }

}
