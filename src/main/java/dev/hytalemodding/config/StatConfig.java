package dev.hytalemodding.config;

public class StatConfig {
    private final String statId;
    private final int baseCost;
    private final double costMultiplier;
    private final int maxLevel;
    private final java.util.List<StatIncrement> increments;

    public StatConfig(String statId, int baseCost, double costMultiplier, int maxLevel, java.util.List<StatIncrement> increments) {
        this.statId = statId;
        this.baseCost = baseCost;
        this.costMultiplier = costMultiplier;
        this.maxLevel = maxLevel;
        this.increments = java.util.Collections.unmodifiableList(new java.util.ArrayList<>(increments));
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

    public java.util.List<StatIncrement> getIncrements() {
        return this.increments;
    }
}
