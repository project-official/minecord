package xyz.netherald.wildDiscord

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.plugin.java.JavaPlugin

var jda: JDA? = null
var inited = false
var instance: WildDiscord? = null

class WildDiscord : JavaPlugin(), Listener {

    override fun onEnable() {
        instance = this
        if(!dataFolder.exists()) {
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
        inited = true
        println("Wild - Discord Plugin load done.")
    }

    override fun onDisable() {
        jda?.shutdown()
    }

    @EventHandler
    fun onChat(event: AsyncPlayerChatEvent) {
        val channel = jda?.getTextChannelById(this.config.getString("channelId")!!)
        channel?.sendMessage("**${event.player.name}**: ${event.message}")?.queue()
    }

}