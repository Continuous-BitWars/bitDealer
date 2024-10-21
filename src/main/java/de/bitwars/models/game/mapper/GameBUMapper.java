package de.bitwars.models.game.mapper;

import de.bitwars.api.models.Game;
import de.bitwars.api.models.GameOptions;
import de.bitwars.api.models.Player;
import de.bitwars.api.models.StatusEnum;
import de.bitwars.api.models.clients.Base;
import de.bitwars.api.models.clients.BaseLevel;
import de.bitwars.api.models.clients.Board;
import de.bitwars.api.models.clients.BoardActions;
import de.bitwars.api.models.clients.GameConfig;
import de.bitwars.api.models.clients.Paths;
import de.bitwars.api.models.clients.PlayerAction;
import de.bitwars.api.models.clients.Position;
import de.bitwars.api.models.clients.Progress;
import de.bitwars.models.game.moduels.GameBU;
import de.bitwars.models.game.moduels.PlayerActionBU;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class GameBUMapper {


    public Game toGame(GameBU gameBU) {
        List<Player> players = gameBU.getPlayers().stream().map(actionProvider -> new Player(actionProvider.getId(), actionProvider.getName(), actionProvider.getUrl(), actionProvider.getColor())).toList();
        GameOptions gameOptions = new GameOptions(((int) gameBU.getTickSpeed().getSeconds()));

        StatusEnum statusEnum = switch (gameBU.getGameStatus()) {
            case STOPPED -> StatusEnum.STOPPED;
            case DONE -> StatusEnum.DONE;
            case RUNNING -> StatusEnum.RUNNING;
        };

        return new Game(gameBU.getId(), gameBU.getName(), gameBU.getGameMap().getName(), players, gameOptions, statusEnum, gameBU.getTick());
    }

    public Board toBoard(GameBU gameBU, long playerId) {
        List<BoardActions> boardActions = gameBU.getGameField().getBoardActions().stream().map(boardActionsBU -> new BoardActions(
                boardActionsBU.getUuid().toString(),
                boardActionsBU.getPlayer(),
                boardActionsBU.getSource(),
                boardActionsBU.getDestination(),
                boardActionsBU.getAmount(),
                new Progress(boardActionsBU.getProgress().getDistance(), boardActionsBU.getProgress().getTraveled())
        )).toList();

        List<Base> bases = gameBU.getGameField().getBases().values().stream().map(baseBU -> new Base(
                baseBU.getUid(),
                baseBU.getPlayerId(),
                baseBU.getPopulation(),
                baseBU.getLevel(),
                baseBU.getUnitsUntilUpgrade(),
                new Position(baseBU.getBasePosition().getX(), baseBU.getBasePosition().getY(), baseBU.getBasePosition().getZ())
        )).toList();

        List<BaseLevel> baseLevels = gameBU.getGameConfig().getBaseLevelsConfig().stream().map(gameConfigBaseLevelsBU -> new BaseLevel(
                gameConfigBaseLevelsBU.getMaxPopulation(),
                gameConfigBaseLevelsBU.getUpgradeCost(),
                gameConfigBaseLevelsBU.getSpawnRate()
        )).toList();
        Paths paths = new Paths(gameBU.getGameConfig().getPathsConfig().getGracePeriod(), gameBU.getGameConfig().getPathsConfig().getDeathRate());
        GameConfig gameConfig = new GameConfig(baseLevels, paths);

        de.bitwars.api.models.clients.Game game = new de.bitwars.api.models.clients.Game(
                gameBU.getId(),
                gameBU.getTick(),
                gameBU.getPlayers().size(),
                gameBU.getRemainingPlayers(),
                playerId

        );
        return new Board(boardActions, bases, gameConfig, game);
    }

    public PlayerActionBU toPlayerActionBU(PlayerAction playerAction) {
        return new PlayerActionBU(playerAction.getSrc(), playerAction.getDest(), playerAction.getAmount());
    }
}
