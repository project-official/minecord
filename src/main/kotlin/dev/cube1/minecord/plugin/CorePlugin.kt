package dev.cube1.minecord.plugin


import dev.cube1.minecord.plugin.Config.prefix
import dev.cube1.minecord.plugin.command.OnlineCommandHandler
import dev.cube1.minecord.plugin.listener.ChatListener
import dev.cube1.minecord.plugin.listener.PlayerListener
import net.dv8tion.jda.api.JDA
import org.bukkit.plugin.java.JavaPlugin

class CorePlugin : JavaPlugin() {

    companion object {
        lateinit var instance: CorePlugin
        lateinit var minecord: Minecord

        fun isMinecordInitialized() = ::minecord.isInitialized
    }

    override fun onEnable() {
        instance = this

        saveDefaultConfig()
        try {
            Config.init()
        } catch (exception: Exception) {
            logger.warning("$prefix Config initializing failed")
            exception.printStackTrace()
            server.pluginManager.disablePlugin(this)
        }
        logger.info("$prefix Config initializing complete")

        val options = Minecord.MinecordOption(
            minecordListeners = listOf(
                ChatListener(),
                PlayerListener()
            ),
            minecordCommandHandlers = listOf(
                OnlineCommandHandler()
            )
        )

         minecord = Minecord(this, Config.discord.token, options)
    }

    override fun onDisable() {
        if (isMinecordInitialized()) {
            minecord.stop()
        }
    }
}
