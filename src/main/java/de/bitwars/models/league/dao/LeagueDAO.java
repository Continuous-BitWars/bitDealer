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
package de.bitwars.models.league.dao;

import de.bitwars.api.models.StatusEnum;
import de.bitwars.models.gameMap.dao.GameMapDAO;
import de.bitwars.models.player.dao.PlayerDAO;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity(name = "league")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LeagueDAO {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private int parallelGames;

    @Enumerated(EnumType.STRING)
    private StatusEnum status;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "league_player",
            joinColumns = @JoinColumn(name = "league_id"),
            inverseJoinColumns = @JoinColumn(name = "player_id")
    )
    private List<PlayerDAO> players;

    // Many-to-Many relationship with GameMapDAO using List
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "league_game_map",
            joinColumns = @JoinColumn(name = "league_id"),
            inverseJoinColumns = @JoinColumn(name = "game_map_id")
    )
    private List<GameMapDAO> gameMaps;
}
