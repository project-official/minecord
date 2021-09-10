package org.netherald.wild.discord.listeners

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.MessageEmbed
import org.bukkit.entity.EntityType
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.netherald.wild.discord.WildDiscord
import org.netherald.wild.discord.utils.FormatModule

class DeathListener(private val plugin: WildDiscord): Listener {

    @EventHandler
    fun onPlayerDeath(event: PlayerDeathEvent) {
        if (event.entityType != EntityType.PLAYER) return
        if (!plugin.config.getBoolean("deathEnable")) return
        val formatModule = FormatModule()

        val channel = WildDiscord.jda?.getTextChannelById(plugin.config.getString("channelId")!!)
        val format: String = plugin.config.getString("deathFormat")?: "**<player>님이 사망 하셨습니다.**"

        if (plugin.config.getBoolean("deathEmbed")) {
            val title: String? = plugin.config.getString("deathEmbedTitle")
            val description: String = formatModule.replaceDeathFormat(event, format)
            val color: Int = plugin.config.getInt("deathEmbedColor")
            val builder = EmbedBuilder().setDescription(description)
                .setColor(color)
                .setAuthor(null, null, "https://crafatar.com/avatars/${event.entity.uniqueId}?size=64&overlay=true")

            if (!(title == null || title == "")) {
                builder.setTitle(title)
            }

            val embed: MessageEmbed = builder.build()
            channel?.sendMessage(embed)?.queue()
        } else {
            channel?.sendMessage(formatModule.replaceDeathFormat(event, format))?.queue()
        }
    }
}