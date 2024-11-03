package de.bitwars.models.game.repository;

import de.bitwars.api.models.StatusEnum;
import de.bitwars.models.game.dao.GameDAO;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class GameRepository implements PanacheRepository<GameDAO> {
    public List<GameDAO> findByStatus(StatusEnum status) {
        return list("status", status);
    }
}
