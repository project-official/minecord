package xyz.netherald.wild.discord.listeners

import io.papermc.paper.event.player.AsyncChatEvent
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import xyz.netherald.wild.discord.WildDiscord

class ChattingListener(private val plugin: WildDiscord): Listener {

    @EventHandler
    fun onChat(event: AsyncChatEvent) {
        val channel = WildDiscord.jda?.getTextChannelById(plugin.config.getString("channelId")!!)
        channel?.sendMessage("**${event.player.name}**: ${event.message()}")?.queue()
    }
}