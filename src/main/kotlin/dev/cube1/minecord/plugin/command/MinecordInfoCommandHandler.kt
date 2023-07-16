package dev.cube1.minecord.plugin.command

import net.dv8tion.jda.api.JDAInfo
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.build.Commands
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData
import java.lang.management.ManagementFactory

class MinecordInfoCommandHandler: MinecordCommandHandler() {

    override fun commandData(): SlashCommandData =
        Commands.slash("info", "마인코드 정보에요")


    @Suppress("UnstableApiUsage")
    override fun handle(event: SlashCommandInteractionEvent) {
        val rb = ManagementFactory.getRuntimeMXBean()
        val r = Runtime.getRuntime()

        val info = """
            MineCord v${plugin.pluginMeta.version}, JDA `v${JDAInfo.VERSION}`,
            `Java ${System.getProperty("java.version")}` and `Kotlin ${KotlinVersion.CURRENT}`
            Bukkit Version `${plugin.server.bukkitVersion}`
            System on `${System.getProperty("os.name")}`
            
            Process Started on <t:${(System.currentTimeMillis() - rb.uptime)/ 1000L}:R>
            Bukkit Process Running on PID `${rb.pid}`
            Assigned `${r.maxMemory() / 1048576}MB` of Max Memories at This Bukkit
            Using `${(r.totalMemory() - r.freeMemory()) / 1048576}MB` at This Bukkit
            
            `${plugin.server.onlinePlayers.size}` Players Online at Server
            Average websocket latency: `${event.jda.gatewayPing}ms`
        """.trimIndent()

        event.reply(info).queue()
    }
}