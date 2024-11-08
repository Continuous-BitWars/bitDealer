package de.bitwars.games;

import de.bitwars.business_logic.moduels.BasePositionBU;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class BasePositionBUTest {

    @Test
    void calcDistance_1() {
        BasePositionBU basePositionSRC = new BasePositionBU(0, 0, 0);
        BasePositionBU basePositionDEST = new BasePositionBU(5, 0, 0);

        int distance = basePositionSRC.calcDistance(basePositionDEST);

        Assertions.assertThat(distance).isEqualTo(5);
    }

    @Test
    void calcDistance_2() {
        BasePositionBU basePositionSRC = new BasePositionBU(0, 0, 0);
        BasePositionBU basePositionDEST = new BasePositionBU(5, 5, 0);

        int distance = basePositionSRC.calcDistance(basePositionDEST);

        Assertions.assertThat(distance).isEqualTo(7);
    }

    @Test
    void calcDistance_3() {
        BasePositionBU basePositionSRC = new BasePositionBU(0, 0, 0);
        BasePositionBU basePositionDEST = new BasePositionBU(5, 5, 5);

        int distance = basePositionSRC.calcDistance(basePositionDEST);

        Assertions.assertThat(distance).isEqualTo(8);
    }
}
