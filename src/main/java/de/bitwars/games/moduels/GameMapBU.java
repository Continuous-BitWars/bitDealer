package de.bitwars.games.moduels;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GameMapBU {
    private String name;
    private int maxPlayerCount;
    private List<BaseBU> bases;
}

