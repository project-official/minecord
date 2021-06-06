package xyz.netherald.wild.discord.listeners

import io.papermc.paper.event.player.AsyncChatEvent
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.ChannelType
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.events.GenericEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.EventListener
import net.kyori.adventure.text.TextComponent
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.*
import xyz.netherald.wild.discord.WildDiscord


@Suppress("DEPRECATION")
class DiscordListener(private val plugin: WildDiscord) : EventListener, Listener {
    val listColor: List<String> = listOf(
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
        val customColor_message: String

        val s: String = replaceChatColor(formatter)
        var markdown_message = ""

        val stringMessage: String = event.message.contentRaw

        if (Markdown) {
            var setBold = false
            var setColor: String? = null
            var setItalic = false
            var setUnderline = false
            var setStrikethrough = false

            var index = 0
            while (index <= stringMessage.length - 1) {
                val value: String = stringMessage.slice(index..stringMessage.length - 1)
                var alreadyColor = false

                if (CustomColor) {
                    for (color in listColor) {
                        if (value.startsWith(color)) {
                            setColor = color

                            markdown_message += color
                            index += color.length
                            alreadyColor = true
                            break
                        }
                    }
                }

                if (value.startsWith("**")) {
                    if (setBold) {
                        setBold = false
                        markdown_message += "${ChatColor.RESET}"
                        if (CustomColor && setColor != null) {
                            markdown_message += setColor
                            index += setColor.length
                        }
                    } else {
                        setBold = true
                        markdown_message += "${ChatColor.BOLD}"
                    }
                    index += 2
                } else if (value.startsWith("*")) {
                    if (setItalic) {
                        setItalic = false
                        markdown_message += "${ChatColor.RESET}"
                        if (CustomColor && setColor != null) {
                            markdown_message += setColor
                            index += setColor.length
                        }
                    } else {
                        setItalic = true
                        markdown_message += "${ChatColor.ITALIC}"
                    }
                    index += 1
                } else if (value.startsWith("~~")) {
                    if (setStrikethrough) {
                        setStrikethrough = false
                        markdown_message += "${ChatColor.RESET}"
                        if (CustomColor && setColor != null) {
                            markdown_message += setColor
                            index += setColor.length
                        }
                    } else {
                        setStrikethrough = true
                        markdown_message += "${ChatColor.STRIKETHROUGH}"
                    }
                    index += 2
                } else if (value.startsWith("__")) {
                    if (setUnderline) {
                        setUnderline = false
                        markdown_message += "${ChatColor.RESET}"
                        if (CustomColor && setColor != null) {
                            markdown_message += setColor
                            index += setColor.length
                        }
                    } else {
                        setUnderline = true
                        markdown_message += "${ChatColor.UNDERLINE}"
                    }
                    index += 2
                } else {
                    if (!alreadyColor) {
                        index += 1
                        markdown_message += value[0]
                    }
                }
            }
        } else {
            markdown_message = stringMessage
        }

        if (CustomColor) {
            customColor_message = replaceChatColor(markdown_message)
        } else {
            customColor_message = markdown_message
        }

        return s.replace("<message>", customColor_message)
            .replace("<sender>", event.author.asTag)
            .replace("<mention>", event.author.asMention)
    }

    private fun replacePlayer(player: Player, formatter: String): String {
        return formatter.replace("<player>", player.name)
            .replace("<address>", player.address.address.hostAddress)
            .replace("<exp>", "${player.exp}")
            .replace("<level>", "${player.level}")
    }

    private fun replaceMsgFormat(event: AsyncChatEvent, formatter: String): String {
        val player: String = replacePlayer(event.player, formatter)
        val message: String = (event.message() as TextComponent).content()
        return player.replace("<message>", message)
    }

    private fun replaceAccessFormat(event: PlayerEvent, formatter: String): String {
        val player: String = replacePlayer(event.player, formatter)
        return player.replace("<online>", "${Bukkit.getOnlinePlayers().size}")
            .replace("<max>", "${Bukkit.getMaxPlayers()}")
    }

    private fun replaceDeathFormat(event: PlayerDeathEvent, formatter: String): String {
        val player: String = replacePlayer(event.entity.player!!, formatter)
        return player.replace("<message>", "${event.deathMessage}")
    }

    private fun replaceAdvancementFormat(event: PlayerAdvancementDoneEvent, formatter: String): String {
        val player: String = replacePlayer(event.player, formatter)

        val advancement: String = event.advancement.key.key
        return player.replace("<advancement>", advancement.replace("adventure/", ""))
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
                                        memberStr += "${i+1} 번째: ${player.name}\n"
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
                            Bukkit.broadcastMessage(replaceChatFormat(event, format, supportMarkdown, customColor))
                        }
                }
            }
        }
    }

    @EventHandler
    fun onChat(event: AsyncChatEvent) {
        val channel = WildDiscord.jda?.getTextChannelById(plugin.config.getString("channelId")!!)

        val format: String = plugin.config.getString("chatFormat") ?: "**<player>**: <message>"
        channel?.sendMessage(replaceMsgFormat(event, format))?.queue()
    }

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        if (!plugin.config.getBoolean("accessEnable")) return
        val channel = WildDiscord.jda?.getTextChannelById(plugin.config.getString("channelId")!!)
        val format: String = plugin.config.getString("joinFormat")?:
            "**<player>**님이 게임에 들어왔습니다. 현재 플레이어 수: <online>/<max>명"

        if (plugin.config.getBoolean("joinEmbed")) {
            val title: String? = plugin.config.getString("joinEmbedTitle")
            val description: String = replaceAccessFormat(event, format)
            val color: Int = plugin.config.getInt("joinEmbedColor")
            val builder = EmbedBuilder().setDescription(description)
                .setColor(color)

            if (!(title == null || title == "")) {
                builder.setTitle(title)
            }

            val embed: MessageEmbed = builder.build()
            channel?.sendMessage(embed)?.queue()
        } else {
            channel?.sendMessage(replaceAccessFormat(event, format))?.queue()
        }
    }

    @EventHandler
    fun onLeave(event: PlayerQuitEvent) {
        if (!plugin.config.getBoolean("accessEnable")) return
        val channel = WildDiscord.jda?.getTextChannelById(plugin.config.getString("channelId")!!)
        val format: String = plugin.config.getString("leaveFormat")?:
            "**<player>**님이 게임에서 나갔습니다. 현재 플레이어 수: <online>/<max>명"

        if (plugin.config.getBoolean("leaveEmbed")) {
            val title: String? = plugin.config.getString("leaveEmbedTitle")
            val description: String = replaceAccessFormat(event, format)
            val color: Int = plugin.config.getInt("leaveEmbedColor")
            val builder = EmbedBuilder().setDescription(description)
                .setColor(color)

            if (!(title == null || title == "")) {
                builder.setTitle(title)
            }

            val embed: MessageEmbed = builder.build()
            channel?.sendMessage(embed)?.queue()
        } else {
            channel?.sendMessage(replaceAccessFormat(event, format))?.queue()
        }
    }

    @EventHandler
    fun onPlayerDeath(event: PlayerDeathEvent) {
        if (event.entityType != EntityType.PLAYER) return
        if (!plugin.config.getBoolean("deathEnable")) return

        val channel = WildDiscord.jda?.getTextChannelById(plugin.config.getString("channelId")!!)
        val format: String = plugin.config.getString("deathFormat")?:
            "**<player>님이 사망 하셨습니다.**"

        if (plugin.config.getBoolean("deathEmbed")) {
            val title: String? = plugin.config.getString("deathEmbedTitle")
            val description: String = replaceDeathFormat(event, format)
            val color: Int = plugin.config.getInt("deathEmbedColor")
            val builder = EmbedBuilder().setDescription(description)
                .setColor(color)

            if (!(title == null || title == "")) {
                builder.setTitle(title)
            }

            val embed: MessageEmbed = builder.build()
            channel?.sendMessage(embed)?.queue()
        } else {
            channel?.sendMessage(replaceDeathFormat(event, format))?.queue()
        }
    }

    @EventHandler
    fun onPlayerKick(event: PlayerKickEvent) {
        if (!plugin.config.getBoolean("kickEnable")) return
        val channel = WildDiscord.jda?.getTextChannelById(plugin.config.getString("channelId")!!)
        val format: String = plugin.config.getString("kickFormat")?:
            "**<player>님이 추방 되었습니다.**"

        if (plugin.config.getBoolean("deathEmbed")) {
            val title: String? = plugin.config.getString("kickEmbedTitle")
            val description: String = replaceAccessFormat(event, format)
            val color: Int = plugin.config.getInt("kickEmbedColor")
            val builder = EmbedBuilder().setDescription(description)
                .setColor(color)

            if (!(title == null || title == "")) {
                builder.setTitle(title)
            }

            val embed: MessageEmbed = builder.build()
            channel?.sendMessage(embed)?.queue()
        } else {
            channel?.sendMessage(replaceAccessFormat(event, format))?.queue()
        }
    }

    @EventHandler
    fun onPlayerAdvancement(event: PlayerAdvancementDoneEvent) {
        if (!plugin.config.getBoolean("advancementEnable")) return
        val channel = WildDiscord.jda?.getTextChannelById(plugin.config.getString("channelId")!!)
        val format: String = plugin.config.getString("advancementFormat")?:
            "**<player>님이 <advancement>를 클리어 하였습니다..**"

        if (plugin.config.getBoolean("advancementEmbed")) {
            val title: String? = plugin.config.getString("advancementEmbedTitle")
            val description: String = replaceAdvancementFormat(event, format)
            val color: Int = plugin.config.getInt("advancementEmbedColor")
            val builder = EmbedBuilder().setDescription(description)
                .setColor(color)

            if (!(title == null || title == "")) {
                builder.setTitle(title)
            }

            val embed: MessageEmbed = builder.build()
            channel?.sendMessage(embed)?.queue()
        } else {
            channel?.sendMessage(replaceAdvancementFormat(event, format))?.queue()
        }
    }
}
