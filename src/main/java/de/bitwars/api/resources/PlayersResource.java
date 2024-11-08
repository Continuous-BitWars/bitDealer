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
package de.bitwars.api.resources;

import de.bitwars.api.interfaces.PlayersApi;
import de.bitwars.api.models.Game;
import de.bitwars.api.models.League;
import de.bitwars.api.models.Player;
import de.bitwars.models.game.GameController;
import de.bitwars.models.game.dao.GameDAO;
import de.bitwars.models.game.mapper.GameMapper;
import de.bitwars.models.league.LeagueController;
import de.bitwars.models.league.dao.LeagueDAO;
import de.bitwars.models.league.mapper.LeagueMapper;
import de.bitwars.models.player.PlayerController;
import de.bitwars.models.player.dao.PlayerDAO;
import de.bitwars.models.player.mapper.PlayerMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
@RequiredArgsConstructor
public class PlayersResource implements PlayersApi {

    private final PlayerController playerController;
    private final GameController gameController;
    private final LeagueController leagueController;

    private final PlayerMapper playerMapper;
    private final GameMapper gameMapper;
    private final LeagueMapper leagueMapper;


    @Override
    public Player createPlayer(Player player) {
        PlayerDAO playerDAO = playerMapper.toPlayerDAO(player);
        playerDAO = playerController.createPlayer(playerDAO);
        return playerMapper.toPlayer(playerDAO);
    }

    @Override
    public void deletePlayer(long playerId) {
        playerController.deletePlayer(playerId);
    }

    @Override
    public Player getPlayerById(long playerId) {
        Optional<PlayerDAO> playerDAO = playerController.getPlayerById(playerId);
        if (playerDAO.isEmpty()) {
            throw new NotFoundException();
        }
        return playerMapper.toPlayer(playerDAO.get());
    }

    @Override
    public List<Game> getGamesForPlayerById(long playerId) {
        List<GameDAO> gameDAOS = gameController.listGamesForPlayer(playerId);
        return gameDAOS.stream().map(gameMapper::toGame).toList();
    }

    @Override
    public List<League> getLeaguesForPlayerById(long playerId) {
        List<LeagueDAO> leagueDAOS = leagueController.listLeagueForPlayer(playerId);
        return leagueDAOS.stream().map(leagueMapper::toLeague).toList();
    }

    @Override
    public List<Player> listPlayers() {
        return playerController.listPlayers().stream().map(playerMapper::toPlayer).toList();
    }

    @Override
    public Player updatePlayer(long playerId, Player player) {
        player.setId(playerId);
        PlayerDAO playerDAO = playerMapper.toPlayerDAO(player);
        playerDAO = playerController.updatePlayer(playerDAO);
        return playerMapper.toPlayer(playerDAO);
    }
}
