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
package de.bitwars.models.gameMap.mapper;

import de.bitwars.api.models.GameMap;
import de.bitwars.models.gameMap.dao.GameMapDAO;
import de.bitwars.models.gameMap.repository.GameMapRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;

@ApplicationScoped
public class GameMapMapper {

    @Inject
    GameMapRepository gameMapRepository;

    public GameMapDAO toGameMapDAO(GameMap gameMap) {
        if (gameMap.getId() != null) {
            Optional<GameMapDAO> gameMapDAO = gameMapRepository.findByIdOptional(gameMap.getId());
            if (gameMapDAO.isPresent()) {
                final GameMapDAO myGameDAO = gameMapDAO.get();
                myGameDAO.setName(gameMap.getName());
                myGameDAO.setJsonString(gameMap.getJsonString());
                myGameDAO.setMaxPlayerCount(gameMap.getMaxPlayerCount());
                myGameDAO.setProviderUrl(gameMap.getProviderUrl());
                return myGameDAO;
            }
        }
        return new GameMapDAO(null, gameMap.getName(), gameMap.getJsonString(), gameMap.getMaxPlayerCount(), gameMap.getProviderUrl());
    }

    public GameMap toGameMap(GameMapDAO gameMapDAO) {
        if (gameMapDAO == null) {
            return null;
        }
        return new GameMap(gameMapDAO.getId(), gameMapDAO.getName(), gameMapDAO.getJsonString(), gameMapDAO.getMaxPlayerCount(), gameMapDAO.getProviderUrl());
    }
}
