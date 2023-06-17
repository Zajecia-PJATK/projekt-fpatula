package com.business.market.simulator;

import com.business.market.simulator.finance.simulation.SimulationDataInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;

@Profile("!test")

public class MainCommandLineRunner implements CommandLineRunner {

    @Autowired
    SimulationDataInitializer simulationDataInitializer;
    @Autowired
    MainController mainController;
    @Override
    public void run(String... args) {
        mainController.runMainMenu();
    }
}
