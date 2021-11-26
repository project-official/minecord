package org.netherald.minecord.listeners

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.netherald.minecord.Minecord
import org.netherald.minecord.utils.FormatModule

class AsyncChatListener(private val plugin: Minecord): Listener {

    @Suppress("DEPRECATION")
    @EventHandler
    fun onChat(event: AsyncPlayerChatEvent) {
        val formatModule = FormatModule()
        val channel = Minecord.jda?.getTextChannelById(plugin.config.getString("channelId")!!)
        val format: String = plugin.config.getString("chatFormat") ?: "**<player>**: <message>"
        channel?.sendMessage(formatModule.replaceMsgFormat(event, format))?.queue()
    }
}