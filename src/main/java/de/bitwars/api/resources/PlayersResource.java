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

import de.bitwars.api.interfaces.PlayersApi;
import de.bitwars.api.models.Player;

import java.util.List;

public class PlayersResource implements PlayersApi {

    @Override
    public Player createPlayer(Player player) {
        return null;
    }

    @Override
    public void deletePlayer(Integer playerId) {
        return;
    }

    @Override
    public Player getPlayerById(Integer playerId) {
        return null;
    }

    @Override
    public List<Player> listPlayers() {
        return List.of();
    }

    @Override
    public Player updatePlayer(Integer playerId, Player player) {
        return null;
    }
}
