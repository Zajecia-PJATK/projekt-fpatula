package com.business.market.simulator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MarketSimulatorApplication extends MainCommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(MarketSimulatorApplication.class, args);
    }


}
