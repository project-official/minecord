package org.netherald.minecord.commands

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.plugin.java.JavaPlugin
import org.netherald.minecord.Minecord

class ReloadCommand(private val plugin: JavaPlugin) : ListenerAdapter() {

    override fun onSlashCommand(event: SlashCommandEvent) {
        if (event.user.isBot) {
            return
        }

        if (event.channel.id != Minecord.instance?.config?.getString("channelId")) {
            return
        }

        if (event.name == "reload") {
            if (!event.member!!.isOwner) {
                val errorEmbed = EmbedBuilder()
                    .setTitle("**:warning: Error!**")
                    .setDescription("Permission denied!")
                    .setFooter("${event.user.name}#${event.user.discriminator}", event.user.avatarUrl)

                event.replyEmbeds(errorEmbed.build()).queue()
            } else {
                val reloadEmbed = EmbedBuilder()
                    .setTitle("**:repeat: Reloading...**")
                    .setDescription("Reloading minecraft server...")
                    .setFooter("${event.user.name}#${event.user.discriminator}", event.user.avatarUrl)

                event.replyEmbeds(reloadEmbed.build()).queue()
                Bukkit.broadcast(Component.text("${ChatColor.GREEN}Reloading..."))

                Bukkit.getScheduler().runTaskLater(plugin, Runnable {
                    Bukkit.getServer().reload()
                }, 40L)
                Bukkit.broadcast(Component.text("${ChatColor.GREEN}Reload Complete!"))
            }
        }
    }
}