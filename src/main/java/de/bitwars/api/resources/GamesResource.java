package de.bitwars.api.resources;

import de.bitwars.api.interfaces.GamesApi;
import de.bitwars.api.models.Game;
import de.bitwars.api.models.GameOptions;
import de.bitwars.api.models.Player;

import java.util.List;

public class GamesResource implements GamesApi {

    @Override
    public Player addPlayerToGame(Integer gameId, Player player) {
        return null;
    }

    @Override
    public Game createGame(Game game) {
        return null;
    }

    @Override
    public void deleteGame(Integer gameId) {

    }

    @Override
    public Game getGameById(Integer gameId) {
        return null;
    }

    @Override
    public List<Game> listGames() {
        return List.of();
    }

    @Override
    public List<Player> listPlayersInGame(Integer gameId) {
        return List.of();
    }

    @Override
    public void removePlayerFromGame(Integer gameId, Integer playerId) {

    }

    @Override
    public Game startGame(Integer gameId, GameOptions gameOptions) {
        return null;
    }

    @Override
    public Game stopGame(Integer gameId) {
        return null;
    }

    @Override
    public Game updateGame(Integer gameId, Game game) {
        return null;
    }
}
