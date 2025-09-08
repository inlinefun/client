package blend.util.extensions

import blend.accessors.InputUtilKeyAccessor
import blend.accessors.KeybindingAccessor
import blend.Blend
import net.minecraft.client.MinecraftClient
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.util.InputUtil
import net.minecraft.text.Text
import org.lwjgl.glfw.GLFW
import java.awt.Color

internal inline val mc: MinecraftClient
    get() = MinecraftClient.getInstance()
private inline val accent: Int
    get() = Color(0, 160, 255).rgb

// Keybinding
val KeyBinding.keycode: Int
    get() {
        return (this as KeybindingAccessor).blend_getBoundKey().code
    }
val KeyBinding.key: InputUtil.Key
    get() {
        return (this as KeybindingAccessor).blend_getBoundKey()
    }
val KeyBinding.type: InputUtil.Type
    get() {
        @Suppress("cast_never_succeeds") // trust the process :pray:
        return (key as InputUtilKeyAccessor).blend_getType()
    }
fun KeyBinding.isHeld(): Boolean {
    return when (this.type) {
        InputUtil.Type.MOUSE -> GLFW.glfwGetMouseButton(mc.window.handle, keycode)
        InputUtil.Type.KEYSYM -> GLFW.glfwGetKey(mc.window.handle, keycode)
        InputUtil.Type.SCANCODE -> GLFW.GLFW_RELEASE // IDK how this works :(
    } == GLFW.GLFW_PRESS
}
fun KeyBinding.simulateAction(press: Boolean) {
    (this as KeybindingAccessor).blend_simulateAction(press)
}

// Text
fun Text.addChatInfo(prefix: Boolean = true) {
    val content = Text.empty()
    if (prefix) {
        content.append(Text.literal(Blend.NAME).withColor(accent))
        content.append(Text.literal(" » ").withColor(Color(0, 255, 25).rgb))
    }
    content.append(this)
    mc.inGameHud.chatHud.addMessage(content)
}
fun Text.addChatWarn(prefix: Boolean = true) {
    val content = Text.empty()
    if (prefix) {
        content.append(Text.literal(Blend.NAME).withColor(accent))
        content.append(Text.literal(" » ").withColor(Color(255, 150, 0).rgb))
    }
    content.append(this)
    mc.inGameHud.chatHud.addMessage(content)
}
fun Text.addChatError(prefix: Boolean = true) {
    val content = Text.empty()
    if (prefix) {
        content.append(Text.literal(Blend.NAME).withColor(accent))
        content.append(Text.literal(" » ").withColor(Color(255, 25, 25).rgb))
    }
    content.append(this)
    mc.inGameHud.chatHud.addMessage(content)
}