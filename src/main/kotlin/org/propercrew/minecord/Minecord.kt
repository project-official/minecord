package org.propercrew.minecord

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import org.propercrew.minecord.commands.Invite
import org.propercrew.minecord.commands.OnlineCommand
import org.propercrew.minecord.commands.PingPong
import org.propercrew.minecord.commands.ReloadCommand
import org.propercrew.minecord.listeners.*

class Minecord : JavaPlugin() {

    companion object {
        var jda: JDA? = null
        var init = false
        var instance: Minecord? = null
    }

    private fun loadJDAModule() {
        val builder = JDABuilder.createDefault(this.config.getString("token")).apply {
            addEventListeners(
                OnlineCommand(),
                SendChatListener(),
                PingPong(),
                ReloadCommand()
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
                CommandData("reload", "You can reload your minecraft server **ADMIN ONLY**")
            )
        }?.queue()

        logger.info("Wild - Discord module enabled!")
    }

    private fun loadEventListener() {
        server.pluginManager.apply {
            registerEvents(AsyncChatListener(), this@Minecord)
            registerEvents(DeathListener(), this@Minecord)
            registerEvents(JoinQuitListener(), this@Minecord)
            registerEvents(KickListener(), this@Minecord)
        }

        logger.info("Wild - Minecraft listener registered!")
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