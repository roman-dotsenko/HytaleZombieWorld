package dev.hytalemodding.spawning;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.math.vector.Vector3f;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

public interface NpcSpawnAdapter {
    Ref<EntityStore> spawnNpc(Store<EntityStore> store, String npcId, Vector3d position, Vector3f rotation);
}
