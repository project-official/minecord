package org.netherald.wild.discord.utils

import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.event.player.PlayerAdvancementDoneEvent
import org.bukkit.event.player.PlayerEvent

class FormatModule {

    private val listColor: List<String> = listOf(
        "<black>",
        "<blue>",
        "<dark_green>",
        "<dark_aqua>",
        "<dark_red>",
        "<dark_purple>",
        "<gold>",
        "<gray>",
        "<dark_gray>",
        "<blue>",
        "<green>",
        "<aqua>",
        "<red>",
        "<light_purple>",
        "<yellow>",
        "<white>",
        "<magic>",
        "<bold>",
        "<strike_through>",
        "<under_line>",
        "<italic>",
        "<reset>"
    )

    private fun replaceChatColor(message: String): String {
        return message.replace("<black>", "${ChatColor.BLACK}")
            .replace("<blue>", "${ChatColor.DARK_BLUE}")
            .replace("<dark_green>", "${ChatColor.DARK_GREEN}")
            .replace("<dark_aqua>", "${ChatColor.DARK_AQUA}")
            .replace("<dark_red>", "${ChatColor.DARK_RED}")
            .replace("<dark_purple>", "${ChatColor.DARK_PURPLE}")
            .replace("<gold>", "${ChatColor.GOLD}")
            .replace("<gray>", "${ChatColor.GRAY}")
            .replace("<dark_gray>", "${ChatColor.DARK_GRAY}")
            .replace("<blue>", "${ChatColor.BLUE}")
            .replace("<green>", "${ChatColor.GREEN}")
            .replace("<aqua>", "${ChatColor.AQUA}")
            .replace("<red>", "${ChatColor.RED}")
            .replace("<light_purple>", "${ChatColor.LIGHT_PURPLE}")
            .replace("<yellow>", "${ChatColor.YELLOW}")
            .replace("<white>", "${ChatColor.WHITE}")
            .replace("<magic>", "${ChatColor.MAGIC}")
            .replace("<bold>", "${ChatColor.BOLD}")
            .replace("<strike_through>", "${ChatColor.STRIKETHROUGH}")
            .replace("<under_line>", "${ChatColor.UNDERLINE}")
            .replace("<italic>", "${ChatColor.ITALIC}")
            .replace("<reset>", "${ChatColor.RESET}")
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

    private fun replacePlayer(player: Player, formatter: String): String {
        return formatter.replace("<player>", player.name)
            .replace("<address>", player.address?.address!!.hostAddress)
            .replace("<exp>", "${player.exp}")
            .replace("<level>", "${player.level}")
    }

    fun replaceMsgFormat(event: AsyncPlayerChatEvent, formatter: String): String {
        val player: String = replacePlayer(event.player, formatter)
        val message: String = event.message
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
        return player.replace("<message>", "${event.deathMessage}")

    }

    fun replaceAdvancementFormat(event: PlayerAdvancementDoneEvent, formatter: String): String {
        val player: String = replacePlayer(event.player, formatter)

        val advancement: String = event.advancement.key.key
        return player.replace("<advancement>", advancement.replace("adventure/", ""))
    }
}