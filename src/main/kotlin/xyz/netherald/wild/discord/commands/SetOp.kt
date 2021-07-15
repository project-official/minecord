package xyz.netherald.wild.discord.commands

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import xyz.netherald.wild.discord.WildDiscord

class SetOp(private val plugin: WildDiscord): ListenerAdapter(), CommandExecutor {

    override fun onSlashCommand(event: SlashCommandEvent) {
        if (event.user.isBot) {
            return
        }

        if (!WildDiscord.opId.contains(event.user.id)) {
            return
        }

        when (event.name) {
            "op" -> {
                val user = event.getOption("user")?.asUser
                if (user != null) {
                    op(event, user)
                }
            }

            "deop" -> {
                val user = event.getOption("user")?.asUser
                if (user != null) {
                    deop(event, user)
                }
            }
        }
    }

    private fun op(event: SlashCommandEvent, user: User) {
        if (WildDiscord.opId.contains(user.id)) {
            return
        }

        val embed = EmbedBuilder()
            .setTitle(":white_check_mark: Successful add op!")
            .setDescription("Now ${user.name}#${user.discriminator} is op!")
            .setFooter("${event.user.name}#${event.user.discriminator}", event.user.avatarUrl)

        event.replyEmbeds(embed.build()).queue()

        WildDiscord.opId.add(user.id)
        plugin.config.set("opID", WildDiscord.opId)
    }

    private fun deop(event: SlashCommandEvent, user: User) {
        if (!WildDiscord.opId.contains(user.id)) {
            return
        }

        val embed = EmbedBuilder()
            .setTitle(":white_check_mark: Successful remove op!")
            .setDescription("Now ${user.name}#${user.discriminator} is not op!")
            .setFooter("${event.user.name}#${event.user.discriminator}", event.user.avatarUrl)

        event.replyEmbeds(embed.build()).queue()

        WildDiscord.opId.remove(user.id)
        plugin.config.set("opID", WildDiscord.opId)
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender.isOp) {
            when (command.name) {
                "d_op" -> {
                    val id = args[0]
                    if (!WildDiscord.opId.contains(id)) {
                        WildDiscord.opId.add(id)

                        plugin.config.set("opID", WildDiscord.opId)
                        sender.sendMessage("$id is added!")
                    }

                    return true
                }

                "d_deop" -> {
                    val id = args[0]
                    if (WildDiscord.opId.contains(id)) {
                        WildDiscord.opId.remove(id)

                        plugin.config.set("opID", WildDiscord.opId)
                        sender.sendMessage("$id is removed!")
                    }

                    return true
                }
            }
        }

        return false
    }
}