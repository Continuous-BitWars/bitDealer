package de.bitwars.models.game.moduels;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BoardActionsProgressBU {
    private int distance;
    private int traveled;

    public void takeStep() {
        this.traveled++;
    }
}
