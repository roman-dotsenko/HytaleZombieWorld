package dev.hytalemodding.bootstrap;

import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import dev.hytalemodding.Main;
import dev.hytalemodding.commands.ReadyCommand;
import dev.hytalemodding.commands.ShopCommand;
import dev.hytalemodding.commands.StatTestCommand;
import dev.hytalemodding.config.ConfigRepository;
import dev.hytalemodding.config.JsonConfigRepository;
import dev.hytalemodding.core.GameDirector;
import dev.hytalemodding.core.SpawnTracker;
import dev.hytalemodding.economy.EconomyService;
import dev.hytalemodding.progression.ProgressionService;
import dev.hytalemodding.spawning.HytaleNpcSpawnAdapter;
import dev.hytalemodding.spawning.SpawnService;
import dev.hytalemodding.state.PlayerClassData;
import dev.hytalemodding.state.PlayerEconomyData;
import dev.hytalemodding.state.PlayerStatsData;
import dev.hytalemodding.state.SpawnedEnemyData;
import dev.hytalemodding.state.WaveStateData;
import dev.hytalemodding.systems.PlayerDamageModifierSystem;
import dev.hytalemodding.systems.PlayerInteractionTimeShiftSystem;
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
        EconomyService economyService = new EconomyService(configRepository);
        ProgressionService progressionService = new ProgressionService(configRepository);
        SpawnTracker spawnTracker = new SpawnTracker();
        SpawnService spawnService = new SpawnService(DEFAULT_WORLD, new HytaleNpcSpawnAdapter(), spawnedEnemyComponent, spawnTracker);

        GameDirector gameDirector = new GameDirector(DEFAULT_WORLD, configRepository, spawnService, spawnTracker);
        this.plugin.setGameDirector(gameDirector);
        this.plugin.setEconomyService(economyService);
        this.plugin.setProgressionService(progressionService);

        this.plugin.getEntityStoreRegistry().registerSystem(
            new SpawnedEnemyDeathSystem(spawnedEnemyComponent, economyComponent, gameDirector, economyService)
        );
        this.plugin.getEntityStoreRegistry().registerSystem(
            new PlayerInteractionTimeShiftSystem(statsComponent, progressionService)
        );
        this.plugin.getEntityStoreRegistry().registerSystem(
            new PlayerDamageModifierSystem(statsComponent, progressionService)
        );
        this.plugin.getCommandRegistry().registerCommand(new ReadyCommand());
        this.plugin.getCommandRegistry().registerCommand(new ShopCommand());
        this.plugin.getCommandRegistry().registerCommand(
            new StatTestCommand("statpower", "Increase melee attack power (test).", ProgressionService.STAT_MELEE_ATTACK_POWER)
        );
        this.plugin.getCommandRegistry().registerCommand(
            new StatTestCommand("statspeed", "Increase melee attack speed (test).", ProgressionService.STAT_MELEE_ATTACK_SPEED)
        );

        gameDirector.start();
    }
}
