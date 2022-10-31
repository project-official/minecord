package dev.cube1.minecord.plugin.util.command

import dev.cube1.minecord.plugin.Config.format
import dev.cube1.minecord.plugin.Config.prefix
import dev.cube1.minecord.plugin.instance
import dev.cube1.minecord.plugin.jda
import dev.cube1.minecord.plugin.util.command.model.CommandHandler
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

object CommandManager : ListenerAdapter() {
    val commands = mutableListOf<CommandHandler>()

    fun registerData() {
        for (i in commands) {
            instance.logger.info("$prefix Register ${i.data.name} command")
            jda.upsertCommand(i.data).queue()
        }

        instance.logger.info("Command register complete")
    }

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        for (i in commands) {
            if (event.interaction.name == i.data.name) {
                try {
                    i.execute(event)
                } catch (exception: Exception) {
                    exception.printStackTrace()
                    return event.replyEmbeds(EmbedBuilder().apply {
                        setTitle("⚠️ **Error**")
                        setDescription(format.error)
                        setColor(0xDD0000)
                    }.build()).queue()
                }
            }
        }
    }
}