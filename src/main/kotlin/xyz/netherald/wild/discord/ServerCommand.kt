package xyz.netherald.wild.discord

import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player

class ServerCommand(private val plugin: WildDiscord): CommandExecutor, TabCompleter {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (command.name == "discord" && sender !is Player && sender.isOp) {
            if (args.size == 2) {
                when (args[0]) {
                    "activity" -> {
                        val address = args[1]
                        WildDiscord.serverAddress = address
                        plugin.address(true)

                        Bukkit.getLogger().info("[Discord] Server address is changed this -> $address")

                        return true
                    }

                    "channel" -> {
                        val channel = Integer.parseInt(args[1])
                        val array = WildDiscord.channelId.add(channel)
                        plugin.config["channelId"] = array

                        sender.sendMessage("[Discord] $channel is successful added!")

                        return true
                    }
                }
            }
        }


        return false
    }

    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<out String>): MutableList<String>? {
        if (command.name == "discord" && sender !is Player && sender.isOp) {
            val commandList = arrayListOf<String>()

            if (args.size == 1) {
                commandList.add("activity")
                commandList.add("channel")
                return commandList
            }
        }

        return null
    }
}