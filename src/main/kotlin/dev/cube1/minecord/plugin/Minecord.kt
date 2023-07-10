package dev.cube1.minecord.plugin

import dev.cube1.minecord.plugin.Config.settings
import dev.cube1.minecord.plugin.listener.Command
import dev.cube1.minecord.plugin.util.command.CommandManager
import dev.cube1.minecord.plugin.util.command.model.CommandHandler
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.requests.GatewayIntent
import org.bukkit.Bukkit

class Minecord(token: String) {
    private val builder = JDABuilder.createLight(token, listOf(
        GatewayIntent.GUILD_PRESENCES,
        GatewayIntent.GUILD_MEMBERS,
        GatewayIntent.GUILD_MESSAGES,
        GatewayIntent.DIRECT_MESSAGES,
        GatewayIntent.MESSAGE_CONTENT
    ))

    fun addListener(listener: Any) {
        builder.addEventListeners(listener)
    }

    fun addCommand(command: CommandHandler) {
        CommandManager.commands += command
    }

    fun reloadModule() {
        jda.shutdown()
        jda = builder.build()
    }

    fun build() {
        builder.addEventListeners(Command)
        builder.setActivity(Activity.playing(settings.activity.context))
        jda = builder.build()
        Bukkit.getScheduler().runTaskAsynchronously(instance, Runnable {
            CommandManager.registerData()
        })
    }
}