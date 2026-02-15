package dev.hytalemodding.commands;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.AbstractCommand;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import dev.hytalemodding.Main;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class ReadyCommand extends AbstractCommand {
    public ReadyCommand() {
        super("ready", "Skip the current break phase.");
    }

    @Nullable
    @Override
    protected CompletableFuture<Void> execute(@Nonnull CommandContext context) {
        Main.get().getGameDirector().forceEndBreak();
        context.sendMessage(Message.raw("Break skipped. Prepare for the next wave."));
        return CompletableFuture.completedFuture(null);
    }
}
