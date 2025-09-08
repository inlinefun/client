package blend

import blend.api.KtorServer
import blend.module.ModuleManager
import blend.util.interfaces.Initializable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import net.minecraft.client.MinecraftClient
import org.slf4j.LoggerFactory
import java.io.File
import java.util.concurrent.Executors

object Blend {

    const val NAME = "Blend"
    const val VERSION = "6.0"
    val logger = LoggerFactory.getLogger(NAME)!!
    val folder = File(MinecraftClient.getInstance().runDirectory, NAME.lowercase()).also { folder ->
        if (!folder.exists())
            folder.mkdir()
    }

    private val dispatcher = Executors.newSingleThreadExecutor { task ->
        Thread(task, "$NAME Client")
    }.asCoroutineDispatcher()
    private val scope = CoroutineScope(context = SupervisorJob() + dispatcher)

    fun initialize() {
        val preInit = System.currentTimeMillis()

        arrayOf(
            ModuleManager
        ).forEach(Initializable::init)

        runAsync {
            KtorServer.start()
            logger.info("Test")
        }

        logger.info("Initialized $NAME v$VERSION in ${System.currentTimeMillis() - preInit}ms")
    }

    fun shutdown() {
        scope.cancel(message = "Client Shutdown")
        dispatcher.close()
        KtorServer.stop()
    }

    fun runAsync(
        block: suspend CoroutineScope.() -> Unit
    ) = scope.launch {
        block()
    }

}