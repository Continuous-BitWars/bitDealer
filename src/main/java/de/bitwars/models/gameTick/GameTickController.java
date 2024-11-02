package de.bitwars.models.gameTick;

import de.bitwars.models.game.dao.GameDAO;
import de.bitwars.models.gameTick.dao.GameTickDAO;
import de.bitwars.models.gameTick.repository.GameTickRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class GameTickController {

    @Inject
    GameTickRepository gameTickRepository;


    @Transactional
    public GameTickDAO storeTick(int tickId, String gameJson, GameDAO gameDAO) {
        GameTickDAO gameTickDAO = new GameTickDAO(null, tickId, gameJson, gameDAO);
        gameTickRepository.persist(gameTickDAO);
        return gameTickDAO;
    }
}
