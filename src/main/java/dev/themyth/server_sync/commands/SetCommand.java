package dev.themyth.server_sync.commands;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import dev.themyth.server_sync.ServerSync;
import dev.themyth.server_sync.commands.argument.EnumArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import org.apache.logging.log4j.core.jmx.Server;

import java.net.URL;

public class SetCommand {

    public void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
            literal("syncset")
                .requires(serverCommandSource ->
                    serverCommandSource.hasPermissionLevel(4)
                )
                .executes(ctx -> {
                    ctx
                        .getSource()
                        .sendError(new LiteralText("You must specify a key"));
                    return 1;
                })
                .then(
                    argument("key", EnumArgumentType.enumeration(Keys.class))
                        .then(
                            argument("value", StringArgumentType.word())
                                .executes(ctx -> {
                                    ctx
                                        .getSource()
                                        .sendFeedback(
                                            new LiteralText(
                                                "NOTE: THIS DOES NOT CHECK IF ANY VALUES ARE CORRECT. BE WARY OF WHAT YOU ARE DOING"
                                            )
                                                .formatted(Formatting.YELLOW),
                                            false
                                        );
                                    final ServerCommandSource source = ctx.getSource();
                                    String data = StringArgumentType.getString(
                                        ctx,
                                        "value"
                                    );
                                    switch (
                                        EnumArgumentType.getEnumeration(
                                            ctx,
                                            "key",
                                            Keys.class
                                        )
                                    ) {
                                        case WEBHOOK_URL -> {
                                            if (
                                                    isNotURL(data)
                                            ) source.sendError(
                                                    new LiteralText(
                                                            "Not a valid URL"
                                                    )
                                            ); else {

                                                source.sendFeedback(
                                                        new LiteralText(
                                                                "Successfully set the token to: " +
                                                                        data
                                                        )
                                                                .formatted(
                                                                        Formatting.GREEN
                                                                ),
                                                        true
                                                );
                                            }
                                        }
                                        case TOKEN -> {
                                            if (
                                                data.length() > 50
                                            ) source.sendError(
                                                new LiteralText(
                                                    "Not a valid token (must be over 50 in length)"
                                                )
                                            ); else {
                                                ServerSync.config.token = data;
                                                source.sendFeedback(
                                                    new LiteralText(
                                                        "Successfully set the token to: " +
                                                        data
                                                    )
                                                        .formatted(
                                                            Formatting.GREEN
                                                        ),
                                                    true
                                                );
                                            }
                                        }
                                        case CHANNEL_ID -> {
                                            // you're on your own at this point lol
                                            if (!isLong(data)) {
                                                ctx
                                                    .getSource()
                                                    .sendError(
                                                        new LiteralText(
                                                            "The channel_id must be of type long (aka big int)"
                                                        )
                                                    );
                                                return 0;
                                            }
                                            ServerSync.config.channelID = Long.parseLong(data);
                                            source.sendFeedback(
                                                new LiteralText(
                                                    "Channel ID set to: " + data
                                                )
                                                    .formatted(
                                                        Formatting.GREEN
                                                    ),
                                                true
                                            );
                                        }
                                        case MEMBERS_SEND_MESSAGES -> {
                                            // this is so straight forward if you get this wrong i have no words
                                            if (!isBoolean(data)) {
                                                ctx
                                                    .getSource()
                                                    .sendError(
                                                        new LiteralText(
                                                            "The parameter must be a boolean"
                                                        )
                                                    );
                                                return 0;
                                            }

                                                ServerSync.config.membersSendMessages = Boolean.parseBoolean(data);

                                            if (
                                                Boolean.parseBoolean(data)
                                            ) source.sendFeedback(
                                                new LiteralText(
                                                    "Members can send messages!"
                                                )
                                                    .formatted(
                                                        Formatting.GREEN
                                                    ),
                                                true
                                            ); else source.sendFeedback(
                                                new LiteralText(
                                                    "Members cannot send messages!"
                                                )
                                                    .formatted(
                                                        Formatting.GREEN
                                                    ),
                                                true
                                            );
                                        }
                                        default -> ctx
                                            .getSource()
                                            .sendError(
                                                new LiteralText(
                                                    "You must specify a valid key!"
                                                )
                                            );
                                    }
                                    return 1;
                                })
                        )
                )
        );
    }

    // discord channel ids are too long for ints, we use longs here
    private boolean isLong(String string) {
        try {
            Long.parseLong(string);
        } catch (Exception ignored) {
            return false;
        }
        return true;
    }

    @SuppressWarnings("ALL")
    private boolean isBoolean(String string) {
        try {
            Boolean.parseBoolean(string);
        } catch (Exception ignored) {
            return false;
        }
        return true;
    }

    private boolean isNotURL(String url) {
        try {
            new URL(url);
        } catch (Exception ignored) {
            return true;
        }
        return false;
    }

}
