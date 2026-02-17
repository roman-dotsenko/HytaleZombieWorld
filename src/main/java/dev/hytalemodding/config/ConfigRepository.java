package dev.hytalemodding.config;

import java.util.List;

public interface ConfigRepository {
    GameConfig getGameConfig(String worldName);

    List<StatConfig> getStatConfigs();

    List<ClassConfig> getClassConfigs();

    List<EnemyRewardConfig> getEnemyRewards();
}
