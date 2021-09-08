package org.proper.minecord.commands

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.ChatColor

class ReloadCommand : ListenerAdapter() {

    override fun onSlashCommand(event: SlashCommandEvent) {
        if (event.name == "reload" || event.member!!.isOwner) {
            val reloadEmbed = EmbedBuilder()
                .setTitle("**:repeat: Reloading...**")
                .setDescription("Reloading minecraft server...")
                .setFooter("${event.user.name}#${event.user.discriminator}", event.user.avatarUrl)

            event.channel.sendMessage(reloadEmbed.build())
            Bukkit.broadcast(Component.text("${ChatColor.GREEN}Reloading..."))
            Bukkit.getServer().reload()
        }
    }
}