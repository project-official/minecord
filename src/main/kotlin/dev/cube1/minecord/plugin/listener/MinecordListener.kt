package dev.cube1.minecord.plugin.listener

import dev.cube1.minecord.plugin.CorePlugin
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.bukkit.event.Listener

abstract class MinecordListener : ListenerAdapter(), Listener {

    val plugin: CorePlugin = CorePlugin.instance
    val jda: JDA
        get() = CorePlugin.minecord.jda
}