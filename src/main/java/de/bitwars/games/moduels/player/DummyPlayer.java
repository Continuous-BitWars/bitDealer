package de.bitwars.games.moduels.player;

import de.bitwars.games.moduels.ActionProvider;
import de.bitwars.games.moduels.GameBU;
import de.bitwars.games.moduels.PlayerActionBU;

import java.util.List;


public class DummyPlayer implements ActionProvider {

    private final long id;

    public DummyPlayer(long id) {
        this.id = id;
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
    public List<PlayerActionBU> requestStep(GameBU gameBU) {
        if (this.id == 1001) {
            return List.of(new PlayerActionBU(0, 1, 1));
        }
        return List.of();
    }
}
