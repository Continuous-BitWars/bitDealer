package de.bitwars.games.moduels;

import java.util.List;


public class DummyPlayer implements ActionProvider {

    private int id;

    public DummyPlayer(int id) {
        this.id = id;
    }


    @Override
    public String getUrl() {
        return "localhost";
    }

    @Override
    public int getId() {
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
