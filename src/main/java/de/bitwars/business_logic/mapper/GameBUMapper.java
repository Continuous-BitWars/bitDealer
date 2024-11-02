package de.bitwars.business_logic.mapper;

import de.bitwars.api.models.clients.*;
import de.bitwars.business_logic.moduels.*;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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

    public GameBU toGameBU(Board board) {
        GameBU gameBU = new GameBU();

        Map<Integer, BaseBU> bases = new HashMap<>();
        List<BoardActionsBU> boardActions = new ArrayList<>();

        board.getBases().forEach(base ->
                bases.put(
                        base.getUid(),
                        new BaseBU(
                                base.getUid(),
                                base.getPlayer(),
                                new BasePositionBU(
                                        base.getPosition().getX(),
                                        base.getPosition().getY(),
                                        base.getPosition().getZ()
                                ),
                                base.getPopulation(),
                                base.getLevel(),
                                base.getUnitsUntilUpgrade()
                        )
                )
        );

        board.getActions().forEach(boardAction -> boardActions.add(new BoardActionsBU(
                UUID.fromString(boardAction.getUuid()),
                boardAction.getPlayer(),
                boardAction.getSrc(),
                boardAction.getDest(),
                boardAction.getAmount(),
                new BoardActionsProgressBU(
                        boardAction.getProgress().getDistance(),
                        boardAction.getProgress().getTraveled()
                )
        )));


        GameConfigPathsBU pathsConfig = new GameConfigPathsBU(
                board.getConfig().getPaths().getGracePeriod(),
                board.getConfig().getPaths().getDeathRate()
        );

        List<GameConfigBaseLevelsBU> baseLevelsConfig = board.getConfig().getBaseLevels().stream().map(baseLevel -> new GameConfigBaseLevelsBU(baseLevel.getMaxPopulation(), baseLevel.getUpgradeCost(), baseLevel.getSpawnRate())).toList();


        gameBU.setGameField(new GameFieldBU(bases, boardActions));
        gameBU.setGameConfig(new GameConfigBU(pathsConfig, baseLevelsConfig));
        gameBU.setId(board.getGame().getUid());
        gameBU.setTick(board.getGame().getTick());
        gameBU.setRemainingPlayers(board.getGame().getRemainingPlayers());
        return gameBU;
    }


}
