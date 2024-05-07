package de.bitwars.games;

import de.bitwars.games.moduels.GameBU;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

public class GameController {

    @ConfigProperty(name = "game.executor.poolsize")
    int executorPoolSize;


    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(executorPoolSize);
    private final Map<GameBU, ScheduledFuture<?>> games = new TreeMap<>(Comparator.comparing(GameBU::getId));

    
}
