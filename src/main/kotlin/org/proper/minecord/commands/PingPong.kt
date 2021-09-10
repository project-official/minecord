package org.proper.minecord.commands

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.proper.minecord.Minecord

class PingPong: ListenerAdapter() {

    override fun onSlashCommand(event: SlashCommandEvent) {
        if (event.user.isBot) {
            return
        }

        if (event.channel.id != Minecord.instance?.config?.getString("channelId")) {
            return
        }

        when (event.name) {
            "ping" -> ping(event)
            "pong" -> pong(event)
        }
    }

    private fun ping(event: SlashCommandEvent) {
        val embed = EmbedBuilder()
            .setTitle("**:ping_pong: Pong!**")
            .setDescription("${event.jda.gatewayPing}ms")
            .setFooter("${event.user.name}#${event.user.discriminator}", event.user.avatarUrl)

        event.replyEmbeds(embed.build()).queue()
    }

    private fun pong(event: SlashCommandEvent) {
        val embed = EmbedBuilder()
            .setTitle("**:ping_pong: Ping!**")
            .setDescription("${event.jda.gatewayPing}ms")
            .setFooter("${event.user.name}#${event.user.discriminator}", event.user.avatarUrl)

        event.replyEmbeds(embed.build()).queue()
    }
}
