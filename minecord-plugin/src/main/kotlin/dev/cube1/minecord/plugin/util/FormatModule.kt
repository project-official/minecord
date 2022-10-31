package dev.cube1.minecord.plugin.util

import dev.cube1.minecord.plugin.instance
import io.papermc.paper.event.player.AsyncChatEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerEvent

class FormatModule(private val context: String) {
    private val color = mutableMapOf(
        "{black}" to ChatColor.BLACK,
        "{blue}" to ChatColor.BLUE,
        "{dark_green}" to ChatColor.DARK_GREEN,
        "{dark_aqua}" to ChatColor.DARK_AQUA,
        "{dark_red}" to ChatColor.DARK_RED,
        "{dark_purple}" to ChatColor.DARK_PURPLE,
        "{gold}" to ChatColor.GOLD,
        "{gray}" to ChatColor.GRAY,
        "{dark_gray}" to ChatColor.DARK_GRAY,
        "{blue}" to ChatColor.BLUE,
        "{green}" to ChatColor.GREEN,
        "{aqua}" to ChatColor.AQUA,
        "{red}" to ChatColor.RED,
        "{light_purple}" to ChatColor.LIGHT_PURPLE,
        "{yellow}" to ChatColor.YELLOW,
        "{white}" to ChatColor.WHITE,
        "{magic}" to ChatColor.MAGIC,
        "{bold}" to ChatColor.BOLD,
        "{strike_through}" to ChatColor.STRIKETHROUGH,
        "{under_line}" to ChatColor.UNDERLINE,
        "{italic}" to ChatColor.ITALIC,
        "{reset}" to ChatColor.RESET
    )

    private fun colorFormat(str: String): String {
        var msg = str
        for (c in color.keys) {
            msg = msg.replace(c, "${color[c]}")
        }
        return msg
    }

    private fun markdown(str: String): String {
        var setBold = false
        var setItalic = false
        var setUnderline = false
        var setStrikethrough = false
        var md = ""

        var index = 0
        while (index <= str.length - 1) {
            val value: String = str.slice(index until str.length)
            when {
                value.startsWith("**") -> {
                    if (setBold) {
                        setBold = false
                        md += "${ChatColor.RESET}"
                    } else {
                        setBold = true
                        md += "${ChatColor.BOLD}"
                    }

                    index += 2
                }
                value.startsWith("*") -> {
                    if (setItalic) {
                        setItalic = false
                        md += "${ChatColor.RESET}"
                    } else {
                        setItalic = true
                        md += "${ChatColor.ITALIC}"
                    }
                    index += 1
                }
                value.startsWith("~~") -> {
                    if (setStrikethrough) {
                        setStrikethrough = false
                        md += "${ChatColor.RESET}"
                    } else {
                        setStrikethrough = true
                        md += "${ChatColor.STRIKETHROUGH}"
                    }
                    index += 2
                }
                value.startsWith("__") -> {
                    if (setUnderline) {
                        setUnderline = false
                        md += "${ChatColor.RESET}"
                    } else {
                        setUnderline = true
                        md += "${ChatColor.UNDERLINE}"
                    }
                    index += 2
                }
            }
        }

        return md
    }

    private fun Player.format(str: String): String {
        return str
            .replace("{user}", name)
            .replace("{exp}", exp.toString())
            .replace("{level}", level.toString())
    }

    fun mcChat(event: MessageReceivedEvent): String = colorFormat(context).replace(
        "{user}",
        event.author.name
    ).replace("{context}", markdown(colorFormat(event.message.contentRaw)))

    fun discordChat(event: AsyncChatEvent): String = event.player.format(context).let {
        val msg = PlainTextComponentSerializer.plainText().serialize(event.message())
        it.replace("{context}", msg)
    }

    fun access(event: PlayerEvent, leave: Boolean): String {
        return event.player.format(context).let {
            if (!leave) {
                return@let it.replace("{online}", instance.server.onlinePlayers.toString())
                    .replace("{max}", instance.server.maxPlayers.toString())
            }

            it.replace("{online}", (instance.server.onlinePlayers - 1).toString())
                .replace("{max}", instance.server.maxPlayers.toString())
        }
    }
}