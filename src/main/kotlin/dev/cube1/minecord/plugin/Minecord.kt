package dev.cube1.minecord.plugin

import club.minnced.discord.webhook.WebhookClientBuilder
import dev.cube1.minecord.plugin.command.MinecordCommandHandler
import dev.cube1.minecord.plugin.listener.MinecordListener
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.requests.GatewayIntent

class Minecord(private val plugin: CorePlugin, token: String, option: MinecordOption) {

    var jda: JDA
        private set


    init {
        val builder = JDABuilder.createLight(token, listOf(
            GatewayIntent.GUILD_PRESENCES,
            GatewayIntent.GUILD_MEMBERS,
            GatewayIntent.GUILD_MESSAGES,
            GatewayIntent.DIRECT_MESSAGES,
            GatewayIntent.MESSAGE_CONTENT,
            GatewayIntent.GUILD_WEBHOOKS,
            GatewayIntent.GUILD_MODERATION
        ))

        option.minecordListeners.forEach { listener ->
            builder.addEventListeners(listener)
            plugin.server.pluginManager.registerEvents(listener, plugin)
        }

        val handlers = option.minecordCommandHandlers.map { handler ->
            val data = handler.commandData()
            return@map data.name to handler::handle
        }.toMap()

        val commandProcessor = CommandProcessor(handlers).also {
            builder.addEventListeners(it)
        }

        jda = builder.build()

        val guildId = Config.Discord.guild_id
        val guild = jda.getGuildById(guildId)

        guild?.updateCommands()?.addCommands(
            option.minecordCommandHandlers.map { it.commandData() }
        )
    }

    val webhookClient = WebhookClientBuilder(Config.Discord.webhook_url).buildJDA()


    fun stop() {
        jda.shutdown()
    }

    private class CommandProcessor(private val handlers: Map<String, (SlashCommandInteractionEvent) -> Unit>) : MinecordListener() {

        override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
            val commandName = event.name

            if (handlers.containsKey(commandName)) {
                val handler = handlers[commandName]
                requireNotNull(handler)
                handler(event)
            }
        }
    }

    data class MinecordOption(
        val minecordListeners: List<MinecordListener>,
        val minecordCommandHandlers: List<MinecordCommandHandler>
    )

}