package dev.cube1.minecord.plugin.listener

import dev.cube1.minecord.plugin.Config
import dev.cube1.minecord.plugin.Config.discord
import dev.cube1.minecord.plugin.jda
import dev.cube1.minecord.plugin.util.FormatModule
import io.papermc.paper.event.player.AsyncChatEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

object Chat : ListenerAdapter(), Listener {
    override fun onMessageReceived(event: MessageReceivedEvent) {
        if (event.author.isBot) return
        if (event.message.channel.id != discord.channels.chat_id) return
        val fmt = FormatModule(Config.format.chat.mc).mcChat(event)
        Bukkit.broadcast(Component.text(fmt))
    }

    @EventHandler
    fun onChat(event: AsyncChatEvent) {
        val chan = jda.getTextChannelById(discord.channels.chat_id)!!
        val fmt = FormatModule(Config.format.chat.discord).discordChat(event)

        chan.sendMessage(fmt).queue()
    }
}