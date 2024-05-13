package de.bitwars.api.models.clients;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BoardActions {
    private String uuid;
    private long player;
    private int src;
    private int dest;
    private int amount;
    private Progress progress;
}
