package de.bitwars.business_logic.moduels;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BasePositionBU {
    private int x;
    private int y;
    private int z;

    public int calcDistance(BasePositionBU basePosition) {
        double xDiff = Math.pow(basePosition.x - this.x, 2);
        double yDiff = Math.pow(basePosition.y - this.y, 2);
        double zDiff = Math.pow(basePosition.z - this.z, 2);
        return (int) Math.sqrt(xDiff + yDiff + zDiff);
    }
}
