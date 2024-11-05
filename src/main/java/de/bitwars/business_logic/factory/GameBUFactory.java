package de.bitwars.business_logic.factory;

import de.bitwars.business_logic.mapper.GameBUMapper;
import de.bitwars.business_logic.moduels.GameBU;
import de.bitwars.live.GameLiveController;
import de.bitwars.models.game.dao.GameDAO;
import de.bitwars.models.gameTick.GameTickController;
import de.bitwars.models.gameTick.mapper.GameTickMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

@ApplicationScoped
public class GameBUFactory {

    @Inject
    GameTickController gameTickController;

    @Inject
    GameBUMapper gameBUMapper;

    @Inject
    GameLiveController gameLiveController;

    @Inject
    EntityManager entityManager;

    @Inject
    GameTickMapper gameTickMapper;

    public GameBU createGameBU(GameDAO gameDAO) {
        GameBU gameBU = new GameBU(gameDAO);
        gameBU.setGameLiveController(gameLiveController);
        gameBU.setGameTickController(gameTickController);
        gameBU.setGameBUMapper(gameBUMapper);
        gameBU.setEntityManager(entityManager);
        gameBU.setGameTickMapper(gameTickMapper);
        return gameBU;
    }
}