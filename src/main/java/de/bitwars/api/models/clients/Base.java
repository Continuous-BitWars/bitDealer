package de.bitwars.api.models.clients;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Base {
    private int uid;
    private long player;
    private int population;
    private int level;
    private int unitsUntilUpgrade;
    private Position position;
    private String name = "";
}

