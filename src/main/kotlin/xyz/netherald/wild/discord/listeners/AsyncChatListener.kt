package xyz.netherald.wild.discord.listeners

import io.papermc.paper.event.player.AsyncChatEvent
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import xyz.netherald.wild.discord.WildDiscord

class AsyncChatListener(private val plugin: WildDiscord): Listener {

    @EventHandler
    fun onChat(event: AsyncChatEvent) {
        val channel = WildDiscord.jda?.getTextChannelById(plugin.config.getString("channelId")!!)
        val format: String = plugin.config.getString("chatFormat") ?: "**<player>**: <message>"
        channel?.sendMessage(DiscordListener.replaceMsgFormat(event, format))?.queue()
    }
}