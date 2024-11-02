package de.bitwars.business_logic;

import de.bitwars.business_logic.factory.GameBUFactory;
import de.bitwars.business_logic.moduels.ActionProvider;
import de.bitwars.business_logic.moduels.GameBU;
import de.bitwars.business_logic.moduels.GameStatus;
import de.bitwars.business_logic.moduels.player.DummyPlayer;
import de.bitwars.business_logic.moduels.player.RemotePlayer;
import de.bitwars.models.game.GameController;
import de.bitwars.models.game.dao.GameDAO;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@ApplicationScoped
public class GameLogic {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameController.class);

    @ConfigProperty(name = "game.executor.poolsize")
    int executorPoolSize;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(executorPoolSize);
    private final Map<GameBU, ScheduledFuture<?>> runningGames = new TreeMap<>(Comparator.comparing(GameBU::getId));

    @Inject
    GameBUFactory gameBUFactory;

    @Scheduled(delayed = "30s", every = "30s")
    void cleanupFinishedGames() {
        LOGGER.debug("Scheduled to cleanup finished Games");
        List<Long> ids = this.runningGames.keySet().stream()
                .filter(gameBU -> gameBU.getGameStatus().equals(GameStatus.DONE))
                .map(GameBU::getId).toList();
        ids.forEach(this::stopGame);
    }

    public Optional<GameBU> getGameById(long gameId) {
        return this.runningGames.keySet().stream().filter(gameBU -> gameBU.getId() == gameId).findFirst();
    }

    public boolean startGame(GameDAO gameDAO) {
        GameBU gameBU = gameBUFactory.createGameBU(gameDAO);
        gameBU.setGameConfig(Config.defaultOptions);//TODO add GameCodnif To Game
        gameBU.setGameMap(Config.defaultMap);

        gameDAO.getLastGameTick().ifPresent(gameBU::loadGame);

        gameDAO.getPlayers().forEach(playerDAO -> {
            LOGGER.info("Add Player to Game: {} -> {}", gameBU.getId(), gameBU.getName());

            ActionProvider actionProvider;
            //TODO: add provider for websocket
            if (playerDAO.getProviderUrl().startsWith("http")) {
                actionProvider = new RemotePlayer(playerDAO.getId(), playerDAO.getName(), playerDAO.getProviderUrl(), playerDAO.getColor());
            } else {
                actionProvider = new DummyPlayer(playerDAO.getId(), playerDAO.getColor());
            }
            gameBU.addPlayer(actionProvider);
        });

        gameBU.setupGameField();


        LOGGER.info("Try start Game: {} -> {}", gameBU.getId(), gameBU.getName());

        synchronized (this) {
            if (this.runningGames.get(gameBU) == null || this.runningGames.get(gameBU).isCancelled()) {
                this.runningGames.put(gameBU, this.scheduler.scheduleAtFixedRate(gameBU, 0, gameDAO.getTimeBetweenTicks(), TimeUnit.SECONDS));
                gameBU.setStatusRunning();
            } else {
                LOGGER.info("Error by start Game: {} -> {}, Game is still Running.", gameBU.getId(), gameBU.getName());
                return false;
            }
        }
        LOGGER.info("Started Game: {} -> {}", gameBU.getId(), gameBU.getName());
        return true;
    }

    public boolean stopGame(long gameId) {
        Optional<GameBU> gameBU = this.getGameById(gameId);
        if (gameBU.isPresent()) {
            GameBU game = gameBU.get();
            this.runningGames.get(game).cancel(true);
            this.runningGames.put(game, null);
            if (game.getGameStatus() != GameStatus.DONE) {
                game.setStatusStopped();
            }
            LOGGER.info("Stopped Game: {} -> {}", game.getId(), game.getName());
            return true;
        }
        LOGGER.info("[{}] Game is not schedule! Update to Stopped", gameId);
        return false;
    }
}
