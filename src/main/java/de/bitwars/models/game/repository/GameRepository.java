package de.bitwars.models.game.repository;

import de.bitwars.api.models.StatusEnum;
import de.bitwars.models.game.dao.GameDAO;
import de.bitwars.models.league.dao.LeagueDAO;
import de.bitwars.models.player.dao.PlayerDAO;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Collections;
import java.util.List;

@ApplicationScoped
public class GameRepository implements PanacheRepository<GameDAO> {
    public List<GameDAO> findByStatus(StatusEnum status) {
        return list("status", status);
    }

    public List<GameDAO> findGamesByLeagueAndStatusNot(LeagueDAO league, StatusEnum status) {
        return list("league = ?1 and status != ?2", league, status);
    }

    public long countGamesByLeagueAndStatusNot(LeagueDAO league, StatusEnum status) {
        return count("league = ?1 and status != ?2", league, status);
    }

    public long countGamesByLeague(LeagueDAO league) {
        return count("league = ?1", league);
    }

    public List<PlayerDAO> getPlayersSortedByUsageInLeague(LeagueDAO league) {
        if (league == null || league.getPlayers() == null || league.getPlayers().isEmpty()) {
            return Collections.emptyList();
        }

        List<Object[]> results = getEntityManager().createQuery(
                        "SELECT p, COUNT(g) FROM games g JOIN g.players p WHERE g.league = :league GROUP BY p ORDER BY COUNT(g) ASC", Object[].class)
                .setParameter("league", league)
                .getResultList();

        return results.stream()
                .map(result -> (PlayerDAO) result[0])
                .toList();
    }
}
