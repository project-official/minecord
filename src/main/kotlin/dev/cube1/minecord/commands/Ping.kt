package dev.cube1.minecord.commands

import dev.cube1.minecord.CommandHandler
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import net.dv8tion.jda.internal.interactions.CommandDataImpl

object Ping: CommandHandler {

    override var data: CommandData = CommandData.fromData(CommandDataImpl(
        "ping",
        "API 레이턴시를 확인합니다."
    ).toData())

    override fun execute(event: SlashCommandInteractionEvent) {
        val embed = EmbedBuilder()
            .setTitle("**:ping_pong: Pong!**")
            .setDescription("${event.jda.gatewayPing}ms")
            .setFooter(event.user.asTag, event.user.avatarUrl)

        event.replyEmbeds(embed.build()).queue()
    }
}
