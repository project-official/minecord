package org.propercrew.minecord.commands

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.plugin.java.JavaPlugin
import org.propercrew.minecord.Minecord

class ReloadCommand: ListenerAdapter() {

    override fun onSlashCommand(event: SlashCommandEvent) {
        val plugin = Minecord.instance!!

        if (event.channel.id != Minecord.instance?.config?.getString("channelId")) {
            return
        }

        if (event.name == "reload") {
            if (event.member!!.isOwner) {
                val reloadEmbed = EmbedBuilder()
                    .setTitle("**:repeat: Reloading...**")
                    .setDescription("Reloading minecraft server...")
                    .setFooter(event.user.asTag, event.user.avatarUrl)

                event.replyEmbeds(reloadEmbed.build()).queue()
                Bukkit.broadcast(Component.text("${ChatColor.GREEN}Reloading..."))

                Bukkit.getScheduler().runTaskLater(plugin, Runnable {
                    Bukkit.getServer().reload()
                }, 60L)

                Bukkit.broadcast(Component.text("${ChatColor.GREEN}Reload Complete!"))
            } else {
                val errorEmbed = EmbedBuilder()
                    .setTitle("**:warning: Error!**")
                    .setDescription("Permission denied!")
                    .setFooter("${event.user.name}#${event.user.discriminator}", event.user.avatarUrl)

                event.replyEmbeds(errorEmbed.build()).queue()
            }
        }
    }
}