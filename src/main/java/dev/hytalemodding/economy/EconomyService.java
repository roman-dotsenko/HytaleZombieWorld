package dev.hytalemodding.economy;

import dev.hytalemodding.config.ConfigRepository;
import dev.hytalemodding.config.EnemyRewardConfig;
import dev.hytalemodding.config.StatConfig;
import dev.hytalemodding.state.PlayerEconomyData;

import java.util.HashMap;
import java.util.Map;

public class EconomyService {
    private final Map<String, StatConfig> statConfigById = new HashMap<>();
    private final Map<String, Integer> enemyRewardsById = new HashMap<>();

    public EconomyService(ConfigRepository configRepository) {
        for (StatConfig config : configRepository.getStatConfigs()) {
            this.statConfigById.put(config.getStatId(), config);
        }
        for (EnemyRewardConfig reward : configRepository.getEnemyRewards()) {
            this.enemyRewardsById.put(reward.getMobId(), reward.getCoins());
        }
    }

    public int getUpgradeCost(String statId, int currentLevel) {
        StatConfig config = this.statConfigById.get(statId);
        if (config == null) {
            return -1;
        }

        double cost = config.getBaseCost() * Math.pow(config.getCostMultiplier(), currentLevel);
        return (int) Math.ceil(cost);
    }

    public boolean trySpend(PlayerEconomyData economyData, int amount) {
        if (economyData == null) {
            return false;
        }

        return economyData.spendCoins(amount);
    }

    public int awardKill(PlayerEconomyData economyData, String enemyType) {
        if (economyData == null || enemyType == null) {
            return 0;
        }

        int coins = this.enemyRewardsById.getOrDefault(enemyType, 0);
        if (coins > 0) {
            economyData.addCoins(coins);
        }

        return coins;
    }
}
