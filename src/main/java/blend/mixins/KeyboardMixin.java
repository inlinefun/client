package blend.mixins;

import blend.event.KeyInputEvent;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Keyboard.class)
public class KeyboardMixin {

    @Shadow
    @Final
    private MinecraftClient client;

    @Inject(
            method = "onKey",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/MinecraftClient;getInactivityFpsLimiter()Lnet/minecraft/client/option/InactivityFpsLimiter;",
                    shift = At.Shift.AFTER
            )
    )
    private void onKey(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
        if (window == client.getWindow().getHandle()) {
            new KeyInputEvent(key, scancode, action, modifiers).call();
        }
    }

}
