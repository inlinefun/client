package blend.module

import blend.event.EventSubscriber
import blend.value.*
import net.minecraft.client.MinecraftClient
import org.lwjgl.glfw.GLFW
import java.awt.Color

abstract class AbstractModule(
    val names: Array<String>,
    val description: String,
    val category: ModuleCategory,
    val canBeEnabled: Boolean = true,
    enabledByDefault: Boolean = false,
    defaultKey: Int = GLFW.GLFW_KEY_UNKNOWN,
    defaultHoldOnly: Boolean = false
): EventSubscriber, ValueParent {

    override val values: MutableList<AbstractValue<*>> = mutableListOf()
    val mc: MinecraftClient
        get() = MinecraftClient.getInstance()
    val bind by bind("Bind", defaultKey, defaultHoldOnly)
    val name = names[0]
    var enabled: Boolean = enabledByDefault
        set(value) {
            if (field != value) {
                field = value
                if (field) {
                    onEnable()
                    register()
                } else {
                    unregister()
                    onDisable()
                }
            }
        }

    init {
        if (enabled) {
            onEnable()
            register()
        } else {
            unregister()
            onDisable()
        }
    }

    fun toggle() {
        enabled = !enabled
    }

    open fun onEnable() {}
    open fun onDisable() {}

    fun boolean(name: String, value: Boolean, parent: ValueParent = this, visibility: () -> Boolean = { true }) = BooleanValue(name, parent, visibility, value)
    fun int(name: String, defaultValue: Int, minimum: Int, maximum: Int, incrementBy: Int = 1, parent: ValueParent = this, visibility: () -> Boolean = { true }) = IntValue(name, parent, visibility, defaultValue, minimum, maximum, incrementBy)
    fun double(name: String, value: Double, min: Double, max: Double, incrementBy: Double, parent: ValueParent = this, visibility: () -> Boolean = { true }) = DoubleValue(name, parent, visibility, value, min, max, incrementBy)
    fun list(name: String, values: Array<String>, parent: ValueParent = this, visibility: () -> Boolean = { true }) = ListValue(name, parent, visibility, values)
    fun color(name: String, value: Color, hasAlpha: Boolean = false, parent: ValueParent = this, visibility: () -> Boolean = { true }) = ColorValue(name, parent, visibility, value, hasAlpha)
    fun bind(name: String, defaultKey: Int, hold: Boolean, parent: ValueParent = this, visibility: () -> Boolean = { true }) = KeyValue(name, parent, visibility, defaultKey, hold)
    fun parent(name: String, visibility: () -> Boolean = { true }) = ExpandableValue(name, this, visibility)
}

enum class ModuleCategory {
    COMBAT,
    VISUAL,
    PLAYER,
    OTHER;
    val label = this.name.lowercase().replaceFirstChar { it.uppercase() }
}