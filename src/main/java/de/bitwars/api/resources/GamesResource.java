/*
 * Copyright (C) 2024 Andreas Heinrich
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.bitwars.api.resources;

import de.bitwars.api.interfaces.GamesApi;
import de.bitwars.api.models.Game;
import de.bitwars.api.models.GameOptions;
import de.bitwars.api.models.Player;
import de.bitwars.games.Config;
import de.bitwars.games.GameController;
import de.bitwars.games.MapController;
import de.bitwars.games.mapper.GameBUMapper;
import de.bitwars.games.moduels.ActionProvider;
import de.bitwars.games.moduels.GameBU;
import de.bitwars.games.moduels.player.DummyPlayer;
import de.bitwars.games.moduels.player.RemotePlayer;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;

import java.util.List;

@ApplicationScoped
@RequiredArgsConstructor
public class GamesResource implements GamesApi {

    private final GameController gameController;
    private final GameBUMapper gameBUMapper;
    private final PlayersResource playersResource;
    private final MapController mapController;

    @Override
    public Game addPlayerToGame(Integer gameId, Player player) {
        if (player.getId() < 100) {
            player = playersResource.getPlayerById(player.getId());
        }

        ActionProvider actionProvider;
        if (player.getProviderUrl().startsWith("http")) {
            actionProvider = new RemotePlayer(player.getId(), player.getName(), player.getProviderUrl(), player.getColor(), gameBUMapper);
        } else {
            actionProvider = new DummyPlayer(player.getId(), player.getColor());
        }

        GameBU gameBU = this.gameController.addPlayerToGame(gameId, actionProvider);
        return this.gameBUMapper.toGame(gameBU);
    }

    @Override
    public Game createGame(Game game) {
        String mapUrl = game.getMap();
        GameBU gameBU = this.gameController.createGame(game.getName(), Config.defaultOptions, mapController.loadFromUrl(mapUrl));
        return this.gameBUMapper.toGame(gameBU);
    }

    @Override
    public void deleteGame(long gameId) {
        this.gameController.deleteGame(gameId);
    }

    @Override
    public Game getGameById(long gameId) {
        GameBU gameBU = this.gameController.getGameById(gameId);
        return this.gameBUMapper.toGame(gameBU);
    }

    @Override
    public List<Game> listGames() {
        List<GameBU> gameBUs = this.gameController.getGames();
        return gameBUs.stream().map(this.gameBUMapper::toGame).toList();
    }

    @Override
    public List<Player> listPlayersInGame(long gameId) {
        Game game = this.getGameById(gameId);
        return game.getPlayers();
    }

    @Override
    public Game removePlayerFromGame(long gameId, long playerId) {
        GameBU gameBU = this.gameController.removePlayerFromGame(gameId, playerId);
        return this.gameBUMapper.toGame(gameBU);
    }

    @Override
    public Game startGame(long gameId, GameOptions gameOptions) {
        GameBU gameBU = this.gameController.startGame(gameId, gameOptions.getTimeBetweenTicks());
        return this.gameBUMapper.toGame(gameBU);
    }

    @Override
    public Game stopGame(long gameId) {
        GameBU gameBU = this.gameController.stopGame(gameId);
        return this.gameBUMapper.toGame(gameBU);
    }

    @Override
    public Game updateGame(long gameId, Game game) {
        throw new NotImplementedException();
    }
}
