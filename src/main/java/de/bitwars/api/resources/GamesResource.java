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

import de.bitwars.api.interfaces.GamesApi;
import de.bitwars.api.models.Game;
import de.bitwars.api.models.GameOptions;
import de.bitwars.api.models.Player;
import de.bitwars.api.models.StatusEnum;
import de.bitwars.api.models.clients.Board;
import de.bitwars.models.game.GameController;
import de.bitwars.models.game.dao.GameDAO;
import de.bitwars.models.game.mapper.GameMapper;
import de.bitwars.models.gameTick.dao.GameTickDAO;
import de.bitwars.models.gameTick.mapper.GameTickMapper;
import de.bitwars.models.player.PlayerController;
import de.bitwars.models.player.dao.PlayerDAO;
import de.bitwars.models.player.mapper.PlayerMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
@RequiredArgsConstructor
public class GamesResource implements GamesApi {

    private final GameController gameController;
    private final GameMapper gameMapper;
    private final PlayerController playerController;
    private final PlayerMapper playerMapper;
    private final GameTickMapper gameTickMapper;

    @Override
    public Game createGame(Game game) {
        if (game.getGameMap().getId() <= 0) {
            throw new IllegalArgumentException("Game map id must be set");
        }
        GameDAO gameDAO = this.gameController.createGame(game.getName(), game.getGameMap().getId());
        return this.gameMapper.toGame(gameDAO);
    }

    @Override
    public void deleteGame(long gameId) {
        this.gameController.deleteGame(gameId);
    }

    @Override
    public Game getGameById(long gameId) {
        Optional<GameDAO> gameDAO = this.gameController.getGameById(gameId);
        if (gameDAO.isEmpty()) {
            throw new NotFoundException();
        }
        return this.gameMapper.toGame(gameDAO.get());
    }

    @Override
    public List<Game> listGames(StatusEnum statusFilter) {
        List<GameDAO> gameDAOs = this.gameController.listGames();
        if (statusFilter != null) {
            gameDAOs = gameDAOs.stream()
                    .filter(game -> game.getStatus().equals(statusFilter))
                    .toList();
        }
        return gameDAOs.stream().map(this.gameMapper::toGame).toList();
    }

    @Override
    public List<Player> listPlayersInGame(long gameId) {
        Game game = this.getGameById(gameId);
        return game.getPlayers();
    }

    @Override
    public Game addPlayerToGame(long gameId, Player player) {
        Optional<PlayerDAO> playerDAO = Optional.empty();
        if (player.getId() > 0) {
            playerDAO = playerController.getPlayerById(player.getId());
        } else if (!player.getProviderUrl().isEmpty() && player.getId() == 0) {
            playerDAO = Optional.of(playerController.createPlayer(playerMapper.toPlayerDAO(player)));
        }

        if (playerDAO.isEmpty()) {
            throw new NotImplementedException("Use Player update function before add player id with other ProviderUrl!");
        }

        GameDAO gameDAO = this.gameController.addPlayerToGame(gameId, playerDAO.get());
        return this.gameMapper.toGame(gameDAO);
    }

    @Override
    public Game removePlayerFromGame(long gameId, long playerId) {
        GameDAO gameDAO = this.gameController.removePlayerFromGame(gameId, playerId);
        return this.gameMapper.toGame(gameDAO);
    }

    @Override
    public Game startGame(long gameId, GameOptions gameOptions) {
        GameDAO gameDAO = this.gameController.startGame(gameId, gameOptions.getTimeBetweenTicks());
        return this.gameMapper.toGame(gameDAO);
    }

    @Override
    public Game stopGame(long gameId) {
        GameDAO gameDAO = this.gameController.stopGame(gameId);
        return this.gameMapper.toGame(gameDAO);
    }

    @Override
    public Game updateGame(long gameId, Game game) {
        game.setId(gameId);
        GameDAO gameDAO = gameMapper.toGameDAO(game);
        gameDAO = gameController.updateGame(gameDAO);
        return this.gameMapper.toGame(gameDAO);
    }

    @Override
    public List<Board> getBoardTicksByGameId(long gameId) {
        List<GameTickDAO> gameTickDAOs = gameController.getBoardTicksByGameId(gameId);
        return gameTickDAOs.stream().map(gameTickMapper::toBoard).toList();
    }
}
