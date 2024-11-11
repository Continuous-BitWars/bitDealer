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
package de.bitwars.models.game.dao;

import de.bitwars.api.models.StatusEnum;
import de.bitwars.models.gameMap.dao.GameMapDAO;
import de.bitwars.models.gameTick.dao.GameTickDAO;
import de.bitwars.models.league.dao.LeagueDAO;
import de.bitwars.models.player.dao.PlayerDAO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Entity(name = "games")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GameDAO {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private int timeBetweenTicks;

    @ManyToOne
    private GameMapDAO map;

    @Enumerated(EnumType.STRING)
    private StatusEnum status;

    @ManyToMany()
    @JoinTable(
            name = "games_player",
            joinColumns = @JoinColumn(name = "game_id"),
            inverseJoinColumns = @JoinColumn(name = "player_id")
    )
    private List<PlayerDAO> players;

    @OneToMany(mappedBy = "game")
    private List<GameTickDAO> gameTicks;

    @ElementCollection()
    @CollectionTable(name = "player_elimination_ticks", joinColumns = @JoinColumn(name = "game_id"))
    @MapKeyJoinColumn(name = "player_id")
    @Column(name = "elimination_tick")
    private Map<PlayerDAO, Integer> playerEliminationTicks;


    @ManyToOne()
    @JoinColumn(name = "league_id")
    private LeagueDAO league;

    public GameDAO(String name, GameMapDAO gameMapDAO) {
        this.name = name;
        this.map = gameMapDAO;

        this.id = null;
        this.timeBetweenTicks = 1;
        this.status = StatusEnum.PENDING;
        this.players = new ArrayList<>();
        this.gameTicks = new ArrayList<>();
        this.playerEliminationTicks = new HashMap<>();
    }


    public boolean removePlayerById(long playerId) {
        if (players != null) {
            return players.removeIf(player -> player.getId().equals(playerId));
        }
        return false;
    }

    public Optional<GameTickDAO> getLastGameTick() {
        if (gameTicks != null && !gameTicks.isEmpty()) {
            return gameTicks.stream()
                    .max(Comparator.comparingLong(GameTickDAO::getTick));
        }
        return Optional.empty();
    }
}

