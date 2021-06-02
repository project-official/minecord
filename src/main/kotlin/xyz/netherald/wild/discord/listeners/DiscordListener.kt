package xyz.netherald.wild.discord.listeners

import net.dv8tion.jda.api.entities.ChannelType
import net.dv8tion.jda.api.events.GenericEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.EventListener
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import xyz.netherald.wild.discord.WildDiscord

@Suppress("DEPRECATION")
class DiscordListener : EventListener {

    override fun onEvent(event: GenericEvent) {
        if(event is MessageReceivedEvent) {
            if(!event.message.author.isBot) {
                if (event.channelType != ChannelType.PRIVATE) {
                    if(event.channel.id == WildDiscord.instance?.config?.getString("channelId"))
                        if (event.message.contentRaw == "!online") {
                            var memberStr = "**온라인 유저** : \n```"
                            memberStr += "인원: ${Bukkit.getOnlinePlayers().size}명"
                            if (Bukkit.getOnlinePlayers() != null) {
                                for ((i, player) in Bukkit.getOnlinePlayers().withIndex()) {
                                    memberStr += "**${player.name}**\n"
                                    println("[Discord] ${i}번째 사람")
                                }
                            } else {
                                memberStr += "**사람이 없습니다**\n"
                            }

                            memberStr += "===${WildDiscord.serverAddress}==="
                            memberStr += "```"
                            event.channel.sendMessage(memberStr).queue()
                        } else {
                            Bukkit.broadcastMessage("<${ChatColor.DARK_PURPLE}${event.author.asTag}${ChatColor.RESET}> ${event.message.contentRaw}")
                        }
                }
            }
        }
    }
}