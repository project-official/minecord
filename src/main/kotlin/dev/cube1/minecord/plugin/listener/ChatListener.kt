package dev.cube1.minecord.plugin.listener

import club.minnced.discord.webhook.WebhookClientBuilder
import club.minnced.discord.webhook.send.WebhookMessageBuilder
import dev.cube1.minecord.plugin.Config
import dev.vankka.mcdiscordreserializer.discord.DiscordSerializer
import dev.vankka.mcdiscordreserializer.minecraft.MinecraftSerializer
import io.papermc.paper.event.player.AsyncChatEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import org.bukkit.event.EventHandler

class ChatListener : MinecordListener() {

    override fun onMessageReceived(event: MessageReceivedEvent) {
        if (event.author.globalName == null) return
        if (event.channel.id != Config.discord.channels.chat_id) return

        val message = MinecraftSerializer.INSTANCE.serialize(event.message.contentDisplay)
        val fullMessage = Component.text()
            .append(Component.text(event.author.globalName!!))
            .append(Component.text(": "))
            .append(message)
            .build()

        plugin.server.broadcast(fullMessage)
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