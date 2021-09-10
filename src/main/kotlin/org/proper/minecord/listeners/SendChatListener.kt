package org.proper.minecord.listeners

import net.dv8tion.jda.api.entities.ChannelType
import net.dv8tion.jda.api.events.GenericEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.EventListener
import org.bukkit.Bukkit
import org.proper.minecord.Minecord
import org.proper.minecord.utils.FormatModule

class SendChatListener(private val plugin: Minecord): EventListener {

    override fun onEvent(event: GenericEvent) {
        if (event is MessageReceivedEvent) {
            if (event.author.isBot) {
                return
            }

            if (event.channelType == ChannelType.PRIVATE) {
                return
            }

            if (event.channel.id == Minecord.instance?.config?.getString("channelId")) {
                val format: String = plugin.config.getString("messageFormat") ?: "<<dark_purple><sender><reset>> <message>"
                val customColor: Boolean = plugin.config.getBoolean("customColor")
                val supportMarkdown: Boolean = plugin.config.getBoolean("customColor")
                val formatModule = FormatModule()

                Bukkit.broadcastMessage(formatModule.replaceChatFormat(event, format, supportMarkdown, customColor))
            }
        }
    }
}