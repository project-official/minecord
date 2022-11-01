package dev.cube1.minecord.plugin

import dev.cube1.minecord.plugin.Config.discord
import dev.cube1.minecord.plugin.Config.format
import dev.cube1.minecord.plugin.Config.prefix
import dev.cube1.minecord.plugin.command.MinecordInfo
import dev.cube1.minecord.plugin.command.OnlineCommand
import dev.cube1.minecord.plugin.command.Ping
import dev.cube1.minecord.plugin.listener.Chat
import dev.cube1.minecord.plugin.listener.Command
import dev.cube1.minecord.plugin.listener.Player
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

lateinit var core: Minecord
lateinit var instance: CorePlugin

class CorePlugin : JavaPlugin() {
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

        core = Minecord(discord.token).apply {
            addListeners(Chat)
            addCommand(MinecordInfo)
            addCommand(OnlineCommand)
            addCommand(Ping)
        }
        core.build()

        logger.info("$prefix Minecord module loading complete")

        server.pluginManager.apply {
            registerEvents(Chat, this@CorePlugin)
            registerEvents(Player, this@CorePlugin)
        }

        Bukkit.getScheduler().runTaskLater(this, Runnable {
            startMessage()
        }, 20L)
        logger.info("$prefix Plugin loading complete")
    }

    override fun onDisable() {
        stopMessage()
        jda.shutdown()
    }

    private fun startMessage() {
        val chan = jda.getTextChannelById(discord.channels.chat_id)!!
        chan.sendMessage(format.enable).queue()
    }

    private fun stopMessage() {
        val chan = jda.getTextChannelById(discord.channels.chat_id)!!
        chan.sendMessage(format.disable).queue()
    }
}
