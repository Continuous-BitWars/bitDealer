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
    private Long id;
    private String name;
    private StatusEnum status = StatusEnum.STOPPED;

    @JsonProperty("parallel_games")
    private int parallelGames;

    private List<Player> players = new ArrayList<>();
    private List<GameMap> maps = new ArrayList<>();

    @JsonProperty("game_count")
    private int gameCount = 0;
}
