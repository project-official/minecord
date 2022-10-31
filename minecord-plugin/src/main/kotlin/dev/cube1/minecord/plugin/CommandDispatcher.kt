package dev.cube1.minecord

import dev.cube1.minecord.plugin.instance
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter

object CommandDispatcher : CommandExecutor, TabCompleter {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (command.name != "invite") return false

        when (args.size) {
            0 -> {
                sender.sendMessage("디스코드 주소: ${instance.config.getString("invite_url")}")
                    return true
                }

            2 -> {
                if (args[0] == "set" && sender.isOp) {
                    val url = try {
                        args[1]
                    } catch (e: ArrayIndexOutOfBoundsException) {
                        sender.sendMessage(Component.text("디스코드 주소를 입력해 주시기 바랍니다.", NamedTextColor.RED))
                        return false
                    }

                    instance.config.set("invite_url", url)
                    sender.sendMessage(Component.text("디스코드 주소가 설정 되었습니다: ", NamedTextColor.GREEN)
                        .append(Component.text(url, NamedTextColor.WHITE)))

                    return true
                }
            }
        }

        return false
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