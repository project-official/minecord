package xyz.netherald.wild.discord

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin

class WildDiscord : JavaPlugin(), Listener {

    companion object {
        var jda: JDA? = null
        var init = false
        var instance: WildDiscord? = null
    }

    override fun onEnable() {
        instance = this
        if (!dataFolder.exists()) {
            this.saveDefaultConfig()
            println("Wild - initiallized configuration!")
        }
        val builder = JDABuilder.createDefault(this.config.getString("token"))
            .setActivity(Activity.playing("Minecraft : netherald.kro.kr"))
            .addEventListeners(DiscordListener())
        jda = builder.build()
        println("Wild - discord module enabled!")
        server.pluginManager.registerEvents(this, this)
        println("Wild - minecraft listener registered!")
        init = true
        println("Wild - Discord Plugin load done.")

        startMessage()
    }

    override fun onDisable() {
        stopMessage()
        jda?.shutdown()
    }

    private fun startMessage() {
        val channel = jda?.getTextChannelById(config.getString("channelId")!!)
        channel?.sendMessage("**서버가 시작 되었습니다.** :white_check_mark:")?.queue()
    }

    private fun stopMessage() {
        val channel = jda?.getTextChannelById(config.getString("channelId")!!)
        channel?.sendMessage("**서버가 종료 되었습니다.** :stop_sign:")?.queue()
    }
}