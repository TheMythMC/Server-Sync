package dev.themyth.server_sync.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;

public interface DeathCallback {
    Event<DeathCallback> EVENT = EventFactory.createArrayBacked(DeathCallback.class, callbacks -> (playerEntity, damageSource) -> {
        for (DeathCallback callback : callbacks) {
            callback.onPlayerDeath(playerEntity, damageSource);
        }
    });

    void onPlayerDeath(ServerPlayerEntity playerEntity, DamageSource damageSource);
}
