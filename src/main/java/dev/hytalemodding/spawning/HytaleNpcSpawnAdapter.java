package dev.hytalemodding.spawning;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.math.vector.Vector3f;
import com.hypixel.hytale.server.core.universe.world.npc.INonPlayerCharacter;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.npc.NPCPlugin;
import it.unimi.dsi.fastutil.Pair;

public class HytaleNpcSpawnAdapter implements NpcSpawnAdapter {
    @Override
    public Ref<EntityStore> spawnNpc(Store<EntityStore> store, String npcId, Vector3d position, Vector3f rotation) {
        Pair<Ref<EntityStore>, INonPlayerCharacter> result = NPCPlugin.get().spawnNPC(store, npcId, null, position, rotation);
        if (result == null) {
            return null;
        }

        return result.first();
    }
}
