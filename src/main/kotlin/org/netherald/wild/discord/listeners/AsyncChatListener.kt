package org.netherald.wild.discord.listeners

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.netherald.wild.discord.WildDiscord
import org.netherald.wild.discord.utils.FormatModule

class AsyncChatListener(private val plugin: WildDiscord): Listener {

    @EventHandler
    fun onChat(event: AsyncPlayerChatEvent) {
        val formatModule = FormatModule()
        val channel = WildDiscord.jda?.getTextChannelById(plugin.config.getString("channelId")!!)
        val format: String = plugin.config.getString("chatFormat") ?: "**<player>**: <message>"
        channel?.sendMessage(formatModule.replaceMsgFormat(event, format))?.queue()
    }
}