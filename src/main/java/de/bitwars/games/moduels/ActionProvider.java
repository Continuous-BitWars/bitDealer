package de.bitwars.games.moduels;

import java.util.List;

public interface ActionProvider {
    default String getUrl() {
        return "";
    }

    default String getName() {
        return "Butterlampe";
    }

    default String getColor() {
        return "#DA3359";
    }

    default long getId() {
        return 0;
    }

    List<PlayerActionBU> requestStep(GameBU gameBU);

}
