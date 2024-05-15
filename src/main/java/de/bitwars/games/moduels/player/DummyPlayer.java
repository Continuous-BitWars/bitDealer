package de.bitwars.games.moduels.player;

import de.bitwars.games.moduels.ActionProvider;
import de.bitwars.games.moduels.BaseBU;
import de.bitwars.games.moduels.GameBU;
import de.bitwars.games.moduels.PlayerActionBU;

import java.util.List;
import java.util.Optional;
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
        if (this.id == 1001 || this.id == 1002) {
            Optional<BaseBU> first = gameBU.getGameField().getBases().values().stream().filter(baseBU -> baseBU.getPlayerId() == this.id).findAny();
            if (first.isPresent()) {
                Random r = new Random();
                int sendCount = (r.nextInt(4) + 1);
                if (sendCount > first.get().getPopulation()) {
                    sendCount = first.get().getPopulation() - 1;
                }
                return List.of(new PlayerActionBU(first.get().getUid(), 2, sendCount));
            }
        }
        return List.of();
    }
}
