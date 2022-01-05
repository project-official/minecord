package org.propercrew.minecord.discordListeners

import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.bukkit.Bukkit
import org.propercrew.minecord.Minecord
import org.propercrew.minecord.Minecord.Companion.instance

class GuildChatListener: ListenerAdapter() {

    override fun onMessageReceived(event: MessageReceivedEvent) {
        if(event.author.id == event.jda.selfUser.id) return
        if(instance?.config!!.getBoolean("console-srv") && event.channel.id == instance?.config?.getString("console-srv-channelId")) {
            if(event.message.contentRaw == "throwerr") {
                throw Exception("Test")
            } else {
                Bukkit.getScheduler().runTask(instance!!, Runnable {
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), event.message.contentRaw)
                })
            }
        }
    }
}