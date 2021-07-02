package xyz.netherald.wild.discord.listeners

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.ChannelType
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.events.GenericEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.EventListener
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import xyz.netherald.wild.discord.WildDiscord
import xyz.netherald.wild.discord.utils.FormatModule

class DiscordListener(private val plugin: WildDiscord) : EventListener {

    override fun onEvent(event: GenericEvent) {
        if (event is MessageReceivedEvent) {
            if (!event.message.author.isBot) {
                if (event.channelType != ChannelType.PRIVATE) {
                    if (event.channel.id == WildDiscord.instance?.config?.getString("channelId"))
                        if (event.message.contentRaw == "!online") {
                            val noMember: String? = plugin.config.getString("NoMemberMessage")
                            if (plugin.config.getInt("styleOnlineCommand") == 0) {
                                var memberStr = "**온라인 유저** : \n```"
                                memberStr += "인원: ${Bukkit.getOnlinePlayers().size}명/ ${Bukkit.getMaxPlayers()}명\n"
                                if (Bukkit.getOnlinePlayers().isNotEmpty()) {
                                    for ((i, player) in Bukkit.getOnlinePlayers().withIndex()) {
                                        memberStr += "$i: ${player.name}\n"
                                    }
                                } else {
                                    memberStr += "${noMember ?: "사람이 없습니다."}\n"
                                }
                                memberStr += "```"
                                memberStr += "========${WildDiscord.serverAddress ?: ""}========\n"

                                event.channel.sendMessage(memberStr).queue()
                            } else if (plugin.config.getInt("styleOnlineCommand") == 1) {
                                val title: String? = plugin.config.getString("embedTitle")
                                val color: Int = plugin.config.getInt("embedColor")
                                val field1: String? = plugin.config.getString("embedField1")
                                val field2: String? = plugin.config.getString("embedField2")

                                val embed = EmbedBuilder().setTitle(title ?: "**온라인 유저**")
                                    .setColor(color)
                                    .addField(
                                        field1 ?: "인원:",
                                        "${Bukkit.getOnlinePlayers().size}명/ ${Bukkit.getMaxPlayers()}명",
                                        false
                                    )

                                var memberStr = "```\n"
                                if (Bukkit.getOnlinePlayers().isNotEmpty()) {
                                    for ((i, player) in Bukkit.getOnlinePlayers().withIndex()) {
                                        memberStr += "${i + 1} 번째: ${player.name}\n"
                                    }
                                } else {
                                    memberStr += "${noMember ?: "사람이 없습니다."}\n"
                                }
                                memberStr += "```"

                                embed.addField(field2 ?: "목록:", memberStr, false)
                                if (WildDiscord.serverAddress != null) embed.setFooter("========${WildDiscord.serverAddress ?: ""}========")

                                val result: MessageEmbed = embed.build()
                                event.channel.sendMessage(result).queue()
                            }
                        } else {
                            val format: String =
                                plugin.config.getString("messageFormat") ?: "<<dark_purple><sender><reset>> <message>"
                            val customColor: Boolean = plugin.config.getBoolean("customColor")
                            val supportMarkdown: Boolean = plugin.config.getBoolean("customColor")
                            val formatModule = FormatModule()
                            Bukkit.broadcast(Component.text(formatModule.replaceChatFormat(event, format, supportMarkdown, customColor)))
                        }
                }
            }
        }
    }
}
