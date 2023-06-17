package com.business.market.simulator.finance.simulation;

import com.business.market.simulator.MainController;
import com.business.market.simulator.utils.InputScanner;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Setter(onMethod_ = @Autowired)
@Controller
public class SimulationController {
    private InputScanner scanner;
    private MarketSimulationService marketSimulationService;
    public void runSimulationMenu() throws ExecutionException, InterruptedException, TimeoutException {
        boolean exit = false;

        while (!exit) {
            System.out.println("Simulation Menu");
            System.out.println("1. " + (MarketSimulationService.isSimulationStarted() ? "Stop simulation":"Start Simulation"));
            System.out.println("2. Change Simulation Speed");
            System.out.println("0. Exit");

            int option = scanner.getScanner().nextInt();

            switch (option) {
                case 1 -> {
                    if (MarketSimulationService.isSimulationStarted()) {
                        System.out.println("Stopping the simulation.");
                        marketSimulationService.startSimulation();
                    } else {
                        System.out.println("Starting the simulation.");
                        marketSimulationService.startSimulation();
                    }
                }
                case 2 -> {
                    changeSimulationSpeed();
                }
                case 0 -> {
                    System.out.println("Exiting.");
                    exit = true;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
            System.out.println();
        }
    }
    private void changeSimulationSpeed(){
        System.out.println("Please choose a simulation speed:");
        System.out.println("1. 0.5x");
        System.out.println("2. 0.75x");
        System.out.println("3. 1x");
        System.out.println("4. 1.5x");
        System.out.println("5. 2x");
        Map<Integer, Double> speeds = new HashMap<>();
        speeds.put(1,0.5);
        speeds.put(2,0.75);
        speeds.put(3,1.);
        speeds.put(4,1.5);
        speeds.put(5,2.);

        int option = scanner.getScanner().nextInt();

        MarketSimulationService.setSimulationSpeed(speeds.getOrDefault(option,1.));
    }
}
