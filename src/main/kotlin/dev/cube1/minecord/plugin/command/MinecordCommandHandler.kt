package dev.cube1.minecord.plugin.command

import dev.cube1.minecord.plugin.CorePlugin
import dev.cube1.minecord.plugin.Minecord
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData

abstract class MinecordCommandHandler {

    val plugin: CorePlugin = CorePlugin.instance
    val jda: JDA
        get() = CorePlugin.minecord.jda

    val minecord: Minecord
        get() = CorePlugin.minecord

    abstract fun commandData(): SlashCommandData
    abstract fun handle(event: SlashCommandInteractionEvent)

}