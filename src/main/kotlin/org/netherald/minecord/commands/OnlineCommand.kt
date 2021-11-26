package org.netherald.minecord.commands

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.bukkit.Bukkit
import org.netherald.minecord.Minecord

class OnlineCommand(private val plugin: Minecord) : ListenerAdapter() {

    override fun onSlashCommand(event: SlashCommandEvent) {
        if (event.user.isBot) return
        if (event.name == "online") onlineStatus(event)
    }

    private fun onlineStatus(event: SlashCommandEvent) {
        if (event.channel.id == Minecord.instance?.config?.getString("channelId")) {
            val noMember: String? = plugin.config.getString("NoMemberMessage")
            if (plugin.config.getInt("styleOnlineCommand") == 0) {
                var memberStr = "**온라인 유저** : \n```"
                memberStr += "인원: ${Bukkit.getOnlinePlayers().size}명/ ${Bukkit.getMaxPlayers()}명\n"
                if (Bukkit.getOnlinePlayers().isNotEmpty()) {
                    for ((i, player) in Bukkit.getOnlinePlayers().withIndex()) {
                        memberStr += "$i: ${player.name}\n"
                    }
                } else {
                    memberStr += "${noMember ?: "사람이 없습니다."}\n"
                }
                memberStr += "```"

                event.reply(memberStr).queue()
            } else if (plugin.config.getInt("styleOnlineCommand") == 1) {
                val title: String? = plugin.config.getString("embedTitle")
                val color: Int = plugin.config.getInt("embedColor")
                val field1: String? = plugin.config.getString("embedField1")
                val field2: String? = plugin.config.getString("embedField2")

                val embed = EmbedBuilder().setTitle(title ?: "**온라인 유저**")
                    .setColor(color)
                    .addField(
                        field1 ?: "인원:",
                        "${Bukkit.getOnlinePlayers().size}명/ ${Bukkit.getMaxPlayers()}명",
                        false
                    )

                var memberStr = "```\n"
                if (Bukkit.getOnlinePlayers().isNotEmpty()) {
                    for ((i, player) in Bukkit.getOnlinePlayers().withIndex()) {
                        memberStr += "${i + 1} 번째: ${player.name} 월드위치: ${player.world.name}\n"
                    }
                } else {
                    memberStr += "${noMember ?: "사람이 없습니다."}\n"
                }
                memberStr += "```"

                embed.addField(field2 ?: "목록:", memberStr, false)
                    .setFooter("${event.user.name}#${event.user.discriminator}", event.user.avatarUrl)

                val result: MessageEmbed = embed.build()
                event.replyEmbeds(result).queue()
            }
        }
    }
}
