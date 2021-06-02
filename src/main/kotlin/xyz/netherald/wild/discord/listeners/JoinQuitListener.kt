package xyz.netherald.wild.discord.listeners

import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import xyz.netherald.wild.discord.WildDiscord

class JoinQuitListener(private val plugin: WildDiscord): Listener {

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        val channel = WildDiscord.jda?.getTextChannelById(plugin.config.getString("channelId")!!)
        channel?.sendMessage("**${event.player.name}**님이 게임에 들어왔습니다. 현재 플레이어 수: ${Bukkit.getOnlinePlayers().size}/${Bukkit.getMaxPlayers()}명")?.queue()
    }

    @EventHandler
    fun onLeave(event: PlayerQuitEvent) {
        val channel = WildDiscord.jda?.getTextChannelById(plugin.config.getString("channelId")!!)
        channel?.sendMessage("**${event.player.name}**님이 게임에서 나갔습니다. 현재 플레이어 수: ${Bukkit.getOnlinePlayers().size - 1}/${Bukkit.getMaxPlayers()}명")?.queue()
    }
}