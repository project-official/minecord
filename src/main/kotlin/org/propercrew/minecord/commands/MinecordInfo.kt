package org.propercrew.minecord.commands

import net.dv8tion.jda.api.JDAInfo
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.bukkit.Bukkit
import org.propercrew.minecord.Minecord
import java.lang.management.ManagementFactory

class MinecordInfo: ListenerAdapter() {

    override fun onSlashCommand(event: SlashCommandEvent) {
        if (event.user.isBot) return
        if (event.name == "info") minecordInfoCommand(event)
    }

    private fun minecordInfoCommand(event: SlashCommandEvent) {
        try {
            val plugin = Minecord.instance!!
            val rb = ManagementFactory.getRuntimeMXBean()
            var info = ""
            val r = Runtime.getRuntime()
            info += "MineCord v${plugin.description.version}, JDA `v${JDAInfo.VERSION}`, \n`Java ${System.getProperty("java.version")}` and `Kotlin ${KotlinVersion.CURRENT}`\n"
            info += "Bukkit Version `${Bukkit.getServer().bukkitVersion}`\n"
            info += "System on `${System.getProperty("os.name")}`\n\n"

            info += "Process Started on <t:${(System.currentTimeMillis() - rb.uptime)/ 1000L}:R>\n"
            info += "Bukkit Process Running on PID `${rb.pid}`\n"
            info += "Assigned `${r.maxMemory() / 1048576}MB` of Max Memories at This Bukkit\n"
            info += "Using `${(r.totalMemory() - r.freeMemory()) / 1048576}MB` at This Bukkit"
            info += "\n\n"
            info += "`${plugin.server.onlinePlayers.size}` Players Online at Server\n"
            info += "\n"
            info += "Average websocket latency: `${event.jda.gatewayPing}ms`\n"

            event.reply(info).queue()
        } catch (e: Exception) {
            event.reply("An error occurred while trying to get the info").queue()
            throw e
        }
    }
}