package dev.hytalemodding.systems;

import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.tick.EntityTickingSystem;
import com.hypixel.hytale.protocol.InteractionType;
import com.hypixel.hytale.server.core.entity.InteractionManager;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.interaction.InteractionModule;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import dev.hytalemodding.progression.ProgressionService;
import dev.hytalemodding.state.PlayerStatsData;

import javax.annotation.Nonnull;

public class PlayerInteractionTimeShiftSystem extends EntityTickingSystem<EntityStore> {
    private final ComponentType<EntityStore, InteractionManager> interactionManagerComponent;
    private final ComponentType<EntityStore, PlayerStatsData> playerStatsComponent;
    private final ProgressionService progressionService;

    public PlayerInteractionTimeShiftSystem(
        ComponentType<EntityStore, PlayerStatsData> playerStatsComponent,
        ProgressionService progressionService
    ) {
        this.interactionManagerComponent = InteractionModule.get().getInteractionManagerComponent();
        this.playerStatsComponent = playerStatsComponent;
        this.progressionService = progressionService;
    }

    @Nonnull
    @Override
    public Query<EntityStore> getQuery() {
        return Query.and(Player.getComponentType(), this.playerStatsComponent, this.interactionManagerComponent);
    }

    @Override
    public void tick(
        float dt,
        int index,
        @Nonnull ArchetypeChunk<EntityStore> chunk,
        @Nonnull Store<EntityStore> store,
        @Nonnull CommandBuffer<EntityStore> commandBuffer
    ) {
        InteractionManager interactionManager = chunk.getComponent(index, this.interactionManagerComponent);
        PlayerStatsData stats = chunk.getComponent(index, this.playerStatsComponent);
        if (interactionManager == null || stats == null) {
            return;
        }

        double speedBonus = this.progressionService.getAttackSpeedBonus(stats);
        float shiftSeconds = (float) Math.max(0.0, speedBonus);

        interactionManager.setGlobalTimeShift(InteractionType.Held, shiftSeconds);
        interactionManager.setGlobalTimeShift(InteractionType.Equipped, shiftSeconds);
    }
}
