package dev.cube1.minecord.plugin.util

import dev.vankka.mcdiscordreserializer.minecraft.MinecraftSerializer
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.User
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.Tag
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags
import org.bukkit.Bukkit
import org.bukkit.Server
import org.bukkit.entity.Player

class PlaceholderBuilder {

    private val resolvers = mutableListOf<TagResolver>(StandardTags.defaults())

    fun useUserTag(user: Player) {
        val resolver = TagResolver.resolver("user", Tag.inserting(user.name()))
        resolvers.add(resolver)
    }

    fun useUserTag(user: User) {
        if (user.globalName == null) return
        val resolver = TagResolver.resolver("user", Tag.inserting(Component.text(user.globalName!!)))
        resolvers.add(resolver)
    }

    fun useMaxPlayerTag(server: Server = Bukkit.getServer()) {
        val resolver = TagResolver.resolver("maxplayer", Tag.inserting(Component.text(server.maxPlayers)))
        resolvers.add(resolver)
    }

    fun useCurrentPlayerTag(server: Server = Bukkit.getServer(), isQuit: Boolean = false) {
        val resolver = TagResolver.resolver(
            "currentplayer",
            Tag.inserting(
                Component.text(
                    server.onlinePlayers.size + if (isQuit) -1 else 0
                )
            )
        )
        resolvers.add(resolver)
    }

    fun useMessageTag(message: Message) {
        val resolver = TagResolver.resolver("message", Tag.inserting(MinecraftSerializer.INSTANCE.serialize(message.contentDisplay)))
        resolvers.add(resolver)
    }

    fun build(): MiniMessage {
        return MiniMessage.builder().tags(
            TagResolver.builder().resolvers(resolvers).build()
        ).build()
    }
}

