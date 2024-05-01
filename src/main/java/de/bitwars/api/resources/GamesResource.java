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
    public void deleteGame(long gameId) {

    }

    @Override
    public Game getGameById(long gameId) {
        return null;
    }

    @Override
    public List<Game> listGames() {
        return List.of();
    }

    @Override
    public List<Player> listPlayersInGame(long gameId) {
        return List.of();
    }

    @Override
    public void removePlayerFromGame(long gameId, long playerId) {

    }

    @Override
    public Game startGame(long gameId, GameOptions gameOptions) {
        return null;
    }

    @Override
    public Game stopGame(long gameId) {
        return null;
    }

    @Override
    public Game updateGame(long gameId, Game game) {
        return null;
    }
}
