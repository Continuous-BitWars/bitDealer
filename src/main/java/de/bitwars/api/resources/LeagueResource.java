package de.bitwars.api.resources;

import de.bitwars.api.interfaces.LeagueApi;
import de.bitwars.api.models.Game;
import de.bitwars.api.models.GameMap;
import de.bitwars.api.models.League;
import de.bitwars.api.models.Player;
import de.bitwars.api.models.Score;
import de.bitwars.models.game.mapper.GameMapper;
import de.bitwars.models.league.LeagueController;
import de.bitwars.models.league.dao.LeagueDAO;
import de.bitwars.models.league.mapper.LeagueMapper;
import de.bitwars.models.scoreboard.ScoreboardController;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
@RequiredArgsConstructor
public class LeagueResource implements LeagueApi {


    private final LeagueMapper leagueMapper;
    private final LeagueController leagueController;
    @Inject
    ScoreboardController scoreboardController;
    @Inject
    GameMapper gameMapper;

    @Override
    public League createLeague(League league) {
        LeagueDAO leagueDAO = leagueMapper.toLeagueDAO(league);
        leagueDAO = leagueController.createLeague(leagueDAO);
        return leagueMapper.toLeague(leagueDAO);
    }

    @Override
    public void deleteLeague(long leagueId) {
        leagueController.deleteLeague(leagueId);

    }

    @Override
    public Score getLeagueScoreboard(long leagueId) {
        Optional<LeagueDAO> leagueDAO = leagueController.getLeagueById(leagueId);
        if (leagueDAO.isEmpty()) {
            throw new NotFoundException();
        }
        Score score = this.scoreboardController.getScoreForGames(leagueDAO.get().getGames());
        score.setLeague(leagueMapper.toLeague(leagueDAO.get()));
        return score;
    }

    @Override
    public List<Game> getLeagueGames(long leagueId) {
        Optional<LeagueDAO> leagueDAOOptional = leagueController.getLeagueById(leagueId);
        if (leagueDAOOptional.isEmpty()) {
            throw new NotFoundException();
        }
        return leagueDAOOptional.get().getGames().stream().map(gameMapper::toGame).toList();
    }


    @Override
    public League getLeagueById(long leagueId) {
        Optional<LeagueDAO> leagueDAO = leagueController.getLeagueById(leagueId);
        if (leagueDAO.isEmpty()) {
            throw new NotFoundException();
        }
        return leagueMapper.toLeague(leagueDAO.get());
    }

    @Override
    public List<League> listLeagues() {
        return leagueController.listLeagues().stream().map(leagueMapper::toLeague).toList();
    }

    @Override
    public League updateLeague(long leagueId, League league) {
        league.setId(leagueId);
        LeagueDAO leagueDAO = leagueMapper.toLeagueDAO(league);
        leagueDAO = leagueController.updateLeague(leagueDAO);
        return leagueMapper.toLeague(leagueDAO);
    }

    @Override
    public League startLeague(long leagueId) {
        LeagueDAO leagueDAO = leagueController.startLeague(leagueId);
        return leagueMapper.toLeague(leagueDAO);
    }

    @Override
    public League stopLeague(long leagueId) {
        LeagueDAO leagueDAO = leagueController.stopLeague(leagueId);
        return leagueMapper.toLeague(leagueDAO);
    }

    @Override
    public League addPlayer(long leagueId, Player player) {
        LeagueDAO leagueDAO = leagueController.addPlayer(leagueId, player.getId());
        return leagueMapper.toLeague(leagueDAO);
    }

    @Override
    public League deletePlayer(long leagueId, long playerId) {
        LeagueDAO leagueDAO = leagueController.deletePlayer(leagueId, playerId);
        return leagueMapper.toLeague(leagueDAO);
    }

    @Override
    public League addGameMap(long leagueId, GameMap gameMap) {
        LeagueDAO leagueDAO = leagueController.addGameMap(leagueId, gameMap.getId());
        return leagueMapper.toLeague(leagueDAO);
    }

    @Override
    public League deleteGameMap(long leagueId, long mapId) {
        LeagueDAO leagueDAO = leagueController.deleteGameMap(leagueId, mapId);
        return leagueMapper.toLeague(leagueDAO);
    }
}
