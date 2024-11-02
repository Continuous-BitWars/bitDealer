package de.bitwars.business_logic.mapper;

import de.bitwars.api.models.clients.*;
import de.bitwars.business_logic.moduels.GameBU;
import de.bitwars.business_logic.moduels.PlayerActionBU;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class GameBUMapper {

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

        Game game = new Game(
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
