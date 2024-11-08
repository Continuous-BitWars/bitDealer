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
package de.bitwars.models.scoreboard;

import de.bitwars.api.models.Score;
import de.bitwars.api.models.ScoreEntity;
import de.bitwars.models.game.dao.GameDAO;
import de.bitwars.models.player.dao.PlayerDAO;
import de.bitwars.models.player.mapper.PlayerMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@ApplicationScoped
public class ScoreboardController {

    @Inject
    PlayerMapper playerMapper;

    //TODO: make faster :D
    public Score getScoreForGames(List<GameDAO> gameDAOs) {
        Score score = new Score();

        gameDAOs.forEach(gameDAO -> {
            List<Map.Entry<PlayerDAO, Integer>> players = gameDAO.getPlayerEliminationTicks().entrySet().stream()
                    .sorted((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()))
                    .toList();

            for (int i = 1; i <= players.size(); i++) {
                Map.Entry<PlayerDAO, Integer> entry = players.get(i - 1);

                Optional<ScoreEntity> scoreEntityOptional = score.getScores().stream().filter(scoreEntity -> scoreEntity.getPlayer().getId().equals(entry.getKey().getId())).findFirst();

                ScoreEntity scoreEntity;
                if (scoreEntityOptional.isPresent()) {
                    scoreEntity = scoreEntityOptional.get();
                } else {
                    scoreEntity = new ScoreEntity();
                    scoreEntity.setPlayer(playerMapper.toPlayer(entry.getKey()));
                    score.getScores().add(scoreEntity);
                }

                switch (gameDAO.getStatus()) {
                    case DONE -> scoreEntity.addPointsForPlace(i);
                    case RUNNING, STOPPED -> scoreEntity.addOpenGame(1);
                }
            }
        });
        score.getScores().sort((s1, s2) -> Integer.compare(s2.getScore(), s1.getScore()));
        return score;
    }
}
