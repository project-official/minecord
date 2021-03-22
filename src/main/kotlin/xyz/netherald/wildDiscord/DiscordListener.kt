package xyz.netherald.wildDiscord

import net.dv8tion.jda.api.entities.ChannelType
import net.dv8tion.jda.api.events.GenericEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.EventListener
import org.bukkit.Bukkit
import org.bukkit.ChatColor

class DiscordListener : EventListener {

    override fun onEvent(event: GenericEvent) {
        if(event is MessageReceivedEvent) {
            if(!event.message.author.isBot) {
                if (event.channelType != ChannelType.PRIVATE) {
                    if(event.channel.id == instance?.config?.getString("channelId"))
                        Bukkit.broadcastMessage("<${ChatColor.DARK_PURPLE}${event.author.asTag}${ChatColor.RESET}> ${event.message.contentRaw}")
                }
            }
        }
    }

}