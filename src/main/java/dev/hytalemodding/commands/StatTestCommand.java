package dev.hytalemodding.commands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractTargetPlayerCommand;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import dev.hytalemodding.Main;
import dev.hytalemodding.economy.EconomyService;
import dev.hytalemodding.progression.ProgressionService;
import dev.hytalemodding.state.PlayerEconomyData;
import dev.hytalemodding.state.PlayerStatsData;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class StatTestCommand extends AbstractTargetPlayerCommand {
    private final String statId;

    public StatTestCommand(String name, String description, String statId) {
        super(name, description);
        this.statId = statId;
    }

    @Override
    protected void execute(
        @Nonnull CommandContext context,
        @Nullable Ref<EntityStore> sourceRef,
        @Nonnull Ref<EntityStore> ref,
        @Nonnull PlayerRef playerRef,
        @Nonnull World world,
        @Nonnull Store<EntityStore> store
    ) {
        PlayerStatsData stats = store.ensureAndGetComponent(ref, Main.get().getPlayerStatsComponent());
        PlayerEconomyData economyData = store.ensureAndGetComponent(ref, Main.get().getPlayerEconomyComponent());
        ProgressionService progressionService = Main.get().getProgressionService();
        EconomyService economyService = Main.get().getEconomyService();

        if (!progressionService.canIncrementStat(stats, this.statId)) {
            int level = progressionService.getStatLevel(stats, this.statId);
            context.sendMessage(Message.raw("Cannot upgrade " + this.statId + ". Current level: " + level + "."));
            return;
        }

        int currentLevel = progressionService.getStatLevel(stats, this.statId);
        int cost = economyService.getUpgradeCost(this.statId, currentLevel);
        if (cost < 0) {
            context.sendMessage(Message.raw("No cost config for " + this.statId + "."));
            return;
        }

        if (!economyService.trySpend(economyData, cost)) {
            context.sendMessage(Message.raw("Not enough coins. Need " + cost + ", have " + economyData.getCoins() + "."));
            return;
        }

        boolean upgraded = progressionService.incrementStat(stats, this.statId);
        int level = progressionService.getStatLevel(stats, this.statId);

        if (upgraded) {
            context.sendMessage(Message.raw("Upgraded " + this.statId + " to level " + level + " (cost " + cost + ")."));
            return;
        }

        context.sendMessage(Message.raw("Upgrade failed for " + this.statId + "."));
    }
}
