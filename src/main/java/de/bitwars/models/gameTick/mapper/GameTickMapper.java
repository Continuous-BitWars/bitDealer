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
package de.bitwars.models.gameTick.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.bitwars.api.models.clients.Board;
import de.bitwars.models.game.GameController;
import de.bitwars.models.gameTick.dao.GameTickDAO;
import jakarta.enterprise.context.ApplicationScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class GameTickMapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameController.class);

    private final ObjectMapper objectMapper = new ObjectMapper();

    public Board toBoard(GameTickDAO gameTickDAO) {
        try {
            return objectMapper.readValue(gameTickDAO.getGameStateJson(), Board.class);
        } catch (JsonProcessingException e) {
            LOGGER.error("[{}]Cant load GameTick: {}", gameTickDAO.getId(), e.getMessage(), e);
            return null;
        }

    }
}
