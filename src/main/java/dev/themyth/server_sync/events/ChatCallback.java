package dev.themyth.server_sync.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.Optional;

public interface ChatCallback {
    Event<ChatCallback> EVENT = EventFactory.createArrayBacked(ChatCallback.class, callbacks -> (player, raw, message) -> {
        Optional<Text> msg = Optional.empty();
        for(ChatCallback callback : callbacks) {
            Optional<Text> result = callback.onServerChat(player, raw, message);
            if(result.isPresent()) msg = result;
        }
        return msg;
    });

    Optional<Text> onServerChat(ServerPlayerEntity player, String rawMessage, Text message);
}
