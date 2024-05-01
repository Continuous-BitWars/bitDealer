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

import de.bitwars.api.models.Player;
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
 * Represents a collection of functions to interact with the API endpoints.
 */
@Path("/players")
@Consumes({"application/json"})
@Produces({"application/json"})
public interface PlayersApi {

    /**
     * @param player
     * @return Player created
     */
    @POST
    Player createPlayer(Player player);


    /**
     * @param playerId
     * @return Player deleted
     */
    @DELETE
    @Path("/{playerId}")
    void deletePlayer(@PathParam("playerId") long playerId);


    /**
     * @param playerId
     * @return Player found
     */
    @GET
    @Path("/{playerId}")
    Player getPlayerById(@PathParam("playerId") long playerId);


    /**
     * @return A list of players
     */
    @GET
    List<Player> listPlayers();


    /**
     * @param playerId
     * @param player
     * @return Player updated
     */
    @PUT
    @Path("/{playerId}")
    Player updatePlayer(@PathParam("playerId") long playerId, Player player);
}


