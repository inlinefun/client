package blend.event

interface Event {
    fun call() = EventManager.post(this)
}

abstract class CancellableEvent: Event {
    var cancelled = false
    fun cancel() {
        cancelled = true
    }
}
