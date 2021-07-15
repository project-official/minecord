package xyz.netherald.wild.discord.commands

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import xyz.netherald.wild.discord.WildDiscord

class OpAddCommand(private val plugin: WildDiscord): CommandExecutor {

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