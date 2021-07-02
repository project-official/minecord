package xyz.netherald.wild.discord

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity
import org.bukkit.Bukkit
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import xyz.netherald.wild.discord.listeners.*

class WildDiscord : JavaPlugin(), Listener {

    companion object {
        var jda: JDA? = null
        var init = false
        var instance: WildDiscord? = null

        var serverAddress: String? = instance?.config?.getString("server_address")
    }

    override fun onEnable() {
        instance = this
        if (!dataFolder.exists()) {
            this.saveDefaultConfig()
            Bukkit.getLogger().info("Wild - Initialized configuration!")
        }

        val builder = JDABuilder.createDefault(this.config.getString("token"))
            .addEventListeners(DiscordListener(this))

        if (this.config.getBoolean("show_activity")) {
            builder.setActivity(Activity.playing("Minecraft : $serverAddress"))
        }

        jda = builder.build()

        Bukkit.getLogger().info("Wild - Discord module enabled!")
        server.pluginManager.registerEvents(this, this)
        Bukkit.getLogger().info("Wild - Minecraft listener registered!")
        init = true
        Bukkit.getLogger().info("Wild - Discord Plugin load done.")

        server.pluginManager.apply {
            registerEvents(AsyncChatListener(this@WildDiscord), this@WildDiscord)
            registerEvents(DeathListener(this@WildDiscord), this@WildDiscord)
            registerEvents(JoinQuitListener(this@WildDiscord), this@WildDiscord)
            registerEvents(KickListener(this@WildDiscord), this@WildDiscord)
        }

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