package dev.themyth.server_sync.mixin;

import dev.themyth.server_sync.events.AdvancementCallback;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.PlayerAdvancementTracker;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerAdvancementTracker.class)
public class MixinPlayerAdvancementTracker {

    @Shadow private ServerPlayerEntity owner;

    @Inject(method = "grantCriterion", at = @At("RETURN"))
    private void sendAdvancementEvent(Advancement advancement, String criterionName, CallbackInfoReturnable<Boolean> cir) {
        AdvancementCallback.EVENT.invoker().onPlayerAdvancement(owner, advancement);
    }
}
