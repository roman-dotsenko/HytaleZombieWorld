package dev.hytalemodding.bootstrap;

import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import dev.hytalemodding.Main;
import dev.hytalemodding.commands.ReadyCommand;
import dev.hytalemodding.config.ConfigRepository;
import dev.hytalemodding.config.JsonConfigRepository;
import dev.hytalemodding.core.GameDirector;
import dev.hytalemodding.core.SpawnTracker;
import dev.hytalemodding.spawning.HytaleNpcSpawnAdapter;
import dev.hytalemodding.spawning.SpawnService;
import dev.hytalemodding.state.PlayerClassData;
import dev.hytalemodding.state.PlayerEconomyData;
import dev.hytalemodding.state.PlayerStatsData;
import dev.hytalemodding.state.SpawnedEnemyData;
import dev.hytalemodding.state.WaveStateData;
import dev.hytalemodding.systems.SpawnedEnemyDeathSystem;

public class GameBootstrap {
    private static final String DEFAULT_WORLD = "default";

    private final Main plugin;

    public GameBootstrap(Main plugin) {
        this.plugin = plugin;
    }

    public void start() {
        ComponentType<EntityStore, PlayerEconomyData> economyComponent = this.plugin.getEntityStoreRegistry()
            .registerComponent(PlayerEconomyData.class, "PlayerEconomyData", PlayerEconomyData.CODEC);
        ComponentType<EntityStore, PlayerStatsData> statsComponent = this.plugin.getEntityStoreRegistry()
            .registerComponent(PlayerStatsData.class, "PlayerStatsData", PlayerStatsData.CODEC);
        ComponentType<EntityStore, PlayerClassData> classComponent = this.plugin.getEntityStoreRegistry()
            .registerComponent(PlayerClassData.class, "PlayerClassData", PlayerClassData.CODEC);
        ComponentType<EntityStore, WaveStateData> waveStateComponent = this.plugin.getEntityStoreRegistry()
            .registerComponent(WaveStateData.class, "WaveStateData", WaveStateData.CODEC);
        ComponentType<EntityStore, SpawnedEnemyData> spawnedEnemyComponent = this.plugin.getEntityStoreRegistry()
            .registerComponent(SpawnedEnemyData.class, "SpawnedEnemyData", SpawnedEnemyData.CODEC);

        this.plugin.setPlayerEconomyComponent(economyComponent);
        this.plugin.setPlayerStatsComponent(statsComponent);
        this.plugin.setPlayerClassComponent(classComponent);
        this.plugin.setWaveStateComponent(waveStateComponent);
        this.plugin.setSpawnedEnemyComponent(spawnedEnemyComponent);

        ConfigRepository configRepository = new JsonConfigRepository("zombieworld/config");
        SpawnTracker spawnTracker = new SpawnTracker();
        SpawnService spawnService = new SpawnService(DEFAULT_WORLD, new HytaleNpcSpawnAdapter(), spawnedEnemyComponent, spawnTracker);

        GameDirector gameDirector = new GameDirector(DEFAULT_WORLD, configRepository, spawnService, spawnTracker);
        this.plugin.setGameDirector(gameDirector);

        this.plugin.getEntityStoreRegistry().registerSystem(new SpawnedEnemyDeathSystem(spawnedEnemyComponent, gameDirector));
        this.plugin.getCommandRegistry().registerCommand(new ReadyCommand());

        gameDirector.start();
    }
}
