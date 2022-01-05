package org.propercrew.minecord

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.core.Logger
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import org.propercrew.minecord.commands.*
import org.propercrew.minecord.discordListeners.GuildChatListener
import org.propercrew.minecord.listeners.*
import org.propercrew.minecord.utils.LogAppender
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.PrintStream


class Minecord : JavaPlugin() {

    companion object {
        var jda: JDA? = null
        var init = false
        var instance: Minecord? = null
    }

    private var isLog4JCapable = false

    private fun loadJDAModule() {
        val builder = JDABuilder.createDefault(this.config.getString("token")).apply {
            addEventListeners(
                OnlineCommand(),
                SendChatListener(),
                PingPong(),
                ReloadCommand(),
                MinecordInfo(),
                GuildChatListener()
            )
        }

        if (this.config.getBoolean("show_activity")) {
            builder.setActivity(Activity.playing("Minecraft : ${config.getString("server_address")}"))
        }

        jda = builder.build()
        val commands = jda?.updateCommands()

        commands?.apply {
            addCommands(
                CommandData("online", "You can see online player!"),
                CommandData("ping", "You can ping pong with my bot"),
                CommandData("pong", "You can ping pong with my bot"),
                CommandData("reload", "You can reload your minecraft server **ADMIN ONLY**"),
                CommandData("info", "You can see info about minecraft server or this bot")
            )
        }?.queue()

        logger.info("MineCord - Discord module enabled!")
    }

    private fun loadEventListener() {
        server.pluginManager.apply {
            registerEvents(AsyncChatListener(), this@Minecord)
            registerEvents(DeathListener(), this@Minecord)
            registerEvents(JoinQuitListener(), this@Minecord)
            registerEvents(KickListener(), this@Minecord)
        }

        logger.info("MineCord - Minecraft listener registered!")
    }

    private fun loadConsoleSRV() {
        if(this.config.getBoolean("console-srv")) {
            if(this.config.getString("console-srv-channelId") == "" || this.config.getString("console-srv-channelId") == null) {
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

//                Bukkit.getScheduler().runTaskTimerAsynchronously(this, Runnable {
//
//                }, 0, 20L)
            }
        }
    }

    private fun loadCommand() {
        getCommand("invite")?.apply {
            setExecutor(Invite)
            tabCompleter = Invite
        }
    }


    override fun onEnable() {
        instance = this
        if (!dataFolder.exists()) {
            this.saveDefaultConfig()
            logger.info("MineCord - Initialized configuration!")
        }

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
        init = false

        try {
            val logger = LogManager.getRootLogger() as Logger
            logger.removeAppender(LogAppender())
        } catch (e: Exception) {
            throw e
        }

        LogAppender().stop()



        jda?.shutdown()
    }

    private fun startMessage() {
        val channel = jda?.getTextChannelById(config.getString("channelId")!!)
        val message: String = config.getString("startMessage")?: "**서버가 시작 되었습니다.** :white_check_mark:"
        if(config.getBoolean("console-srv")) {
            val srvChannel = jda?.getTextChannelById(config.getString("console-srv-channelId")!!)
            srvChannel?.sendMessage(message)?.queue()
        }

        channel?.sendMessage(message)?.queue()
    }

    private fun stopMessage() {
        val channel = jda?.getTextChannelById(config.getString("channelId")!!)
        val message: String = config.getString("stopMessage")?: "**서버가 종료 되었습니다.** :stop_sign:"
        if(config.getBoolean("console-srv")) {
            val srvChannel = jda?.getTextChannelById(config.getString("console-srv-channelId")!!)
            srvChannel?.sendMessage(message)?.queue()
        }
        channel?.sendMessage(message)?.queue()
    }
}