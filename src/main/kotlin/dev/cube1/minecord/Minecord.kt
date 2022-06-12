package dev.cube1.minecord

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.core.Logger
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import dev.cube1.minecord.utils.LogAppender

lateinit var instance: Minecord
lateinit var jda: JDA

class Minecord : JavaPlugin() {

    private fun loadJDAModule() {
        val builder = JDABuilder.createDefault(this.config.getString("token")).apply {
            registerEvents(this)
            addEventListeners(ListenerHandler)
        }

        if (this.config.getBoolean("show_activity")) {
            builder.setActivity(Activity.playing("Minecraft : ${config.getString("server_address")}"))
        }

        jda = builder.build()
        registerData(jda)

        logger.info("MineCord - Discord module enabled!")
    }

    private fun loadEventListener() {
        server.pluginManager.registerEvents(ListenerHandler, this@Minecord)
        logger.info("MineCord - Minecraft listener registered!")
    }

    private fun loadCommand() {
        getCommand("invite")?.apply {
            setExecutor(CommandDispatcher)
            tabCompleter = CommandDispatcher
        }
    }

    private fun loadConsoleSRV() {
        if (config.getBoolean("console_srv")) {
            if (config.getString("console_srv_channelId") == "" || this.config.getString("console_srv_channel_id") == null) {
                throw Exception("You must set console-srv-channelId in config.yml!")
            } else {
                try {
                    val logger = LogManager.getRootLogger() as Logger
                    logger.addAppender(LogAppender())
                } catch (e: Exception) {
                    throw e
                } finally {
                    logger.info("MineCord - ConsoleSRV module enabled!")
                }
            }
        }
    }

    override fun onEnable() {
        instance = this
        this.saveDefaultConfig()

        loadJDAModule()
        loadEventListener()
        loadCommand()

        logger.info("MineCord - Discord Plugin successful loaded.")
        Bukkit.getScheduler().runTaskLater(this, Runnable {
            loadConsoleSRV()
            startMessage()
        }, 20L)

    }

    override fun onDisable() {
        stopMessage()
        try {
            val logger = LogManager.getRootLogger() as Logger
            logger.removeAppender(LogAppender())
        } catch (e: Exception) {
            throw e
        }

        LogAppender().stop()
        jda.shutdown()
    }

    private fun startMessage() {
        val channel = jda.getTextChannelById(config.getString("channel_id")!!)
        val message: String = config.getString("start_message")?: "**서버가 시작 되었습니다.** :white_check_mark:"
        if (config.getBoolean("console_srv")) {
            val srvChannel = jda.getTextChannelById(config.getString("console_srv_channel_id")!!)
            srvChannel?.sendMessage(message)?.queue()
        }

        channel?.sendMessage(message)?.queue()
    }

    private fun stopMessage() {
        val channel = jda.getTextChannelById(config.getString("channel_id")!!)
        val message: String = config.getString("stop_message")?: "**서버가 종료 되었습니다.** :stop_sign:"
        if (config.getBoolean("console_srv")) {
            val srvChannel = jda.getTextChannelById(config.getString("console_srv_channel_id")!!)
            srvChannel?.sendMessage(message)?.queue()
        }

        channel?.sendMessage(message)?.queue()
    }
}
