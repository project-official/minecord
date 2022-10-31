package dev.cube1.minecord.plugin

import dev.cube1.minecord.plugin.Config.settings
import dev.cube1.minecord.plugin.util.command.CommandManager
import dev.cube1.minecord.plugin.util.command.model.CommandHandler
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity
import org.bukkit.Bukkit

lateinit var jda: JDA

class Minecord(token: String) {
    private val builder = JDABuilder.createDefault(token)

    fun addListeners(vararg listeners: Any) {
        builder.addEventListeners(listeners)
    }

    fun addCommand(command: CommandHandler) {
        CommandManager.commands += command
    }

    fun dropCommand(command: CommandHandler) {
        CommandManager.commands -= command
    }

    init {
        builder.addEventListeners(CommandManager)
        builder.setActivity(Activity.playing(settings.activity.context))
    }

    fun build() {
        jda = builder.build()
        Bukkit.getScheduler().runTaskAsynchronously(instance, Runnable {
            CommandManager.registerData()
        })
    }
}