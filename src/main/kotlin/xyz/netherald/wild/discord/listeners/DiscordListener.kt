package xyz.netherald.wild.discord.listeners

import io.papermc.paper.event.player.AsyncChatEvent
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.ChannelType
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.events.GenericEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.EventListener
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.*
import xyz.netherald.wild.discord.WildDiscord

class DiscordListener(private val plugin: WildDiscord) : EventListener {
    private val listColor: List<String> = listOf(
        "<black>", "<dark_blue>", "<dark_green>", "<dark_aqua>", "<red>",
        "<dark_purple>", "<gold>", "<gray>", "<dark_gray>", "<blue>", "<green>", "<aqua>", "<purple>", "<yellow>",
        "<white>"
    )

    private fun replaceChatColor(message: String): String {
        return message.replace("<black>", "${ChatColor.BLACK}")
            .replace("<dark_blue>", "${ChatColor.DARK_BLUE}")
            .replace("<dark_green>", "${ChatColor.DARK_GREEN}")
            .replace("<dark_aqua>", "${ChatColor.DARK_AQUA}")
            .replace("<red>", "${ChatColor.DARK_RED}")
            .replace("<dark_purple>", "${ChatColor.DARK_PURPLE}")
            .replace("<gold>", "${ChatColor.GOLD}")
            .replace("<gray>", "${ChatColor.GRAY}")
            .replace("<dark_gray>", "${ChatColor.DARK_GRAY}")
            .replace("<blue>", "${ChatColor.BLUE}")
            .replace("<green>", "${ChatColor.GREEN}")
            .replace("<aqua>", "${ChatColor.AQUA}")
            .replace("<purple>", "${ChatColor.LIGHT_PURPLE}")
            .replace("<yellow>", "${ChatColor.YELLOW}")
            .replace("<white>", "${ChatColor.WHITE}")
            .replace("<magic>", "${ChatColor.MAGIC}")
            .replace("<bold>", "${ChatColor.BOLD}")
            .replace("<strikethrough>", "${ChatColor.STRIKETHROUGH}")
            .replace("<underline>", "${ChatColor.UNDERLINE}")
            .replace("<italic>", "${ChatColor.ITALIC}")
            .replace("<reset>", "${ChatColor.RESET}")
    }

    private fun replaceChatFormat(
        event: MessageReceivedEvent,
        formatter: String,
        Markdown: Boolean,
        CustomColor: Boolean
    ): String {
        val customColorMessage: String

        val s: String = replaceChatColor(formatter)
        var markdownMessage = ""

        val stringMessage: String = event.message.contentRaw

        if (Markdown) {
            var setBold = false
            var setColor: String? = null
            var setItalic = false
            var setUnderline = false
            var setStrikethrough = false

            var index = 0
            while (index <= stringMessage.length - 1) {
                val value: String = stringMessage.slice(index until stringMessage.length)
                var alreadyColor = false

                if (CustomColor) {
                    for (color in listColor) {
                        if (value.startsWith(color)) {
                            setColor = color

                            markdownMessage += color
                            index += color.length
                            alreadyColor = true
                            break
                        }
                    }
                }

                if (value.startsWith("**")) {
                    if (setBold) {
                        setBold = false
                        markdownMessage += "${ChatColor.RESET}"
                        if (CustomColor && setColor != null) {
                            markdownMessage += setColor
                            index += setColor.length
                        }
                    } else {
                        setBold = true
                        markdownMessage += "${ChatColor.BOLD}"
                    }
                    index += 2
                } else if (value.startsWith("*")) {
                    if (setItalic) {
                        setItalic = false
                        markdownMessage += "${ChatColor.RESET}"
                        if (CustomColor && setColor != null) {
                            markdownMessage += setColor
                            index += setColor.length
                        }
                    } else {
                        setItalic = true
                        markdownMessage += "${ChatColor.ITALIC}"
                    }
                    index += 1
                } else if (value.startsWith("~~")) {
                    if (setStrikethrough) {
                        setStrikethrough = false
                        markdownMessage += "${ChatColor.RESET}"
                        if (CustomColor && setColor != null) {
                            markdownMessage += setColor
                            index += setColor.length
                        }
                    } else {
                        setStrikethrough = true
                        markdownMessage += "${ChatColor.STRIKETHROUGH}"
                    }
                    index += 2
                } else if (value.startsWith("__")) {
                    if (setUnderline) {
                        setUnderline = false
                        markdownMessage += "${ChatColor.RESET}"
                        if (CustomColor && setColor != null) {
                            markdownMessage += setColor
                            index += setColor.length
                        }
                    } else {
                        setUnderline = true
                        markdownMessage += "${ChatColor.UNDERLINE}"
                    }
                    index += 2
                } else {
                    if (!alreadyColor) {
                        index += 1
                        markdownMessage += value[0]
                    }
                }
            }
        } else {
            markdownMessage = stringMessage
        }

        customColorMessage = if (CustomColor) {
            replaceChatColor(markdownMessage)
        } else {
            markdownMessage
        }

        return s.replace("<message>", customColorMessage)
            .replace("<sender>", event.author.asTag)
            .replace("<mention>", event.author.asMention)
    }

    companion object {

        private fun replacePlayer(player: Player, formatter: String): String {
            return formatter.replace("<player>", player.name)
                .replace("<address>", player.address.address.hostAddress)
                .replace("<exp>", "${player.exp}")
                .replace("<level>", "${player.level}")
        }

        fun replaceMsgFormat(event: AsyncChatEvent, formatter: String): String {
            val player: String = replacePlayer(event.player, formatter)
            val message: String = (event.message() as TextComponent).content()
            return player.replace("<message>", message)
        }

        fun replaceAccessFormat(event: PlayerEvent, formatter: String, leave: Boolean): String {
            val player: String = replacePlayer(event.player, formatter)
            return if (!leave) {
                player.replace("<online>", "${Bukkit.getOnlinePlayers().size}")
                    .replace("<max>", "${Bukkit.getMaxPlayers()}")
            } else {
                player.replace("<online>", "${Bukkit.getOnlinePlayers().size - 1}")
                    .replace("<max>", "${Bukkit.getMaxPlayers()}")
            }
        }

        fun replaceDeathFormat(event: PlayerDeathEvent, formatter: String): String {
            val player: String = replacePlayer(event.entity.player!!, formatter)
            return player.replace("<message>", "${event.deathMessage()}")

        }

        fun replaceAdvancementFormat(event: PlayerAdvancementDoneEvent, formatter: String): String {
            val player: String = replacePlayer(event.player, formatter)

            val advancement: String = event.advancement.key.key
            return player.replace("<advancement>", advancement.replace("adventure/", ""))
        }
    }

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
                            Bukkit.broadcast(Component.text(replaceChatFormat(event, format, supportMarkdown, customColor)))
                        }
                }
            }
        }
    }
}
