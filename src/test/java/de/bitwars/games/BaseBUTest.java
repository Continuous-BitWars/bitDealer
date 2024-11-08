package de.bitwars.games;

import de.bitwars.business_logic.moduels.BaseBU;
import de.bitwars.business_logic.moduels.BasePositionBU;
import de.bitwars.business_logic.moduels.BoardActionsBU;
import de.bitwars.business_logic.moduels.BoardActionsProgressBU;
import de.bitwars.business_logic.moduels.GameConfigBaseLevelsBU;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.UUID;

public class BaseBUTest {

    @Test
    void upgradeTest_0() {
        BaseBU baseBU = new BaseBU(0, 1, new BasePositionBU(0, 0, 0), 10, 0, 0, "");

        ArrayList<GameConfigBaseLevelsBU> configBasisLevel = new ArrayList<>();
        configBasisLevel.add(new GameConfigBaseLevelsBU(20, 1000, 1));
        configBasisLevel.add(new GameConfigBaseLevelsBU(40, 1000, 2));

        baseBU.upgrade(500, configBasisLevel);

        Assertions.assertThat(baseBU.getPopulation()).isEqualTo(10);
        Assertions.assertThat(baseBU.getLevel()).isEqualTo(0);
        Assertions.assertThat(baseBU.getUnitsUntilUpgrade()).isEqualTo(500);
    }

    @Test
    void upgradeTest_1() {
        BaseBU baseBU = new BaseBU(0, 1, new BasePositionBU(0, 0, 0), 10, 1, 0, "");

        ArrayList<GameConfigBaseLevelsBU> configBasisLevel = new ArrayList<>();
        configBasisLevel.add(new GameConfigBaseLevelsBU(20, 1000, 1));
        configBasisLevel.add(new GameConfigBaseLevelsBU(40, 1000, 2));

        baseBU.upgrade(500, configBasisLevel);

        Assertions.assertThat(baseBU.getPopulation()).isEqualTo(10);
        Assertions.assertThat(baseBU.getLevel()).isEqualTo(1);
        Assertions.assertThat(baseBU.getUnitsUntilUpgrade()).isEqualTo(500);
    }

    @Test
    void upgradeTest_2() {
        BaseBU baseBU = new BaseBU(0, 1, new BasePositionBU(0, 0, 0), 10, 1, 0, "");

        ArrayList<GameConfigBaseLevelsBU> configBasisLevel = new ArrayList<>();
        configBasisLevel.add(new GameConfigBaseLevelsBU(20, 1000, 1));
        configBasisLevel.add(new GameConfigBaseLevelsBU(40, 1000, 2));

        baseBU.upgrade(1000, configBasisLevel);

        Assertions.assertThat(baseBU.getPopulation()).isEqualTo(10);
        Assertions.assertThat(baseBU.getLevel()).isEqualTo(2);
        Assertions.assertThat(baseBU.getUnitsUntilUpgrade()).isEqualTo(0);
    }

    @Test
    void upgradeTest_3() {
        BaseBU baseBU = new BaseBU(0, 1, new BasePositionBU(0, 0, 0), 10, 1, 0, "");

        ArrayList<GameConfigBaseLevelsBU> configBasisLevel = new ArrayList<>();
        configBasisLevel.add(new GameConfigBaseLevelsBU(20, 1000, 1));
        configBasisLevel.add(new GameConfigBaseLevelsBU(40, 1000, 2));

        baseBU.upgrade(1500, configBasisLevel);

        Assertions.assertThat(baseBU.getPopulation()).isEqualTo(10);
        Assertions.assertThat(baseBU.getLevel()).isEqualTo(2);
        Assertions.assertThat(baseBU.getUnitsUntilUpgrade()).isEqualTo(500);
    }

    @Test
    void upgradeTest_4() {
        BaseBU baseBU = new BaseBU(0, 1, new BasePositionBU(0, 0, 0), 10, 1, 0, "");

        ArrayList<GameConfigBaseLevelsBU> configBasisLevel = new ArrayList<>();
        configBasisLevel.add(new GameConfigBaseLevelsBU(20, 1000, 1));
        configBasisLevel.add(new GameConfigBaseLevelsBU(40, 1000, 2));

        baseBU.upgrade(2000, configBasisLevel);

        Assertions.assertThat(baseBU.getPopulation()).isEqualTo(10);
        Assertions.assertThat(baseBU.getLevel()).isEqualTo(2);
        Assertions.assertThat(baseBU.getUnitsUntilUpgrade()).isEqualTo(1000);
    }

    @Test
    void upgradeTest_5() {
        BaseBU baseBU = new BaseBU(0, 1, new BasePositionBU(0, 0, 0), 10, 1, 0, "");

        ArrayList<GameConfigBaseLevelsBU> configBasisLevel = new ArrayList<>();
        configBasisLevel.add(new GameConfigBaseLevelsBU(20, 1000, 1));
        configBasisLevel.add(new GameConfigBaseLevelsBU(40, 1000, 2));
        configBasisLevel.add(new GameConfigBaseLevelsBU(60, 1000, 2));

        baseBU.upgrade(2000, configBasisLevel);

        Assertions.assertThat(baseBU.getPopulation()).isEqualTo(10);
        Assertions.assertThat(baseBU.getLevel()).isEqualTo(3);
        Assertions.assertThat(baseBU.getUnitsUntilUpgrade()).isEqualTo(0);
    }

    @Test
    void upgradeTest_6() {
        BaseBU baseBU = new BaseBU(0, 1, new BasePositionBU(0, 0, 0), 10, 1, 0, "");

        ArrayList<GameConfigBaseLevelsBU> configBasisLevel = new ArrayList<>();
        configBasisLevel.add(new GameConfigBaseLevelsBU(20, 1000, 1));
        configBasisLevel.add(new GameConfigBaseLevelsBU(40, 1000, 2));
        configBasisLevel.add(new GameConfigBaseLevelsBU(60, 1000, 2));

        baseBU.upgrade(3500, configBasisLevel);

        Assertions.assertThat(baseBU.getPopulation()).isEqualTo(10);
        Assertions.assertThat(baseBU.getLevel()).isEqualTo(3);
        Assertions.assertThat(baseBU.getUnitsUntilUpgrade()).isEqualTo(1500);
    }

    @Test
    void upgradeTest_7() {
        BaseBU baseBU = new BaseBU(0, 1, new BasePositionBU(0, 0, 0), 10, 1, 0, "");

        ArrayList<GameConfigBaseLevelsBU> configBasisLevel = new ArrayList<>();
        configBasisLevel.add(new GameConfigBaseLevelsBU(20, 1000, 1));
        configBasisLevel.add(new GameConfigBaseLevelsBU(40, 1000, 2));
        configBasisLevel.add(new GameConfigBaseLevelsBU(60, 1000, 2));

        baseBU.upgrade(800, configBasisLevel);

        Assertions.assertThat(baseBU.getPopulation()).isEqualTo(10);
        Assertions.assertThat(baseBU.getLevel()).isEqualTo(1);
        Assertions.assertThat(baseBU.getUnitsUntilUpgrade()).isEqualTo(800);

        baseBU.upgrade(800, configBasisLevel);

        Assertions.assertThat(baseBU.getPopulation()).isEqualTo(10);
        Assertions.assertThat(baseBU.getLevel()).isEqualTo(2);
        Assertions.assertThat(baseBU.getUnitsUntilUpgrade()).isEqualTo(600);

        baseBU.upgrade(800, configBasisLevel);

        Assertions.assertThat(baseBU.getPopulation()).isEqualTo(10);
        Assertions.assertThat(baseBU.getLevel()).isEqualTo(3);
        Assertions.assertThat(baseBU.getUnitsUntilUpgrade()).isEqualTo(400);

        baseBU.upgrade(800, configBasisLevel);

        Assertions.assertThat(baseBU.getPopulation()).isEqualTo(10);
        Assertions.assertThat(baseBU.getLevel()).isEqualTo(3);
        Assertions.assertThat(baseBU.getUnitsUntilUpgrade()).isEqualTo(1200);
    }

    @Test
    void moveAway_0() {
        BaseBU baseBU = new BaseBU(0, 1, new BasePositionBU(0, 0, 0), 1000, 1, 0, "");

        baseBU.moveAway(100);

        Assertions.assertThat(baseBU.getPopulation()).isEqualTo(900);
        Assertions.assertThat(baseBU.getLevel()).isEqualTo(1);
        Assertions.assertThat(baseBU.getUnitsUntilUpgrade()).isEqualTo(0);
    }

    @Test
    void moveAway_1() {
        BaseBU baseBU = new BaseBU(0, 1, new BasePositionBU(0, 0, 0), 100, 1, 0, "");

        baseBU.moveAway(100);

        Assertions.assertThat(baseBU.getPopulation()).isEqualTo(0);
        Assertions.assertThat(baseBU.getLevel()).isEqualTo(1);
        Assertions.assertThat(baseBU.getUnitsUntilUpgrade()).isEqualTo(0);
    }

    @Test
    void moveAway_2() {
        BaseBU baseBU = new BaseBU(0, 1, new BasePositionBU(0, 0, 0), 100, 1, 0, "");

        baseBU.moveAway(200);

        Assertions.assertThat(baseBU.getPopulation()).isEqualTo(-100);
        Assertions.assertThat(baseBU.getLevel()).isEqualTo(1);
        Assertions.assertThat(baseBU.getUnitsUntilUpgrade()).isEqualTo(0);
    }

    @Test
    void moveAway_3() {
        BaseBU baseBU = new BaseBU(0, 1, new BasePositionBU(0, 0, 0), 100, 1, 0, "");

        baseBU.moveAway(-200);

        Assertions.assertThat(baseBU.getPopulation()).isEqualTo(300);
        Assertions.assertThat(baseBU.getLevel()).isEqualTo(1);
        Assertions.assertThat(baseBU.getUnitsUntilUpgrade()).isEqualTo(0);
    }

    @Test
    void fight_0() {
        BaseBU baseBU = new BaseBU(1, 1, new BasePositionBU(0, 0, 0), 100, 1, 0, "");

        BoardActionsBU boardActionsBU = new BoardActionsBU(
                UUID.fromString("9852c739-cf1d-49ee-bb80-4d9c372eda22"),
                2,
                2,
                1,
                50,
                new BoardActionsProgressBU(10, 10)
        );

        baseBU.fight(boardActionsBU);

        Assertions.assertThat(baseBU.getPopulation()).isEqualTo(50);
        Assertions.assertThat(baseBU.getLevel()).isEqualTo(1);
        Assertions.assertThat(baseBU.getUnitsUntilUpgrade()).isEqualTo(0);
        Assertions.assertThat(baseBU.getPlayerId()).isEqualTo(1);
    }

    @Test
    void fight_1() {
        BaseBU baseBU = new BaseBU(1, 1, new BasePositionBU(0, 0, 0), 100, 1, 0, "");

        BoardActionsBU boardActionsBU = new BoardActionsBU(
                UUID.fromString("9852c739-cf1d-49ee-bb80-4d9c372eda22"),
                2,
                2,
                1,
                100,
                new BoardActionsProgressBU(10, 10)
        );

        baseBU.fight(boardActionsBU);

        Assertions.assertThat(baseBU.getPopulation()).isEqualTo(0);
        Assertions.assertThat(baseBU.getLevel()).isEqualTo(1);
        Assertions.assertThat(baseBU.getUnitsUntilUpgrade()).isEqualTo(0);
        Assertions.assertThat(baseBU.getPlayerId()).isEqualTo(1);
    }

    @Test
    void fight_2() {
        BaseBU baseBU = new BaseBU(1, 1, new BasePositionBU(0, 0, 0), 100, 1, 0, "");

        BoardActionsBU boardActionsBU = new BoardActionsBU(
                UUID.fromString("9852c739-cf1d-49ee-bb80-4d9c372eda22"),
                2,
                2,
                1,
                101,
                new BoardActionsProgressBU(10, 10)
        );

        baseBU.fight(boardActionsBU);

        Assertions.assertThat(baseBU.getPopulation()).isEqualTo(1);
        Assertions.assertThat(baseBU.getLevel()).isEqualTo(1);
        Assertions.assertThat(baseBU.getUnitsUntilUpgrade()).isEqualTo(0);
        Assertions.assertThat(baseBU.getPlayerId()).isEqualTo(2);
    }

    @Test
    void fight_3() {
        BaseBU baseBU = new BaseBU(1, 1, new BasePositionBU(0, 0, 0), 100, 1, 0, "");

        BoardActionsBU boardActionsBU = new BoardActionsBU(
                UUID.fromString("9852c739-cf1d-49ee-bb80-4d9c372eda22"),
                2,
                2,
                1,
                150,
                new BoardActionsProgressBU(10, 10)
        );

        baseBU.fight(boardActionsBU);

        Assertions.assertThat(baseBU.getPopulation()).isEqualTo(50);
        Assertions.assertThat(baseBU.getLevel()).isEqualTo(1);
        Assertions.assertThat(baseBU.getUnitsUntilUpgrade()).isEqualTo(0);
        Assertions.assertThat(baseBU.getPlayerId()).isEqualTo(2);
    }

    @Test
    void takeTick_0() {
        BaseBU baseBU = new BaseBU(0, 0, new BasePositionBU(0, 0, 0), 10, 1, 0, "");

        ArrayList<GameConfigBaseLevelsBU> configBasisLevel = new ArrayList<>();
        configBasisLevel.add(new GameConfigBaseLevelsBU(20, 1000, 1));
        configBasisLevel.add(new GameConfigBaseLevelsBU(40, 1000, 2));
        configBasisLevel.add(new GameConfigBaseLevelsBU(60, 1000, 2));

        baseBU.takeTick(configBasisLevel);

        Assertions.assertThat(baseBU.getPopulation()).isEqualTo(10);
        Assertions.assertThat(baseBU.getLevel()).isEqualTo(1);
        Assertions.assertThat(baseBU.getUnitsUntilUpgrade()).isEqualTo(0);
    }

    @Test
    void takeTick_1() {
        BaseBU baseBU = new BaseBU(0, 1, new BasePositionBU(0, 0, 0), 10, 1, 0, "");

        ArrayList<GameConfigBaseLevelsBU> configBasisLevel = new ArrayList<>();
        configBasisLevel.add(new GameConfigBaseLevelsBU(20, 1000, 1));
        configBasisLevel.add(new GameConfigBaseLevelsBU(40, 1000, 2));
        configBasisLevel.add(new GameConfigBaseLevelsBU(60, 1000, 3));

        baseBU.takeTick(configBasisLevel);

        Assertions.assertThat(baseBU.getPopulation()).isEqualTo(11);
        Assertions.assertThat(baseBU.getLevel()).isEqualTo(1);
        Assertions.assertThat(baseBU.getUnitsUntilUpgrade()).isEqualTo(0);
    }

    @Test
    void takeTick_2() {
        BaseBU baseBU = new BaseBU(0, 1, new BasePositionBU(0, 0, 0), 20, 1, 0, "");

        ArrayList<GameConfigBaseLevelsBU> configBasisLevel = new ArrayList<>();
        configBasisLevel.add(new GameConfigBaseLevelsBU(20, 1000, 1));
        configBasisLevel.add(new GameConfigBaseLevelsBU(40, 1000, 2));
        configBasisLevel.add(new GameConfigBaseLevelsBU(60, 1000, 3));

        baseBU.takeTick(configBasisLevel);

        Assertions.assertThat(baseBU.getPopulation()).isEqualTo(20);
        Assertions.assertThat(baseBU.getLevel()).isEqualTo(1);
        Assertions.assertThat(baseBU.getUnitsUntilUpgrade()).isEqualTo(0);
    }

    @Test
    void takeTick_3() {
        BaseBU baseBU = new BaseBU(0, 1, new BasePositionBU(0, 0, 0), 30, 1, 0, "");

        ArrayList<GameConfigBaseLevelsBU> configBasisLevel = new ArrayList<>();
        configBasisLevel.add(new GameConfigBaseLevelsBU(20, 1000, 1));
        configBasisLevel.add(new GameConfigBaseLevelsBU(40, 1000, 2));
        configBasisLevel.add(new GameConfigBaseLevelsBU(60, 1000, 3));

        baseBU.takeTick(configBasisLevel);

        Assertions.assertThat(baseBU.getPopulation()).isEqualTo(29);
        Assertions.assertThat(baseBU.getLevel()).isEqualTo(1);
        Assertions.assertThat(baseBU.getUnitsUntilUpgrade()).isEqualTo(0);
    }

    @Test
    void takeTick_4() {
        BaseBU baseBU = new BaseBU(0, 1, new BasePositionBU(0, 0, 0), 30, 0, 0, "");

        ArrayList<GameConfigBaseLevelsBU> configBasisLevel = new ArrayList<>();
        configBasisLevel.add(new GameConfigBaseLevelsBU(20, 1000, 1));
        configBasisLevel.add(new GameConfigBaseLevelsBU(40, 1000, 2));
        configBasisLevel.add(new GameConfigBaseLevelsBU(60, 1000, 3));

        baseBU.takeTick(configBasisLevel);

        Assertions.assertThat(baseBU.getPopulation()).isEqualTo(30);
        Assertions.assertThat(baseBU.getLevel()).isEqualTo(0);
        Assertions.assertThat(baseBU.getUnitsUntilUpgrade()).isEqualTo(0);
    }

    @Test
    void takeTick_5() {
        BaseBU baseBU = new BaseBU(0, 1, new BasePositionBU(0, 0, 0), 20, 3, 0, "");

        ArrayList<GameConfigBaseLevelsBU> configBasisLevel = new ArrayList<>();
        configBasisLevel.add(new GameConfigBaseLevelsBU(20, 1000, 1));
        configBasisLevel.add(new GameConfigBaseLevelsBU(40, 1000, 2));
        configBasisLevel.add(new GameConfigBaseLevelsBU(60, 1000, 50));

        baseBU.takeTick(configBasisLevel);

        Assertions.assertThat(baseBU.getPopulation()).isEqualTo(70);
        Assertions.assertThat(baseBU.getLevel()).isEqualTo(3);
        Assertions.assertThat(baseBU.getUnitsUntilUpgrade()).isEqualTo(0);
    }

    @Test
    void takeTick_6() {
        BaseBU baseBU = new BaseBU(0, 1, new BasePositionBU(0, 0, 0), 70, 3, 0, "");

        ArrayList<GameConfigBaseLevelsBU> configBasisLevel = new ArrayList<>();
        configBasisLevel.add(new GameConfigBaseLevelsBU(20, 1000, 1));
        configBasisLevel.add(new GameConfigBaseLevelsBU(40, 1000, 2));
        configBasisLevel.add(new GameConfigBaseLevelsBU(60, 1000, 50));

        baseBU.takeTick(configBasisLevel);

        Assertions.assertThat(baseBU.getPopulation()).isEqualTo(20);
        Assertions.assertThat(baseBU.getLevel()).isEqualTo(3);
        Assertions.assertThat(baseBU.getUnitsUntilUpgrade()).isEqualTo(0);
    }

    @Test
    void takeTick_7() {
        BaseBU baseBU = new BaseBU(0, 1, new BasePositionBU(0, 0, 0), 70, 4, 0, "");

        ArrayList<GameConfigBaseLevelsBU> configBasisLevel = new ArrayList<>();
        configBasisLevel.add(new GameConfigBaseLevelsBU(20, 1000, 1));
        configBasisLevel.add(new GameConfigBaseLevelsBU(40, 1000, 2));
        configBasisLevel.add(new GameConfigBaseLevelsBU(60, 1000, 50));

        baseBU.takeTick(configBasisLevel);

        Assertions.assertThat(baseBU.getPopulation()).isEqualTo(70);
        Assertions.assertThat(baseBU.getLevel()).isEqualTo(4);
        Assertions.assertThat(baseBU.getUnitsUntilUpgrade()).isEqualTo(0);
    }
}
