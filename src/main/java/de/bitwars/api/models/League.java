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
    private List<Game> games = new ArrayList<>();

    public League addGameItem(Game gameItem) {
        if (this.games == null) {
            this.games = new ArrayList<>();
        }
        this.games.add(gameItem);
        return this;
    }

    public League removeGameItem(Game gameItem) {
        if (gameItem != null && this.games != null) {
            this.games.remove(gameItem);
        }
        return this;
    }

    public League addMapsItem(GameMap gameMapItem) {
        if (this.maps == null) {
            this.maps = new ArrayList<>();
        }
        this.maps.add(gameMapItem);
        return this;
    }

    public League removeMapsItem(GameMap gameMapItem) {
        if (gameMapItem != null && this.maps != null) {
            this.maps.remove(gameMapItem);
        }
        return this;
    }

    public League addPlayersItem(Player playersItem) {
        if (this.players == null) {
            this.players = new ArrayList<>();
        }
        this.players.add(playersItem);
        return this;
    }

    public League removePlayersItem(Player playersItem) {
        if (playersItem != null && this.players != null) {
            this.players.remove(playersItem);
        }
        return this;
    }
}
