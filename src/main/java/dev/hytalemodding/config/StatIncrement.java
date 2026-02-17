package dev.hytalemodding.config;

public class StatIncrement {
    private final int level;
    private final double bonusDamage;
    private final double bonusAttackSpeed;

    public StatIncrement(int level, double bonusDamage, double bonusAttackSpeed) {
        this.level = level;
        this.bonusDamage = bonusDamage;
        this.bonusAttackSpeed = bonusAttackSpeed;
    }

    public int getLevel() {
        return this.level;
    }

    public double getBonusDamage() {
        return this.bonusDamage;
    }

    public double getBonusAttackSpeed() {
        return this.bonusAttackSpeed;
    }
}
