package org.propercrew.minecord.commands

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.plugin.java.JavaPlugin
import org.propercrew.minecord.Minecord

class ReloadCommand(private val plugin: JavaPlugin) : ListenerAdapter() {

    override fun onSlashCommand(event: SlashCommandEvent) {
        if (event.channel.id != Minecord.instance?.config?.getString("channelId")) {
            return
        }

        if (event.name == "reload") {
            if (event.member!!.isOwner) {
                val reloadEmbed = EmbedBuilder()
                    .setTitle("**:repeat: Reloading...**")
                    .setDescription("Reloading minecraft server...")
                    .setFooter("${event.user.name}#${event.user.discriminator}", event.user.avatarUrl)

                event.replyEmbeds(reloadEmbed.build()).queue()
                Bukkit.broadcastMessage("${ChatColor.GREEN}Reloading...")

                Bukkit.getScheduler().runTaskLater(plugin, {
                    Bukkit.getServer().reload()
                }, 60L)

                Bukkit.broadcastMessage("${ChatColor.GREEN}Reload Complete!")
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