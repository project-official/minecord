package xyz.netherald.wildDiscord

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
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

    @EventHandler
    fun onPlayerDeath(event : PlayerDeathEvent) {
        val channel = jda?.getTextChannelById(config.getString("channelId")!!)
        channel?.sendMessage("**${event.deathMessage}**")?.queue()
    }

    @EventHandler
    fun onChat(event: AsyncPlayerChatEvent) {
        val channel = jda?.getTextChannelById(this.config.getString("channelId")!!)
        channel?.sendMessage("**${event.player.name}**: ${event.message}")?.queue()
    }

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        val channel = jda?.getTextChannelById(this.config.getString("channelId")!!)
        channel?.sendMessage("**${event.player.name}**님이 게임에 들어왔습니다. 현재 플레이어 수: ${Bukkit.getOnlinePlayers().size}/${Bukkit.getMaxPlayers()}명")?.queue()
    }

    @EventHandler
    fun onLeave(event: PlayerQuitEvent) {
        val channel = jda?.getTextChannelById(this.config.getString("channelId")!!)
        channel?.sendMessage("**${event.player.name}**님이 게임에서 나갔습니다. 현재 플레이어 수: ${Bukkit.getOnlinePlayers().size - 1}/${Bukkit.getMaxPlayers()}명")?.queue()
    }

}