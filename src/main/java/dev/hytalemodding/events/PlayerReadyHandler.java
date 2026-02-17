package dev.hytalemodding.events;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import dev.hytalemodding.Main;
import dev.hytalemodding.state.PlayerEconomyData;
import dev.hytalemodding.ui.CoinHud;

import java.util.concurrent.CompletableFuture;

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
            player.sendMessage(Message.raw("CoinHUD initializing.."));

            PlayerRef playerRef = store.getComponent(ref, PlayerRef.getComponentType());
            if (playerRef != null) {
                CoinHud coinHud = new CoinHud(playerRef);
                player.getHudManager().setCustomHud(playerRef, coinHud);

                PlayerEconomyData economyData = store.getComponent(ref, Main.get().getPlayerEconomyComponent());
                if (economyData != null) {
                    coinHud.setCoins(economyData.getCoins());
                }
                player.sendMessage(Message.raw("CoinHUD initialized!"));
            }

        });

        player.sendMessage(Message.raw("Zombie World initialized. Type /shop to upgrade stats."));
    }
}
