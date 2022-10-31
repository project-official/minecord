package dev.cube1.minecord.plugin

object Config {
    const val render = "https://crafatar.com"
    const val prefix = "Minecord -"

    lateinit var discord: ConfigModel.Discord
    lateinit var settings: ConfigModel.Settings
    lateinit var format: ConfigModel.Format
    lateinit var color: ConfigModel.Color

    fun init() = instance.config.apply {
        discord = ConfigModel.Discord(
            token = getString("discord.token")!!,
            guild_id = getString("discord.guild_id")!!,
            ConfigModel.Channels(
                chat_id = getString("discord.channels.chat_id")!!
            )
        )
        settings = ConfigModel.Settings(
            activity = ConfigModel.Activity(
                enable = getBoolean("settings.activity.enable"),
                context = getString("settings.activity.context") ?: "Minecord"
            ),
            style = getInt("settings.style")
        )
        format = ConfigModel.Format(
            enable = getString("format.enable")
                ?: "\uD83C\uDFC1 서버가 활성화 되었어요",
            disable = getString("format.disable")
                ?: "\uD83D\uDED1 서버가 비활성화 되었어요",
            error = getString("format.error")
                ?: "명령어 실행 도중에 오류가 발생 했어요",
            chat = ConfigModel.Chat(
                mc = getString("format.chat.mc")
                    ?: "{aqua}{user}{reset}: {context}",
                discord = getString("format.chat.discord")
                    ?: "{user}: {context}",
            ),
            player = ConfigModel.Player(
                join = getString("format.player.join")
                    ?: "{user}님이 게임에 접속했어요",
                quit = getString("format.player.quit")
                    ?: "{user}님이 게임에서 나갔어요",
                death = getString("format.player.death")
                    ?: "{user}님이 사망 하셨어요",
                advancement = getString("format.player.advancement")
                    ?: "{user}님이 {advancement}을(를) 클리어 하셨어요",
                kick = getString("format.player.kick")
                    ?: "{user}님이 추방 되었어요"
            )
        )

        color = ConfigModel.Color(
            default = getInt("color.default"),
            join = getInt("color.join"),
            quit = getInt("color.quit"),
            death = getInt("color.death"),
            kick = getInt("color.kick")
        )
    }

    object ConfigModel {
        // Discord
        data class Channels(val chat_id: String)
        data class Discord(
            val token: String,
            val guild_id: String,
            val channels: Channels
        )

        // Settings
        data class Activity(val enable: Boolean, val context: String)
        data class Settings(
            val activity: Activity,
            val style: Int
        )

        // Format
        data class Chat(val mc: String, val discord: String)
        data class Player(
            val join: String,
            val quit: String,
            val death: String,
            val advancement: String,
            val kick: String
        )
        data class Format(
            val enable: String,
            val disable: String,
            val error: String,
            val chat: Chat,
            val player: Player
        )

        // Color
        data class Color(
            val default: Int,
            val join: Int,
            val quit: Int,
            val death: Int,
            val kick: Int
        )
    }
}