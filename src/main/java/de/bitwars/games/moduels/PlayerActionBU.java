package de.bitwars.games.moduels;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PlayerActionBU {
    private int source;
    private int destination;
    private int amount;
}
