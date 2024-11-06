package de.bitwars.models.game;

import de.bitwars.api.models.StatusEnum;
import de.bitwars.business_logic.GameLogic;
import de.bitwars.models.game.dao.GameDAO;
import de.bitwars.models.game.repository.GameRepository;
import de.bitwars.models.gameMap.GameMapController;
import de.bitwars.models.gameMap.dao.GameMapDAO;
import de.bitwars.models.gameTick.dao.GameTickDAO;
import de.bitwars.models.player.dao.PlayerDAO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@ApplicationScoped
public class GameController {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameController.class);

    @Inject
    GameRepository gameRepository;
    @Inject
    GameLogic gameLogic;
    @Inject
    GameMapController gameMapController;
    @Inject
    EntityManager entityManager;


    @Transactional
    public GameDAO createGame(String name, Long mapId) {
        Optional<GameMapDAO> gameMapDAO = gameMapController.getGameMapById(mapId);
        if (gameMapDAO.isEmpty()) {
            //TODO: Create GameMap is not exist???
            throw new NotFoundException(String.format("GameMap with id %d not found", mapId));
        }
        GameDAO gameDAO = new GameDAO(name, gameMapDAO.get());
        gameRepository.persist(gameDAO);

        entityManager.flush();

        if (gameDAO.getId() == null) {
            throw new PersistenceException(String.format("Failed to persist the game with name: %s.", name));
        }

        return gameDAO;
    }

    @Transactional
    public void deleteGame(long gameId) {
        Optional<GameDAO> gameDAO = getGameById(gameId);
        if (gameDAO.isPresent()) {
            GameDAO game = gameDAO.get();
            if (game.getStatus().equals(StatusEnum.PENDING)) {
                gameRepository.delete(game);
            } else {
                throw new IllegalStateException(String.format("Can't delete Game with id %d, game is running.", gameId));
            }
        } else {
            throw new NotFoundException(String.format("Game with id %d not found", gameId));
        }
    }

    public Optional<GameDAO> getGameById(long gameId) {
        return gameRepository.findByIdOptional(gameId);
    }

    public List<GameDAO> listGames() {
        return gameRepository.listAll();
    }

    @Transactional
    public GameDAO addPlayerToGame(long gameId, PlayerDAO playerDAO) {
        Optional<GameDAO> game = getGameById(gameId);
        if (game.isPresent()) {
            GameDAO gameDAO = game.get();

            if (gameDAO.getPlayers().contains(playerDAO)) {
                throw new IllegalStateException(String.format("Can't add player to game %s: Player already in Game", gameId));
            }
            if (gameDAO.getPlayers().size() >= gameDAO.getMap().getMaxPlayerCount()) {
                throw new IllegalStateException(String.format("Can't add player to game %s: Game is full", gameId));
            }

            if (gameDAO.getStatus().equals(StatusEnum.PENDING)) {
                gameDAO.getPlayers().add(playerDAO);
            } else {
                throw new IllegalStateException("Game is not in pending state.");
            }
            return gameDAO;
        }
        throw new NotFoundException(String.format("Game with id %d not found.", gameId));
    }

    @Transactional
    public GameDAO removePlayerFromGame(long gameId, long playerId) {
        Optional<GameDAO> game = getGameById(gameId);
        if (game.isPresent()) {
            GameDAO gameDAO = game.get();

            if (gameDAO.getStatus().equals(StatusEnum.PENDING)) {
                boolean succeed = gameDAO.removePlayerById(playerId);
                if (!succeed) {
                    throw new IllegalArgumentException("Player not found.");
                }
            } else {
                throw new IllegalStateException("Game is running.");
            }
            return gameDAO;
        }
        throw new NotFoundException(String.format("Game with id %d not found.", gameId));
    }

    @Transactional
    public GameDAO startGame(long gameId, int timeBetweenTicks) {
        Optional<GameDAO> game = getGameById(gameId);
        if (game.isPresent()) {
            GameDAO gameDAO = game.get();


            if (gameDAO.getStatus().equals(StatusEnum.DONE)) {
                LOGGER.info("[{}] Game already done.", gameId);
                throw new IllegalStateException("Game already Done.");
            }
            if (gameDAO.getStatus().equals(StatusEnum.RUNNING)) {
                LOGGER.info("[{}] Game already running.", gameId);
                throw new IllegalStateException("Game already running.");
            }
            if (gameDAO.getPlayers().size() < 2) {
                LOGGER.info("[{}] Game has to less Players than 2.", gameId);
                throw new IllegalStateException("Game has to less Players than 2.");
            }
            if (timeBetweenTicks <= 0) {
                LOGGER.info("[{}] Time between ticks must be greater than zero.", gameId);
                throw new IllegalStateException("Time between ticks must be greater than zero.");
            }

            gameDAO.setTimeBetweenTicks(timeBetweenTicks);


            boolean succeed = gameLogic.startGame(gameDAO);
            if (!succeed) {
                throw new RuntimeException(String.format("Can't start game with id %d.", gameId));
            }
            return gameDAO;
        }
        throw new NotFoundException(String.format("Game with id %d not found.", gameId));
    }

    @Transactional
    public GameDAO stopGame(long gameId) {
        Optional<GameDAO> game = getGameById(gameId);
        if (game.isPresent()) {
            GameDAO gameDAO = game.get();

            if (!gameDAO.getStatus().equals(StatusEnum.RUNNING)) {
                throw new IllegalStateException("Game is not Running.");
            }

            boolean succeed = gameLogic.stopGame(gameDAO.getId());
            gameDAO.setStatus(StatusEnum.STOPPED);
            if (!succeed) {
                LOGGER.info("[{}] Stopping was not successfully for Game!.", gameId);
            }
            return gameDAO;
        }
        LOGGER.info("[{}] Game not found to stop.", gameId);
        throw new NotFoundException("Game not found.");
    }

    @Transactional
    public GameDAO updateGame(GameDAO newGame) {
        Optional<GameDAO> gameDAO = gameRepository.findByIdOptional(newGame.getId());
        if (gameDAO.isPresent()) {
            GameDAO game = gameDAO.get();

            if (!game.getStatus().equals(StatusEnum.PENDING)) {
                throw new IllegalStateException("Game is Running.");
            }
            game.setName(newGame.getName());

            if (!Objects.equals(newGame.getMap().getId(), game.getMap().getId())) {
                Optional<GameMapDAO> gameMapDAO = gameMapController.getGameMapById(newGame.getMap().getId());
                gameMapDAO.ifPresent(game::setMap);
            }
            return game;
        }
        throw new NotFoundException("Game not found.");
    }

    public List<GameTickDAO> getBoardTicksByGameId(long gameId) {
        Optional<GameDAO> gameDAO = this.getGameById(gameId);
        return gameDAO
                .orElseThrow(() -> new NotFoundException(String.format("Game with id %d not found.", gameId)))
                .getGameTicks();
    }
}
