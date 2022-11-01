package dev.cube1.minecord.plugin.listener

import dev.cube1.minecord.plugin.Config.discord
import dev.cube1.minecord.plugin.Config.format
import dev.cube1.minecord.plugin.Config.settings
import dev.cube1.minecord.plugin.Config.color
import dev.cube1.minecord.plugin.Config.render
import dev.cube1.minecord.plugin.jda
import dev.cube1.minecord.plugin.util.FormatModule
import net.dv8tion.jda.api.EmbedBuilder
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.*

object Player : Listener {
    private val embed = EmbedBuilder()

    private fun indicator(char: Char, color: Int): Component {
        return Component.text("[", NamedTextColor.GOLD).append(Component.text(
            char,
            TextColor.color(color)
        ).append(Component.text("] ", NamedTextColor.GOLD)))
    }

    private fun style(event: Any, fmt: String, color: Int) {
        val player = when (event) {
            is PlayerEvent -> event.player
            is PlayerDeathEvent -> event.player
            else -> throw IllegalStateException()
        }

        val chan = jda.getTextChannelById(discord.channels.chat_id)!!
        if (settings.style == 1) {
            return chan.sendMessageEmbeds(embed.apply {
                setAuthor(
                    fmt,
                    null,
                    "$render/avatars/${player.uniqueId}?size=64&overlay=true"
                )
                setColor(color)
            }.build()).queue()
        }

        chan.sendMessage(fmt).queue()
    }

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        val fmt = FormatModule(format.player.join).access(event, leave = false)
        if (settings.custom_message) {
            event.joinMessage(indicator(char = '+', color.join).append(Component.text(
                event.player.name,
                NamedTextColor.YELLOW
            )))
        }

        style(event, fmt, color.join)
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        val fmt = FormatModule(format.player.quit).access(event, leave = true)
        if (settings.custom_message) {
            event.quitMessage(indicator(char = '-', color.quit).append(Component.text(
                event.player.name,
                NamedTextColor.YELLOW
            )))
        }

        style(event, fmt, color.quit)
    }

    @EventHandler
    fun onPlayerDeath(event: PlayerDeathEvent) {
        val fmt = FormatModule(format.player.death).death(event)
        if (settings.custom_message) {
            event.deathMessage(indicator(char = 'X', color.death).append(Component.text(
                event.player.name,
                NamedTextColor.YELLOW
            )))
        }

        style(event, fmt, color.death)
    }

    @EventHandler
    fun onPlayerAdvancement(event: PlayerAdvancementDoneEvent) {
        if (event.advancement.key.key.contains("recipes/")) return
        val fmt = FormatModule(format.player.advancement).advancement(event)
        style(event, fmt, color.default)
    }

    @EventHandler
    fun onPlayerKick(event: PlayerKickEvent) {
        val fmt = FormatModule(format.player.kick).kick(event)
        if (settings.custom_message) {
            event.leaveMessage(indicator(char = 'K', color.kick).append(Component.text(
                event.player.name,
                NamedTextColor.YELLOW
            )))
        }

        style(event, fmt, color.kick)
    }
}