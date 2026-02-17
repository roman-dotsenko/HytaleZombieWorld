package dev.hytalemodding.systems;

import com.hypixel.hytale.component.Archetype;
import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.SystemGroup;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.entity.damage.Damage;
import com.hypixel.hytale.server.core.modules.entity.damage.DamageEventSystem;
import com.hypixel.hytale.server.core.modules.entity.damage.DamageModule;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import dev.hytalemodding.progression.ProgressionService;
import dev.hytalemodding.state.PlayerStatsData;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PlayerDamageModifierSystem extends DamageEventSystem {
    private final ComponentType<EntityStore, PlayerStatsData> playerStatsComponent;
    private final ProgressionService progressionService;

    public PlayerDamageModifierSystem(
        ComponentType<EntityStore, PlayerStatsData> playerStatsComponent,
        ProgressionService progressionService
    ) {
        this.playerStatsComponent = playerStatsComponent;
        this.progressionService = progressionService;
    }

    @Nullable
    @Override
    public SystemGroup<EntityStore> getGroup() {
        return DamageModule.get().getFilterDamageGroup();
    }

    @Nonnull
    @Override
    public Query<EntityStore> getQuery() {
        return Archetype.empty();
    }

    @Override
    public void handle(
        int index,
        @Nonnull ArchetypeChunk<EntityStore> archetypeChunk,
        @Nonnull Store<EntityStore> store,
        @Nonnull CommandBuffer<EntityStore> commandBuffer,
        @Nonnull Damage damage
    ) {
        if (damage.isCancelled()) {
            return;
        }

        if (!(damage.getSource() instanceof Damage.EntitySource entitySource)) {
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

        PlayerStatsData stats = commandBuffer.getComponent(sourceRef, this.playerStatsComponent);
        if (stats == null) {
            return;
        }

        double bonusDamage = this.progressionService.getAttackPowerBonus(stats);
        if (bonusDamage <= 0.0) {
            return;
        }

        damage.setAmount((float) (damage.getAmount() + bonusDamage));
    }
}
