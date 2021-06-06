package xyz.netherald.wild.discord

import dev.kord.common.Color
import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.core.behavior.channel.MessageChannelBehavior
import dev.kord.core.behavior.channel.createEmbed
import dev.kord.core.entity.User
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.core.on
import dev.kord.rest.builder.message.EmbedBuilder
import io.papermc.paper.event.player.AsyncChatEvent
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import net.kyori.adventure.text.TextComponent
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerAdvancementDoneEvent
import org.bukkit.event.player.PlayerEvent
import org.bukkit.plugin.java.JavaPlugin

class WildDiscord : JavaPlugin() {

    companion object {
        var instance:WildDiscord? = null
        val listColor: List<String> = listOf(
            "<black>", "<dark_blue>", "<dark_green>", "<dark_aqua>", "<red>",
            "<dark_purple>", "<gold>", "<gray>", "<dark_gray>", "<blue>", "<green>", "<aqua>", "<purple>", "<yellow>",
            "<white>"
        )
    }

    lateinit var kord:Kord

    @DelicateCoroutinesApi
    override fun onEnable() {
        instance = this
        server.pluginManager.registerEvents(BukkitListener(),this)
        val running = initializeFunAsync()
    }

    @DelicateCoroutinesApi
    override fun onDisable() {
        val logout = logoutAsync()
    }

    @DelicateCoroutinesApi
    fun logoutAsync() = GlobalScope.async {
        kord.logout()
    }


    @DelicateCoroutinesApi
    fun initializeFunAsync() = GlobalScope.async{
        kord = Kord(config.getString("token")!!)
        kord.on<MessageCreateEvent> {
            if (message.channelId.value != config.getLong("channelId")) return@on
            if (message.author?.isBot == true) return@on
            when (message.content) {
                "!online" -> {
                    message.channel.createEmbed {
                        color = Color(config.getInt("joinEmbedColor"))
                        title = "**온라인 유저**"
                        fields = mutableListOf(EmbedBuilder.Field().apply {
                            name = "인원 : "
                            value = "${server.onlinePlayers.size}명 / ${server.maxPlayers}명"
                            inline = false

                        }, EmbedBuilder.Field().apply {
                            name = "목록 : "
                            value = run { ->
                                var memberStr: String = "```"
                                if (server.onlinePlayers.size != 0) {
                                    for ((i, player) in server.onlinePlayers.withIndex()) {
                                        memberStr += "${i + 1} 번째: ${player.name}\n"
                                    }
                                } else {
                                    memberStr += "서버에 사람이 없습니다."
                                }
                                memberStr += "```"
                                memberStr
                            }
                            inline = false
                        })
                    }
                }
                else -> {
                    server.broadcastMessage(
                        ChatFormatter.replaceChatFormat(
                            message.content,
                            message.author!!,
                            if (config.getString("messageFormat")
                                    .isNullOrEmpty()
                            ) "<<dark_purple><sender><reset>> <message>" else config.getString("messageFormat")!!,
                            config.getBoolean("customColor"),
                            config.getBoolean("customColor")
                        )
                    )
                }
            }
        }

        kord.login()

    }

    inner class BukkitListener : Listener {

        @DelicateCoroutinesApi
        @EventHandler
        fun onChatAsync(event: AsyncChatEvent) = GlobalScope.async{
            val channel = kord.getChannel(Snowflake(config.getString("channelId")!!)) as MessageChannelBehavior
            channel.createMessage(ChatFormatter.replaceMsgFormat(event,config.getString("chatFormat")!!))
        }
    }

}

object ChatFormatter {
    suspend fun replaceChatColor(message: String): String {
        return message.replace("<black>", "${ChatColor.BLACK}")
            .replace("<dark_blue>", "${ChatColor.DARK_BLUE}")
            .replace("<dark_green>", "${ChatColor.DARK_GREEN}")
            .replace("<dark_aqua>", "${ChatColor.DARK_AQUA}")
            .replace("<red>", "${ChatColor.DARK_RED}")
            .replace("<dark_purple>", "${ChatColor.DARK_PURPLE}")
            .replace("<gold>", "${ChatColor.GOLD}")
            .replace("<gray>", "${ChatColor.GRAY}")
            .replace("<dark_gray>", "${ChatColor.DARK_GRAY}")
            .replace("<blue>", "${ChatColor.BLUE}")
            .replace("<green>", "${ChatColor.GREEN}")
            .replace("<aqua>", "${ChatColor.AQUA}")
            .replace("<purple>", "${ChatColor.LIGHT_PURPLE}")
            .replace("<yellow>", "${ChatColor.YELLOW}")
            .replace("<white>", "${ChatColor.WHITE}")
            .replace("<magic>", "${ChatColor.MAGIC}")
            .replace("<bold>", "${ChatColor.BOLD}")
            .replace("<strikethrough>", "${ChatColor.STRIKETHROUGH}")
            .replace("<underline>", "${ChatColor.UNDERLINE}")
            .replace("<italic>", "${ChatColor.ITALIC}")
            .replace("<reset>", "${ChatColor.RESET}")
    }
    suspend fun replaceChatFormat(
        message: String,
        author:User,
        formatter: String,
        Markdown: Boolean,
        CustomColor: Boolean
    ): String {
        val customColor_message: String

        val s: String = replaceChatColor(formatter)
        var markdown_message = ""

        if (Markdown) {
            var setBold = false
            var setColor: String? = null
            var setItalic = false
            var setUnderline = false
            var setStrikethrough = false

            var index = 0
            while (index <= message.length - 1) {
                val value: String = message.slice(index..message.length - 1)
                var alreadyColor = false

                if (CustomColor) {
                    for (color in WildDiscord.listColor) {
                        if (value.startsWith(color)) {
                            setColor = color

                            markdown_message += color
                            index += color.length
                            alreadyColor = true
                            break
                        }
                    }
                }

                if (value.startsWith("**")) {
                    if (setBold) {
                        setBold = false
                        markdown_message += "${ChatColor.RESET}"
                        if (CustomColor && setColor != null) {
                            markdown_message += setColor
                            index += setColor.length
                        }
                    } else {
                        setBold = true
                        markdown_message += "${ChatColor.BOLD}"
                    }
                    index += 2
                } else if (value.startsWith("*")) {
                    if (setItalic) {
                        setItalic = false
                        markdown_message += "${ChatColor.RESET}"
                        if (CustomColor && setColor != null) {
                            markdown_message += setColor
                            index += setColor.length
                        }
                    } else {
                        setItalic = true
                        markdown_message += "${ChatColor.ITALIC}"
                    }
                    index += 1
                } else if (value.startsWith("~~")) {
                    if (setStrikethrough) {
                        setStrikethrough = false
                        markdown_message += "${ChatColor.RESET}"
                        if (CustomColor && setColor != null) {
                            markdown_message += setColor
                            index += setColor.length
                        }
                    } else {
                        setStrikethrough = true
                        markdown_message += "${ChatColor.STRIKETHROUGH}"
                    }
                    index += 2
                } else if (value.startsWith("__")) {
                    if (setUnderline) {
                        setUnderline = false
                        markdown_message += "${ChatColor.RESET}"
                        if (CustomColor && setColor != null) {
                            markdown_message += setColor
                            index += setColor.length
                        }
                    } else {
                        setUnderline = true
                        markdown_message += "${ChatColor.UNDERLINE}"
                    }
                    index += 2
                } else {
                    if (!alreadyColor) {
                        index += 1
                        markdown_message += value[0]
                    }
                }
            }
        } else {
            markdown_message = message
        }

        if (CustomColor) {
            customColor_message = replaceChatColor(markdown_message)
        } else {
            customColor_message = markdown_message
        }

        return s.replace("<message>", customColor_message)
            .replace("<sender>", author.tag)
            .replace("<mention>", author.mention)
    }

    suspend fun replacePlayer(player: Player, formatter: String): String {
        return formatter.replace("<player>", player.name)
            .replace("<address>", player.address.address.hostAddress)
            .replace("<exp>", "${player.exp}")
            .replace("<level>", "${player.level}")
    }

    suspend fun replaceMsgFormat(event: AsyncChatEvent, formatter: String): String {
        val player: String = replacePlayer(event.player, formatter)
        val message: String = (event.message() as TextComponent).content()
        return player.replace("<message>", message)
    }

    suspend fun replaceAccessFormat(event: PlayerEvent, formatter: String): String {
        val player: String = replacePlayer(event.player, formatter)
        return player.replace("<online>", "${Bukkit.getOnlinePlayers().size}")
            .replace("<max>", "${Bukkit.getMaxPlayers()}")
    }

    suspend fun replaceDeathFormat(event: PlayerDeathEvent, formatter: String): String {
        val player: String = replacePlayer(event.entity.player!!, formatter)
        return player.replace("<message>", "${event.deathMessage}")
    }

    suspend fun replaceAdvancementFormat(event: PlayerAdvancementDoneEvent, formatter: String): String {
        val player: String = replacePlayer(event.player, formatter)

        val advancement: String = event.advancement.key.key
        return player.replace("<advancement>", advancement.replace("adventure/", ""))
    }
}