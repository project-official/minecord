package dev.cube1.minecord.plugin.listener

import dev.cube1.minecord.plugin.Config
import dev.cube1.minecord.plugin.Config.discord
import dev.cube1.minecord.plugin.Config.format
import dev.cube1.minecord.plugin.Config.settings
import dev.cube1.minecord.plugin.instance
import dev.cube1.minecord.plugin.jda
import dev.cube1.minecord.plugin.util.FormatModule
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.MessageEmbed
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import org.bukkit.entity.EntityType
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerAdvancementDoneEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerKickEvent
import org.bukkit.event.player.PlayerQuitEvent

object Player : Listener {
    private fun indicator(char: Char, color: Int): Component {
        return Component.text("[", NamedTextColor.GOLD).append(Component.text(
            char,
            TextColor.color(color)
        ).append(Component.text("] ", NamedTextColor.GOLD)))
    }

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        val fmt = FormatModule(format.player.join).access(event, leave = false)
        val chan = jda.getTextChannelById(discord.channels.chat_id)!!
        event.joinMessage(indicator('+', Config.color.join).append(Component.text(
            event.player.name,
            NamedTextColor.YELLOW
        )))

        if (settings.style == 1) {
            return
        }

        chan.sendMessage(fmt).queue()
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        if (!instance.config.getBoolean("access_enable")) return
        val formatModule = FormatModule()
        val channel = jda.getTextChannelById(instance.config.getString("channel_id")!!)
        val format: String = instance.config.getString("leave_format")?:
        "**<player>**님이 게임에서 나갔습니다. 현재 플레이어 수: <online>/<max>명"

        if (instance.config.getBoolean("leave_embed")) {
            val title: String? = instance.config.getString("leave_embed_title")
            val description: String = formatModule.replaceAccessFormat(event, format, true)
            val color: Int = instance.config.getInt("leave_embed_color")
            val builder = EmbedBuilder().setColor(color).setAuthor(
                description,
                null,
                "https://crafatar.com/avatars/${event.player.uniqueId}?size=64&overlay=true"
            )

            if (!(title == null || title == "")) {
                builder.setTitle(title)
            }

            val embed: MessageEmbed = builder.build()
            channel?.sendMessageEmbeds(embed)?.queue()
        } else {
            channel?.sendMessage(formatModule.replaceAccessFormat(event, format, true))?.queue()
        }
    }

    @EventHandler
    fun onPlayerDeath(event: PlayerDeathEvent) {
        if (event.entityType != EntityType.PLAYER) return
        if (!instance.config.getBoolean("death_enable")) return
        val formatModule = FormatModule()

        val channel = jda.getTextChannelById(instance.config.getString("channel_id")!!)
        val format: String = instance.config.getString("death_format")?: "**<player>님이 사망 하셨습니다.**"

        if (instance.config.getBoolean("death_embed")) {
            val title: String? = instance.config.getString("death_embed_title")
            val description: String = formatModule.replaceDeathFormat(event, format)
            val color: Int = instance.config.getInt("death_embed_color")
            val builder = EmbedBuilder().setColor(color)
                .setAuthor(description, null, "https://crafatar.com/avatars/${event.entity.uniqueId}?size=64&overlay=true")

            if (!(title == null || title == "")) {
                builder.setTitle(title)
            }

            val embed: MessageEmbed = builder.build()
            channel?.sendMessageEmbeds(embed)?.queue()
        } else {
            channel?.sendMessage(formatModule.replaceDeathFormat(event, format))?.queue()
        }
    }

    @EventHandler
    fun onPlayerAdvancement(event: PlayerAdvancementDoneEvent) {
        if (!instance.config.getBoolean("advancement_enable")) return
        if (event.advancement.key.key.contains("recipes/")) return
        val formatModule = FormatModule()
        val channel = jda.getTextChannelById(instance.config.getString("channel_id")!!)
        val format: String = instance.config.getString("advancement_format")
            ?: "**<player>님이 <advancement>를 클리어 하였습니다..**"

        if (instance.config.getBoolean("advancement_embed")) {
            val title: String? = instance.config.getString("advancement_embed_title")
            val description: String = formatModule.replaceAdvancementFormat(event, format)
            val color: Int = instance.config.getInt("advancement_embed_color")
            val builder = EmbedBuilder().setDescription(description).setColor(color).setAuthor(
                null,
                null,
                "https://crafatar.com/avatars/${event.player.uniqueId}?size=64&overlay=true"
            )

            if (!(title == null || title == "")) {
                builder.setTitle(title)
            }

            val embed: MessageEmbed = builder.build()
            channel?.sendMessageEmbeds(embed)?.queue()
        } else {
            channel?.sendMessage(formatModule.replaceAdvancementFormat(event, format))?.queue()
        }
    }

    @EventHandler
    fun onPlayerKick(event: PlayerKickEvent) {
        if (!instance.config.getBoolean("kick_enable")) return
        val formatModule = FormatModule()
        val channel = jda.getTextChannelById(instance.config.getString("channel_id")!!)
        val format: String = instance.config.getString("kickFormat")
            ?: "**<player>님이 추방 되었습니다.**"

        if (instance.config.getBoolean("deathEmbed")) {
            val title: String? = instance.config.getString("kickEmbedTitle")
            val description: String = formatModule.replaceAccessFormat(event, format, true)
            val color: Int = instance.config.getInt("kickEmbedColor")
            val builder = EmbedBuilder().setColor(color).setAuthor(
                description,
                null,
                "https://crafatar.com/avatars/${event.player.uniqueId}?size=64&overlay=true"
            )

            if (!(title == null || title == "")) {
                builder.setTitle(title)
            }

            val embed: MessageEmbed = builder.build()
            channel?.sendMessageEmbeds(embed)?.queue()
        } else {
            channel?.sendMessage(formatModule.replaceAccessFormat(event, format, true))?.queue()
        }
    }
}