package dev.hytalemodding.config;

public class EnemyRewardConfig {
    private final String mobId;
    private final int coins;

    public EnemyRewardConfig(String mobId, int coins) {
        this.mobId = mobId;
        this.coins = coins;
    }

    public String getMobId() {
        return this.mobId;
    }

    public int getCoins() {
        return this.coins;
    }
}
