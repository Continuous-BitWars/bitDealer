package de.bitwars.business_logic;

import de.bitwars.api.models.StatusEnum;
import de.bitwars.models.game.GameController;
import de.bitwars.models.game.dao.GameDAO;
import de.bitwars.models.gameMap.dao.GameMapDAO;
import de.bitwars.models.league.dao.LeagueDAO;
import de.bitwars.models.league.repository.LeagueRepository;
import de.bitwars.models.player.dao.PlayerDAO;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@ApplicationScoped
public class GameScheduler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameScheduler.class);

    @Inject
    LeagueRepository leagueRepository;

    @Inject
    GameController gameController;

    @Transactional
    @Scheduled(delayed = "10s", every = "60s")
    void checkLeagueState() {
        LOGGER.info("Scheduled to check league state");

        List<LeagueDAO> leagueDAOs = this.leagueRepository.findByStatus(StatusEnum.RUNNING);
        leagueDAOs.forEach(league -> {
            int toStartedGameCount = league.getParallelGames() - league.getRunningGames().size();
            if (toStartedGameCount > 0) {
                LOGGER.info("Starting {} games for league {}: {}", toStartedGameCount, league.getId(), league.getName());
                for (int i = 0; i < toStartedGameCount; i++) {
                    this.startGameForLeague(league);
                }
            } else {
                LOGGER.info("League {} info: {} - {} => {} => {}", league.getId(), league.getParallelGames(), league.getRunningGames().size(), toStartedGameCount, league.getRunningGames().stream().map(GameDAO::getId).toArray());
            }
        });
    }

    @Transactional
    void startGameForLeague(LeagueDAO league) {
        LOGGER.debug("Starting game for league {}: {}", league.getId(), league.getName());


        String name = String.format("[L%d] %s - Game %d", league.getId(), league.getName(), (league.getGames().size() + 1));
        GameMapDAO gameMapDAO = league.getLeastUsedGameMap();
        List<PlayerDAO> playerDAOS = league.getPlayersSortedByUsageInGames();

        GameDAO newGame = gameController.createGame(name, gameMapDAO.getId());
        newGame.setLeague(league);
        league.getGames().add(newGame);

        int maxPlayerCount = Math.min(gameMapDAO.getMaxPlayerCount(), playerDAOS.size());
        playerDAOS.subList(0, maxPlayerCount).forEach(playerDAO -> {
            gameController.addPlayerToGame(newGame.getId(), playerDAO);
        });

        gameController.startGame(newGame.getId(), league.getDefaultTimeBetweenTicks());
    }
}
