package dev.themyth.server_sync.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.advancement.Advancement;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;

public interface AdvancementCallback {
    Event<AdvancementCallback> EVENT = EventFactory.createArrayBacked(AdvancementCallback.class, callbacks -> (playerEntity, advancement) -> {
        for (AdvancementCallback callback : callbacks) {
            callback.onPlayerAdvancement(playerEntity, advancement);
        }
    });

    void onPlayerAdvancement(ServerPlayerEntity playerEntity, Advancement advancement);
}
