package dev.hytalemodding.events;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import dev.hytalemodding.Main;

public class PlayerReadyHandler {
    public static void onPlayerReady(PlayerReadyEvent event) {
        Player player = event.getPlayer();
        Ref<EntityStore> ref = event.getPlayerRef();
        World world = player.getWorld();
        if (world == null || ref == null || !ref.isValid()) {
            return;
        }

        world.execute(() -> {
            Store<EntityStore> store = world.getEntityStore().getStore();
            store.ensureAndGetComponent(ref, Main.get().getPlayerEconomyComponent());
            store.ensureAndGetComponent(ref, Main.get().getPlayerStatsComponent());
            store.ensureAndGetComponent(ref, Main.get().getPlayerClassComponent());
        });

        player.sendMessage(Message.raw("Zombie World initialized. Type /ready to skip breaks."));
    }
}
