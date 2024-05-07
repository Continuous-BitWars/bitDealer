package de.bitwars.games.moduels;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GameConfigBU {
    private GameConfigPathsBU pathsConfig;
    private List<GameConfigBaseLevelsBU> baseLevelsConfig;
}
