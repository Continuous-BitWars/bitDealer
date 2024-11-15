package de.bitwars.models.gameTick.repository;

import de.bitwars.models.game.dao.GameDAO;
import de.bitwars.models.gameTick.dao.GameTickDAO;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;


@ApplicationScoped
public class GameTickRepository implements PanacheRepository<GameTickDAO> {
    public long countGameTicksFromGame(GameDAO gameDAO) {
        return count("game = ?1", gameDAO);
    }
}
