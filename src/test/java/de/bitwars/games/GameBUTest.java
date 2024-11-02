package de.bitwars.games;

import de.bitwars.business_logic.moduels.ActionProvider;
import de.bitwars.business_logic.moduels.BaseBU;
import de.bitwars.business_logic.moduels.BasePositionBU;
import de.bitwars.business_logic.moduels.GameBU;
import de.bitwars.business_logic.moduels.GameConfigBU;
import de.bitwars.business_logic.moduels.GameConfigBaseLevelsBU;
import de.bitwars.business_logic.moduels.GameConfigPathsBU;
import de.bitwars.business_logic.moduels.GameMapBU;
import de.bitwars.business_logic.moduels.player.DummyPlayer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GameBUTest {

    @Test
    void testGameRun() {
        final long gameId = 1L;

        final Collection<ActionProvider> players = new ArrayList<>();
        players.add(new DummyPlayer(1001, "#FF0000"));
        players.add(new DummyPlayer(1002, "#0000FF"));

        final List<BaseBU> bases = new ArrayList<>();
        bases.add(new BaseBU(0, 1, new BasePositionBU(0, 0, 0), 10, 0, 0));
        bases.add(new BaseBU(1, 0, new BasePositionBU(2, 0, 0), 10, 0, 0));
        bases.add(new BaseBU(2, 2, new BasePositionBU(4, 0, 0), 10, 0, 0));
        final GameMapBU gameMapBU = new GameMapBU("Demo", 2, bases);

        ArrayList<GameConfigBaseLevelsBU> configBasisLevel = new ArrayList<>();
        configBasisLevel.add(new GameConfigBaseLevelsBU(20, 1000, 1));

        final GameConfigBU gameConfig = new GameConfigBU(new GameConfigPathsBU(10, 0), configBasisLevel);


        GameBU testGame = new GameBU(gameId, "TEST_GAME", gameConfig, gameMapBU, null);
        players.forEach(testGame::addPlayer);
        for (int i = 0; i < 10; i++) {
            testGame.run();

        }


        Assertions.assertThat(testGame.getPlayers()).hasSize(2);
    }
}
