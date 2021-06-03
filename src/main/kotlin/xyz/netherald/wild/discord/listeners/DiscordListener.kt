package xyz.netherald.wild.discord.listeners

import io.papermc.paper.event.player.AsyncChatEvent
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.ChannelType
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.events.GenericEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.EventListener
import net.kyori.adventure.text.TextComponent
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import xyz.netherald.wild.discord.WildDiscord


@Suppress("DEPRECATION")
class DiscordListener(private val plugin: WildDiscord) : EventListener, Listener {

    override fun onEvent(event: GenericEvent) {
        if(event is MessageReceivedEvent) {
            if(!event.message.author.isBot) {
                if (event.channelType != ChannelType.PRIVATE) {
                    if(event.channel.id == WildDiscord.instance?.config?.getString("channelId"))
                        if (event.message.contentRaw == "!online") {
                            if (plugin.config.getInt("styleOnlineCommand") == 0) {
                                var memberStr = "**온라인 유저** : \n```"
                                memberStr += "인원: ${Bukkit.getOnlinePlayers().size}명/ ${Bukkit.getMaxPlayers()}명\n"
                                if (Bukkit.getOnlinePlayers().isNotEmpty()) {
                                    for ((i, player) in Bukkit.getOnlinePlayers().withIndex()) {
                                        memberStr += "$i: ${player.name}\n"
                                    }
                                } else {
                                    memberStr += "사람이 없습니다\n"
                                }
                                memberStr += "```"
                                memberStr += "========${WildDiscord.serverAddress ?: ""}========\n"

                                event.channel.sendMessage(memberStr).queue()
                            }
                            else if (plugin.config.getInt("styleOnlineCommand") == 1) {
                                var embed = EmbedBuilder().setTitle("**온라인 유저**")
                                    .setColor(0x0070ff.toInt())
                                    .addField("인원:", "${Bukkit.getOnlinePlayers().size}명/ ${Bukkit.getMaxPlayers()}명",false)

                                var memberStr: String = "```\n"
                                if (Bukkit.getOnlinePlayers().isNotEmpty()) {
                                    for ((i, player) in Bukkit.getOnlinePlayers().withIndex()) {
                                        memberStr += "$i 번째: ${player.name}\n"
                                    }
                                } else {
                                    memberStr += "사람이 없습니다\n"
                                }
                                memberStr += "```"

                                embed.addField("목록:", memberStr, false)
                                if (WildDiscord.serverAddress != null) embed.setFooter("========${WildDiscord.serverAddress ?: ""}========")

                                val result: MessageEmbed = embed.build()
                                event.channel.sendMessage(result).queue()
                            }
                        } else {
                            Bukkit.broadcastMessage("<${ChatColor.DARK_PURPLE}${event.author.asTag}${ChatColor.RESET}> ${event.message.contentRaw}")
                        }
                }
            }
        }
    }

    @EventHandler
    fun onChat(event: AsyncChatEvent) {
        val channel = WildDiscord.jda?.getTextChannelById(plugin.config.getString("channelId")!!)
        channel?.sendMessage("**${event.player.name}**: ${(event.message() as TextComponent).content()}")?.queue()
    }

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        val channel = WildDiscord.jda?.getTextChannelById(plugin.config.getString("channelId")!!)
        channel?.sendMessage("**${event.player.name}**님이 게임에 들어왔습니다. 현재 플레이어 수: ${Bukkit.getOnlinePlayers().size}/${Bukkit.getMaxPlayers()}명")?.queue()
    }

    @EventHandler
    fun onLeave(event: PlayerQuitEvent) {
        val channel = WildDiscord.jda?.getTextChannelById(plugin.config.getString("channelId")!!)
        channel?.sendMessage("**${event.player.name}**님이 게임에서 나갔습니다. 현재 플레이어 수: ${Bukkit.getOnlinePlayers().size - 1}/${Bukkit.getMaxPlayers()}명")?.queue()
    }

    @EventHandler
    fun onPlayerDeath(event : PlayerDeathEvent) {
        val channel = WildDiscord.jda?.getTextChannelById(plugin.config.getString("channelId")!!)
        channel?.sendMessage("**${event.deathMessage()}**")?.queue()
    }
}