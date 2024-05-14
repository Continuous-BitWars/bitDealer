package de.bitwars.games;

import de.bitwars.games.moduels.BaseBU;
import de.bitwars.games.moduels.BasePositionBU;
import de.bitwars.games.moduels.GameConfigBU;
import de.bitwars.games.moduels.GameConfigBaseLevelsBU;
import de.bitwars.games.moduels.GameConfigPathsBU;
import de.bitwars.games.moduels.GameMapBU;

import java.util.List;

public class Config {

    //TODO: replace code to new functions

    public static GameMapBU defaultMap = new GameMapBU("Default", 2, List.of(
            new BaseBU(1, 1, new BasePositionBU(0, 0, 0), 10, 0, 0),
            new BaseBU(2, 0, new BasePositionBU(3, 0, 0), 10, 0, 0),
            new BaseBU(3, 2, new BasePositionBU(6, 0, 0), 10, 0, 0)
    ));
    public static GameConfigBU defaultOptions = new GameConfigBU(
            new GameConfigPathsBU(10, 1),
            List.of(
                    new GameConfigBaseLevelsBU(20, 1000, 1),
                    new GameConfigBaseLevelsBU(40, 1000, 2),
                    new GameConfigBaseLevelsBU(80, 1000, 3)
            )
    );
}
