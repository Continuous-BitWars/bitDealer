package de.bitwars.business_logic.moduels;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BaseBU {
    private int uid;
    private long playerId;
    private BasePositionBU basePosition;

    private int population;
    private int level;
    private int unitsUntilUpgrade;
    private String name;

    public int calcDistance(BaseBU destinationBase) {
        return basePosition.calcDistance(destinationBase.basePosition);
    }

    public void takeTick(List<GameConfigBaseLevelsBU> configBaseLevels) {
        if (this.playerId == 0) {
            return;
        }
        int maxLevel = (configBaseLevels.size() - 1);
        if (this.level < 0 || this.level > maxLevel) {
            return;
        }
        GameConfigBaseLevelsBU baseLevel = configBaseLevels.get(this.level);

        if (this.population < baseLevel.getMaxPopulation()) {
            this.population += baseLevel.getSpawnRate();
        } else if (this.population > baseLevel.getMaxPopulation()) {
            this.population -= baseLevel.getSpawnRate();
        }
    }

    public void fight(BoardActionsBU boardActions) {
        this.population -= boardActions.getAmount();
        if (this.population < 0) {
            this.population *= -1;
            this.playerId = boardActions.getPlayer();
        }
    }

    public void moveAway(int amount) {
        this.population -= amount;
    }

    public void upgrade(int amount, List<GameConfigBaseLevelsBU> configBaseLevels) {
        this.unitsUntilUpgrade += amount;

        int maxLevel = (configBaseLevels.size() - 1);
        if (this.level < 0 || this.level >= maxLevel) {
            return;
        }
        GameConfigBaseLevelsBU currentBaseLevel = configBaseLevels.get(this.level);

        while (this.level < maxLevel && this.unitsUntilUpgrade >= currentBaseLevel.getUpgradeCost()) {
            this.unitsUntilUpgrade -= currentBaseLevel.getUpgradeCost();
            this.level++;
            currentBaseLevel = configBaseLevels.get(this.level);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseBU baseBU = (BaseBU) o;
        return uid == baseBU.uid;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(uid);
    }
}
