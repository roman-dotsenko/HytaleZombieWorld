package dev.hytalemodding.spawning;

import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.math.vector.Vector3f;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import dev.hytalemodding.config.SpawnDefinition;
import dev.hytalemodding.config.WaveTickDefinition;
import dev.hytalemodding.core.SpawnTracker;
import dev.hytalemodding.state.SpawnedEnemyData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class SpawnService {
    private static final double MIN_SPAWN_RADIUS = 3.0;
    private static final double MAX_SPAWN_RADIUS = 10.0;

    private final String worldName;
    private final NpcSpawnAdapter spawnAdapter;
    private final ComponentType<EntityStore, SpawnedEnemyData> spawnedEnemyComponentType;
    private final SpawnTracker spawnTracker;

    public SpawnService(
        String worldName,
        NpcSpawnAdapter spawnAdapter,
        ComponentType<EntityStore, SpawnedEnemyData> spawnedEnemyComponentType,
        SpawnTracker spawnTracker
    ) {
        this.worldName = worldName;
        this.spawnAdapter = spawnAdapter;
        this.spawnedEnemyComponentType = spawnedEnemyComponentType;
        this.spawnTracker = spawnTracker;
    }

    public void spawnTick(String waveId, int tickIndex, WaveTickDefinition tick) {
        World world = Universe.get().getWorld(this.worldName);
        if (world == null) {
            return;
        }

        Collection<PlayerRef> playerRefs = world.getPlayerRefs();
        if (playerRefs.isEmpty()) {
            return;
        }

        List<PlayerRef> players = new ArrayList<>(playerRefs);
        PlayerRef target = players.get(ThreadLocalRandom.current().nextInt(players.size()));

        world.execute(() -> {
            Ref<EntityStore> targetRef = target.getReference();
            if (targetRef == null || !targetRef.isValid()) {
                return;
            }

            Store<EntityStore> store = world.getEntityStore().getStore();
            TransformComponent transform = store.getComponent(targetRef, TransformComponent.getComponentType());
            if (transform == null) {
                return;
            }

            Vector3d playerPos = transform.getPosition();
            Vector3f rotation = new Vector3f(0.0F, 0.0F, 0.0F);

            for (SpawnDefinition spawn : tick.getSpawns()) {
                int spawnedCount = 0;
                for (int i = 0; i < spawn.getCount(); i++) {
                    Vector3d spawnPos = randomOffsetPosition(playerPos);
                    Ref<EntityStore> npcRef = this.spawnAdapter.spawnNpc(store, spawn.getMobId(), spawnPos, rotation);
                    if (npcRef != null && npcRef.isValid()) {
                        store.addComponent(npcRef, this.spawnedEnemyComponentType,
                            new SpawnedEnemyData(spawn.getMobId(), waveId, tickIndex, npcRef.toString()));
                        spawnedCount++;
                    }
                }

                this.spawnTracker.registerSpawn(waveId, tickIndex, spawnedCount);
            }
        });
    }

    private Vector3d randomOffsetPosition(Vector3d basePosition) {
        double angle = ThreadLocalRandom.current().nextDouble(0.0, Math.PI * 2.0);
        double distance = ThreadLocalRandom.current().nextDouble(MIN_SPAWN_RADIUS, MAX_SPAWN_RADIUS);
        return basePosition.clone().add(Math.cos(angle) * distance, 0.0, Math.sin(angle) * distance);
    }
}
