package de.bitwars.api.interfaces;

import de.bitwars.api.models.Score;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

@Path("/scoreboard")
@Consumes({"application/json"})
@Produces({"application/json"})
public interface ScoreboardApi {


    /**
     * Get Score of all Games
     *
     * @return The Score all Games
     */
    @GET
    Score getScoreboard();
}
