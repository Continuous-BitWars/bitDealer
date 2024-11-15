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
package de.bitwars.models.league.repository;

import de.bitwars.api.models.StatusEnum;
import de.bitwars.models.league.dao.LeagueDAO;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class LeagueRepository implements PanacheRepository<LeagueDAO> {
    public List<LeagueDAO> findByStatus(StatusEnum status) {
        return list("status", status);
    }

    public List<LeagueDAO> findLeaguesByPlayerId(Long playerId) {
        return list("SELECT g FROM league g JOIN g.players p WHERE p.id = ?1", playerId);
    }
}
