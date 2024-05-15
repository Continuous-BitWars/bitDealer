package de.bitwars.games.moduels.player;

import de.bitwars.games.moduels.ActionProvider;
import de.bitwars.games.moduels.GameBU;
import de.bitwars.games.moduels.PlayerActionBU;

import java.util.List;

public class RemotePlayer implements ActionProvider {

    private final long id;
    private final String name;
    private final String url;
    private final String color;

    public RemotePlayer(long id, String name, String url, String color) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.color = color;
    }

    @Override
    public String getUrl() {
        return this.url;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public List<PlayerActionBU> requestStep(GameBU gameBU) {
        //TODO: make request
        return List.of();
    }
}
