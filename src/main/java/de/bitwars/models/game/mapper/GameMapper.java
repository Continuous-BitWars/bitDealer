package de.bitwars.models.game.mapper;

import de.bitwars.api.models.Game;
import de.bitwars.api.models.GameMap;
import de.bitwars.api.models.GameOptions;
import de.bitwars.api.models.Player;
import de.bitwars.models.game.dao.GameDAO;
import de.bitwars.models.game.repository.GameRepository;
import de.bitwars.models.gameMap.mapper.GameMapMapper;
import de.bitwars.models.gameTick.repository.GameTickRepository;
import de.bitwars.models.league.mapper.LeagueMapper;
import de.bitwars.models.player.mapper.PlayerMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class GameMapper {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameMapper.class);


    @Inject
    GameRepository gameRepository;

    @Inject
    GameTickRepository gameTickRepository;

    @Inject
    GameMapMapper gameMapMapper;

    @Inject
    PlayerMapper playerMapper;
    @Inject
    LeagueMapper leagueMapper;

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
                //TODO: add mapping for myGameDAO.setLeagueDAO();
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
                new HashMap<>(),
                null,
                null
        );
    }

    @Transactional
    public Game toGame(GameDAO gameDAO) {
        if (gameDAO == null) {
            LOGGER.warn("Received null GameDAO. Returning null.");
            return null;
        }

        long startTotal = System.nanoTime();
        LOGGER.debug("Starting conversion of GameDAO (ID: {}) to Game.", gameDAO.getId());

        // Schritt 1: Eliminated Players umwandeln
        long startEliminatedPlayers = System.nanoTime();
        HashMap<Long, Integer> eliminatedPlayers = new HashMap<>();
        gameDAO.getPlayerEliminationTicks().forEach((playerDAO, integer) ->
                eliminatedPlayers.put(playerDAO.getId(), integer)
        );
        long endEliminatedPlayers = System.nanoTime();
        LOGGER.debug("Converted eliminated players in {} ms", (endEliminatedPlayers - startEliminatedPlayers) / 1_000_000);

        // Schritt 2: Players umwandeln
        long startPlayers = System.nanoTime();
        List<Player> players = gameDAO.getPlayers().stream()
                .map(playerMapper::toPlayer)
                .toList();
        long endPlayers = System.nanoTime();
        LOGGER.debug("Converted players in {} ms", (endPlayers - startPlayers) / 1_000_000);

        // Schritt 3: GameMap umwandeln
        long startGameMap = System.nanoTime();
        GameMap gameMap = gameMapMapper.toGameMap(gameDAO.getMap());
        long endGameMap = System.nanoTime();
        LOGGER.debug("Converted game map in {} ms", (endGameMap - startGameMap) / 1_000_000);

        // Schritt 5: Game-Objekt erstellen
        long startCreation = System.nanoTime();
        Game game = new Game(
                gameDAO.getId(),
                gameDAO.getName(),
                players,
                gameMap,
                new GameOptions(gameDAO.getTimeBetweenTicks()),
                gameDAO.getStatus(),
                Optional.ofNullable(gameDAO.getGameTicksCount()).orElse((int) gameTickRepository.countGameTicksFromGame(gameDAO)),
                eliminatedPlayers,
                gameDAO.getLeague().getId()
        );
        long endCreation = System.nanoTime();
        LOGGER.debug("Created Game object in {} ms", (endCreation - startCreation) / 1_000_000);

        long endTotal = System.nanoTime();
        LOGGER.debug("Finished converting GameDAO (ID: {}) to Game in {} ms", gameDAO.getId(), (endTotal - startTotal) / 1_000_000);

        return game;
    }
}
