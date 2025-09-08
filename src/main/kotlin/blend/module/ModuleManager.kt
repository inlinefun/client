package blend.module

import blend.module.impl.movement.SprintModule
import blend.util.interfaces.Initializable

object ModuleManager: Initializable {

    var modules = emptyArray<AbstractModule>()
        private set

    override fun init() {
        modules = arrayOf(
            SprintModule
        )
    }

}