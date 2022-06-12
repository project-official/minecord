package dev.cube1.minecord

import dev.cube1.minecord.utils.FormatModule
import io.papermc.paper.event.player.AsyncChatEvent
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.ChannelType
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.EntityType
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.*

object ListenerHandler : ListenerAdapter(), Listener {

    override fun onMessageReceived(event: MessageReceivedEvent) {
        if (event.author.isBot) return
        if (event.channelType == ChannelType.PRIVATE) return
        if (event.channel.id == instance.config.getString("channel_id")) {
            val format: String = instance.config.getString("message_format")
                ?: "<<dark_purple><sender><reset>> <message>"
            val customColor: Boolean = instance.config.getBoolean("custom_color")
            val supportMarkdown: Boolean = instance.config.getBoolean("support_markdown")
            val formatModule = FormatModule()
            val msg = MiniMessage.miniMessage().deserialize(formatModule.replaceChatFormat(
                event,
                format,
                supportMarkdown,
                customColor
            ))
            Bukkit.broadcast(msg)
        }
    }

    @EventHandler
    fun onChat(event: AsyncChatEvent) {
        val formatModule = FormatModule()
        val channel = jda.getTextChannelById(instance.config.getString("channel_id")!!)
        val format: String = instance.config.getString("chat_format") ?: "**<player>**: <message>"

        channel?.sendMessage(formatModule.replaceMsgFormat(event, format))?.queue()
    }

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        if (!instance.config.getBoolean("access_enable")) return
        val formatModule = FormatModule()
        val channel = jda.getTextChannelById(instance.config.getString("channel_id")!!)
        val format: String = instance.config.getString("joinFormat")?:
        "**<player>**님이 게임에 들어왔습니다. 현재 플레이어 수: <online>/<max>명"

        if (instance.config.getBoolean("join_embed")) {
            val title: String? = instance.config.getString("join_embed_title")
            val description: String = formatModule.replaceAccessFormat(event, format, false)
            val color: Int = instance.config.getInt("join_embed_color")
            val builder = EmbedBuilder().setColor(color)
                .setAuthor(description, null, "https://crafatar.com/avatars/${event.player.uniqueId}?size=64&overlay=true")

            if (!(title == null || title == "")) {
                builder.setTitle(title)
            }

            val embed: MessageEmbed = builder.build()
            channel?.sendMessageEmbeds(embed)?.queue()
        } else {
            channel?.sendMessage(formatModule.replaceAccessFormat(event, format, false))?.queue()
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
}