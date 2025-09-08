package blend.event

import net.minecraft.client.MinecraftClient
import org.greenrobot.eventbus.EventBus

object EventManager {

    inline val mc: MinecraftClient
        get() = MinecraftClient.getInstance()

    private val bus = EventBus
        .builder()
        .apply {
            logSubscriberExceptions(true)
            logNoSubscriberMessages(false)
            sendNoSubscriberEvent(false)
            sendSubscriberExceptionEvent(false)
            throwSubscriberException(false)
        }
        .build()

    fun <T: EventSubscriber> register(subscriber: T) {
        try {
            bus.register(subscriber)
        } catch (_: Exception) {}
    }

    fun <T: EventSubscriber> unregister(subscriber: T) {
        if (bus.isRegistered(subscriber))
            bus.unregister(subscriber)
    }

    fun <T: Event> post(event: T) {
        if (mc.player == null || mc.world == null)
            return
        bus.post(event)
    }

}

interface EventSubscriber {
    fun register() = EventManager.register(this)
    fun unregister() = EventManager.unregister(this)
}
