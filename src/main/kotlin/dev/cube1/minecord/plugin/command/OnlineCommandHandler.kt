package dev.cube1.minecord.plugin.command

import dev.cube1.minecord.plugin.Config
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.build.Commands
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData

class OnlineCommandHandler : MinecordCommandHandler() {

    override fun commandData(): SlashCommandData = Commands.slash("online", "현재 접속해 있는 유저의 리스트를 확인해볼 수 있어요")
    override fun handle(event: SlashCommandInteractionEvent) {
        if (event.channel.id != Config.discord.channels.chat_id) {
            return event.reply(Config.format.channel).setEphemeral(true).queue()
        }

        val onlinePlayers = plugin.server.onlinePlayers

        val embed = EmbedBuilder().apply {
            setTitle("\uD83D\uDDD2️ **온라인 유저 리스트**")
            addField("**인원**", "${onlinePlayers.size}/${plugin.server.maxPlayers}", false)
            addField("**플레이어**", onlinePlayers.joinToString(prefix = "```* ", postfix = "```", separator = "\n* ") { it.name }, false)
            setFooter(event.user.globalName, event.user.avatarUrl ?: event.user.defaultAvatarUrl)
            setColor(Config.color.default)
        }.build()

        event.replyEmbeds(embed).queue()
    }
}