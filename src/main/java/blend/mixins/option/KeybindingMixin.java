package blend.mixins.option;

import blend.accessors.KeybindingAccessor;
import blend.accessors.MouseAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(KeyBinding.class)
public abstract class KeybindingMixin implements KeybindingAccessor {

    @Shadow
    private InputUtil.Key boundKey;

    @Override
    public InputUtil.Key blend_getBoundKey() {
        return boundKey;
    }

    @Override
    public void blend_simulateAction(boolean pressed) {
        final InputUtil.Type type = boundKey.getCategory();
        final int action = pressed ? GLFW.GLFW_PRESS : GLFW.GLFW_RELEASE;
        final long window = MinecraftClient.getInstance().getWindow().getHandle();
        switch (type) {
            case MOUSE -> ((MouseAccessor)MinecraftClient.getInstance().mouse).blend_onMouse(window, boundKey.getCode(), action, 0);
            case KEYSYM -> MinecraftClient.getInstance().keyboard.onKey(window, boundKey.getCode(), 0, action, 0);
            default -> {}
        }
    }

}
