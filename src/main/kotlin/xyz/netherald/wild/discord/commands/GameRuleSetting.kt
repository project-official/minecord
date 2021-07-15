package xyz.netherald.wild.discord.commands

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.bukkit.Bukkit
import org.bukkit.GameRule
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import xyz.netherald.wild.discord.WildDiscord

class GameRuleSetting: ListenerAdapter(), CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender.isOp) {
            if (command.name == "d_gamerule") {
                when (args[0]) {
                    "keepInventory" -> {
                        when (args[1]) {
                            "true" -> {
                                for (world in Bukkit.getWorlds()) {
                                    world.setGameRule(GameRule.KEEP_INVENTORY, true)
                                }

                                sender.sendMessage("Gamerule has successful updated: true")
                                return true
                            }

                            "false" -> {
                                for (world in Bukkit.getWorlds()) {
                                    world.setGameRule(GameRule.KEEP_INVENTORY, true)
                                }

                                sender.sendMessage("Gamerule has successful updated: false")
                                return true
                            }
                        }
                    }
                }
            }
        }

        return false
    }

    override fun onSlashCommand(event: SlashCommandEvent) {
        if (!event.user.isBot) {
            return
        }

        if (!WildDiscord.opId.contains(event.user.id)) {
            return
        }

        val command: String = event.getOption("gamerule_type")?.asString.toString()

        if (event.name == "rule") {
            val ruleOptions = event.getOption("value")?.asBoolean
            when (command) {
                "keep_inventory" -> {
                    if (ruleOptions != null) {
                        keepInventory(event, ruleOptions)
                    }
                }

                "mob_griefing" -> {
                    if (ruleOptions != null) {
                        mobGriefing(event, ruleOptions)
                    }
                }
            }
        }
    }

    private fun keepInventory(event: SlashCommandEvent, rule: Boolean) {
        for (world in Bukkit.getWorlds()) {
            world.setGameRule(GameRule.KEEP_INVENTORY, rule)
            println(rule) // Debug Code
        }

        val embed = EmbedBuilder()
            .setTitle(":white_check_mark: Game rule is successful setting!")
            .setDescription("Now rule ${GameRule.KEEP_INVENTORY.name} is $rule!")
            .setFooter("${event.user.name}#${event.user.discriminator}", event.user.avatarUrl)

        event.replyEmbeds(embed.build()).queue()
    }

    private fun mobGriefing(event: SlashCommandEvent, rule: Boolean) {
        for (world in Bukkit.getWorlds()) {
            world.setGameRule(GameRule.MOB_GRIEFING, rule)
            println(rule) // Debug Code
        }

        val embed = EmbedBuilder()
            .setTitle(":white_check_mark: Game rule is successful setting!")
            .setDescription("Now rule ${GameRule.MOB_GRIEFING.name} is $rule!")
            .setFooter("${event.user.name}#${event.user.discriminator}", event.user.avatarUrl)

        event.replyEmbeds(embed.build()).queue()
    }
}