package de.bitwars.models.gameTick;

import de.bitwars.models.game.GameController;
import de.bitwars.models.game.dao.GameDAO;
import de.bitwars.models.gameTick.dao.GameTickDAO;
import de.bitwars.models.gameTick.repository.GameTickRepository;
import de.bitwars.models.player.dao.PlayerDAO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@ApplicationScoped
public class GameTickController {
    private static final Logger Logger = LoggerFactory.getLogger(GameTickController.class);


    @Inject
    GameTickRepository gameTickRepository;

    @Inject
    GameController gameController;

    @Transactional
    public GameTickDAO storeTick(int tickId, String gameJson, GameDAO gameDAO) {
        return this.storeTick(tickId, gameJson, gameDAO, 5);
    }

    @Transactional
    public GameTickDAO storeTick(int tickId, String gameJson, GameDAO gameDAO, int retrys) {
        Optional<GameDAO> newGame = gameController.getGameById(gameDAO.getId());
        if (newGame.isPresent()) {
            GameDAO game = newGame.get();
            GameTickDAO gameTickDAO = new GameTickDAO(null, tickId, gameJson, game);
            gameTickRepository.persist(gameTickDAO);
            return gameTickDAO;
        } else {
            Logger.error("Could not find game wit id {} for storeTick {}: (retry: {})", gameDAO.getId(), tickId, retrys);
            if (retrys > 0) {
                return this.storeTick(tickId, gameJson, gameDAO, retrys - 1);
            } else {
                throw new IllegalStateException("Could not find game wit id after all retrys!" + gameDAO.getId());
            }
        }
    }


    @Transactional
    public GameDAO storeEliminatedTick(GameDAO gameDAO, PlayerDAO playerDAO, int tick) {
        Optional<GameDAO> newGame = gameController.getGameById(gameDAO.getId());
        if (newGame.isPresent()) {
            GameDAO game = newGame.get();
            game.getPlayerEliminationTicks().putIfAbsent(playerDAO, tick);
            return game;
        } else {
            throw new RuntimeException("Could not find game for storeEliminatedTick: " + gameDAO.getId());
        }
    }


}
