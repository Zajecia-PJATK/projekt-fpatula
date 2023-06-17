package com.business.market.simulator.user;

import jakarta.transaction.Transactional;
import lombok.Setter;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Setter(onMethod_ = {@Autowired})
@Service
public class UserAuthenticationService {
    private UserService userService;

    private boolean equalsUserPassword(User user, String password) {
        String sha3Hex = DigestUtils.sha3_256Hex(password);
        return user.getPasswordHash().equals(sha3Hex);
    }

    private boolean isStrongPassword(String password) {
        return password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9]).{8,}$");
    }

    @Transactional
    public User registerUser(String username, String password) throws UserAuthenticationException {
        if (userService.userExists(username)) {
            throw new UserAuthenticationException("User with username: " + username + " already exists");
        }
        if (username.matches("SIM*")) {
            throw new UserAuthenticationException("Used reserved keyword in username");
        }
        if (!isStrongPassword(password)) {
            throw new UserAuthenticationException("Provided password is to weak, password must contain at least 8 characters, one lowercase character, one uppercase character and one numeric character  ");
        }
        User user = new User();
        user.setUsername(username);
        user.setPasswordHash(DigestUtils.sha3_256Hex(password));
        return userService.persistUser(user);
    }
    @Transactional
    public User loginUser(String username, String password) throws UserAuthenticationException {
        User foundUser;
        if (!userService.userExists(username)) {
            throw new UserAuthenticationException("User with username: " + username + " doesn't exist");
        }
        foundUser = userService.getUserByUsername(username);
        if (!equalsUserPassword(foundUser, password)) {
            throw new UserAuthenticationException("Passwords don't match");
        }
        if (foundUser.isDeleted()) {
            throw new UserAuthenticationException("Account was deleted");
        }
        return foundUser;
    }
}
