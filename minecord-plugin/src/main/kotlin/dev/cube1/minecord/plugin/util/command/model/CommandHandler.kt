package dev.cube1.minecord.plugin.util.command.model

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.build.CommandData

interface CommandHandler {
    var data: CommandData
    fun execute(event: SlashCommandInteractionEvent)
}