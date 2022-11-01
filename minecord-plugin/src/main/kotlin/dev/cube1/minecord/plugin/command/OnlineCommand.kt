package dev.cube1.minecord.plugin.command

import dev.cube1.minecord.plugin.Config.format
import dev.cube1.minecord.plugin.Config.settings
import dev.cube1.minecord.plugin.util.command.model.CommandHandler
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import net.dv8tion.jda.internal.interactions.CommandDataImpl
import org.bukkit.Bukkit

object OnlineCommand : CommandHandler {

    override var data: CommandData = CommandData.fromData(CommandDataImpl(
        "online",
        "온라인 커맨드 입니다."
    ).toData())

    override fun execute(event: SlashCommandInteractionEvent) {
        val current = Bukkit.getOnlinePlayers()
        val status = "`${current}/${Bukkit.getMaxPlayers()}명`"
        fun online(): String {
            var str = "```\n"
            if (current.isNotEmpty()) {
                for ((i, j) in current.withIndex()) {
                    str += "${i + 1}: ${j.name}\n"
                }
            } else {
                str += "${format.online}\n"
            }
            str += "```\n"

            return str
        }

        when (settings.style) {
            0 -> {
                var msg = "**온라인 유저**:\n"
                msg += "$status\n"
                msg += online()

                event.reply(msg).queue()
            }

            1 -> {
                val embed = EmbedBuilder().apply {
                    setTitle("\uD83D\uDDD2️ **온라인 유저 리스트**")
                    addField("**인원**", status, false)
                    addField("**플레이어**", online(), false)
                    setFooter(event.user.asTag, event.user.avatarUrl ?: event.user.defaultAvatarUrl)
                }.build()

                event.replyEmbeds(embed).queue()
            }
        }
    }
}
