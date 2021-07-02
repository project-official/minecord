package xyz.netherald.wild.discord.listeners

import io.papermc.paper.event.player.AsyncChatEvent
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import xyz.netherald.wild.discord.WildDiscord
import xyz.netherald.wild.discord.utils.FormatModule

class AsyncChatListener(private val plugin: WildDiscord): Listener {

    @EventHandler
    fun onChat(event: AsyncChatEvent) {
        val formatModule = FormatModule()
        val channel = WildDiscord.jda?.getTextChannelById(plugin.config.getString("channelId")!!)
        val format: String = plugin.config.getString("chatFormat") ?: "**<player>**: <message>"
        channel?.sendMessage(formatModule.replaceMsgFormat(event, format))?.queue()
    }
}