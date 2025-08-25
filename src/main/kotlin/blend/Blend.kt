package blend

import blend.api.KtorServer
import org.slf4j.LoggerFactory

object Blend {

    const val NAME = "Blend"
    const val VERSION = "6.0"
    val logger = LoggerFactory.getLogger(NAME)!!

    fun initialize() {
        val preInit = System.currentTimeMillis()
        KtorServer.start()
        logger.info("Initialized $NAME v$VERSION in ${System.currentTimeMillis() - preInit}ms")
    }

    fun shutdown() {
        KtorServer.stop()
    }

}