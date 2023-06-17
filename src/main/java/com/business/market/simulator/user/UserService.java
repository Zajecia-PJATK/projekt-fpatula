package com.business.market.simulator.user;

import com.business.market.simulator.finance.FinanceService;
import com.business.market.simulator.finance.instrument.active.ActiveInstrument;
import com.business.market.simulator.finance.transaction.MarketTransaction;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

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
    public User createUser(String username, String password, BigDecimal balance){
        User user = new User();
        user.setUsername(username);
        user.setPasswordHash(password);
        user.setBalance(balance);
        return userRepository.save(user);
    }

    public User removeUser(User user) {
        user.setDeleted(true);
        return userRepository.save(user);
    }

    public User getUserByUsername(String username) {
        return userRepository.findUserDetailedByUsername(username);
    }

    public BigDecimal getPortfolioValue(User user) {
        List<ActiveInstrument> ownedInstruments = user.getOwnedInstruments();
        return ownedInstruments.parallelStream().map(ownedInstrument -> financeService.getMarketValue(ownedInstrument)).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public User withdrawFromUserBalance(User user, BigDecimal amount){
        User.UserOperations userOperations = user.new UserOperations();
        userOperations.withdrawBalance(amount);
        return persistUser(user);
    }

    public User addToUserBalance(User user, BigDecimal amount){
        User.UserOperations userOperations = user.new UserOperations();
        userOperations.addToBalance(amount);
        return persistUser(user);
    }

    public List<User> getUsersContaining(String containingString) {
        return userRepository.findByUsernameContaining(containingString);
    }

}
