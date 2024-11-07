package de.bitwars.models.game.mapper;

import de.bitwars.api.models.Game;
import de.bitwars.api.models.GameOptions;
import de.bitwars.models.game.dao.GameDAO;
import de.bitwars.models.game.repository.GameRepository;
import de.bitwars.models.gameMap.mapper.GameMapMapper;
import de.bitwars.models.player.mapper.PlayerMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

@ApplicationScoped
public class GameMapper {

    @Inject
    GameRepository gameRepository;

    @Inject
    GameMapMapper gameMapMapper;

    @Inject
    PlayerMapper playerMapper;

    public GameDAO toGameDAO(Game game) {
        if (game.getId() != null) {
            Optional<GameDAO> gameDAO = gameRepository.findByIdOptional(game.getId());
            if (gameDAO.isPresent()) {
                final GameDAO myGameDAO = gameDAO.get();
                Optional.ofNullable(game.getName()).ifPresent(myGameDAO::setName);
                Optional.ofNullable(game.getGameOptions()).ifPresent(gameOptions -> {
                    myGameDAO.setTimeBetweenTicks(gameOptions.getTimeBetweenTicks());
                });
                Optional.ofNullable(game.getGameMap()).ifPresent(gameMap -> {
                    myGameDAO.setMap(gameMapMapper.toGameMapDAO(gameMap));
                });
                Optional.ofNullable(game.getStatus()).ifPresent(myGameDAO::setStatus);
                Optional.ofNullable(game.getPlayers()).ifPresent(players -> {
                    myGameDAO.setPlayers(players.stream().map(playerMapper::toPlayerDAO).toList());
                });
                //TODO: add mapping for myGameDAO.setPlayerEliminationTicks();
                return myGameDAO;
            }
        }
        return new GameDAO(
                null,
                game.getName(),
                game.getGameOptions().getTimeBetweenTicks(),
                gameMapMapper.toGameMapDAO(game.getGameMap()),
                game.getStatus(),
                game.getPlayers().stream().map(playerMapper::toPlayerDAO).toList(),
                new ArrayList<>(),
                new HashMap<>()
        );
    }

    public Game toGame(GameDAO gameDAO) {
        if (gameDAO == null) {
            return null;
        }
        HashMap<Long, Integer> eliminatedPlayers = new HashMap<>();
        gameDAO.getPlayerEliminationTicks().forEach((playerDAO, integer) -> eliminatedPlayers.put(playerDAO.getId(), integer));

        return new Game(
                gameDAO.getId(),
                gameDAO.getName(),
                gameDAO.getPlayers().stream().map(playerMapper::toPlayer).toList(),
                gameMapMapper.toGameMap(gameDAO.getMap()),
                new GameOptions(gameDAO.getTimeBetweenTicks()),
                gameDAO.getStatus(),
                gameDAO.getGameTicks().size(),
                eliminatedPlayers
        );
    }
}
