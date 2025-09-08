package blend.module.impl.movement

import blend.event.HandleInputEvent
import blend.module.AbstractModule
import blend.module.ModuleCategory
import blend.util.extensions.isHeld
import org.greenrobot.eventbus.Subscribe

object SprintModule: AbstractModule(
    names = arrayOf("Sprint"),
    description = "Makes the player sprint",
    category = ModuleCategory.PLAYER,
    enabledByDefault = true
) {

    override fun onDisable() {
        mc.options.sprintKey.let { bind ->
            bind.isPressed = mc.options.sprintToggled.value || bind.isHeld()
        }
    }

    @Subscribe
    fun onInputHandle(event: HandleInputEvent) {
        mc.options.sprintKey.isPressed = true
    }

}