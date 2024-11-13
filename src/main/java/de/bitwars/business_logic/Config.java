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
                    new GameConfigBaseLevelsBU(20, 10, 1), //ESP
                    new GameConfigBaseLevelsBU(40, 20, 2), //Nano
                    new GameConfigBaseLevelsBU(80, 30, 3), //miniPC
                    new GameConfigBaseLevelsBU(100, 40, 4), //PC
                    new GameConfigBaseLevelsBU(200, 50, 5), //Server L1
                    new GameConfigBaseLevelsBU(300, 100, 6), //Server L2
                    new GameConfigBaseLevelsBU(400, 200, 7), //Server L3
                    new GameConfigBaseLevelsBU(500, 400, 8), //Server L4
                    new GameConfigBaseLevelsBU(600, 600, 9), //Server L5
                    new GameConfigBaseLevelsBU(700, 800, 10), //Server L6
                    new GameConfigBaseLevelsBU(800, 1000, 15), //Server L7
                    new GameConfigBaseLevelsBU(900, 1500, 20), //Server L8
                    new GameConfigBaseLevelsBU(1000, 2000, 25), //Server L9
                    new GameConfigBaseLevelsBU(2000, 3000, 50) //Server L10
            )
    );
}
