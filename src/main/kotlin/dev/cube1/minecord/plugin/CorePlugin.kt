package dev.cube1.minecord.plugin


import dev.cube1.minecord.plugin.command.MinecordInfoCommandHandler
import dev.cube1.minecord.plugin.command.OnlineCommandHandler
import dev.cube1.minecord.plugin.listener.ChatListener
import dev.cube1.minecord.plugin.listener.PlayerListener
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

        val options = Minecord.MinecordOption(
            minecordListeners = listOf(
                ChatListener(),
                PlayerListener()
            ),
            minecordCommandHandlers = listOf(
                OnlineCommandHandler(),
                MinecordInfoCommandHandler()
            )
        )

         minecord = Minecord(this, Config.Discord.token, options)
    }

    override fun onDisable() {
        if (isMinecordInitialized()) {
            minecord.stop()
        }
    }
}
