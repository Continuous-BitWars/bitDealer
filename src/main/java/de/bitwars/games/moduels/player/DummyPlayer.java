package de.bitwars.games.moduels.player;

import de.bitwars.games.moduels.ActionProvider;
import de.bitwars.games.moduels.BaseBU;
import de.bitwars.games.moduels.GameBU;
import de.bitwars.games.moduels.PlayerActionBU;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Random;


public class DummyPlayer implements ActionProvider {

    private static final Logger log = LoggerFactory.getLogger(DummyPlayer.class);
    private final long id;
    private final String color;

    public DummyPlayer(long id, String color) {
        this.id = id;
        this.color = color;
    }


    @Override
    public String getUrl() {
        return "localhost";
    }

    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return "DummyPlayer_" + this.id;
    }

    @Override
    public String getColor() {
        return this.color;
    }

    @Override
    public List<PlayerActionBU> requestStep(GameBU gameBU) {
        if (this.id >= 1001 && this.id <= 1010) {
            Random rand = new Random();
            log.debug("requestStep {} -> 01", this.id);
            List<BaseBU> ownBases = gameBU.getGameField().getBases().values().stream().filter(baseBU -> baseBU.getPlayerId() == this.id && baseBU.getPopulation() > 2).toList();
            log.debug("requestStep {} -> 02", this.id);
            List<BaseBU> otherBases = gameBU.getGameField().getBases().values().stream().filter(baseBU -> baseBU.getPlayerId() != this.id).toList();
            log.debug("requestStep {} -> 03", this.id);
            if (!ownBases.isEmpty() && !otherBases.isEmpty()) {
                log.debug("requestStep {} -> 04", this.id);
                BaseBU baseSource = ownBases.get(rand.nextInt(ownBases.size()));
                log.debug("requestStep {} -> 05", this.id);
                BaseBU baseTarget = otherBases.get(rand.nextInt(otherBases.size()));
                log.debug("requestStep {} -> 06", this.id);
                int sendCount = rand.nextInt(baseSource.getPopulation() - 1);
                log.debug("requestStep {} -> 07", this.id);

                return List.of(new PlayerActionBU(baseSource.getUid(), baseTarget.getUid(), sendCount));

            }
        }
        return List.of();
    }
}
