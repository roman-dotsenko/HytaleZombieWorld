package dev.hytalemodding.config;

public class StatConfig {
    private final String statId;
    private final int baseCost;
    private final double costMultiplier;
    private final int maxLevel;

    public StatConfig(String statId, int baseCost, double costMultiplier, int maxLevel) {
        this.statId = statId;
        this.baseCost = baseCost;
        this.costMultiplier = costMultiplier;
        this.maxLevel = maxLevel;
    }

    public String getStatId() {
        return this.statId;
    }

    public int getBaseCost() {
        return this.baseCost;
    }

    public double getCostMultiplier() {
        return this.costMultiplier;
    }

    public int getMaxLevel() {
        return this.maxLevel;
    }
}
