package dev.cube1.minecord.plugin


import dev.cube1.minecord.plugin.Config.prefix
import dev.cube1.minecord.plugin.listener.ChatListener
import net.dv8tion.jda.api.JDA
import org.bukkit.plugin.java.JavaPlugin

class CorePlugin : JavaPlugin() {

    companion object {
        lateinit var instance: CorePlugin
        lateinit var minecord: Minecord
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

        minecord = Minecord(this, Config.discord.token)
        minecord.addListener(ChatListener())
        minecord.start()

    }

    override fun onDisable() {
        minecord.stop()
    }
}
