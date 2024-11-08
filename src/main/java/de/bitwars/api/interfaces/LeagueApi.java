package de.bitwars.api.interfaces;

import de.bitwars.api.models.Game;
import de.bitwars.api.models.GameMap;
import de.bitwars.api.models.League;
import de.bitwars.api.models.Player;
import de.bitwars.api.models.Score;
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
 * Represents a collection of functions to interact with the API endpoints for League, including subresources for players and GameMaps.
 */
@Path("/leagues")
@Consumes({"application/json"})
@Produces({"application/json"})
public interface LeagueApi {

    // CRUD operations for League

    /**
     * Creates a new League.
     *
     * @param league The League to be created
     * @return The created League
     */
    @POST
    League createLeague(League league);

    /**
     * Deletes a League by its ID.
     *
     * @param leagueId The ID of the League to delete
     */
    @DELETE
    @Path("/{leagueId}")
    void deleteLeague(@PathParam("leagueId") long leagueId);

    /**
     * Retrieves a Score for League by its ID.
     *
     * @param leagueId The ID of the League
     * @return The Scoreboard of League with the specified ID
     */
    @GET
    @Path("/{leagueId}/scoreboard")
    Score getLeagueScoreboard(@PathParam("leagueId") long leagueId);

    /**
     * Retrieves a List of Games of League by its ID.
     *
     * @param leagueId The ID of the League
     * @return The Games of League with the specified ID
     */
    @GET
    @Path("/{leagueId}/games")
    List<Game> getLeagueGames(@PathParam("leagueId") long leagueId);

    /**
     * Retrieves a League by its ID.
     *
     * @param leagueId The ID of the League
     * @return The League with the specified ID
     */
    @GET
    @Path("/{leagueId}")
    League getLeagueById(@PathParam("leagueId") long leagueId);


    /**
     * Lists all available Leagues.
     *
     * @return A list of all Leagues
     */
    @GET
    List<League> listLeagues();

    /**
     * Updates an existing League by its ID.
     *
     * @param leagueId The ID of the League to update
     * @param league   The League data to update
     * @return The updated League
     */
    @PUT
    @Path("/{leagueId}")
    League updateLeague(@PathParam("leagueId") long leagueId, League league);

    // Subresoucre for status

    /**
     * @param leagueId
     * @return Start League
     * @return Started League
     */
    @POST
    @Path("/{leagueId}/running")
    League startLeague(@PathParam("leagueId") long leagueId);


    /**
     * @param leagueId
     * @return Stop League
     * @return Stopped League
     */
    @DELETE
    @Path("/{leagueId}/running")
    League stopLeague(@PathParam("leagueId") long leagueId);


    // Subresource CRUD for Players

    /**
     * Creates a new Player within a specific League.
     *
     * @param leagueId The ID of the League to which the player belongs
     * @param player   The Player to be created
     * @return The update league
     */
    @POST
    @Path("/{leagueId}/players")
    League addPlayer(@PathParam("leagueId") long leagueId, Player player);

    /**
     * Deletes a Player by its ID within a specific League.
     *
     * @param leagueId The ID of the League
     * @param playerId The ID of the Player to delete
     * @return The update league
     */
    @DELETE
    @Path("/{leagueId}/players/{playerId}")
    League deletePlayer(@PathParam("leagueId") long leagueId, @PathParam("playerId") long playerId);


    // Subresource CRUD for GameMaps

    /**
     * Creates a new GameMap within a specific League.
     *
     * @param leagueId The ID of the League to which the GameMap belongs
     * @param gameMap  The GameMap to be created
     * @return The update GameMap
     */
    @POST
    @Path("/{leagueId}/maps")
    League addGameMap(@PathParam("leagueId") long leagueId, GameMap gameMap);

    /**
     * Deletes a GameMap by its ID within a specific League.
     *
     * @param leagueId The ID of the League
     * @param mapId    The ID of the GameMap to delete
     * @return The update GameMap
     */
    @DELETE
    @Path("/{leagueId}/maps/{mapId}")
    League deleteGameMap(@PathParam("leagueId") long leagueId, @PathParam("mapId") long mapId);
}
