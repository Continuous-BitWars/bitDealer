package de.bitwars.models.game.repository;

import de.bitwars.models.game.dao.GameDAO;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

public class GameRepository implements PanacheRepository<GameDAO> {
}
