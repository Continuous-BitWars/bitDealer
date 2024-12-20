package de.bitwars.business_logic;

import de.bitwars.api.models.StatusEnum;
import de.bitwars.business_logic.factory.GameBUFactory;
import de.bitwars.business_logic.mapper.GameBUMapper;
import de.bitwars.business_logic.mapper.GameMapBUMapper;
import de.bitwars.business_logic.moduels.ActionProvider;
import de.bitwars.business_logic.moduels.GameBU;
import de.bitwars.business_logic.moduels.GameStatus;
import de.bitwars.business_logic.moduels.player.DummyPlayer;
import de.bitwars.business_logic.moduels.player.RemotePlayer;
import de.bitwars.models.game.GameController;
import de.bitwars.models.game.dao.GameDAO;
import de.bitwars.models.game.repository.GameRepository;
import de.bitwars.models.gameTick.repository.GameTickRepository;
import io.quarkus.scheduler.Scheduled;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
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
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@ApplicationScoped
public class GameLogic {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameController.class);

    @ConfigProperty(name = "game.executor.poolsize")
    int executorPoolSize;

    private final ScheduledExecutorService scheduler;
    private final Map<GameBU, ScheduledFuture<?>> runningGames = new TreeMap<>(Comparator.comparing(GameBU::getId));

    @Inject
    GameBUFactory gameBUFactory;

    @Inject
    GameRepository gameRepository;
    @Inject
    GameTickRepository gameTickRepository;

    @Inject
    GameMapBUMapper gameMapBUMapper;

    @Inject
    GameBUMapper gameBUMapper;


    public GameLogic() {
        LOGGER.info("GameLogic started");
        int finaleExecutorPoolSize = Math.max(executorPoolSize, 10);
        LOGGER.info("Started scheduler with {} Threads (executorPoolSize: {})", finaleExecutorPoolSize, executorPoolSize);
        scheduler = Executors.newScheduledThreadPool(finaleExecutorPoolSize);
    }

    @PostConstruct
    public void init() {
        LOGGER.info("Initializing GameLogic and starting all running games...");

        List<GameDAO> runningGames = gameRepository.findByStatus(StatusEnum.RUNNING);
        runningGames.forEach(this::startGame);

        LOGGER.info("Initialized {} running games.", runningGames.size());
    }

    @Transactional
    @Scheduled(delayed = "30s", every = "30s")
    void cleanupFinishedGames() {
        LOGGER.info("Scheduled to cleanup finished Games");
        List<Long> ids = this.runningGames.keySet().stream()
                .filter(gameBU -> gameBU.getGameStatus().equals(GameStatus.DONE))
                .peek(GameBU::setStatusDone)
                .map(GameBU::getId).toList();
        ids.forEach(this::stopGame);
    }

    public Optional<GameBU> getGameById(long gameId) {
        return this.runningGames.keySet().stream().filter(gameBU -> gameBU.getId() == gameId).findFirst();
    }

    public boolean startGame(GameDAO gameDAO) {
        GameBU gameBU = gameBUFactory.createGameBU(gameDAO);
        gameBU.setGameConfig(Config.defaultOptions);//TODO add GameCodnif To Game
        gameBU.setGameMap(gameMapBUMapper.toGameBU(gameDAO.getMap()));

        gameDAO.getPlayers().forEach(playerDAO -> {
            LOGGER.info("Add Player {} >{}< to Game: {} -> {}", playerDAO.getId(), playerDAO.getName(), gameBU.getId(), gameBU.getName());

            ActionProvider actionProvider;
            //TODO: add provider for websocket
            if (playerDAO.getProviderUrl().startsWith("http")) {
                actionProvider = new RemotePlayer(playerDAO.getId(), playerDAO.getName(), playerDAO.getProviderUrl(), playerDAO.getColor(), this.gameBUMapper);
            } else {
                actionProvider = new DummyPlayer(playerDAO.getId(), playerDAO.getColor());
            }
            gameBU.addPlayer(actionProvider);
        });

        gameDAO.getLastGameTick().ifPresent(gameBU::loadGame);

        gameBU.setupGameField();


        LOGGER.info("Try start Game: {} -> {}", gameBU.getId(), gameBU.getName());

        synchronized (this) {
            if (this.runningGames.get(gameBU) == null || this.runningGames.get(gameBU).isCancelled()) {
                this.runningGames.put(gameBU, this.scheduler.scheduleAtFixedRate(gameBU, 5, gameDAO.getTimeBetweenTicks(), TimeUnit.SECONDS));
                gameBU.setStatusRunning();
            } else {
                LOGGER.info("Error by start Game: {} -> {}, Game is still Running.", gameBU.getId(), gameBU.getName());
                return false;
            }
        }
        LOGGER.info("Started Game: {} -> {}", gameBU.getId(), gameBU.getName());
        return true;
    }


    @Transactional
    public boolean stopGame(long gameId) {
        Optional<GameBU> gameBU = this.getGameById(gameId);
        GameDAO gameDAO = gameRepository.findById(gameId);

        if (gameBU.isPresent() && gameDAO != null) {
            GameBU game = gameBU.get();
            this.runningGames.get(game).cancel(true);
            this.runningGames.remove(game);
            if (game.getGameStatus() != GameStatus.DONE) {
                gameDAO.setStatus(StatusEnum.STOPPED);
            } else {
                gameDAO.setStatus(StatusEnum.DONE);
                gameDAO.setGameTicksCount((int) gameTickRepository.countGameTicksFromGame(gameDAO));
            }
            LOGGER.info("Stopped Game: {} -> {}", game.getId(), game.getName());
            return true;
        }
        LOGGER.info("[{}] Game is not schedule! Update to Stopped", gameId);
        return false;
    }


    @Scheduled(delayed = "10s", every = "10s")
    void checkPool() {
        LOGGER.info("Check Thread Pool size: {}/{}", getRunningThreadCount(), executorPoolSize);
    }

    public int getRunningThreadCount() {
        if (scheduler instanceof ThreadPoolExecutor threadPoolExecutor) {
            return threadPoolExecutor.getActiveCount();
        }
        throw new UnsupportedOperationException("Scheduler is not an instance of ThreadPoolExecutor");
    }

}
