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

import de.bitwars.api.interfaces.GameMapApi;
import de.bitwars.api.models.GameMap;
import de.bitwars.models.gameMap.GameMapController;
import de.bitwars.models.gameMap.dao.GameMapDAO;
import de.bitwars.models.gameMap.mapper.GameMapMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
@RequiredArgsConstructor
public class GameMapResource implements GameMapApi {

    private final GameMapController gameMapController;
    private final GameMapMapper gameMapMapper;


    @Override
    public GameMap createGameMap(GameMap gameMap) {
        GameMapDAO gameMapDAO = gameMapMapper.toGameMapDAO(gameMap);
        gameMapDAO = gameMapController.createGameMap(gameMapDAO);
        return gameMapMapper.toGameMap(gameMapDAO);
    }

    @Override
    public void deleteGameMap(long mapId) {
        gameMapController.deleteGameMap(mapId);
    }

    @Override
    public GameMap getGameMapById(long mapId) {
        Optional<GameMapDAO> gameMapDAO = gameMapController.getGameMapById(mapId);
        if (gameMapDAO.isEmpty()) {
            throw new NotFoundException();
        }
        return gameMapMapper.toGameMap(gameMapDAO.get());
    }

    @Override
    public List<GameMap> listGameMaps() {
        return gameMapController.listGameMaps().stream().map(gameMapMapper::toGameMap).toList();
    }

    @Override
    public GameMap updateGameMap(long mapId, GameMap gameMap) {
        gameMap.setId(mapId);
        GameMapDAO gameMapDAO = gameMapMapper.toGameMapDAO(gameMap);
        gameMapDAO = gameMapController.updateGameMap(gameMapDAO);
        return gameMapMapper.toGameMap(gameMapDAO);
    }
}
