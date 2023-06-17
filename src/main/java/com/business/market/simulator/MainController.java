package com.business.market.simulator;

import com.business.market.simulator.finance.FinanceController;
import com.business.market.simulator.finance.instrument.FinancialInstrumentController;
import com.business.market.simulator.finance.owner.OwnerController;
import com.business.market.simulator.finance.simulation.SimulationController;
import com.business.market.simulator.user.User;
import com.business.market.simulator.user.UserController;
import com.business.market.simulator.utils.InputScanner;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
@Setter(onMethod_ = @Autowired)
@Controller
public class MainController {

    private InputScanner scanner;
    private UserController userController;
    private SimulationController simulationController;
    private OwnerController ownerController;
    private FinancialInstrumentController financialInstrumentController;
    private FinanceController financeController;

    public void runMainMenu(){
        System.out.println("Welcome in your personal market sim");
        Scanner scn = scanner.getScanner();
        User authenticatedUser = userController.authenticateUser();
        if (!Objects.isNull(authenticatedUser)){
            String choice;
            boolean run = true;
            while (run) {
                System.out.println("Welcome to the market!");
                System.out.println("Please select an option:");
                System.out.println("1. Go to user operation and info");
                System.out.println("2. Go to simulation management");
                System.out.println("3. Show instruments info");
                System.out.println("4. Show assets owners info");
                System.out.println("5. Exit");

                choice = scn.nextLine();

                switch (choice) {
                    case "1" -> authenticatedUser = userController.showUserOptionsMenu(authenticatedUser);
                    case "2" -> {
                        try {
                            simulationController.runSimulationMenu();
                        } catch (ExecutionException | InterruptedException | TimeoutException e) {
                            System.err.println(e.getMessage());
                            throw new RuntimeException(e);
                        }
                    }
                    case "3" -> financialInstrumentController.showFinancialInstrumentTableMenu();
                    case "4" -> ownerController.showOwnersMenu();
                    case "5" -> {
                        System.out.println("Exiting.");
                        return;
                    }
                    default -> {
                        System.out.println("Invalid choice. Please try again.");
                        run = false;
                    }
                }

                System.out.println();
            }
        }
        else {
            System.out.println("We will see again...");
        }
    }

}
