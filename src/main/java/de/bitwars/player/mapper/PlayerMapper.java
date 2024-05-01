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
package de.bitwars.player.mapper;

import de.bitwars.api.models.Player;
import de.bitwars.player.dao.PlayerDAO;
import de.bitwars.player.repository.PlayerRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;

@ApplicationScoped
public class PlayerMapper {

    @Inject
    PlayerRepository playerRepository;

    public PlayerDAO toPlayerDAO(Player player) {
        if (player.getId() != null) {
            Optional<PlayerDAO> playerDAO = playerRepository.findByIdOptional(player.getId());
            if (playerDAO.isPresent()) {
                final PlayerDAO myPlayerDAO = playerDAO.get();
                myPlayerDAO.setName(player.getName());
                myPlayerDAO.setProviderUrl(player.getProviderUrl());
                return myPlayerDAO;
            }
        }
        return new PlayerDAO(null, player.getName(), player.getProviderUrl());
    }

    public Player toPlayer(PlayerDAO playerDAO) {
        if (playerDAO == null) {
            return null;
        }
        return new Player(playerDAO.getId(), playerDAO.getName(), playerDAO.getProviderUrl());
    }
}
