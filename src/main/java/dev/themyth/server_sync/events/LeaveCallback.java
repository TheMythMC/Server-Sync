package dev.themyth.server_sync.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.network.ServerPlayerEntity;

public interface LeaveCallback {
    Event<LeaveCallback> EVENT = EventFactory.createArrayBacked(LeaveCallback.class, callbacks -> playerEntity -> {
        for (LeaveCallback callback : callbacks) {
            callback.onLeave(playerEntity);
        }
    });

    void onLeave(ServerPlayerEntity playerEntity);
}
