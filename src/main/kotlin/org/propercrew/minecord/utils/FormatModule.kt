package org.propercrew.minecord.utils

import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.event.player.PlayerAdvancementDoneEvent
import org.bukkit.event.player.PlayerEvent

class FormatModule {

    private val listColorMap: HashMap<String, ChatColor> = hashMapOf(
        Pair("<black>", ChatColor.BLACK),
        Pair("<blue>", ChatColor.BLUE),
        Pair("<dark_green>", ChatColor.DARK_GREEN),
        Pair("<dark_aqua>", ChatColor.DARK_AQUA),
        Pair("<dark_red>", ChatColor.DARK_RED),
        Pair("<dark_purple>", ChatColor.DARK_PURPLE),
        Pair("<gold>", ChatColor.GOLD),
        Pair("<gray>", ChatColor.GRAY),
        Pair("<dark_gray>", ChatColor.DARK_GRAY),
        Pair("<blue>", ChatColor.BLUE),
        Pair("<green>", ChatColor.GREEN),
        Pair("<aqua>", ChatColor.AQUA),
        Pair("<red>", ChatColor.RED),
        Pair("<light_purple>", ChatColor.LIGHT_PURPLE),
        Pair("<yellow>", ChatColor.YELLOW),
        Pair("<white>", ChatColor.WHITE),
        Pair("<magic>", ChatColor.MAGIC),
        Pair("<bold>", ChatColor.BOLD),
        Pair("<strike_through>", ChatColor.STRIKETHROUGH),
        Pair("<under_line>", ChatColor.UNDERLINE),
        Pair("<italic>", ChatColor.ITALIC),
        Pair("<reset>", ChatColor.RESET)
    )

    private fun replaceChatColor(message: String): String {
        var msg: String = message
        for(color in listColorMap.keys) {
            msg = message.replace(color, "${listColorMap[color]}")
        }
        return msg
    }

    fun replaceChatFormat(
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
                    for (color in listColorMap.keys) {
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

    private fun replacePlayer(player: Player, formatter: String): String {
        return formatter.replace("<player>", player.name)
            .replace("<address>", player.address?.address!!.hostAddress)
            .replace("<exp>", "${player.exp}")
            .replace("<level>", "${player.level}")
    }

    @Suppress("DEPRECATION")
    fun replaceMsgFormat(event: AsyncPlayerChatEvent, formatter: String): String {
        val player: String = replacePlayer(event.player, formatter)
        val message = event.message
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
        return player.replace("<message>", event.deathMessage.toString())
    }

    fun replaceAdvancementFormat(event: PlayerAdvancementDoneEvent, formatter: String): String {
        val player: String = replacePlayer(event.player, formatter)

        val advancement: String = event.advancement.key.key
        return player.replace("<advancement>", advancement.replace("adventure/", ""))
    }
}