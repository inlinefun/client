package blend.api

import io.ktor.server.application.Application
import io.ktor.server.http.content.staticResources
import io.ktor.server.routing.routing

fun Application.configureRouting() {
    routing {
        staticResources(
            remotePath = "/",
            basePackage = "/static/"
        )
    }
}
