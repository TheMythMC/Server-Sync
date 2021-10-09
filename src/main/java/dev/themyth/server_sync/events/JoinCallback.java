package dev.themyth.server_sync.events;

import net.fabricmc.fabric.api.event.EventFactory;
import net.fabricmc.fabric.api.event.Event;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.network.ServerPlayerEntity;

public interface JoinCallback {
    Event<JoinCallback> EVENT = EventFactory.createArrayBacked(JoinCallback.class, callbacks -> (connection, playerEntity) -> {
        for(JoinCallback callback : callbacks) {
            callback.onJoin(connection, playerEntity);
        }
    });
    void onJoin(ClientConnection connection, ServerPlayerEntity entity);
}
