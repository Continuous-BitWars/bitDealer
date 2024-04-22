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
package de.bitwars.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@JsonTypeName("Game")
public class Game {
    private Long id;
    private String name;
    private List<Player> players = new ArrayList<>();
    private GameOptions gameOptions;

    private StatusEnum status;
    private Long roundNumber;


    @JsonProperty("id")
    public Long getId() {
        return this.id;
    }

    @JsonProperty("id")
    public void setId(Long id) {
        this.id = id;
    }

    @JsonProperty("name")
    public String getName() {
        return this.name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }


    @JsonProperty("players")
    public List<Player> getPlayers() {
        return this.players;
    }

    @JsonProperty("players")
    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public Game addPlayersItem(Player playersItem) {
        if (this.players == null) {
            this.players = new ArrayList<>();
        }

        this.players.add(playersItem);
        return this;
    }

    public Game removePlayersItem(Player playersItem) {
        if (playersItem != null && this.players != null) {
            this.players.remove(playersItem);
        }
        return this;
    }

    @JsonProperty("game_options")
    public GameOptions getGameOptions() {
        return this.gameOptions;
    }

    @JsonProperty("game_options")
    public void setGameOptions(GameOptions gameOptions) {
        this.gameOptions = gameOptions;
    }

    @JsonProperty("status")
    public StatusEnum getStatus() {
        return this.status;
    }

    @JsonProperty("status")
    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    @JsonProperty("round_number")
    public Long getRoundNumber() {
        return this.roundNumber;
    }

    @JsonProperty("round_number")
    public void setRoundNumber(Long roundNumber) {
        this.roundNumber = roundNumber;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Game game = (Game) o;
        return Objects.equals(this.id, game.id)
                && Objects.equals(this.name, game.name)
                && Objects.equals(this.players, game.players)
                && Objects.equals(this.gameOptions, game.gameOptions)
                && Objects.equals(this.status, game.status)
                && Objects.equals(this.roundNumber, game.roundNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, players, gameOptions, status, roundNumber);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Game {\n");

        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    players: ").append(toIndentedString(players)).append("\n");
        sb.append("    gameOptions: ").append(toIndentedString(gameOptions)).append("\n");
        sb.append("    status: ").append(toIndentedString(status)).append("\n");
        sb.append("    roundNumber: ").append(toIndentedString(roundNumber)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}


