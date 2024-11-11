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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@JsonTypeName("Game")
@Getter
@Setter
@AllArgsConstructor
public class Game {
    private Long id;
    private String name;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<Player> players = new ArrayList<>();

    @JsonProperty("game_map")
    private GameMap gameMap;
    
    @JsonProperty(value = "game_options", access = JsonProperty.Access.READ_ONLY)
    private GameOptions gameOptions;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private StatusEnum status;

    @JsonProperty(value = "round_number", access = JsonProperty.Access.READ_ONLY)
    private int roundNumber;

    private Map<Long, Integer> eliminatedPlayers;

    private League league;
}


