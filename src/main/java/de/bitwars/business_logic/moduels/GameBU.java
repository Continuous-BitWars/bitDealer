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
import java.util.stream.Stream;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ApplicationScoped
public class GameBU implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(GameBU.class);

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
        this.entityManager.merge(this.getGameDAO());
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
        log.info("[{}] Tick {} is Start!", this.getId(), this.tick);

        if (!this.gameStatus.equals(GameStatus.RUNNING)) {
            log.info("Cancel GameStep, GameStatus is not Running: {} -> {}", this.getId(), this.getName());
            this.sendGameStateToWebsocket();
            return;
        }
        log.debug("requestPlayerActions: {}", this.getId());
        requestPlayerActions();

        log.debug("getBases: {}", this.getId());
        gameField.getBases().values().forEach(base -> base.takeTick(this.gameConfig.getBaseLevelsConfig()));

        log.debug("getBoardActions: {}", this.getId());
        gameField.getBoardActions().forEach(boardActions -> boardActions.takeTick(this.gameConfig.getPathsConfig()));

        log.debug("takeFight: {}", this.getId());
        takeFight();

        this.gameField.getBases().values().forEach(base -> {
            log.debug("{} - {}: {}", base.getUid(), base.getPlayerId(), base.getPopulation());
        });
        this.gameField.getBoardActions().forEach(actions -> {
            log.debug(actions.getPlayer() + ": " + actions.getSource() + " -> " + actions.getDestination() + " | " + actions.getAmount() + " | " + actions.getProgress().getTraveled() + "/" + actions.getProgress().getDistance());
        });
        log.debug("-----");


        storeGameState();
        sendGameStateToWebsocket();


        cleanup();
        checkIsDone();

        log.info("[{}] Tick {} is Done!", this.getId(), this.tick);
        this.tick++;

    }

    private void storeGameState() {
        log.debug("[{}] storeGameState", this.getId());
        Board board = this.gameBUMapper.toBoard(this, 0);
        String message = null;
        try {
            message = objectMapper.writeValueAsString(board);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        GameTickDAO gameTickDAO = this.gameTickController.storeTick(tick, message, gameDAO);
        log.debug("[{}] gameTickDAO from Tick {} has id: {}", this.getId(), gameTickDAO.getTick(), gameTickDAO.getId());

    }

    private void sendGameStateToWebsocket() {
        if (this.gameLiveController != null) {
            log.debug("broadcastGameStep start");
            this.gameLiveController.broadcastGameStep(this);
            log.debug("broadcastGameStep done");
        } else {
            log.info("GameLiveController is null. Can't send Websocket Update!");
        }
    }

    private void checkIsDone() {
        List<Long> basesIds = this.gameField.getBases().values().stream().map(BaseBU::getPlayerId).filter(playerId -> playerId != 0).distinct().toList();
        List<Long> actionsIds = this.gameField.getBoardActions().stream().map(BoardActionsBU::getPlayer).distinct().toList();
        List<Long> activePlayer = Stream.concat(basesIds.stream(), actionsIds.stream()).distinct().toList();
        this.remainingPlayers = activePlayer.size();

        log.debug("checkIsDone: remainingPlayers={}", this.remainingPlayers);
        if (this.remainingPlayers <= 1) {
            log.info("[{}] Game ist done, only one player remaining", this.getId());
            this.setStatusDone();
        }
    }

    private void requestPlayerActions() {
        players.forEach(player -> {
            //TODO: use parallelStream
            if (this.gameField.getBases().values().stream().noneMatch(base -> base.getPlayerId() == player.getId())) {
                return;
            }

            List<PlayerActionBU> playerActions = player.requestStep(this);
            playerActions.forEach(playerAction -> {
                BaseBU sourceBase = this.gameField.getBases().get(playerAction.getSource());
                BaseBU destinationBase = this.gameField.getBases().get(playerAction.getDestination());

                if (sourceBase == null || destinationBase == null) {
                    log.info(String.format("[%d] Player %d try to use illegal Action! %d -> %d: %d", this.id, player.getId(), playerAction.getSource(), playerAction.getDestination(), playerAction.getAmount()));
                    return;
                }
                if (verifyPlayerAction(sourceBase, player.getId(), playerAction)) {
                    int distance = sourceBase.calcDistance(destinationBase);
                    sourceBase.moveAway(playerAction.getAmount());
                    this.gameField.getBoardActions().add(new BoardActionsBU(player.getId(), playerAction, distance));
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

    private void cleanup() {
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
        log.info("[{}] load GameBU from GameTickDAO with Tick {}", this.getId(), gameTickDAO.getTick());

        Board board = gameTickMapper.toBoard(gameTickDAO);
        GameBU newGame = gameBUMapper.toGameBU(board);

        this.gameField = newGame.gameField;
        this.gameConfig = newGame.gameConfig;
        this.id = newGame.id;
        this.tick = newGame.tick + 1;
        this.remainingPlayers = newGame.remainingPlayers;

        if (this.players.size() != board.getGame().getPlayerCount()) {
            log.error("[{}] Can't load game, Player Count is not egle! {} != {}", this.getId(), this.players.size(), board.getGame().getPlayerCount());
            throw new IllegalStateException("Can't load game, Player Count is not egle!");
        }

    }
}
