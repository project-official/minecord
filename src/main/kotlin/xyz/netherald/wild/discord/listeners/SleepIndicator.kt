package xyz.netherald.wild.discord.listeners

import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerBedEnterEvent
import org.bukkit.event.player.PlayerBedLeaveEvent
import xyz.netherald.wild.discord.WildDiscord

class SleepIndicator(private val plugin: WildDiscord): Listener {

    private var sleepCount = 0;
    private val channel = WildDiscord.jda?.getTextChannelById(plugin.config.getString("channelId")!!)

    @EventHandler
    fun onBedEnter(event: PlayerBedEnterEvent) {
        setSleepCount(getSleepCount() + 1)
        channel?.sendMessage("**${event.player.name}님이 수면 중 입니다! `[${sleepCount}/${plugin.server.onlinePlayers}]`**")?.queue()
        Bukkit.broadcast(Component.text("<Discord> ${ChatColor.GREEN}${event.player.name}${ChatColor.GREEN}님이 수면 중 입니다! " +
                "${ChatColor.RESET}[${ChatColor.GOLD}${sleepCount}${ChatColor.RESET}/${ChatColor.GOLD}${plugin.server.onlinePlayers}${ChatColor.RESET}]")
        )
    }

    @EventHandler
    fun onBedLeave(event: PlayerBedLeaveEvent) {
        setSleepCount(getSleepCount() - 1)
        channel?.sendMessage("**${event.player.name}님이 침대에서 나오셨습니다! `[${sleepCount}/${plugin.server.onlinePlayers}]`**")?.queue()
        Bukkit.broadcast(Component.text("<Discord> ${ChatColor.YELLOW}${event.player.name}${ChatColor.RED}님이 침대에서 나오셨습니다! " +
                "${ChatColor.RESET}[${ChatColor.GOLD}${sleepCount}${ChatColor.RESET}/${ChatColor.GOLD}${plugin.server.onlinePlayers}${ChatColor.RESET}]")
        )
    }

    fun getSleepCount(): Int {
        return sleepCount
    }

    fun setSleepCount(count: Int) {
        sleepCount = count
    }
}