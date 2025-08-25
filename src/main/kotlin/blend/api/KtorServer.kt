package blend.api

import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

object KtorServer {

    val server = embeddedServer(
        factory = Netty,
        port = 5555,
        host = "0.0.0.0",
        module = {
            configureRouting()
        }
    )

    fun start() {
        server.start(wait = false)
    }

    fun stop() {
        server.stop()
    }

}