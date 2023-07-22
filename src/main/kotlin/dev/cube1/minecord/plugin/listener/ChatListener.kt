package dev.cube1.minecord.plugin.listener

import club.minnced.discord.webhook.WebhookClientBuilder
import club.minnced.discord.webhook.send.WebhookMessageBuilder
import dev.cube1.minecord.plugin.Config
import dev.cube1.minecord.plugin.util.PlaceholderBuilder
import dev.vankka.mcdiscordreserializer.discord.DiscordSerializer
import dev.vankka.mcdiscordreserializer.minecraft.MinecraftSerializer
import io.papermc.paper.event.player.AsyncChatEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import org.bukkit.event.EventHandler
import java.lang.IllegalStateException

class ChatListener : MinecordListener() {

    override fun onMessageReceived(event: MessageReceivedEvent) {
        try {
            if (event.author.globalName == null || event.channel.id != Config.Discord.chat_id) return

            val placeholder = PlaceholderBuilder().apply {
                useUserTag(event.author)
                useMessageTag(event.message)
            }.build()

            val message = placeholder.deserialize(Config.Format.discord_user_chat)

            plugin.server.broadcast(message)
        } catch (ignore: IllegalStateException) {}

    }

    @EventHandler
    fun onMinecraftChat(event: AsyncChatEvent) {
        val username = (event.player.name() as TextComponent).content()
        val avatar = "https://mc-heads.net/avatar/${event.player.uniqueId}"
        val message = DiscordSerializer.INSTANCE.serialize(event.message())

        val discordMessage = WebhookMessageBuilder().apply {
            setUsername(username)
            setAvatarUrl(avatar)
            setContent(message)
        }.build()

        minecord.webhookClient.send(discordMessage)
    }

}