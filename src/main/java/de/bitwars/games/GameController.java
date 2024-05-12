package de.bitwars.games;

import de.bitwars.games.moduels.ActionProvider;
import de.bitwars.games.moduels.GameBU;
import de.bitwars.games.moduels.GameConfigBU;
import de.bitwars.games.moduels.GameMapBU;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

@ApplicationScoped
public class GameController {

    private static long idSequence = 1;

    @ConfigProperty(name = "game.executor.poolsize")
    int executorPoolSize;


    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(executorPoolSize);
    private final Map<GameBU, ScheduledFuture<?>> games = new TreeMap<>(Comparator.comparing(GameBU::getId));

    public GameBU createGame(String name, GameConfigBU gameConfig, GameMapBU gameMap) {
        GameBU game = new GameBU(idSequence++, name, gameConfig, gameMap);
        this.games.put(game, null);

        return game;
    }

    public List<GameBU> getGames() {
        return this.games.keySet().stream().toList();
    }

    public boolean deleteGame(long gameId) {
        ScheduledFuture<?> scheduledFuture = this.games.remove(new GameBU(gameId));
        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
        }
        return scheduledFuture != null;
    }

    public GameBU getGameById(long gameId) {
        return this.games.keySet().stream().filter(gameBU -> gameBU.getId() == gameId).findFirst().orElseThrow(NotFoundException::new);
    }

    public GameBU addPlayerToGame(Integer gameId, ActionProvider actionProvider) {
        GameBU gameBU = this.getGameById(gameId);
        gameBU.addPlayer(actionProvider);
        return gameBU;
    }

    public GameBU removePlayerFromGame(long gameId, long playerId) {
        GameBU gameBU = this.getGameById(gameId);
        gameBU.removePlayer(playerId);
        return gameBU;
    }
}
