package dev.cube1.minecord.plugin.util.command

import dev.cube1.minecord.plugin.Config.prefix
import dev.cube1.minecord.plugin.instance
import dev.cube1.minecord.plugin.jda
import dev.cube1.minecord.plugin.util.command.model.CommandHandler

object CommandManager {
    val commands = mutableListOf<CommandHandler>()

    fun registerData() {
        for (i in commands) {
            instance.logger.info("$prefix Register ${i.data.name} command")
            jda.upsertCommand(i.data).queue()
        }

        instance.logger.info("Command register complete")
    }
}