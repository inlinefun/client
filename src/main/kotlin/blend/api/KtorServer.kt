package blend.api

import io.ktor.server.cio.CIO
import io.ktor.server.engine.applicationEnvironment
import io.ktor.server.engine.connector
import io.ktor.server.engine.embeddedServer
import org.slf4j.LoggerFactory

object KtorServer {

    private val server = embeddedServer(
        configure = {
            connector {
                host = "0.0.0.0"
                port = 5555
            }
        },
        factory = CIO,
        module = {
            configureRouting()
        },
        environment = applicationEnvironment {
            log = LoggerFactory.getLogger("Ktor")
        }
    )

    fun start() = server.start(wait = false)
    fun stop() = server.stop()

}