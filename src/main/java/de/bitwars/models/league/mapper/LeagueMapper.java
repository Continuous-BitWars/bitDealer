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
package de.bitwars.models.league.mapper;

import de.bitwars.api.models.Game;
import de.bitwars.api.models.GameMap;
import de.bitwars.api.models.League;
import de.bitwars.api.models.Player;
import de.bitwars.models.game.dao.GameDAO;
import de.bitwars.models.game.mapper.GameMapper;
import de.bitwars.models.gameMap.dao.GameMapDAO;
import de.bitwars.models.gameMap.mapper.GameMapMapper;
import de.bitwars.models.league.dao.LeagueDAO;
import de.bitwars.models.league.repository.LeagueRepository;
import de.bitwars.models.player.dao.PlayerDAO;
import de.bitwars.models.player.mapper.PlayerMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class LeagueMapper {

    @Inject
    LeagueRepository leagueRepository;

    @Inject
    GameMapMapper gameMapMapper;

    @Inject
    PlayerMapper playerMapper;
    @Inject
    GameMapper gameMapper;


    public LeagueDAO toLeagueDAO(League league) {
        List<PlayerDAO> playerDAOs = league.getPlayers().stream().map(playerMapper::toPlayerDAO).toList();
        List<GameMapDAO> gameMapDAOs = league.getMaps().stream().map(gameMapMapper::toGameMapDAO).toList();
        List<GameDAO> gameDAOs = league.getGames().stream().map(gameMapper::toGameDAO).toList();

        if (league.getId() != null) {
            Optional<LeagueDAO> leagueDAO = leagueRepository.findByIdOptional(league.getId());
            if (leagueDAO.isPresent()) {
                final LeagueDAO myLeagueDAO = leagueDAO.get();
                myLeagueDAO.setName(league.getName());
                myLeagueDAO.setParallelGames(league.getParallelGames());
                myLeagueDAO.setStatus(league.getStatus());
                myLeagueDAO.setPlayers(playerDAOs);
                myLeagueDAO.setGameMaps(gameMapDAOs);

                return myLeagueDAO;
            }
        }
        return new LeagueDAO(null, league.getName(), league.getParallelGames(), league.getStatus(), playerDAOs, gameMapDAOs, gameDAOs);
    }

    public League toLeague(LeagueDAO leagueDAO) {
        if (leagueDAO == null) {
            return null;
        }

        List<Player> players = leagueDAO.getPlayers().stream().map(playerMapper::toPlayer).toList();
        List<GameMap> gameMaps = leagueDAO.getGameMaps().stream().map(gameMapMapper::toGameMap).toList();
        List<Game> games = leagueDAO.getGames().stream().map(gameMapper::toGame).toList();
        return new League(leagueDAO.getId(), leagueDAO.getName(), leagueDAO.getStatus(), leagueDAO.getParallelGames(), players, gameMaps, games);
    }
}
