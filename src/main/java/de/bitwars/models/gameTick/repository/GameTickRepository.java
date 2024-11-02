package de.bitwars.models.gameTick.repository;

import de.bitwars.models.gameTick.dao.GameTickDAO;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;


@ApplicationScoped
public class GameTickRepository implements PanacheRepository<GameTickDAO> {
}
