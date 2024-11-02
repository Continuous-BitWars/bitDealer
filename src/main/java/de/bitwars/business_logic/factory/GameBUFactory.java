package de.bitwars.business_logic.factory;

import de.bitwars.business_logic.mapper.GameBUMapper;
import de.bitwars.business_logic.moduels.GameBU;
import de.bitwars.live.GameLiveController;
import de.bitwars.models.game.dao.GameDAO;
import de.bitwars.models.gameTick.GameTickController;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class GameBUFactory {

    @Inject
    GameTickController gameTickController;

    @Inject
    GameBUMapper gameBUMapper;

    @Inject
    GameLiveController gameLiveController;

    public GameBU createGameBU(GameDAO gameDAO) {
        GameBU gameBU = new GameBU(gameDAO);
        gameBU.setGameLiveController(gameLiveController);
        gameBU.setGameTickController(gameTickController);
        gameBU.setGameBUMapper(gameBUMapper);
        return gameBU;
    }
}