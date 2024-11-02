package de.bitwars.api.models.clients;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Game {
    private long uid;
    private int tick;
    private int playerCount;
    private int remainingPlayers;
    private long player;
}
