package de.bitwars.business_logic.moduels;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.bitwars.api.models.StatusEnum;
import de.bitwars.api.models.clients.Board;
import de.bitwars.business_logic.mapper.GameBUMapper;
import de.bitwars.live.GameLiveController;
import de.bitwars.models.game.dao.GameDAO;
import de.bitwars.models.gameTick.GameTickController;
import de.bitwars.models.gameTick.dao.GameTickDAO;
import de.bitwars.models.gameTick.mapper.GameTickMapper;
import de.bitwars.models.player.dao.PlayerDAO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ApplicationScoped
public class GameBU implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameBU.class);

    private long id;
    private String name;
    private GameMapBU gameMap;
    private GameConfigBU gameConfig;
    private GameLiveController gameLiveController;
    private GameDAO gameDAO;

    private GameStatus gameStatus;

    private int tick;
    private int remainingPlayers;
    private List<ActionProvider> players;
    private GameFieldBU gameField;

    @Inject
    GameTickController gameTickController;
    @Inject
    GameBUMapper gameBUMapper;
    @Inject
    EntityManager entityManager;
    @Inject
    GameTickMapper gameTickMapper;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public GameBU(long gameId, String name, GameConfigBU gameConfig, GameMapBU gameMap, GameLiveController gameLiveController) {
        this.id = gameId;
        this.gameConfig = gameConfig;
        this.gameMap = gameMap;
        this.name = name;
        this.gameField = new GameFieldBU(gameMap);
        this.gameStatus = GameStatus.STOPPED;
        this.players = new ArrayList<>();
        this.tick = 0;
        this.remainingPlayers = 0;
        this.gameLiveController = gameLiveController;
    }

    public GameBU(GameDAO gameDAO) {
        this.id = gameDAO.getId();
        this.name = gameDAO.getName();
        this.gameStatus = GameStatus.STOPPED;
        this.gameDAO = gameDAO;

        this.gameMap = null;
        this.gameConfig = null;
        this.gameLiveController = null;
        this.tick = 0;
        this.remainingPlayers = 0;
        this.players = new ArrayList<>();
        this.gameField = null;
    }

    @Transactional
    public void setStatusRunning() {
        this.setGameStatus(GameStatus.RUNNING);
        this.getGameDAO().setStatus(StatusEnum.RUNNING);
    }

    @Transactional
    public void setStatusStopped() {
        this.setGameStatus(GameStatus.STOPPED);
        this.getGameDAO().setStatus(StatusEnum.STOPPED);
    }

    @Transactional
    public void setStatusDone() {
        this.setGameStatus(GameStatus.DONE);
        this.getGameDAO().setStatus(StatusEnum.DONE);
    }

    public void addPlayer(ActionProvider player) {
        if (gameStatus.equals(GameStatus.RUNNING)) {
            throw new IllegalStateException("Game is already running.");
        }
        if (gameStatus.equals(GameStatus.DONE)) {
            throw new IllegalStateException("Game is already done.");
        }
        if (players.size() >= this.gameMap.getMaxPlayerCount()) {
            throw new IllegalArgumentException("Too many players");
        }
        players.add(player);

        this.remainingPlayers++;
    }


    public void setupGameField() {
        if (this.tick > 0) {
            return;
        }

        this.gameField = new GameFieldBU(this.gameMap);

        for (int i = 0; i < players.size(); i++) {
            int finalI = i;
            this.gameMap.getBases().stream()
                    .filter(base -> base.getPlayerId() == (finalI + 1))
                    .map(BaseBU::getUid)
                    .forEach(baseId -> this.gameField.getBases().get(baseId).setPlayerId(players.get(finalI).getId()));
        }
    }

    @Override
    public void run() {
        LOGGER.info("[{}] Tick {} is Start!", this.getId(), this.tick);
        final long timeStart = System.currentTimeMillis();

        if (!this.gameStatus.equals(GameStatus.RUNNING)) {
            LOGGER.info("[{}] Cancel GameStep, GameStatus is not Running: {}", this.getId(), gameStatus.name());
            this.sendGameStateToWebsocket();
            return;
        }
        LOGGER.debug("requestPlayerActions: {}", this.getId());
        requestPlayerActions();

        LOGGER.debug("getBases: {}", this.getId());
        gameField.getBases().values().forEach(base -> base.takeTick(this.gameConfig.getBaseLevelsConfig()));

        LOGGER.debug("getBoardActions: {}", this.getId());
        gameField.getBoardActions().forEach(boardActions -> boardActions.takeTick(this.gameConfig.getPathsConfig()));

        LOGGER.debug("takeFight: {}", this.getId());
        takeFight();

        this.gameField.getBases().values().forEach(base -> {
            LOGGER.debug("{} - {}: {}", base.getUid(), base.getPlayerId(), base.getPopulation());
        });
        this.gameField.getBoardActions().forEach(actions -> {
            LOGGER.debug(actions.getPlayer() + ": " + actions.getSource() + " -> " + actions.getDestination() + " | " + actions.getAmount() + " | " + actions.getProgress().getTraveled() + "/" + actions.getProgress().getDistance());
        });
        LOGGER.debug("-----");


        storeGameState();
        sendGameStateToWebsocket();


        cleanup();
        checkIsDone();

        final long timeEnd = System.currentTimeMillis();

        long time = (timeEnd - timeStart);
        LOGGER.info("[{}] Tick {} is Done! Tick needs: {} ms", this.getId(), this.tick, time);

        if (time > 1000) {
            LOGGER.warn("[{}] Tick {} Needs longer: {} ms", this.getId(), this.tick, time);
        }
        this.tick++;

    }

    private void storeGameState() {
        LOGGER.debug("[{}] storeGameState", this.getId());
        Board board = this.gameBUMapper.toBoard(this, 0);
        String message = null;
        try {
            message = objectMapper.writeValueAsString(board);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        GameTickDAO gameTickDAO = this.gameTickController.storeTick(tick, message, gameDAO);
        LOGGER.debug("[{}] gameTickDAO from Tick {} has id: {}", this.getId(), gameTickDAO.getTick(), gameTickDAO.getId());

    }

    private void sendGameStateToWebsocket() {
        if (this.gameLiveController != null) {
            LOGGER.debug("broadcastGameStep start");
            this.gameLiveController.broadcastGameStep(this);
            LOGGER.debug("broadcastGameStep done");
        } else {
            LOGGER.info("GameLiveController is null. Can't send Websocket Update!");
        }
    }

    private void checkIsDone() {
        LOGGER.info("[{}] Check is Player Death!", this.getId());

        List<Long> basesIds = this.gameField.getBases().values().stream().map(BaseBU::getPlayerId).filter(playerId -> playerId != 0).distinct().toList();
        List<Long> actionsIds = this.gameField.getBoardActions().stream().map(BoardActionsBU::getPlayer).distinct().toList();
        List<Long> activePlayer = Stream.concat(basesIds.stream(), actionsIds.stream()).distinct().toList();

        LOGGER.info("[{}] Player old {} -> {}!", this.getId(), this.remainingPlayers, activePlayer.size());

        if (this.remainingPlayers != activePlayer.size()) {
            storeDeathPlayerToDatabase(activePlayer);
        }
        this.remainingPlayers = activePlayer.size();


        LOGGER.debug("checkIsDone: remainingPlayers={}", this.remainingPlayers);
        if (this.remainingPlayers <= 1) {
            LOGGER.info("[{}] Game ist done, only one player remaining. Player {} Win!", this.getId(), activePlayer.get(0));
            Optional<PlayerDAO> playerDAOOptional = this.getGameDAO().getPlayers().stream().filter(playerDAO -> Objects.equals(playerDAO.getId(), activePlayer.get(0))).findFirst();
            playerDAOOptional.ifPresent(playerDAO -> setGameDAO(this.gameTickController.storeEliminatedTick(this.getGameDAO(), playerDAO, (tick + 1))));
            this.setStatusDone();
        }
    }

    void storeDeathPlayerToDatabase(List<Long> activePlayer) {
        LOGGER.info("[{}] Players diff -> Min one player is death!", this.getId());

        List<Long> allPlayers = this.players.stream().map(ActionProvider::getId).distinct().toList();
        LOGGER.debug("[{}] checkIsDone find death 1", this.getId());
        List<Long> deathPlayers = gameDAO.getPlayerEliminationTicks().keySet().stream().map(PlayerDAO::getId).toList();
        LOGGER.debug("[{}] checkIsDone find death 2", this.getId());

        List<Long> diff = new ArrayList<>(allPlayers);
        diff.removeAll(activePlayer);
        diff.removeAll(deathPlayers);

        LOGGER.info("[{}] Players with ids {} is now Death!", this.getId(), diff.toArray());

        diff.forEach(playerId -> {
            Optional<PlayerDAO> playerDAOOptional = this.getGameDAO().getPlayers().stream().filter(playerDAO -> Objects.equals(playerDAO.getId(), playerId)).findFirst();
            if (playerDAOOptional.isPresent()) {
                PlayerDAO playerDAO = playerDAOOptional.get();
                setGameDAO(this.gameTickController.storeEliminatedTick(this.getGameDAO(), playerDAO, tick));
            }
            LOGGER.info("[{}] Players with id {} is now Death!", this.getId(), playerId);
        });
        LOGGER.info("[{}] Players is in database!", this.getId());
    }

    private void requestPlayerActions() {
        players.forEach(player -> {
            if (this.gameField.getBases().values().stream().noneMatch(base -> base.getPlayerId() == player.getId())) {
                return;
            }

            List<PlayerActionBU> playerActions = player.requestStep(this);
            playerActions.forEach(playerAction -> {
                BaseBU sourceBase = this.gameField.getBases().get(playerAction.getSource());
                BaseBU destinationBase = this.gameField.getBases().get(playerAction.getDestination());

                if (sourceBase == null || destinationBase == null) {
                    LOGGER.info(String.format("[%d] Player %d try to use illegal Action (sourceBase or destinationBase is null)! %d -> %d: %d", this.id, player.getId(), playerAction.getSource(), playerAction.getDestination(), playerAction.getAmount()));
                    return;
                }
                if (verifyPlayerAction(sourceBase, player.getId(), playerAction)) {
                    int distance = sourceBase.calcDistance(destinationBase);
                    sourceBase.moveAway(playerAction.getAmount());
                    this.gameField.getBoardActions().add(new BoardActionsBU(player.getId(), playerAction, distance));
                } else {
                    LOGGER.info(String.format("[%d] Player %d try to use illegal Action (verifyPlayerAction)! %d -> %d: %d", this.id, player.getId(), playerAction.getSource(), playerAction.getDestination(), playerAction.getAmount()));
                }
            });
        });
    }

    private void takeFight() {
        gameField.getBoardActions().stream().filter(BoardActionsBU::isInDestination).forEach(boardActions -> {
            BaseBU sourceBase = this.gameField.getBases().get(boardActions.getSource());
            BaseBU destinationBase = this.gameField.getBases().get(boardActions.getDestination());
            if (sourceBase == null || destinationBase == null) {
                throw new RuntimeException("Illegal Action: Base " + boardActions.getSource() + " or " + boardActions.getDestination() + " are null");
            }

            if (sourceBase.equals(destinationBase)) {
                sourceBase.upgrade(boardActions.getAmount(), this.gameConfig.getBaseLevelsConfig());
            } else if (destinationBase.getPlayerId() == boardActions.getPlayer()) {
                destinationBase.moveAway(-boardActions.getAmount());
            } else {
                destinationBase.fight(boardActions);
            }
        });
    }


    public void cleanup() {
        List<BoardActionsBU> newBoardActions = this.gameField.getBoardActions().stream()
                .filter(i -> !i.isInDestination())
                .filter(i -> i.getAmount() > 0)
                .toList();
        gameField.getBoardActions().clear();
        gameField.getBoardActions().addAll(newBoardActions);
    }

    private boolean verifyPlayerAction(BaseBU baseBU, long playerId, PlayerActionBU playerAction) {
        return baseBU.getPlayerId() == playerId &&
                baseBU.getPopulation() >= playerAction.getAmount() &&
                playerAction.getAmount() > 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameBU gameBU = (GameBU) o;
        return id == gameBU.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public void loadGame(GameTickDAO gameTickDAO) {
        LOGGER.info("[{}] load GameBU from GameTickDAO with Tick {}", this.getId(), gameTickDAO.getTick());

        Board board = gameTickMapper.toBoard(gameTickDAO);
        GameBU newGame = gameBUMapper.toGameBU(board);

        this.gameField = newGame.gameField;
        this.gameConfig = newGame.gameConfig;
        this.id = newGame.id;
        this.tick = newGame.tick + 1;
        this.remainingPlayers = newGame.remainingPlayers;

        if (this.players.size() != board.getGame().getPlayerCount()) {
            LOGGER.error("[{}] Can't load game, Player Count is not egle! {} != {}", this.getId(), this.players.size(), board.getGame().getPlayerCount());
            throw new IllegalStateException("Can't load game, Player Count is not egle!");
        }

    }
}
