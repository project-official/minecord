package dev.cube1.minecord.plugin

import dev.cube1.minecord.plugin.Config.discord
import dev.cube1.minecord.plugin.Config.prefix
import dev.cube1.minecord.plugin.listener.Chat
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
        val channel = jda.getTextChannelById(config.getString("channel_id")!!)
        val message = config.getString("start_message")?: ":white_check_mark: **서버가 시작 되었습니다.**"

        channel?.sendMessage(message)?.queue()
    }

    private fun stopMessage() {
        val channel = jda.getTextChannelById(config.getString("channel_id")!!)
        val message = config.getString("stop_message")?: ":stop_sign: **서버가 종료 되었습니다.**"

        channel?.sendMessage(message)?.queue()
    }
}
