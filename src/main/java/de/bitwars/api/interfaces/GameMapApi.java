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
package de.bitwars.api.interfaces;

import de.bitwars.api.models.GameMap;
import de.bitwars.api.models.GameMapJson;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;

import java.util.List;

/**
 * Represents a collection of functions to interact with the API endpoints for GameMap.
 */
@Path("/maps")
@Consumes({"application/json"})
@Produces({"application/json"})
public interface GameMapApi {

    /**
     * Creates a new GameMap.
     *
     * @param gameMap The GameMap to be created
     * @return The created GameMap
     */
    @POST
    GameMap createGameMap(GameMap gameMap);

    /**
     * Deletes a GameMap by its ID.
     *
     * @param mapId The ID of the GameMap to delete
     */
    @DELETE
    @Path("/{mapId}")
    void deleteGameMap(@PathParam("mapId") long mapId);

    /**
     * Retrieves a GameMap by its ID.
     *
     * @param mapId The ID of the GameMap
     * @return The GameMap with the specified ID
     */
    @GET
    @Path("/{mapId}")
    GameMap getGameMapById(@PathParam("mapId") long mapId);

    /**
     * Retrieves a GameMap Json Value by its ID.
     *
     * @param mapId The ID of the GameMap
     * @return The GameMapJson with for specified ID
     */
    @GET
    @Path("/{mapId}/json")
    GameMapJson getGameMapJsonById(@PathParam("mapId") long mapId);

    /**
     * Updates an existing GameMapJson by its GameMap ID.
     *
     * @param mapId       The ID of the GameMap to update
     * @param gameMapJson The GameMapJson data to update JSON Value
     * @return The updated GameMapJson
     */
    @PUT
    @Path("/{mapId}/json")
    GameMapJson updateGameMapJsonById(@PathParam("mapId") long mapId, GameMapJson gameMapJson);

    /**
     * Lists all available GameMaps.
     *
     * @return A list of all GameMaps
     */
    @GET
    List<GameMap> listGameMaps();

    /**
     * Updates an existing GameMap by its ID.
     *
     * @param mapId   The ID of the GameMap to update
     * @param gameMap The GameMap data to update
     * @return The updated GameMap
     */
    @PUT
    @Path("/{mapId}")
    GameMap updateGameMap(@PathParam("mapId") long mapId, GameMap gameMap);
}
