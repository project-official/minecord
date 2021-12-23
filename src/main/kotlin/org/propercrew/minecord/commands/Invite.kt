package org.propercrew.minecord.commands

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.propercrew.minecord.Minecord.Companion.instance

object Invite: CommandExecutor, TabCompleter {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (command.name == "invite") {
            when (args.size) {
                0 -> {
                    sender.sendMessage("디스코드 주소: ${instance?.config?.getString("invite_url")}")
                    return true
                }

                2 -> {
                    if (args[0] == "set" && sender.isOp) {
                        instance?.config?.set("invite_url", args[1])
                        sender.sendMessage("디스코드 주소가 설정 되었습니다. ${args[1]}")

                        return true
                    }
                }
            }
        }

        return false;
    }

    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<out String>): MutableList<String>? {
        if (command.name == "invite") {
            if (args.size == 1) {
                return mutableListOf("set")
            }
        }

        return null
    }
}