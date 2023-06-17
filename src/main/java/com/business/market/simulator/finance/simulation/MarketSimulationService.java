package com.business.market.simulator.finance.simulation;

import com.business.market.simulator.user.User;
import com.business.market.simulator.user.UserService;
import jakarta.annotation.PreDestroy;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.util.DefaultPropertiesPersister;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.*;

@PropertySource({"classpath:sim.properties"})
@Service
@Setter(onMethod_ = {@Autowired})
public class MarketSimulationService {
    @Setter(AccessLevel.NONE)
    private final static int[] marketOpenHours = new int[]{14, 21};
    @Setter(AccessLevel.NONE)
    private static Timestamp currentSimulationTimestamp;
    @Setter(AccessLevel.PRIVATE)
    private UserService userService;
    @Setter(AccessLevel.NONE)
    private MarketAlgorithmService marketAlgorithmService;
    @Setter(AccessLevel.NONE)
    private SimulationThread simulationThread;
    @Setter(AccessLevel.NONE)
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    @Setter(AccessLevel.NONE)
    private Future<?> runningSimulation;
    @Setter(AccessLevel.NONE)
    @Getter
    private static boolean simulationStarted;
    @Autowired
    public MarketSimulationService(MarketAlgorithmService marketAlgorithmService) {
        this.marketAlgorithmService = marketAlgorithmService;
        simulationThread = new SimulationThread(this.marketAlgorithmService);
    }

    public static boolean isMarketOpen(Timestamp currentTimestamp) {
        int currentHour = currentTimestamp.toLocalDateTime().getHour();
        return marketOpenHours[0] <= currentHour && marketOpenHours[1] >= currentHour;
    }

    public static synchronized void increaseTimestampByMinutes(int minutes) {
        currentSimulationTimestamp = Timestamp.from(currentSimulationTimestamp.toInstant().plus(minutes, ChronoUnit.MINUTES));
    }

    public static synchronized Timestamp getCurrentSimulationTimestamp() {
        return currentSimulationTimestamp;
    }

    @Value("${sim.current_time}")
    private void setCurrentSimulationTimestamp(String stringTimestamp) {
        MarketSimulationService.currentSimulationTimestamp = Timestamp.from(Instant.ofEpochSecond(Long.parseLong(stringTimestamp)));
    }

    public static void setSimulationSpeed(double value) {
        MarketAlgorithmService.setSimulationsSpeed(value);
    }

    private void saveTimestamp(Timestamp newTimestamp) throws SimulationException {
        try {
            Properties properties = new Properties();
            properties.setProperty("sim.current_time", newTimestamp.toInstant().toString());
            File propertiesFile = new File("sim.properties");
            OutputStream outputStream = new FileOutputStream(propertiesFile);
            DefaultPropertiesPersister propertiesPersister = new DefaultPropertiesPersister();
            propertiesPersister.store(properties, outputStream, "simulation timestamp");
        } catch (IOException e) {
            throw new SimulationException("Could not save current simulation time");
        }
    }

    private List<User> getSimUsers() {
        return userService.getUsersContaining("SIM");
    }

    public void startSimulation() {
        runningSimulation = executorService.submit(simulationThread);
        simulationStarted = true;
    }

    public void stopSimulation() throws ExecutionException, InterruptedException, TimeoutException {
        simulationThread.doStop();
        runningSimulation.get(120, TimeUnit.SECONDS);
        simulationStarted = false;
    }

    public class SimulationThread implements Runnable {
        private final MarketAlgorithmService marketAlgorithmService;
        private boolean doStop = false;

        public SimulationThread(MarketAlgorithmService marketAlgorithmService) {
            this.marketAlgorithmService = marketAlgorithmService;
        }

        public synchronized void doStop() {
            this.doStop = true;
        }

        private synchronized boolean signalStop() {
            return this.doStop;
        }

        @Override
        public void run() {
            List<User> simUsers = getSimUsers();
            Map<User, Integer> usersOperations;
            try {
                usersOperations = marketAlgorithmService.createUserOperations(simUsers);
            } catch (SimulationException e) {
                throw new RuntimeException(e);
            }
            while (!signalStop()) {
                if (isMarketOpen(getCurrentSimulationTimestamp())) {
                    marketAlgorithmService.performTradersActions(usersOperations);
                    marketAlgorithmService.updateMarket();

                } else {
                    marketAlgorithmService.forwardMarketClosedTime();
                }
            }
        }

        @PreDestroy
        private void finishSimulation() throws SimulationException {
            saveTimestamp(currentSimulationTimestamp);
        }
    }
}
