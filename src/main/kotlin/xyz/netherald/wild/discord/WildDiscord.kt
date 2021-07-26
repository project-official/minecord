package xyz.netherald.wild.discord

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import xyz.netherald.wild.discord.commands.*
import xyz.netherald.wild.discord.listeners.*

class WildDiscord : JavaPlugin() {

    companion object {
        var jda: JDA? = null
        var init = false
        var instance: WildDiscord? = null
    }

    private fun loadJDAModule() {
        val builder = JDABuilder.createDefault(this.config.getString("token")).apply {
            addEventListeners(OnlineCommand(this@WildDiscord))
            addEventListeners(SendChatListener(this@WildDiscord))
            addEventListeners(PingPong())
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
        }?.queue()

        logger.info("Wild - Discord module enabled!")
    }

    private fun loadEventListener() {
        server.pluginManager.apply {
            registerEvents(AsyncChatListener(this@WildDiscord), this@WildDiscord)
            registerEvents(DeathListener(this@WildDiscord), this@WildDiscord)
            registerEvents(JoinQuitListener(this@WildDiscord), this@WildDiscord)
            registerEvents(KickListener(this@WildDiscord), this@WildDiscord)
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