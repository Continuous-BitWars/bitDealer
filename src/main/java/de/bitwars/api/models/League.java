package de.bitwars.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@JsonTypeName("League")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class League {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    private String name;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private StatusEnum status = StatusEnum.STOPPED;

    @JsonProperty("parallel_games")
    private int parallelGames;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<Player> players = new ArrayList<>();
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<GameMap> maps = new ArrayList<>();

    @JsonProperty(value = "game_count", access = JsonProperty.Access.READ_ONLY)
    private int gameCount = 0;

    @JsonProperty(value = "league_options", access = JsonProperty.Access.READ_ONLY)
    private LeagueOptions leagueOptions;

}
