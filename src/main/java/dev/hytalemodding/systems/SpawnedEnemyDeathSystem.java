package dev.hytalemodding.systems;

import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.server.core.modules.entity.damage.DeathComponent;
import com.hypixel.hytale.server.core.modules.entity.damage.DeathSystems;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import dev.hytalemodding.core.GameDirector;
import dev.hytalemodding.state.SpawnedEnemyData;

import javax.annotation.Nonnull;

public class SpawnedEnemyDeathSystem extends DeathSystems.OnDeathSystem {
    private final ComponentType<EntityStore, SpawnedEnemyData> spawnedEnemyComponent;
    private final GameDirector gameDirector;

    public SpawnedEnemyDeathSystem(
        ComponentType<EntityStore, SpawnedEnemyData> spawnedEnemyComponent,
        GameDirector gameDirector
    ) {
        this.spawnedEnemyComponent = spawnedEnemyComponent;
        this.gameDirector = gameDirector;
    }

    @Nonnull
    @Override
    public Query<EntityStore> getQuery() {
        return Query.and(this.spawnedEnemyComponent);
    }

    @Override
    public void onComponentAdded(
        @Nonnull Ref<EntityStore> ref,
        @Nonnull DeathComponent component,
        @Nonnull Store<EntityStore> store,
        @Nonnull CommandBuffer<EntityStore> commandBuffer
    ) {
        SpawnedEnemyData data = commandBuffer.getComponent(ref, this.spawnedEnemyComponent);
        if (data == null) {
            return;
        }

        this.gameDirector.onEnemyKilled(data);
    }
}
