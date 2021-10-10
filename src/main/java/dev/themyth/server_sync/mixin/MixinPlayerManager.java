package dev.themyth.server_sync.mixin;

import dev.themyth.server_sync.events.JoinCallback;
import dev.themyth.server_sync.events.LeaveCallback;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerManager.class)
public abstract class MixinPlayerManager {
    @Inject(method="onPlayerConnect", at = @At("RETURN"))
    private void connection(ClientConnection connection, ServerPlayerEntity player, CallbackInfo ci) {
        JoinCallback.EVENT.invoker().onJoin(connection, player);
    }

    @Inject(method = "remove", at = @At("RETURN"))
    private void disconnect(ServerPlayerEntity player, CallbackInfo ci) {
        LeaveCallback.EVENT.invoker().onLeave(player);
    }

}
