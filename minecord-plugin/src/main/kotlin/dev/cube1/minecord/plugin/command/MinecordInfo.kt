package dev.cube1.minecord.plugin.command

import dev.cube1.minecord.plugin.util.command.model.CommandHandler
import net.dv8tion.jda.api.JDAInfo
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import net.dv8tion.jda.internal.interactions.CommandDataImpl
import org.bukkit.Bukkit
import dev.cube1.minecord.plugin.instance
import java.lang.management.ManagementFactory

object MinecordInfo : CommandHandler {

    override var data: CommandData = CommandData.fromData(CommandDataImpl(
        "info",
        "마인코드 정보예요"
    ).toData())

    override fun execute(event: SlashCommandInteractionEvent) {
        try {
            val rb = ManagementFactory.getRuntimeMXBean()
            val r = Runtime.getRuntime()

            val info = """
                MineCord v${instance.description.version}, JDA `v${JDAInfo.VERSION}`,
                `Java ${System.getProperty("java.version")}` and `Kotlin ${KotlinVersion.CURRENT}`
                Bukkit Version `${Bukkit.getServer().bukkitVersion}`
                System on `${System.getProperty("os.name")}`
                
                Process Started on <t:${(System.currentTimeMillis() - rb.uptime)/ 1000L}:R>
                Bukkit Process Running on PID `${rb.pid}`
                Assigned `${r.maxMemory() / 1048576}MB` of Max Memories at This Bukkit
                Using `${(r.totalMemory() - r.freeMemory()) / 1048576}MB` at This Bukkit
                
                `${instance.server.onlinePlayers.size}` Players Online at Server
                Average websocket latency: `${event.jda.gatewayPing}ms`
            """.trimIndent()

            event.reply(info).queue()
        } catch (e: Exception) {
            throw e
        }
    }
}