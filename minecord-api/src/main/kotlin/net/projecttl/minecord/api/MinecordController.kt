package net.projecttl.minecord.api

import dev.cube1.minecord.plugin.core
import dev.cube1.minecord.plugin.instance
import dev.cube1.minecord.plugin.util.command.model.CommandHandler
import org.bukkit.plugin.java.JavaPlugin

class MinecordController(plugin: JavaPlugin) {
    fun addListeners(vararg listeners: Any) {
        for (i in listeners) {
            core.addListener(i)
        }
    }

    fun addCommands(vararg handler: CommandHandler) {
        for (i in handler) {
            core.addCommand(i)
        }
    }

    init {
        try {
            plugin.logger.info("minecord ${instance.description.version}")
        } catch (exception: Exception) {
            plugin.logger.warning("You must put minecord plugin in your bukkit server")
            exception.printStackTrace()

            plugin.onDisable()
        }

        core.reloadModule()
    }
}