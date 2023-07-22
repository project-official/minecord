package dev.cube1.minecord.plugin.util

import org.bukkit.entity.Player

data class SimpleAvatarOption(
    val size: Int = 180,
    val showOverlay: Boolean = true
)

data class IsometricAvatarOption(
    val size: Int = 180,
    val showOverlay: Boolean = true,
    val facing: AvatarFacing = AvatarFacing.RIGHT
)

enum class AvatarFacing {
    LEFT, RIGHT
}

object AvatarApi {

    fun simpleAvatar(player: Player, option: SimpleAvatarOption = SimpleAvatarOption()): String {
        return "https://mc-heads.net/avatar/${player.uniqueId}/" + option.size.toString() + (if (option.showOverlay) "/" else "/nohelm")
    }

    fun isometricHead(player: Player, option: IsometricAvatarOption = IsometricAvatarOption()): String {
        return "https://mc-heads.net/head/${player.uniqueId}/" + option.size.toString() + (if (option.showOverlay) "/" else "/nohelm") + if (option.facing == AvatarFacing.LEFT) "/left" else "/right"
    }

    fun isometricBody(player: Player, option: IsometricAvatarOption = IsometricAvatarOption()): String {
        return "https://mc-heads.net/body/${player.uniqueId}/" + option.size.toString() + (if (option.showOverlay) "/" else "/nohelm") + if (option.facing == AvatarFacing.LEFT) "/left" else "/right"
    }

    fun fullBodyAvatar(player: Player, option: SimpleAvatarOption): String {
        return "https://mc-heads.net/player/${player.uniqueId}/" + option.size.toString() + (if (option.showOverlay) "/" else "/nohelm")
    }

}
