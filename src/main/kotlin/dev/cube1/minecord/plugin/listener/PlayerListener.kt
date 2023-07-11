package dev.cube1.minecord.plugin.listener

import dev.cube1.minecord.plugin.Config
import net.dv8tion.jda.api.EmbedBuilder
import net.kyori.adventure.text.Component
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class PlayerListener : MinecordListener() {

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        // TODO Need Update

        if (Config.settings.custom_message) {
            event.joinMessage(
                Component.text(
                    Config.format.player.join.replace("{user}", event.player.name)
                )
            )
        }

        val embed = EmbedBuilder()
            .setAuthor(
                "${event.player.name}님이 게임에 들어왔습니다. 현재 플레이어 수: ${plugin.server.onlinePlayers.size}/${plugin.server.maxPlayers}명",
                null,
                "${Config.render}/avatars/${event.player.uniqueId}?size=64&overlay=true"
            )
            .setColor(Config.color.join)
            .build()

        minecord.webhookClient.send(embed)
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        // TODO Need Update

        if (Config.settings.custom_message) {
            event.quitMessage(
                Component.text(
                    Config.format.player.quit.replace("{user}", event.player.name)
                )
            )
        }

        val embed = EmbedBuilder()
            .setAuthor(
                "${event.player.name}님이 게임에서 나갔습니다. 현재 플레이어 수: ${plugin.server.onlinePlayers.size}/${plugin.server.maxPlayers}명",
                null,
                "${Config.render}/avatars/${event.player.uniqueId}?size=64&overlay=true"
            )
            .setColor(Config.color.quit)
            .build()

        minecord.webhookClient.send(embed)
    }
}