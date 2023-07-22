package dev.cube1.minecord.plugin.listener

import dev.cube1.minecord.plugin.Config
import dev.cube1.minecord.plugin.util.AvatarApi
import dev.cube1.minecord.plugin.util.PlaceholderBuilder
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

    val discordChannel by  lazy { jda.getTextChannelById(Config.Discord.chat_id) }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val placeholder = PlaceholderBuilder().apply {
            useMaxPlayerTag()
            useCurrentPlayerTag()
            useUserTag(event.player)
        }.build()

        discordChannel?.manager?.setTopic("서버 인원 : ${plugin.server.onlinePlayers.size}/${plugin.server.maxPlayers}")?.queue()

        event.joinMessage(placeholder.deserialize(Config.Format.user_join))


        val embed = EmbedBuilder()
            .setAuthor(
                "${event.player.name}님이 게임에 들어왔습니다. 현재 플레이어 수: ${plugin.server.onlinePlayers.size}/${plugin.server.maxPlayers}명",
                null,
                AvatarApi.simpleAvatar(event.player)
            )
            .setColor(Config.Color.join)
            .build()

        minecord.webhookClient.send(embed)
    }

    /**
     * Player Quit and Player Ban
     */
    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        discordChannel?.manager?.setTopic("서버 인원 : ${plugin.server.onlinePlayers.size-1}/${plugin.server.maxPlayers}")?.queue()

        val placeholder = PlaceholderBuilder().apply {
            useUserTag(event.player)
            useMaxPlayerTag()
            useCurrentPlayerTag(isQuit = true)
        }.build()

         if (event.player.isBanned) {
            val message = Component.text()
                .append(event.player.name())
                .append(Component.text("님이 차단되셨어요"))
                .build()

             event.quitMessage(message)


            val embed = EmbedBuilder()
                .setAuthor(PlainTextComponentSerializer.plainText().serialize(message), null, AvatarApi.simpleAvatar(event.player))
                .setColor(Config.Color.default)
                .build()

            minecord.webhookClient.send(embed)
            return
        }

        event.quitMessage(
            placeholder.deserialize(Config.Format.user_quit)
        )


        val embed = EmbedBuilder()
            .setAuthor(
                "${event.player.name}님이 게임에서 나갔습니다. 현재 플레이어 수: ${plugin.server.onlinePlayers.size-1}/${plugin.server.maxPlayers}명",
                null,
                "https://crafatar.com/avatars/${event.player.uniqueId}?size=64&overlay=true"
            )
            .setColor(Config.Color.quit)
            .build()

        minecord.webhookClient.send(embed)
    }

    @EventHandler
    fun onPlayerAdvancementComplete(event: PlayerAdvancementDoneEvent) {
        if (event.advancement.key.key.contains("recipes/")) return

        val advancement = PlainTextComponentSerializer.plainText().serialize(event.advancement.display!!.title())
        val embed = EmbedBuilder()
            .setAuthor("${event.player.name}님이 ${advancement}를 클리어 하셨어요", null,  AvatarApi.simpleAvatar(event.player))
            .setColor(Config.Color.default)
            .build()

        minecord.webhookClient.send(embed)
    }

    @EventHandler
    fun onPlayerKicked(event: PlayerKickEvent) {
        val message = Component.text()
            .append(event.player.name())
            .append(Component.text("님이 추방되셨어요"))
            .build()

        event.leaveMessage(message)


        val embed = EmbedBuilder()
            .setAuthor(PlainTextComponentSerializer.plainText().serialize(message), null, AvatarApi.simpleAvatar(event.player))
            .setColor(Config.Color.default)
            .build()

        minecord.webhookClient.send(embed)
    }
}