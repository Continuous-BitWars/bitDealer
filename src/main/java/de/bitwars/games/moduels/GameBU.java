package de.bitwars.games.moduels;

import de.bitwars.live.GameLiveController;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GameBU implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(GameBU.class);
    private long id;
    private String name;
    private Long tick;
    private Duration tickSpeed;
    private int remainingPlayers;
    private GameStatus gameStatus;

    private List<ActionProvider> players;
    private GameFieldBU gameField;
    private GameMapBU gameMap;
    private GameConfigBU gameConfig;

    private GameLiveController gameLiveController;


    public GameBU(long gameId, String name, GameConfigBU gameConfig, GameMapBU gameMap, GameLiveController gameLiveController) {
        this.id = gameId;
        this.gameConfig = gameConfig;
        this.gameMap = gameMap;
        this.name = name;
        this.gameField = new GameFieldBU(gameMap);
        this.gameStatus = GameStatus.STOPPED;
        this.players = new ArrayList<>();
        this.tickSpeed = Duration.ofSeconds(1);
        this.tick = 0L;
        this.remainingPlayers = 0;
        this.gameLiveController = gameLiveController;
    }

    public GameBU(long gameId) {
        this(gameId, "", null, null, null);
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

        this.gameMap.getBases().stream()
                .filter(base -> base.getPlayerId() == players.size())
                .map(BaseBU::getUid)
                .forEach(baseId -> this.gameField.getBases().get(baseId).setPlayerId(player.getId()));

        this.remainingPlayers++;
    }

    @Override
    public void run() {
        if (!this.gameStatus.equals(GameStatus.RUNNING)) {
            return;
        }
        //TODO: replace all startplayer to Free!
        //TODO: check if Player in Game

        requestPlayerActions();
        gameField.getBases().values().forEach(base -> base.takeTick(this.gameConfig.getBaseLevelsConfig()));
        gameField.getBoardActions().forEach(boardActions -> boardActions.takeTick(this.gameConfig.getPathsConfig()));
        takeFight();

        this.gameField.getBases().values().forEach(base -> {
            log.debug("{} - {}: {}", base.getUid(), base.getPlayerId(), base.getPopulation());
        });
        this.gameField.getBoardActions().forEach(actions -> {
            log.debug(actions.getPlayer() + ": " + actions.getSource() + " -> " + actions.getDestination() + " | " + actions.getAmount() + " | " + actions.getProgress().getTraveled() + "/" + actions.getProgress().getDistance());
        });
        log.debug("-----");

        cleanup();
        checkIsDone();


        //TODO: Store Step

        if (this.gameLiveController != null) {
            this.gameLiveController.broadcastGameStep(this);
        } else {
            log.info("GameLiveController is null. Can't send Websocket Update!");
        }

        this.tick++;
    }


    private void checkIsDone() {
        this.remainingPlayers = this.gameField.getBases().values().stream().map(BaseBU::getPlayerId).filter(playerId -> playerId != 0).distinct().toList().size();
        if (this.remainingPlayers <= 1) {
            this.gameStatus = GameStatus.DONE;
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
                baseBU.getPopulation() >= playerAction.getAmount();
    }

    public void removePlayer(long playerId) {
        throw new NotImplementedException();
        //TODO: Implemented
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
}