package xyz.netherald.wild.discord.listeners

import io.papermc.paper.event.player.AsyncChatEvent
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.ChannelType
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.events.GenericEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.EventListener
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.TranslatableComponent
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import xyz.netherald.wild.discord.WildDiscord


@Suppress("DEPRECATION")
class DiscordListener(private val plugin: WildDiscord) : EventListener, Listener {
    val listColor: List<String> = listOf("<black>", "<dark_blue>", "<dark_green>", "<dark_aqua>", "<red>",
        "<dark_purple>", "<gold>", "<gray>", "<dark_gray>", "<blue>", "<green>", "<aqua>", "<purple>", "<yellow>",
        "<white>")

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

    private fun replaceChatFormat(event: MessageReceivedEvent, formatter: String, Markdown: Boolean, CustomColor: Boolean): String {
        val msg: String;

        val s: String = replaceChatColor(formatter);
        var m_message: String = "";

        var stringMessage: String = event.message.contentRaw

        if (Markdown) {
            var setBold: Boolean = false
            var setColor: String? = null
            var setItalic: Boolean = false
            var setUnderline: Boolean = false
            var setStrikethrough: Boolean = false

            var index: Int = 0
            while (index <= stringMessage.length -1) {
                var value: String = stringMessage.slice(index..stringMessage.length -1);
                var alreadyColor: Boolean = false

                if (CustomColor) {
                    for (color in listColor) {
                        if (value.startsWith(color)) {
                            setColor = color
                            index += color.length
                            alreadyColor = true
                            break
                        }
                    }
                }

                if (value.startsWith("**")) {
                    if (setBold) {
                        setBold = false
                        m_message += "${ChatColor.RESET}"
                        if (CustomColor && setColor != null) {
                            m_message += setColor
                            index += setColor.length
                        }
                    } else {
                        setBold = true
                        m_message += "${ChatColor.BOLD}"
                    }
                    index += 2
                } else if (value.startsWith("*")) {
                    if (setItalic) {
                        setItalic = false
                        m_message += "${ChatColor.RESET}"
                        if (CustomColor && setColor != null) {
                            m_message += setColor
                            index += setColor.length
                        }
                    } else {
                        setItalic = true
                        m_message += "${ChatColor.ITALIC}"
                    }
                    index += 1
                } else if (value.startsWith("~~")) {
                    if (setStrikethrough) {
                        setStrikethrough = false
                        m_message += "${ChatColor.RESET}"
                        if (CustomColor && setColor != null) {
                            m_message += setColor
                            index += setColor.length
                        }
                    } else {
                        setStrikethrough = true
                        m_message += "${ChatColor.STRIKETHROUGH}"
                    }
                    index += 2
                } else if (value.startsWith("__")) {
                    if (setUnderline) {
                        setUnderline = false
                        m_message += "${ChatColor.RESET}"
                        if (CustomColor && setColor != null) {
                            m_message += setColor
                            index += setColor.length
                        }
                    } else {
                        setUnderline = true
                        m_message += "${ChatColor.UNDERLINE}"
                    }
                    index += 2
                } else {
                    if (!alreadyColor) {
                        index += 1
                        m_message += value[0]
                    }
                }
            }
        } else {
            m_message = stringMessage
        }

        if (CustomColor) {
            msg = replaceChatColor(m_message)
        }
        else {
            msg = m_message
        }

        return s.replace("<message>", msg)
            .replace("<sender>", event.author.asTag)
            .replace("<mention>", event.author.asMention)
    }

    override fun onEvent(event: GenericEvent) {
        if(event is MessageReceivedEvent) {
            if(!event.message.author.isBot) {
                if (event.channelType != ChannelType.PRIVATE) {
                    if(event.channel.id == WildDiscord.instance?.config?.getString("channelId"))
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
                                    memberStr += "${noMember?:"사람이 없습니다."}\n"
                                }
                                memberStr += "```"
                                memberStr += "========${WildDiscord.serverAddress ?: ""}========\n"

                                event.channel.sendMessage(memberStr).queue()
                            }
                            else if (plugin.config.getInt("styleOnlineCommand") == 1) {
                                val title: String? = plugin.config.getString("embedTitle")
                                val color: Int = plugin.config.getInt("embedColor")
                                val field1: String? = plugin.config.getString("embedField1")
                                val field2: String? = plugin.config.getString("embedField2")

                                val embed = EmbedBuilder().setTitle(title?: "**온라인 유저**")
                                    .setColor(color)
                                    .addField(field1?: "인원:", "${Bukkit.getOnlinePlayers().size}명/ ${Bukkit.getMaxPlayers()}명",false)

                                var memberStr = "```\n"
                                if (Bukkit.getOnlinePlayers().isNotEmpty()) {
                                    for ((i, player) in Bukkit.getOnlinePlayers().withIndex()) {
                                        memberStr += "$i 번째: ${player.name}\n"
                                    }
                                } else {
                                    memberStr += "${noMember?:"사람이 없습니다."}\n"
                                }
                                memberStr += "```"

                                embed.addField(field2?:  "목록:", memberStr, false)
                                if (WildDiscord.serverAddress != null) embed.setFooter("========${WildDiscord.serverAddress ?: ""}========")

                                val result: MessageEmbed = embed.build()
                                event.channel.sendMessage(result).queue()
                            }
                        } else {
                            val format: String = plugin.config.getString("messageFormat")?:"<<dark_purple><sender><reset>> <message>"
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
        channel?.sendMessage("**${event.player.name}**: ${(event.message() as TextComponent).content()}")?.queue()
    }

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        val channel = WildDiscord.jda?.getTextChannelById(plugin.config.getString("channelId")!!)
        channel?.sendMessage("**${event.player.name}**님이 게임에 들어왔습니다. 현재 플레이어 수: ${Bukkit.getOnlinePlayers().size}/${Bukkit.getMaxPlayers()}명")?.queue()
    }

    @EventHandler
    fun onLeave(event: PlayerQuitEvent) {
        val channel = WildDiscord.jda?.getTextChannelById(plugin.config.getString("channelId")!!)
        channel?.sendMessage("**${event.player.name}**님이 게임에서 나갔습니다. 현재 플레이어 수: ${Bukkit.getOnlinePlayers().size - 1}/${Bukkit.getMaxPlayers()}명")?.queue()
    }

    @EventHandler
    fun onPlayerDeath(event : PlayerDeathEvent) {
        val channel = WildDiscord.jda?.getTextChannelById(plugin.config.getString("channelId")!!)
        channel?.sendMessage("**${event.deathMessage()}**")?.queue()
    }
}