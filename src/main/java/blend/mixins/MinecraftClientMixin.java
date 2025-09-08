package blend.mixins;

import blend.Blend;
import blend.event.HandleInputEvent;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    @Inject(
            method = "close",
            at = @At(
                    value = "HEAD"
            )
    )
    private void onClose(CallbackInfo ci) {
        try {
            Blend.INSTANCE.shutdown();
        } catch (Exception e) {
            Blend.INSTANCE.getLogger().error("Error shutting down client", e);
        }
    }

    @Inject(
            method = "handleInputEvents",
            at = @At(
                    value = "HEAD"
            )
    )
    private void onHandleInputs(CallbackInfo ci) {
        HandleInputEvent.INSTANCE.call();
    }

}
