package de.bitwars.games;

import de.bitwars.games.moduels.ActionProvider;
import de.bitwars.games.moduels.GameBU;
import de.bitwars.games.moduels.GameConfigBU;
import de.bitwars.games.moduels.GameMapBU;
import de.bitwars.games.moduels.GameStatus;
import de.bitwars.games.moduels.player.DummyPlayer;
import de.bitwars.live.GameLiveController;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@ApplicationScoped
public class GameController {

    private static long idSequence = 1;

    @ConfigProperty(name = "game.executor.poolsize")
    int executorPoolSize;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(executorPoolSize);
    private final Map<GameBU, ScheduledFuture<?>> games = new TreeMap<>(Comparator.comparing(GameBU::getId));

    @Inject
    GameLiveController gameLiveController;

    @PostConstruct
    void postConstruct() {
        GameBU gameBU = this.createGame("Default", Config.defaultOptions, Config.defaultMap);
        this.addPlayerToGame(gameBU.getId(), new DummyPlayer(1001));
        this.addPlayerToGame(gameBU.getId(), new DummyPlayer(1002));
    }

    public GameBU createGame(String name, GameConfigBU gameConfig, GameMapBU gameMap) {
        GameBU game = new GameBU(idSequence++, name, gameConfig, gameMap, gameLiveController);
        this.games.put(game, null);
        return game;
    }

    public List<GameBU> getGames() {
        return this.games.keySet().stream().toList();
    }

    public boolean deleteGame(long gameId) {
        ScheduledFuture<?> scheduledFuture = this.games.remove(getGameById(gameId));
        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
        }
        return true;
    }

    public GameBU getGameById(long gameId) {
        return this.games.keySet().stream().filter(gameBU -> gameBU.getId() == gameId).findFirst().orElseThrow(NotFoundException::new);
    }

    public GameBU addPlayerToGame(long gameId, ActionProvider actionProvider) {
        GameBU gameBU = this.getGameById(gameId);
        gameBU.addPlayer(actionProvider);
        return gameBU;
    }

    public GameBU removePlayerFromGame(long gameId, long playerId) {
        GameBU gameBU = this.getGameById(gameId);
        gameBU.removePlayer(playerId);
        return gameBU;
    }

    public GameBU startGame(long gameId, long timeBetweenTicksInSeconds) {
        GameBU gameBU = this.getGameById(gameId);

        synchronized (this) {
            if (this.games.get(gameBU) == null || this.games.get(gameBU).isCancelled()) {
                gameBU.setTickSpeed(Duration.ofSeconds(timeBetweenTicksInSeconds));
                gameBU.setGameStatus(GameStatus.RUNNING);
                this.games.put(gameBU, this.scheduler.scheduleAtFixedRate(gameBU, 0, gameBU.getTickSpeed().getSeconds(), TimeUnit.SECONDS));
            }
        }
        return gameBU;
    }

    public GameBU stopGame(long gameId) {
        GameBU gameBU = this.getGameById(gameId);

        if (gameBU != null) {
            this.games.get(gameBU).cancel(true);
            gameBU.setGameStatus(GameStatus.STOPPED);
        }
        return gameBU;
    }
}
