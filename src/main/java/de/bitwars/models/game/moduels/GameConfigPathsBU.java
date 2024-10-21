package de.bitwars.models.game.moduels;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GameConfigPathsBU {
    private int gracePeriod;
    private int deathRate;
}
