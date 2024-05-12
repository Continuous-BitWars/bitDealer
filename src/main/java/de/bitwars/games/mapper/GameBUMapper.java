package de.bitwars.games.mapper;

import de.bitwars.api.models.Game;
import de.bitwars.api.models.GameOptions;
import de.bitwars.api.models.Player;
import de.bitwars.api.models.StatusEnum;
import de.bitwars.games.moduels.GameBU;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class GameBUMapper {


    public Game toGame(GameBU gameBU) {
        List<Player> players = gameBU.getPlayers().stream().map(actionProvider -> new Player(actionProvider.getId(), actionProvider.getName(), actionProvider.getUrl())).toList();
        GameOptions gameOptions = new GameOptions(((int) gameBU.getTickSpeed().getSeconds()));

        StatusEnum statusEnum = switch (gameBU.getGameStatus()) {
            case STOPPED -> StatusEnum.STOPPED;
            case DONE -> StatusEnum.DONE;
            case RUNNING -> StatusEnum.RUNNING;
        };

        return new Game(gameBU.getId(), gameBU.getName(), players, gameOptions, statusEnum, gameBU.getTick());
    }

}
