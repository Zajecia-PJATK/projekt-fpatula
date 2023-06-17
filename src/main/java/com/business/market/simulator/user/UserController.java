package com.business.market.simulator.user;

import com.business.market.simulator.finance.transaction.MarketTransaction;
import com.business.market.simulator.utils.InputScanner;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Set;

@Controller
@Setter(onMethod_ = {@Autowired})
public class UserController {

    private UserAuthenticationService userAuthenticationService;
    private UserService userService;

    private InputScanner scanner;

    public User authenticateUser() {
        boolean isAuthenticated = false;
        Scanner scn = scanner.getScanner();
        User authenticatedUser = null;
        while (!isAuthenticated) {
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            int choice = scn.nextInt();
            scn.nextLine();
            try {
                switch (choice) {
                    case 1:
                        authenticatedUser = login();
                        isAuthenticated = true;
                        break;
                    case 2:
                        authenticatedUser = register();
                        isAuthenticated = true;
                        break;
                    case 3:
                        System.out.println("Exiting.");
                        isAuthenticated = true;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (UserAuthenticationException e) {
                e.printStackTrace(System.err);
                System.out.println(e.getMessage());
            }
            System.out.println();
        }
        return authenticatedUser;
    }

    private User login() throws UserAuthenticationException {
        Scanner scn = scanner.getScanner();
        System.out.print("Enter your username: ");
        String username = scn.nextLine();
        System.out.print("Enter your password: ");
        String password = scn.nextLine();
        return userAuthenticationService.loginUser(username, password);
    }

    private User register() throws UserAuthenticationException {
        Scanner scn = scanner.getScanner();
        System.out.print("Enter a username: ");
        String username = scn.nextLine();
        System.out.print("Enter a password: ");
        String password = scn.nextLine();
        return userAuthenticationService.registerUser(username, password);
    }

    private User addToBalance(User user) {
        System.out.println("Enter the amount to be added to account: ");
        try {
            BigDecimal balanceToAdd = scanner.getScanner().nextBigDecimal();
            user = userService.addToUserBalance(user, balanceToAdd);
            System.out.println("Operation success");
        } catch (InputMismatchException e) {
            System.err.println(e);
            System.out.println("Input mismatch for amount");
        }
        catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
        }
        return user;
    }

    private User withdrawFromBalance(User user) {
        System.out.println("Enter the amount to withdraw from account: ");
        try {
            BigDecimal withdrawAmount = scanner.getScanner().nextBigDecimal();
            user = userService.withdrawFromUserBalance(user,withdrawAmount);
            System.out.println("Operation success");
        } catch (InputMismatchException e) {
            System.err.println(e);
            System.out.println("Input mismatch for amount");
        }
        catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
        }
        return user;
    }

    private void generateUserDataView(User user) {
        System.out.println("User: " + user.getUsername());
        System.out.println("Balance: $" + user.getBalance().setScale(3, RoundingMode.HALF_DOWN));
        System.out.println("Portfolio Value: $"+ userService.getPortfolioValue(user).setScale(3,RoundingMode.HALF_DOWN)+"\n");
        System.out.println("Transaction History:");
        System.out.println("---------------------");
        System.out.println("Date          | Buyer Id  | Seller Id | Symbol | Amount   ");
        System.out.println("-------------------------------------------");
        Set<MarketTransaction> userTransactions = user.getUserTransactions();
        for (MarketTransaction transaction : userTransactions) {
            System.out.printf("%-14s| %-12s| %-12s| %-8s| $%9s%n",
                    transaction.getTransactionTimestamp().toLocalDateTime().format(DateTimeFormatter.ISO_DATE), transaction.getBuyerId(), transaction.getSellerId(), transaction.getTargetInstrument().getFinancialInstrument().getSymbol(), transaction.getTransactionValue().setScale(3,RoundingMode.HALF_DOWN));
        }
    }

    public User showUserOptionsMenu(User user) {
        boolean show = true;

        while (show) {
            System.out.println("Please select an option:");
            System.out.println("1. Add funds to account");
            System.out.println("2. Withdraw funds from account");
            System.out.println("3. Show user data");
            System.out.println("0. Exit");

            int option = scanner.getScanner().nextInt();

            switch (option) {
                case 1 -> user = addToBalance(user);
                case 2 -> user = withdrawFromBalance(user);
                case 3 -> generateUserDataView(user);
                case 0 -> {
                    System.out.println("Returning to main.");
                    show = false;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
            System.out.println();
        }
        return user;
    }

}
