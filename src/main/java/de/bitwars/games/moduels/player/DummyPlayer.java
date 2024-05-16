package de.bitwars.games.moduels.player;

import de.bitwars.games.moduels.ActionProvider;
import de.bitwars.games.moduels.BaseBU;
import de.bitwars.games.moduels.GameBU;
import de.bitwars.games.moduels.PlayerActionBU;

import java.util.List;
import java.util.Random;


public class DummyPlayer implements ActionProvider {

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
            List<BaseBU> ownBases = gameBU.getGameField().getBases().values().stream().filter(baseBU -> baseBU.getPlayerId() == this.id).toList();
            List<BaseBU> otherBases = gameBU.getGameField().getBases().values().stream().filter(baseBU -> baseBU.getPlayerId() != this.id).toList();
            if (!ownBases.isEmpty() && !otherBases.isEmpty()) {
                BaseBU baseSource = ownBases.get(rand.nextInt(ownBases.size()));
                BaseBU baseTarget = otherBases.get(rand.nextInt(otherBases.size()));
                int sendCount = rand.nextInt(baseSource.getPopulation() - 1);

                return List.of(new PlayerActionBU(baseSource.getUid(), baseTarget.getUid(), sendCount));

            }
        }
        return List.of();
    }
}
