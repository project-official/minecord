package org.netherald.minecord.listeners

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.MessageEmbed
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerAdvancementDoneEvent
import org.netherald.minecord.Minecord
import org.netherald.minecord.utils.FormatModule

class AdvancementListener(private val plugin: Minecord): Listener {

    @EventHandler
    fun onPlayerAdvancement(event: PlayerAdvancementDoneEvent) {
        if (!plugin.config.getBoolean("advancementEnable")) return
        val formatModule = FormatModule()
        val channel = Minecord.jda?.getTextChannelById(plugin.config.getString("channelId")!!)
        val format: String = plugin.config.getString("advancementFormat")?:
        "**<player>님이 <advancement>를 클리어 하였습니다..**"

        if (plugin.config.getBoolean("advancementEmbed")) {
            val title: String? = plugin.config.getString("advancementEmbedTitle")
            val description: String = formatModule.replaceAdvancementFormat(event, format)
            val color: Int = plugin.config.getInt("advancementEmbedColor")
            val builder = EmbedBuilder().setDescription(description)
                .setColor(color)
                .setAuthor(null, null, "https://crafatar.com/avatars/$%7B${event.player.uniqueId}%7D?size=64&overlay=true%22")

            if (!(title == null || title == "")) {
                builder.setTitle(title)
            }

            val embed: MessageEmbed = builder.build()
            channel?.sendMessage(embed)?.queue()
        } else {
            channel?.sendMessage(formatModule.replaceAdvancementFormat(event, format))?.queue()
        }
    }
}