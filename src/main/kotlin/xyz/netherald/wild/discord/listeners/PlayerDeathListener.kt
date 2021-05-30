package xyz.netherald.wild.discord.listeners

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import xyz.netherald.wild.discord.WildDiscord

class PlayerDeathListener(private val plugin: WildDiscord): Listener {

    @EventHandler
    fun onPlayerDeath(event : PlayerDeathEvent) {
        val channel = WildDiscord.jda?.getTextChannelById(plugin.config.getString("channelId")!!)
        channel?.sendMessage("**${event.deathMessage()}**")?.queue()
    }
}