package dev.cube1.minecord

import dev.cube1.minecord.commands.MinecordInfo
import dev.cube1.minecord.commands.OnlineCommand
import dev.cube1.minecord.commands.Ping
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.commands.build.CommandData

val commands = mutableListOf<CommandHandler>().also {
    it += MinecordInfo
    it += OnlineCommand
    it += Ping
}

fun registerEvents(builder: JDABuilder) {
    for (i in commands) {
        class Data : ListenerAdapter() {
            override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
                if (event.user.isBot) return
                if (event.name == i.data.name) {
                    i.execute(event)
                }
            }
        }

        builder.addEventListeners(Data())
    }
}

fun registerData(jda: JDA) {
    for (i in commands) {
        jda.getGuildById("${instance.config.getString("guild_id")}")?.upsertCommand(i.data)!!.queue()
    }
}

interface CommandHandler {
    var data: CommandData
    fun execute(event: SlashCommandInteractionEvent)
}