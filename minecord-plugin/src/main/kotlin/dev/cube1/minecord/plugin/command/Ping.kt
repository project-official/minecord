package dev.cube1.minecord.plugin.command

import dev.cube1.minecord.plugin.util.command.model.CommandHandler
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import net.dv8tion.jda.internal.interactions.CommandDataImpl

object Ping : CommandHandler {

    override var data: CommandData = CommandData.fromData(CommandDataImpl(
        "ping",
        "API 레이턴시를 확인할 수 있어요"
    ).toData())

    override fun execute(event: SlashCommandInteractionEvent) {
        val before = System.currentTimeMillis()
        var embed = EmbedBuilder().apply {
            setTitle("⏳ **Measuring**")
            setDescription("Just hold on sec...")
            setFooter(event.user.asTag, event.user.avatarUrl ?: event.user.defaultAvatarUrl)
            setColor(0x0D0D0D)
        }

        event.replyEmbeds(embed.build()).queue {
            val latency = System.currentTimeMillis() - before
            embed = EmbedBuilder().apply {
                setTitle("\uD83C\uDFD3 **Pong!**")
                setDescription("""
                    **BOT**: ${latency}**ms**
                    **API**: ${event.jda.gatewayPing}**ms**
                """.trimIndent())
                setFooter(event.user.asTag, event.user.avatarUrl)
            }

            it.editOriginalEmbeds(embed.build()).queue()
        }
    }
}
