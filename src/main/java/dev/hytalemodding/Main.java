package dev.hytalemodding;

import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import dev.hytalemodding.bootstrap.GameBootstrap;
import dev.hytalemodding.core.GameDirector;
import dev.hytalemodding.events.PlayerReadyHandler;
import dev.hytalemodding.state.PlayerClassData;
import dev.hytalemodding.state.PlayerEconomyData;
import dev.hytalemodding.state.PlayerStatsData;
import dev.hytalemodding.state.SpawnedEnemyData;
import dev.hytalemodding.state.WaveStateData;

import javax.annotation.Nonnull;

public class Main extends JavaPlugin {
    private static Main instance;

    private ComponentType<EntityStore, PlayerEconomyData> playerEconomyComponent;
    private ComponentType<EntityStore, PlayerStatsData> playerStatsComponent;
    private ComponentType<EntityStore, PlayerClassData> playerClassComponent;
    private ComponentType<EntityStore, WaveStateData> waveStateComponent;
    private ComponentType<EntityStore, SpawnedEnemyData> spawnedEnemyComponent;
    private GameDirector gameDirector;

    public Main(@Nonnull JavaPluginInit init) {
        super(init);
        instance = this;
    }

    public static Main get() {
        return instance;
    }

    @Override
    protected void setup() {
        GameBootstrap bootstrap = new GameBootstrap(this);
        bootstrap.start();
        this.getEventRegistry().registerGlobal(PlayerReadyEvent.class, PlayerReadyHandler::onPlayerReady);
    }

    public ComponentType<EntityStore, PlayerEconomyData> getPlayerEconomyComponent() {
        return this.playerEconomyComponent;
    }

    public void setPlayerEconomyComponent(ComponentType<EntityStore, PlayerEconomyData> componentType) {
        this.playerEconomyComponent = componentType;
    }

    public ComponentType<EntityStore, PlayerStatsData> getPlayerStatsComponent() {
        return this.playerStatsComponent;
    }

    public void setPlayerStatsComponent(ComponentType<EntityStore, PlayerStatsData> componentType) {
        this.playerStatsComponent = componentType;
    }

    public ComponentType<EntityStore, PlayerClassData> getPlayerClassComponent() {
        return this.playerClassComponent;
    }

    public void setPlayerClassComponent(ComponentType<EntityStore, PlayerClassData> componentType) {
        this.playerClassComponent = componentType;
    }

    public ComponentType<EntityStore, WaveStateData> getWaveStateComponent() {
        return this.waveStateComponent;
    }

    public void setWaveStateComponent(ComponentType<EntityStore, WaveStateData> componentType) {
        this.waveStateComponent = componentType;
    }

    public ComponentType<EntityStore, SpawnedEnemyData> getSpawnedEnemyComponent() {
        return this.spawnedEnemyComponent;
    }

    public void setSpawnedEnemyComponent(ComponentType<EntityStore, SpawnedEnemyData> componentType) {
        this.spawnedEnemyComponent = componentType;
    }

    public GameDirector getGameDirector() {
        return this.gameDirector;
    }

    public void setGameDirector(GameDirector gameDirector) {
        this.gameDirector = gameDirector;
    }
}