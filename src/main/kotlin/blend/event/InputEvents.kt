package blend.event

data class KeyInputEvent(
    val keycode: Int,
    val scancode: Int,
    val action: Int,
    val modifiers: Int
): Event

data object HandleInputEvent: Event
