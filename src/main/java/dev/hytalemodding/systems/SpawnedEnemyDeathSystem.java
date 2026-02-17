package dev.hytalemodding.systems;

import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.entity.damage.Damage;
import com.hypixel.hytale.server.core.modules.entity.damage.DeathComponent;
import com.hypixel.hytale.server.core.modules.entity.damage.DeathSystems;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import dev.hytalemodding.core.GameDirector;
import dev.hytalemodding.economy.EconomyService;
import dev.hytalemodding.state.PlayerEconomyData;
import dev.hytalemodding.state.SpawnedEnemyData;
import dev.hytalemodding.ui.CoinHud;

import javax.annotation.Nonnull;

public class SpawnedEnemyDeathSystem extends DeathSystems.OnDeathSystem {
    private final ComponentType<EntityStore, SpawnedEnemyData> spawnedEnemyComponent;
    private final ComponentType<EntityStore, PlayerEconomyData> playerEconomyComponent;
    private final GameDirector gameDirector;
    private final EconomyService economyService;

    public SpawnedEnemyDeathSystem(
        ComponentType<EntityStore, SpawnedEnemyData> spawnedEnemyComponent,
        ComponentType<EntityStore, PlayerEconomyData> playerEconomyComponent,
        GameDirector gameDirector,
        EconomyService economyService
    ) {
        this.spawnedEnemyComponent = spawnedEnemyComponent;
        this.playerEconomyComponent = playerEconomyComponent;
        this.gameDirector = gameDirector;
        this.economyService = economyService;
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

        awardKillCoins(component, commandBuffer, data.getEnemyType());

        this.gameDirector.onEnemyKilled(data);
    }

    private void awardKillCoins(DeathComponent component, CommandBuffer<EntityStore> commandBuffer, String enemyType) {
        Damage deathInfo = component.getDeathInfo();
        if (deathInfo == null) {
            return;
        }

        if (!(deathInfo.getSource() instanceof Damage.EntitySource entitySource)) {
            return;
        }

        Ref<EntityStore> sourceRef = entitySource.getRef();
        if (sourceRef == null || !sourceRef.isValid()) {
            return;
        }

        Player player = commandBuffer.getComponent(sourceRef, Player.getComponentType());
        if (player == null) {
            return;
        }

        PlayerEconomyData economyData = commandBuffer.ensureAndGetComponent(sourceRef, this.playerEconomyComponent);
        int awarded = this.economyService.awardKill(economyData, enemyType);
        if (awarded > 0) {
            updateCoinHud(player, economyData.getCoins());
        }
    }

    private void updateCoinHud(Player player, int coins) {
        if (player.getHudManager().getCustomHud() instanceof CoinHud coinHud) {
            coinHud.setCoins(coins);
        }
    }
}
