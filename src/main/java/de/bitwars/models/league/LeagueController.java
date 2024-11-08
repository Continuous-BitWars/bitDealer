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
package de.bitwars.models.league;

import de.bitwars.api.models.StatusEnum;
import de.bitwars.models.gameMap.GameMapController;
import de.bitwars.models.gameMap.dao.GameMapDAO;
import de.bitwars.models.league.dao.LeagueDAO;
import de.bitwars.models.league.repository.LeagueRepository;
import de.bitwars.models.player.PlayerController;
import de.bitwars.models.player.dao.PlayerDAO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class LeagueController {

    @Inject
    LeagueRepository leagueRepository;

    @Inject
    PlayerController playerController;

    @Inject
    GameMapController gameMapController;

    @Transactional
    public LeagueDAO createLeague(LeagueDAO leagueDAO) {
        leagueRepository.persist(leagueDAO);
        return leagueDAO;
    }

    @Transactional
    public void deleteLeague(long leagueId) {
        Optional<LeagueDAO> leagueDAO = leagueRepository.findByIdOptional(leagueId);
        leagueDAO.ifPresent(dao -> leagueRepository.delete(dao));
    }

    public Optional<LeagueDAO> getLeagueById(long leagueId) {
        return leagueRepository.findByIdOptional(leagueId);
    }

    public List<LeagueDAO> listLeagues() {
        return leagueRepository.listAll();

    }

    @Transactional
    public LeagueDAO updateLeague(LeagueDAO newLeagueDAO) {
        Optional<LeagueDAO> leagueDAO = leagueRepository.findByIdOptional(newLeagueDAO.getId());
        if (leagueDAO.isPresent()) {
            LeagueDAO myLeagueDAO = leagueDAO.get();
            myLeagueDAO.setName(newLeagueDAO.getName());
            myLeagueDAO.setParallelGames(newLeagueDAO.getParallelGames());
            return myLeagueDAO;
        }
        throw new NotFoundException(String.format("League with id %s not found", newLeagueDAO.getId()));

    }

    @Transactional
    public LeagueDAO startLeague(long leagueId) {
        Optional<LeagueDAO> leagueDAOOptional = this.getLeagueById(leagueId);
        if (leagueDAOOptional.isPresent()) {
            LeagueDAO leagueDAO = leagueDAOOptional.get();
            leagueDAO.setStatus(StatusEnum.RUNNING);
            return leagueDAO;
        }
        throw new NotFoundException(String.format("League with id %s not found", leagueId));
    }

    @Transactional
    public LeagueDAO stopLeague(long leagueId) {
        Optional<LeagueDAO> leagueDAOOptional = this.getLeagueById(leagueId);
        if (leagueDAOOptional.isPresent()) {
            LeagueDAO leagueDAO = leagueDAOOptional.get();
            leagueDAO.setStatus(StatusEnum.STOPPED);
            return leagueDAO;
        }
        throw new NotFoundException(String.format("League with id %s not found", leagueId));
    }

    @Transactional
    public LeagueDAO addPlayer(long leagueId, long playerId) {
        Optional<LeagueDAO> leagueDAOOptional = this.getLeagueById(leagueId);
        Optional<PlayerDAO> playerDAOOptional = playerController.getPlayerById(playerId);

        if (leagueDAOOptional.isPresent() && playerDAOOptional.isPresent()) {
            LeagueDAO leagueDAO = leagueDAOOptional.get();
            PlayerDAO playerDAO = playerDAOOptional.get();

            if (leagueDAO.getPlayers().contains(playerDAO)) {
                throw new BadRequestException(String.format("Player with id %s already exists", playerId));
            }

            leagueDAO.getPlayers().add(playerDAO);


            return leagueDAO;
        }
        throw new NotFoundException(String.format("League or Player with id %d or %d not found", leagueId, playerId));
    }

    @Transactional
    public LeagueDAO deletePlayer(long leagueId, long playerId) {
        Optional<LeagueDAO> leagueDAOOptional = this.getLeagueById(leagueId);

        if (leagueDAOOptional.isPresent() && playerId > 0) {
            LeagueDAO leagueDAO = leagueDAOOptional.get();

            leagueDAO.getPlayers().removeIf(p -> p.getId() == playerId);

            return leagueDAO;
        }
        throw new NotFoundException(String.format("League or Player with id %d or %d not found", leagueId, playerId));
    }

    @Transactional
    public LeagueDAO addGameMap(long leagueId, long gameMapId) {
        Optional<LeagueDAO> leagueDAOOptional = this.getLeagueById(leagueId);
        Optional<GameMapDAO> gameMapDAOOptional = gameMapController.getGameMapById(gameMapId);

        if (leagueDAOOptional.isPresent() && gameMapDAOOptional.isPresent()) {
            LeagueDAO leagueDAO = leagueDAOOptional.get();
            GameMapDAO gameMapDAO = gameMapDAOOptional.get();

            if (leagueDAO.getGameMaps().contains(gameMapDAO)) {
                throw new BadRequestException(String.format("GameMap with id %s already exists", gameMapDAO));
            }
            leagueDAO.getGameMaps().add(gameMapDAO);

            return leagueDAO;
        }
        throw new NotFoundException(String.format("League or GameMap with id %d or %d not found", leagueId, gameMapId));
    }

    @Transactional
    public LeagueDAO deleteGameMap(long leagueId, long gameMapId) {
        Optional<LeagueDAO> leagueDAOOptional = this.getLeagueById(leagueId);

        if (leagueDAOOptional.isPresent() && gameMapId > 0) {
            LeagueDAO leagueDAO = leagueDAOOptional.get();

            boolean isSuccess = leagueDAO.getGameMaps().removeIf(gameMap -> gameMap.getId() == gameMapId);
            if (isSuccess) {
                return leagueDAO;
            }
        }
        throw new NotFoundException(String.format("League or GameMap with id %d or %d not found", leagueId, gameMapId));
    }

    public List<LeagueDAO> listLeagueForPlayer(long playerId) {
        return listLeagues().stream()
                .filter(league -> league.getPlayers().stream().anyMatch(player -> player.getId().equals(playerId))).toList();
    }
}
