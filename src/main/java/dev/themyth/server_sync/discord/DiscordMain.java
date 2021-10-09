package dev.themyth.server_sync.discord;

import dev.themyth.server_sync.ServerSync;
import discord4j.common.util.Snowflake;
import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.spec.WebhookExecuteSpec;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.net.http.HttpClient;
import java.util.Objects;
import java.util.Optional;

public class DiscordMain {
    // this is needed for webhooks
    HttpClient hClient = HttpClient.newHttpClient();

    public static DiscordMain DISCORD_CLIENT = new DiscordMain();
    public final DiscordClient client;
    public final GatewayDiscordClient gatewayDiscordClient;

    public DiscordMain() {
        client = DiscordClient.create(ServerSync.config.token);
         gatewayDiscordClient = client.gateway().login().block();
    }

    public void registerChannelListener() {
        gatewayDiscordClient.on(MessageCreateEvent.class).subscribe(event -> ServerSync.sendServerMessage(event.getMessage().getContent()));
    }
    public void sendMessage(String message) {
        client.getChannelById(Snowflake.of((Long) ServerSync.config.channelID)).createMessage(message).block();

    }
    public Optional<Text> sendWebhookMessage(ServerPlayerEntity player, String message) {

        String[] webhookUrl = ServerSync.config.webhookURL.split("/");

        assert gatewayDiscordClient != null;
        Objects.requireNonNull(gatewayDiscordClient.getWebhookByIdWithToken(Snowflake.of(webhookUrl[webhookUrl.length - 2]), webhookUrl[webhookUrl.length - 1]).block())
                .execute(WebhookExecuteSpec.create()
                        .withAvatarUrl("https://crafatar.com/avatars/" + player.getUuidAsString())
                        .withContent(message)
                        .withUsername(player.getName().asString()));

        return Optional.empty();
    }
}
