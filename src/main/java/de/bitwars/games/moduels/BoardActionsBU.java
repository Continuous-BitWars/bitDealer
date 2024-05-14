package de.bitwars.games.moduels;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BoardActionsBU {
    private static final Logger log = LoggerFactory.getLogger(BoardActionsBU.class);
    private UUID uuid;
    private long player;

    private int source;
    private int destination;
    private int amount;

    private BoardActionsProgressBU progress;

    public BoardActionsBU(long playerId, PlayerActionBU playerAction, int distance) {
        this.uuid = UUID.randomUUID();
        this.player = playerId;
        this.source = playerAction.getSource();
        this.destination = playerAction.getDestination();
        this.amount = playerAction.getAmount();
        this.progress = new BoardActionsProgressBU(distance, 0);
    }

    public void takeTick(GameConfigPathsBU configPaths) {
        if (isInDestination()) {
            log.warn("Calculation over BoardActions in Destination");
            return;
        }
        this.progress.takeStep();

        if (this.progress.getTraveled() > configPaths.getGracePeriod()) {
            this.amount -= configPaths.getDeathRate();
        }
    }

    public boolean isInDestination() {
        return progress.getTraveled() >= progress.getDistance();
    }

    public boolean isDone() {
        return progress.getTraveled() > progress.getDistance();
    }
}
