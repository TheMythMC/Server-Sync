package dev.themyth.server_sync.commands;

import com.mojang.brigadier.CommandDispatcher;
import dev.themyth.server_sync.ServerSync;
import net.minecraft.server.command.ServerCommandSource;

import static net.minecraft.server.command.CommandManager.literal;

public class ReloadConfigs {
    public void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("reloadconfigs").executes(ctx -> {
            ctx.getSource().sendFeedback(ServerSync.initDiscord(), true);
            return 0;
        }));
    }
}
