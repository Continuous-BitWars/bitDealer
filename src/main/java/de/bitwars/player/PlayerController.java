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
package de.bitwars.player;

import de.bitwars.player.dao.PlayerDAO;
import de.bitwars.player.repository.PlayerRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class PlayerController {

    @Inject
    PlayerRepository playerRepository;

    @Transactional
    public PlayerDAO createPlayer(PlayerDAO player) {
        playerRepository.persist(player);
        return player;
    }

    @Transactional
    public void deletePlayer(long playerId) {
        Optional<PlayerDAO> playerDAO = playerRepository.findByIdOptional(playerId);
        playerDAO.ifPresent(dao -> playerRepository.delete(dao));
    }

    public Optional<PlayerDAO> getPlayerById(long playerId) {
        return playerRepository.findByIdOptional(playerId);
    }

    public List<PlayerDAO> listPlayers() {
        return playerRepository.listAll();
    }

    @Transactional
    public PlayerDAO updatePlayer(PlayerDAO newPlayer) {
        Optional<PlayerDAO> playerDAO = playerRepository.findByIdOptional(newPlayer.getId());
        if (playerDAO.isPresent()) {
            PlayerDAO player = playerDAO.get();
            player.setName(newPlayer.getName());
            player.setProviderUrl(newPlayer.getProviderUrl());
            player.setColor(newPlayer.getColor());
            return player;
        }
        throw new NotFoundException(String.format("Player with id %s not found", newPlayer.getId()));
    }
}
