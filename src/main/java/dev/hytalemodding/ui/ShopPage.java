package dev.hytalemodding.ui;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.packets.interface_.CustomPageLifetime;
import com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.entity.entities.player.pages.InteractiveCustomUIPage;
import com.hypixel.hytale.server.core.ui.builder.EventData;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.ui.builder.UIEventBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import dev.hytalemodding.Main;
import dev.hytalemodding.economy.EconomyService;
import dev.hytalemodding.progression.ProgressionService;
import dev.hytalemodding.state.PlayerEconomyData;
import dev.hytalemodding.state.PlayerStatsData;

import javax.annotation.Nonnull;

public class ShopPage extends InteractiveCustomUIPage<ShopPage.Data> {
	private static final String UI_PATH = "shop.ui";
	private static final String ACTION_BUY_POWER = "BuyPower";
	private static final String ACTION_BUY_SPEED = "BuySpeed";
	private static final String ACTION_CLOSE = "Close";

	public ShopPage(@Nonnull PlayerRef playerRef) {
		super(playerRef, CustomPageLifetime.CanDismiss, Data.CODEC);
	}

	@Override
	public void build(
		@Nonnull Ref<EntityStore> ref,
		@Nonnull UICommandBuilder commandBuilder,
		@Nonnull UIEventBuilder eventBuilder,
		@Nonnull Store<EntityStore> store
	) {
		commandBuilder.append(UI_PATH);
		PlayerEconomyData economyData = store.ensureAndGetComponent(ref, Main.get().getPlayerEconomyComponent());
		PlayerStatsData statsData = store.ensureAndGetComponent(ref, Main.get().getPlayerStatsComponent());
		updateLabels(commandBuilder, economyData, statsData);

		eventBuilder.addEventBinding(
			CustomUIEventBindingType.Activating,
			"#BuyPowerButton",
			EventData.of("Action", ACTION_BUY_POWER),
			false
		);
		eventBuilder.addEventBinding(
			CustomUIEventBindingType.Activating,
			"#BuySpeedButton",
			EventData.of("Action", ACTION_BUY_SPEED),
			false
		);
		eventBuilder.addEventBinding(
			CustomUIEventBindingType.Activating,
			"#CloseButton",
			EventData.of("Action", ACTION_CLOSE),
			false
		);
	}

	@Override
	public void handleDataEvent(@Nonnull Ref<EntityStore> ref, @Nonnull Store<EntityStore> store, @Nonnull Data data) {
		if (ACTION_CLOSE.equals(data.action)) {
			close();
			return;
		}

		Player player = store.getComponent(ref, Player.getComponentType());
		if (player == null) {
			sendUpdate();
			return;
		}

		PlayerEconomyData economyData = store.ensureAndGetComponent(ref, Main.get().getPlayerEconomyComponent());
		PlayerStatsData statsData = store.ensureAndGetComponent(ref, Main.get().getPlayerStatsComponent());
		ProgressionService progressionService = Main.get().getProgressionService();
		EconomyService economyService = Main.get().getEconomyService();

		if (ACTION_BUY_POWER.equals(data.action)) {
			handlePurchase(player, economyData, statsData, progressionService, economyService, ProgressionService.STAT_MELEE_ATTACK_POWER);
		} else if (ACTION_BUY_SPEED.equals(data.action)) {
			handlePurchase(player, economyData, statsData, progressionService, economyService, ProgressionService.STAT_MELEE_ATTACK_SPEED);
		}

		UICommandBuilder commandBuilder = new UICommandBuilder();
		updateLabels(commandBuilder, economyData, statsData);
		sendUpdate(commandBuilder, false);
	}

	private void handlePurchase(
		Player player,
		PlayerEconomyData economyData,
		PlayerStatsData statsData,
		ProgressionService progressionService,
		EconomyService economyService,
		String statId
	) {
		if (!progressionService.canIncrementStat(statsData, statId)) {
			player.sendMessage(Message.raw("Stat already at max level."));
			return;
		}

		int currentLevel = progressionService.getStatLevel(statsData, statId);
		int cost = economyService.getUpgradeCost(statId, currentLevel);
		if (cost < 0) {
			player.sendMessage(Message.raw("No cost config for " + statId + "."));
			return;
		}

		if (!economyService.trySpend(economyData, cost)) {
			player.sendMessage(Message.raw("Not enough coins. Need " + cost + ", have " + economyData.getCoins() + "."));
			return;
		}

		if (!progressionService.incrementStat(statsData, statId)) {
			player.sendMessage(Message.raw("Upgrade failed for " + statId + "."));
			return;
		}

		updateCoinHud(player, economyData.getCoins());
		player.sendMessage(Message.raw("Upgraded " + statId + " to level " + progressionService.getStatLevel(statsData, statId) + "."));
	}

	private void updateLabels(UICommandBuilder commandBuilder, PlayerEconomyData economyData, PlayerStatsData statsData) {
		ProgressionService progressionService = Main.get().getProgressionService();
		EconomyService economyService = Main.get().getEconomyService();

		int powerLevel = progressionService.getStatLevel(statsData, ProgressionService.STAT_MELEE_ATTACK_POWER);
		int speedLevel = progressionService.getStatLevel(statsData, ProgressionService.STAT_MELEE_ATTACK_SPEED);
		int powerCost = economyService.getUpgradeCost(ProgressionService.STAT_MELEE_ATTACK_POWER, powerLevel);
		int speedCost = economyService.getUpgradeCost(ProgressionService.STAT_MELEE_ATTACK_SPEED, speedLevel);

		commandBuilder.set("#CoinCount.TextSpans", Message.raw("Coins: " + economyData.getCoins()));
		commandBuilder.set("#PowerLevel.TextSpans", Message.raw("Level: " + powerLevel));
		commandBuilder.set("#SpeedLevel.TextSpans", Message.raw("Level: " + speedLevel));
		commandBuilder.set("#PowerCost.TextSpans", Message.raw("Cost: " + powerCost));
		commandBuilder.set("#SpeedCost.TextSpans", Message.raw("Cost: " + speedCost));
	}

	private void updateCoinHud(Player player, int coins) {
		if (player.getHudManager().getCustomHud() instanceof CoinHud coinHud) {
			coinHud.setCoins(coins);
		}
	}

	public static class Data {
		public static final BuilderCodec<Data> CODEC = BuilderCodec.builder(Data.class, Data::new)
			.append(new KeyedCodec<>("Action", Codec.STRING), (data, value) -> data.action = value, data -> data.action)
			.add()
			.build();

		private String action = "";
	}
}
