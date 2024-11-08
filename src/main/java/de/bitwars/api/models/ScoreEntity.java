package de.bitwars.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ScoreEntity {

    static int[] SCORE_POINTS = new int[]{8, 5, 3, 0};

    Player player;
    int score;

    @JsonProperty("wins_by_place")
    Map<Integer, Integer> winsByPlace = new HashMap<>();

    @JsonProperty("open_games_count")
    int openGamesCount = 0;

    public void addPointsForPlace(int pos) {
        int key = Math.min(pos, SCORE_POINTS.length);
        winsByPlace.put(key, winsByPlace.getOrDefault(key, 0) + 1);

        calculateScore();
    }

    void calculateScore() {
        this.score = 0;
        for (int i = 0; i < SCORE_POINTS.length; i++) {
            score += SCORE_POINTS[i] * winsByPlace.getOrDefault(i + 1, 0);
        }
    }

    public void addOpenGame(int count) {
        this.openGamesCount += count;
    }
}
