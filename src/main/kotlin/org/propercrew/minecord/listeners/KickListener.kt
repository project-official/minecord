package org.propercrew.minecord.listeners

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.MessageEmbed
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerKickEvent
import org.propercrew.minecord.Minecord
import org.propercrew.minecord.utils.FormatModule

class KickListener: Listener {

    @EventHandler
    fun onPlayerKick(event: PlayerKickEvent) {
        val plugin = Minecord.instance!!

        if (!plugin.config.getBoolean("kickEnable")) return
        val formatModule = FormatModule()
        val channel = Minecord.jda?.getTextChannelById(plugin.config.getString("channelId")!!)
        val format: String = plugin.config.getString("kickFormat")?:
        "**<player>님이 추방 되었습니다.**"

        if (plugin.config.getBoolean("deathEmbed")) {
            val title: String? = plugin.config.getString("kickEmbedTitle")
            val description: String = formatModule.replaceAccessFormat(event, format, true)
            val color: Int = plugin.config.getInt("kickEmbedColor")
            val builder = EmbedBuilder().setColor(color)
                .setAuthor(description, null, "https://crafatar.com/avatars/${event.player.uniqueId}?size=64&overlay=true")

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