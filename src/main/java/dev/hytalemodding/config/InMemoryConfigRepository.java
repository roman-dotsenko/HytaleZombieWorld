package dev.hytalemodding.config;

import java.util.List;

public class InMemoryConfigRepository implements ConfigRepository {
    private final DefaultConfigFactory factory = new DefaultConfigFactory();
    private final GameConfig gameConfig = this.factory.createDefaultGameConfig();
    private final List<StatConfig> statConfigs = this.factory.createDefaultStatConfigs();
    private final List<ClassConfig> classConfigs = this.factory.createDefaultClassConfigs();

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
}
