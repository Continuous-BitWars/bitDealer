package de.bitwars.games.repository;

import de.bitwars.games.dao.GameDAO;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

public class GameRepository implements PanacheRepository<GameDAO> {
}
