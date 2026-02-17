package dev.hytalemodding.ui;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.player.hud.CustomUIHud;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;

import javax.annotation.Nonnull;

public class CoinHud extends CustomUIHud {
	private static final String UI_PATH = "CoinHud.ui";
	private static final String COIN_LABEL_SELECTOR = "#CoinValue.TextSpans";

	private int coins;

	public CoinHud(@Nonnull PlayerRef playerRef) {
		super(playerRef);
		this.coins = 0;
	}

	@Override
	protected void build(@Nonnull UICommandBuilder commandBuilder) {
		commandBuilder.append(UI_PATH);
		updateCoins(commandBuilder, this.coins);
	}

	public void setCoins(int coins) {
		this.coins = coins;
		UICommandBuilder commandBuilder = new UICommandBuilder();
		updateCoins(commandBuilder, coins);
		update(false, commandBuilder);
	}

	private void updateCoins(UICommandBuilder commandBuilder, int coins) {
		commandBuilder.set(COIN_LABEL_SELECTOR, Message.raw("Coins: " + coins));
	}
}
