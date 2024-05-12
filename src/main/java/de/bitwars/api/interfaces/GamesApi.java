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

import de.bitwars.api.models.Game;
import de.bitwars.api.models.GameOptions;
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
@Path("/games")
@Consumes({"application/json"})
@Produces({"application/json"})
public interface GamesApi {
    String PARAM_GAME_ID = "gameId";

    /**
     * @param gameId
     * @param player
     * @return Player added to the game
     */
    @POST
    @Path("/{gameId}/players")
    Game addPlayerToGame(@PathParam(PARAM_GAME_ID) Integer gameId, Player player);


    /**
     * @param game
     * @return Game created
     */
    @POST
    Game createGame(Game game);


    /**
     * @param gameId
     * @return Game deleted
     */
    @DELETE
    @Path("/{gameId}")
    void deleteGame(@PathParam(PARAM_GAME_ID) long gameId);


    /**
     * @param gameId
     * @return Game found
     */
    @GET
    @Path("/{gameId}")
    Game getGameById(@PathParam(PARAM_GAME_ID) long gameId);


    /**
     * @return A list of games
     */
    @GET
    List<Game> listGames();


    /**
     * @param gameId
     * @return A list of players in the game
     */
    @GET
    @Path("/{gameId}/players")
    List<Player> listPlayersInGame(@PathParam(PARAM_GAME_ID) long gameId);


    /**
     * @param gameId
     * @param playerId
     * @return Player removed from the game
     */
    @DELETE
    @Path("/{gameId}/players/{playerId}")
    Game removePlayerFromGame(@PathParam(PARAM_GAME_ID) long gameId, @PathParam("playerId") long playerId);


    /**
     * @param gameId
     * @param gameOptions
     * @return Start Game
     * @return Can`t Start Game
     */
    @POST
    @Path("/{gameId}/running")
    Game startGame(@PathParam(PARAM_GAME_ID) long gameId, GameOptions gameOptions);


    /**
     * @param gameId
     * @return Stop Game
     * @return Can`t Stop Game
     */
    @DELETE
    @Path("/{gameId}/running")
    Game stopGame(@PathParam(PARAM_GAME_ID) long gameId);


    /**
     * @param gameId
     * @param game
     * @return Game updated
     */
    @PUT
    @Path("/{gameId}")
    Game updateGame(@PathParam(PARAM_GAME_ID) long gameId, Game game);

}
