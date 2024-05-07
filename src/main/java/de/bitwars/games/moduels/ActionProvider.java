package de.bitwars.games.moduels;

import java.util.List;

public interface ActionProvider {
    default String getUrl() {
        return "";
    }

    default int getId() {
        return 0;
    }

    List<PlayerActionBU> requestStep(GameBU gameBU);

}
