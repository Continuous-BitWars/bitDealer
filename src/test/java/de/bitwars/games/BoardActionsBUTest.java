package de.bitwars.games;

import de.bitwars.business_logic.moduels.BoardActionsBU;
import de.bitwars.business_logic.moduels.BoardActionsProgressBU;
import de.bitwars.business_logic.moduels.GameConfigPathsBU;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public class BoardActionsBUTest {

    @Test
    void isInDestination_0() {
        BoardActionsBU boardActionsBU = new BoardActionsBU(
                UUID.fromString("9852c739-cf1d-49ee-bb80-4d9c372eda22"),
                2,
                2,
                1,
                100,
                new BoardActionsProgressBU(10, 0)
        );

        boolean isInDestinations = boardActionsBU.isInDestination();

        Assertions.assertThat(isInDestinations).isFalse();
    }

    @Test
    void isInDestination_1() {
        BoardActionsBU boardActionsBU = new BoardActionsBU(
                UUID.fromString("9852c739-cf1d-49ee-bb80-4d9c372eda22"),
                2,
                2,
                1,
                100,
                new BoardActionsProgressBU(10, 9)
        );

        boolean isInDestinations = boardActionsBU.isInDestination();

        Assertions.assertThat(isInDestinations).isFalse();
    }

    @Test
    void isInDestination_2() {
        BoardActionsBU boardActionsBU = new BoardActionsBU(
                UUID.fromString("9852c739-cf1d-49ee-bb80-4d9c372eda22"),
                2,
                2,
                1,
                100,
                new BoardActionsProgressBU(10, 10)
        );

        boolean isInDestinations = boardActionsBU.isInDestination();

        Assertions.assertThat(isInDestinations).isTrue();
    }

    @Test
    void isInDestination_3() {
        BoardActionsBU boardActionsBU = new BoardActionsBU(
                UUID.fromString("9852c739-cf1d-49ee-bb80-4d9c372eda22"),
                2,
                2,
                1,
                100,
                new BoardActionsProgressBU(10, 11)
        );

        boolean isInDestinations = boardActionsBU.isInDestination();

        Assertions.assertThat(isInDestinations).isTrue();
    }

    @Test
    void isInDestination_4() {
        BoardActionsBU boardActionsBU = new BoardActionsBU(
                UUID.fromString("9852c739-cf1d-49ee-bb80-4d9c372eda22"),
                2,
                2,
                1,
                100,
                new BoardActionsProgressBU(10, 15)
        );

        boolean isInDestinations = boardActionsBU.isInDestination();

        Assertions.assertThat(isInDestinations).isTrue();
    }

    @Test
    void takeTick_0() {
        BoardActionsBU boardActionsBU = new BoardActionsBU(
                UUID.fromString("9852c739-cf1d-49ee-bb80-4d9c372eda22"),
                2,
                2,
                1,
                100,
                new BoardActionsProgressBU(10, 10)
        );

        GameConfigPathsBU gameConfigPathsBU = new GameConfigPathsBU(100, 0);

        boardActionsBU.takeTick(gameConfigPathsBU);

        Assertions.assertThat(boardActionsBU.getAmount()).isEqualTo(100);
        Assertions.assertThat(boardActionsBU.getProgress().getTraveled()).isEqualTo(10);
        Assertions.assertThat(boardActionsBU.getProgress().getDistance()).isEqualTo(10);
    }

    @Test
    void takeTick_1() {
        BoardActionsBU boardActionsBU = new BoardActionsBU(
                UUID.fromString("9852c739-cf1d-49ee-bb80-4d9c372eda22"),
                2,
                2,
                1,
                100,
                new BoardActionsProgressBU(10, 0)
        );

        GameConfigPathsBU gameConfigPathsBU = new GameConfigPathsBU(100, 0);

        boardActionsBU.takeTick(gameConfigPathsBU);

        Assertions.assertThat(boardActionsBU.getAmount()).isEqualTo(100);
        Assertions.assertThat(boardActionsBU.getProgress().getTraveled()).isEqualTo(1);
        Assertions.assertThat(boardActionsBU.getProgress().getDistance()).isEqualTo(10);
    }

    @Test
    void takeTick_2() {
        BoardActionsBU boardActionsBU = new BoardActionsBU(
                UUID.fromString("9852c739-cf1d-49ee-bb80-4d9c372eda22"),
                2,
                2,
                1,
                100,
                new BoardActionsProgressBU(10, 4)
        );

        GameConfigPathsBU gameConfigPathsBU = new GameConfigPathsBU(5, 1);

        boardActionsBU.takeTick(gameConfigPathsBU);

        Assertions.assertThat(boardActionsBU.getAmount()).isEqualTo(100);
        Assertions.assertThat(boardActionsBU.getProgress().getTraveled()).isEqualTo(5);
        Assertions.assertThat(boardActionsBU.getProgress().getDistance()).isEqualTo(10);
    }

    @Test
    void takeTick_3() {
        BoardActionsBU boardActionsBU = new BoardActionsBU(
                UUID.fromString("9852c739-cf1d-49ee-bb80-4d9c372eda22"),
                2,
                2,
                1,
                100,
                new BoardActionsProgressBU(10, 5)
        );

        GameConfigPathsBU gameConfigPathsBU = new GameConfigPathsBU(5, 1);

        boardActionsBU.takeTick(gameConfigPathsBU);

        Assertions.assertThat(boardActionsBU.getAmount()).isEqualTo(99);
        Assertions.assertThat(boardActionsBU.getProgress().getTraveled()).isEqualTo(6);
        Assertions.assertThat(boardActionsBU.getProgress().getDistance()).isEqualTo(10);
    }

    @Test
    void takeTick_4() {
        BoardActionsBU boardActionsBU = new BoardActionsBU(
                UUID.fromString("9852c739-cf1d-49ee-bb80-4d9c372eda22"),
                2,
                2,
                1,
                100,
                new BoardActionsProgressBU(10, 7)
        );

        GameConfigPathsBU gameConfigPathsBU = new GameConfigPathsBU(5, 1);

        boardActionsBU.takeTick(gameConfigPathsBU);

        Assertions.assertThat(boardActionsBU.getAmount()).isEqualTo(99);
        Assertions.assertThat(boardActionsBU.getProgress().getTraveled()).isEqualTo(8);
        Assertions.assertThat(boardActionsBU.getProgress().getDistance()).isEqualTo(10);
    }

    @Test
    void takeTick_9() {
        BoardActionsBU boardActionsBU = new BoardActionsBU(
                UUID.fromString("9852c739-cf1d-49ee-bb80-4d9c372eda22"),
                2,
                2,
                1,
                100,
                new BoardActionsProgressBU(10, 9)
        );

        GameConfigPathsBU gameConfigPathsBU = new GameConfigPathsBU(5, 1);

        boardActionsBU.takeTick(gameConfigPathsBU);

        Assertions.assertThat(boardActionsBU.getAmount()).isEqualTo(99);
        Assertions.assertThat(boardActionsBU.getProgress().getTraveled()).isEqualTo(10);
        Assertions.assertThat(boardActionsBU.getProgress().getDistance()).isEqualTo(10);
    }
}
