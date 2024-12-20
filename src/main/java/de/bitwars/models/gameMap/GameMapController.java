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
package de.bitwars.models.gameMap;

import de.bitwars.business_logic.GameMapLoader;
import de.bitwars.business_logic.moduels.GameMapBU;
import de.bitwars.models.gameMap.dao.GameMapDAO;
import de.bitwars.models.gameMap.repository.GameMapRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class GameMapController {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameMapController.class);

    @Inject
    GameMapRepository gameMapRepository;

    @Transactional
    public GameMapDAO createGameMap(GameMapDAO gameMapDAO) {
        if (gameMapDAO.getJsonString().isEmpty() && !gameMapDAO.getProviderUrl().isEmpty()) {
            gameMapDAO.setJsonString(GameMapLoader.fetchJsonFromUrl(gameMapDAO.getProviderUrl()));
            GameMapBU gameMapBU = GameMapLoader.mapJsonToGameMapBU(gameMapDAO.getJsonString());
            gameMapDAO.setMaxPlayerCount(gameMapBU.getMaxPlayerCount());

            if (gameMapDAO.getName().isEmpty()) {
                gameMapDAO.setName(gameMapBU.getName());
            }
        }
        gameMapRepository.persist(gameMapDAO);
        return gameMapDAO;
    }

    @Transactional
    public void deleteGameMap(long gameId) {
        Optional<GameMapDAO> playerDAO = gameMapRepository.findByIdOptional(gameId);
        playerDAO.ifPresent(dao -> gameMapRepository.delete(dao));
    }

    public Optional<GameMapDAO> getGameMapById(long mapId) {
        return gameMapRepository.findByIdOptional(mapId);
    }

    public List<GameMapDAO> listGameMaps() {
        return gameMapRepository.listAll();
    }

    @Transactional
    public GameMapDAO updateGameMap(GameMapDAO newGameMapDAO) {
        Optional<GameMapDAO> gameMapDAO = gameMapRepository.findByIdOptional(newGameMapDAO.getId());
        if (gameMapDAO.isPresent()) {
            GameMapDAO gameMap = gameMapDAO.get();
            gameMap.setName(newGameMapDAO.getName());
            gameMap.setJsonString(newGameMapDAO.getJsonString());
            gameMap.setMaxPlayerCount(newGameMapDAO.getMaxPlayerCount());
            gameMap.setProviderUrl(newGameMapDAO.getProviderUrl());
            return gameMap;
        }
        throw new NotFoundException(String.format("GameMap with id %s not found", newGameMapDAO.getId()));
    }

    @Transactional
    public GameMapDAO updateGameMapJsonById(long mapId, String value) {
        Optional<GameMapDAO> gameMapDAO = gameMapRepository.findByIdOptional(mapId);
        if (gameMapDAO.isPresent()) {
            GameMapDAO gameMap = gameMapDAO.get();
            gameMap.setJsonString(value);
            return gameMap;
        }
        throw new NotFoundException(String.format("GameMap with id %s not found", mapId));

    }
}
