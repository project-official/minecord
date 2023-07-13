package dev.cube1.minecord.plugin.listener

import dev.cube1.minecord.plugin.Config
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerAdvancementDoneEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerKickEvent
import org.bukkit.event.player.PlayerQuitEvent

class PlayerListener : MinecordListener() {

    val discordChannel by  lazy { jda.getTextChannelById(Config.discord.channels.chat_id) }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {

        discordChannel?.manager?.setTopic("서버 인원 : ${plugin.server.onlinePlayers.size}/${plugin.server.maxPlayers}")?.queue()

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

    /**
     * Player Quit and Player Ban
     */
    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        discordChannel?.manager?.setTopic("서버 인원 : ${plugin.server.onlinePlayers.size-1}/${plugin.server.maxPlayers}")?.queue()

        // Invoking ClassCastException
        // Check https://github.com/PaperMC/Paper/issues/9455
         if (event.player.isBanned) {
            val message = Component.text()
                .append(event.player.name())
                .append(Component.text("님이 차단되셨어요"))
                .build()
            if (Config.settings.custom_message) {
                event.quitMessage(message)
            }

            val embed = EmbedBuilder()
                .setAuthor(PlainTextComponentSerializer.plainText().serialize(message), null, "${Config.render}/avatars/${event.player.uniqueId}?size=64&overlay=true")
                .setColor(Config.color.default)
                .build()

            minecord.webhookClient.send(embed)
            return
        }

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
                "${event.player.name}님이 게임에서 나갔습니다. 현재 플레이어 수: ${plugin.server.onlinePlayers.size-1}/${plugin.server.maxPlayers}명",
                null,
                "${Config.render}/avatars/${event.player.uniqueId}?size=64&overlay=true"
            )
            .setColor(Config.color.quit)
            .build()

        minecord.webhookClient.send(embed)
    }

    @EventHandler
    fun onPlayerAdvancementComplete(event: PlayerAdvancementDoneEvent) {
        if (event.advancement.key.key.contains("recipes/")) return

        val advancement = PlainTextComponentSerializer.plainText().serialize(event.advancement.display!!.title())
        val embed = EmbedBuilder()
            .setAuthor("${event.player.name}님이 ${advancement}를 클리어 하셨어요", null,  "${Config.render}/avatars/${event.player.uniqueId}?size=64&overlay=true")
            .setColor(Config.color.default)
            .build()

        minecord.webhookClient.send(embed)
    }

    @EventHandler
    fun onPlayerKicked(event: PlayerKickEvent) {
        val message = Component.text()
            .append(event.player.name())
            .append(Component.text("님이 추방되셨어요"))
            .build()
        if (Config.settings.custom_message) {
            event.leaveMessage(message)
        }

        val embed = EmbedBuilder()
            .setAuthor(PlainTextComponentSerializer.plainText().serialize(message), null, "${Config.render}/avatars/${event.player.uniqueId}?size=64&overlay=true")
            .setColor(Config.color.default)
            .build()

        minecord.webhookClient.send(embed)
    }
}