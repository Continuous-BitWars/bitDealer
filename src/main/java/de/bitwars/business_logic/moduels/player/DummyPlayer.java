package de.bitwars.business_logic.moduels.player;

import de.bitwars.business_logic.moduels.ActionProvider;
import de.bitwars.business_logic.moduels.BaseBU;
import de.bitwars.business_logic.moduels.GameBU;
import de.bitwars.business_logic.moduels.PlayerActionBU;
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
        if (this.id >= 0 && this.id <= 2000) {
            Random rand = new Random();

            if (rand.nextBoolean()) {
                List<BaseBU> ownBases = gameBU.getGameField().getBases().values().stream().filter(baseBU -> baseBU.getPlayerId() == this.id && baseBU.getPopulation() > 2).toList();
                if (ownBases.isEmpty()) {
                    return List.of();
                }
                BaseBU baseSource = ownBases.get(rand.nextInt(ownBases.size()));
                int sendCount = rand.nextInt(baseSource.getPopulation() - 1);
                return List.of(new PlayerActionBU(baseSource.getUid(), baseSource.getUid(), sendCount));
            } else {
                List<BaseBU> ownBases = gameBU.getGameField().getBases().values().stream().filter(baseBU -> baseBU.getPlayerId() == this.id && baseBU.getPopulation() > 2).toList();
                List<BaseBU> otherBases = gameBU.getGameField().getBases().values().stream().filter(baseBU -> baseBU.getPlayerId() != this.id).toList();
                if (!ownBases.isEmpty() && !otherBases.isEmpty()) {
                    BaseBU baseSource = ownBases.get(rand.nextInt(ownBases.size()));
                    BaseBU baseTarget = otherBases.get(rand.nextInt(otherBases.size()));
                    int sendCount = rand.nextInt(baseSource.getPopulation() - 1);
                    return List.of(new PlayerActionBU(baseSource.getUid(), baseTarget.getUid(), sendCount));
                }
            }
        }
        return List.of();
    }
}
