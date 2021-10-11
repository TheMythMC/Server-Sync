package dev.themyth.server_sync;

import dev.themyth.server_sync.commands.SetCommand;
import dev.themyth.server_sync.discord.DiscordMain;
import dev.themyth.server_sync.events.*;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.MessageType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.LiteralText;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

@Environment(EnvType.SERVER)
public class ServerSync implements ModInitializer {
    public static Configuration config;
    public static final Logger LOGGER = LogManager.getLogger();
    public static MinecraftServer SERVER_INSTANCE = null;

    @Override
    public void onInitialize() {
        AutoConfig.register(Configuration.class, JanksonConfigSerializer::new);
        config = AutoConfig.getConfigHolder(Configuration.class).getConfig();
        if (FabricLoader.getInstance().getEnvironmentType() != EnvType.SERVER) throw new IllegalStateException("Server-Sync can only be installed on server");
        CommandRegistrationCallback.EVENT.register(
                (dispatcher, dedicated) -> new SetCommand().register(dispatcher)
        );
        // Discord stuff here
        initDiscord();
    }

    public static void sendServerMessage(String message) {
        SERVER_INSTANCE.getPlayerManager().broadcastChatMessage(new LiteralText(message), MessageType.SYSTEM, Util.NIL_UUID);
    }

    public Text initDiscord() {
        try
        {
            DiscordMain.DISCORD_CLIENT = new DiscordMain();
            DiscordMain.DISCORD_CLIENT.registerChannelListener();
            ServerLifecycleEvents.SERVER_STARTING.register(e -> DiscordMain.DISCORD_CLIENT.sendMessage("**Server Starting!**"));
            ServerLifecycleEvents.SERVER_STARTED.register(e -> DiscordMain.DISCORD_CLIENT.sendMessage("**Server Started!**"));
            ServerLifecycleEvents.SERVER_STOPPING.register(e -> DiscordMain.DISCORD_CLIENT.sendMessage("**Server Stopping!**"));
            ServerLifecycleEvents.SERVER_STOPPED.register(e -> DiscordMain.DISCORD_CLIENT.sendMessage("**Server Stopped!**"));
            ChatCallback.EVENT.register((player, rawMessage, message) -> DiscordMain.DISCORD_CLIENT.sendWebhookMessage(player, rawMessage));
            JoinCallback.EVENT.register((connection, player) -> DiscordMain.DISCORD_CLIENT.sendMessage("**" + player.getName().asString() + " has joined the game!**"));
            LeaveCallback.EVENT.register(playerEntity -> DiscordMain.DISCORD_CLIENT.sendMessage("**" + playerEntity.getName().asString() + "has left the server.**"));
            DeathCallback.EVENT.register(((playerEntity, damageSource) -> DiscordMain.DISCORD_CLIENT.sendMessage("**" + damageSource.getDeathMessage(playerEntity).asString() + "**")));
            AdvancementCallback.EVENT.register(((playerEntity, advancement) -> {
                switch (advancement.getDisplay().getFrame()) {
                    case GOAL -> DiscordMain.DISCORD_CLIENT.sendMessage("**" + playerEntity.getName().asString() + " has made the goal [" + advancement.getDisplay().getTitle().asString() + "]**");
                    case TASK -> DiscordMain.DISCORD_CLIENT.sendMessage("**" + playerEntity.getName().asString() + " has made the advancement [" + advancement.getDisplay().getTitle().asString() + "]**");
                    case CHALLENGE -> DiscordMain.DISCORD_CLIENT.sendMessage("**" + playerEntity.getName().asString() + " has made completed the challenge [" + advancement.getDisplay().getTitle().asString() + "]**");
                }
            }));
        }
        catch (Exception e) {
            LOGGER.error("There has been an error with your Discord configurations. Make sure they are correct and restart");
            return new LiteralText("There has been an error with your Discord configurations. Make sure they are correct and restart").formatted(Formatting.RED);
        }
        return new LiteralText("Success!").formatted(Formatting.GREEN);
    }

    static {
        Path configPath = Paths.get("config/server_sync.json");
        if (!configPath.toFile().exists()) {
            try {
                configPath.toFile().createNewFile();
                BufferedWriter bw = new BufferedWriter(
                    new FileWriter(configPath.toFile())
                );
                bw.write("{}");
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
