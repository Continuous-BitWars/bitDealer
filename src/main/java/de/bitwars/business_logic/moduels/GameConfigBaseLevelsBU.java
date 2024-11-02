package de.bitwars.business_logic.moduels;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GameConfigBaseLevelsBU {
    private int maxPopulation;
    private int upgradeCost;
    private int spawnRate;
}
