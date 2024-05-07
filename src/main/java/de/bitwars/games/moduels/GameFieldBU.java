package de.bitwars.games.moduels;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GameFieldBU {
    private Map<Integer, BaseBU> bases;
    private List<BoardActionsBU> boardActions;

    public GameFieldBU(GameMapBU gameMap) {
        this.boardActions = new ArrayList<>();
        this.bases = new HashMap<>();
        gameMap.getBases().forEach(baseBU -> this.bases.put(baseBU.getUid(), baseBU));
    }
}

