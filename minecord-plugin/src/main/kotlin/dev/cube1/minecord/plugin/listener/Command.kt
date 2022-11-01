package dev.cube1.minecord.plugin.listener

import dev.cube1.minecord.plugin.Config
import dev.cube1.minecord.plugin.util.command.CommandManager
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

object Command : ListenerAdapter() {
    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        for (i in CommandManager.commands) {
            if (event.interaction.name == i.data.name) {
                try {
                    i.execute(event)
                } catch (exception: Exception) {
                    exception.printStackTrace()
                    return event.replyEmbeds(EmbedBuilder().apply {
                        setTitle("⚠ **Error**")
                        setDescription(Config.format.error)
                        setColor(0xDD0000)
                    }.build()).queue()
                }
            }
        }
    }
}