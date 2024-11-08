package de.bitwars.business_logic;

import de.bitwars.business_logic.moduels.BaseBU;
import de.bitwars.business_logic.moduels.BasePositionBU;
import de.bitwars.business_logic.moduels.GameConfigBU;
import de.bitwars.business_logic.moduels.GameConfigBaseLevelsBU;
import de.bitwars.business_logic.moduels.GameConfigPathsBU;
import de.bitwars.business_logic.moduels.GameMapBU;

import java.util.List;

public class Config {

    public static GameMapBU defaultMap = new GameMapBU("Default", 4, List.of(
            new BaseBU(1, 1, new BasePositionBU(0, 0, 0), 10, 0, 0, ""),

            new BaseBU(2, 0, new BasePositionBU(3, -3, 0), 20, 1, 0, ""),
            new BaseBU(3, 0, new BasePositionBU(3, 3, 0), 20, 1, 0, ""),
            new BaseBU(4, 0, new BasePositionBU(3, 0, -3), 20, 1, 0, ""),
            new BaseBU(5, 0, new BasePositionBU(3, 0, 3), 20, 1, 0, ""),
            new BaseBU(6, 0, new BasePositionBU(3, 0, 0), 100, 1, 0, ""),

            new BaseBU(7, 2, new BasePositionBU(6, 0, 0), 10, 0, 0, ""),
            new BaseBU(8, 3, new BasePositionBU(3, 6, 0), 10, 0, 0, ""),
            new BaseBU(9, 4, new BasePositionBU(3, -6, 0), 10, 0, 0, "")
    ));
    public static GameConfigBU defaultOptions = new GameConfigBU(
            new GameConfigPathsBU(10, 1),
            List.of(
                    new GameConfigBaseLevelsBU(20, 0, 1),
                    new GameConfigBaseLevelsBU(40, 10, 2),
                    new GameConfigBaseLevelsBU(80, 20, 3),
                    new GameConfigBaseLevelsBU(100, 30, 4),
                    new GameConfigBaseLevelsBU(200, 40, 5)
            )
    );
}
