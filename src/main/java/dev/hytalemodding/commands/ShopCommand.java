package dev.hytalemodding.commands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractTargetPlayerCommand;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import dev.hytalemodding.ui.ShopPage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ShopCommand extends AbstractTargetPlayerCommand {
	public ShopCommand() {
		super("shop", "Open the stat upgrade shop.");
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
		Player player = store.getComponent(ref, Player.getComponentType());
		if (player == null) {
			return;
		}

		player.getPageManager().openCustomPage(ref, store, new ShopPage(playerRef));
	}
}
