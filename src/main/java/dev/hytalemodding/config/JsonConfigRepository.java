package dev.hytalemodding.config;

import java.util.List;

public class JsonConfigRepository implements ConfigRepository {
    private final JsonConfigLoader loader;
    private final DefaultConfigFactory fallbackFactory;

    private GameConfig gameConfig;
    private List<StatConfig> statConfigs;
    private List<ClassConfig> classConfigs;
    private List<EnemyRewardConfig> enemyRewards;

    public JsonConfigRepository(String basePath) {
        this.loader = new JsonConfigLoader(basePath);
        this.fallbackFactory = new DefaultConfigFactory();
        load();
    }

    @Override
    public GameConfig getGameConfig(String worldName) {
        return this.gameConfig;
    }

    @Override
    public List<StatConfig> getStatConfigs() {
        return this.statConfigs;
    }

    @Override
    public List<ClassConfig> getClassConfigs() {
        return this.classConfigs;
    }

    @Override
    public List<EnemyRewardConfig> getEnemyRewards() {
        return this.enemyRewards;
    }

    private void load() {
        GameConfig loadedGameConfig = this.loader.loadGameConfig();
        List<StatConfig> loadedStats = this.loader.loadStatConfigs();
        List<ClassConfig> loadedClasses = this.loader.loadClassConfigs();
        List<EnemyRewardConfig> loadedRewards = this.loader.loadEnemyRewards();

        this.gameConfig = loadedGameConfig != null ? loadedGameConfig : this.fallbackFactory.createDefaultGameConfig();
        this.statConfigs = loadedStats != null ? loadedStats : this.fallbackFactory.createDefaultStatConfigs();
        this.classConfigs = loadedClasses != null ? loadedClasses : this.fallbackFactory.createDefaultClassConfigs();
        this.enemyRewards = loadedRewards != null ? loadedRewards : this.fallbackFactory.createDefaultEnemyRewards();
    }
}
