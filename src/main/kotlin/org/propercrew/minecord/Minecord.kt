package org.propercrew.minecord

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import org.propercrew.minecord.commands.OnlineCommand
import org.propercrew.minecord.commands.PingPong
import org.propercrew.minecord.commands.ReloadCommand
import org.proper.minecord.listeners.*
import org.propercrew.minecord.listeners.*

class Minecord : JavaPlugin() {

    companion object {
        var jda: JDA? = null
        var init = false
        var instance: Minecord? = null
    }

    private fun loadJDAModule() {
        val builder = JDABuilder.createDefault(this.config.getString("token")).apply {
            addEventListeners(OnlineCommand(this@Minecord))
            addEventListeners(SendChatListener(this@Minecord))
            addEventListeners(PingPong())
            addEventListeners(ReloadCommand(this@Minecord))
        }

        if (this.config.getBoolean("show_activity")) {
            builder.setActivity(Activity.playing("Minecraft : ${config.getString("server_address")}"))
        }

        jda = builder.build()
        val commands = jda?.updateCommands()

        commands?.apply {
            addCommands(CommandData("online", "You can see online player!"))
            addCommands(CommandData("ping", "You can pingpong with my bot"))
            addCommands(CommandData("pong", "You can pingpong with my bot"))
            addCommands(CommandData("reload", "You can reload your minecraft server **ADMIN ONLY**"))
        }?.queue()

        logger.info("Wild - Discord module enabled!")
    }

    private fun loadEventListener() {
        server.pluginManager.apply {
            registerEvents(AsyncChatListener(this@Minecord), this@Minecord)
            registerEvents(DeathListener(this@Minecord), this@Minecord)
            registerEvents(JoinQuitListener(this@Minecord), this@Minecord)
            registerEvents(KickListener(this@Minecord), this@Minecord)
        }

        logger.info("Wild - Minecraft listener registered!")
    }

    /*
    private fun loadCommand() {
    }
     */

    override fun onEnable() {
        instance = this
        if (!dataFolder.exists()) {
            this.saveDefaultConfig()
            logger.info("Wild - Initialized configuration!")
        }

        loadJDAModule()
        loadEventListener()
        // loadCommand()

        logger.info("Wild - Discord Plugin successful loaded.")
        Bukkit.getScheduler().runTaskLater(this, Runnable {
            startMessage()
        }, 20L)
    }

    override fun onDisable() {
        stopMessage()
        init = false

        jda?.shutdown()
    }

    private fun startMessage() {
        val channel = jda?.getTextChannelById(config.getString("channelId")!!)
        val message: String = config.getString("startMessage")?: "**서버가 시작 되었습니다.** :white_check_mark:"
        channel?.sendMessage(message)?.queue()
    }

    private fun stopMessage() {
        val channel = jda?.getTextChannelById(config.getString("channelId")!!)
        val message: String = config.getString("stopMessage")?: "**서버가 종료 되었습니다.** :stop_sign:"
        channel?.sendMessage(message)?.queue()
    }
}