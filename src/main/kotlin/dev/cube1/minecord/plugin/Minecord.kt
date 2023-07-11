package dev.cube1.minecord.plugin

import club.minnced.discord.webhook.WebhookClientBuilder
import dev.cube1.minecord.plugin.listener.MinecordListener
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.requests.GatewayIntent

class Minecord(private val plugin: CorePlugin, token: String) {

    lateinit var jda: JDA
        private set

    val webhookClient = WebhookClientBuilder(Config.discord.webhook_url).buildJDA()

    private val builder = JDABuilder.createLight(token, listOf(
        GatewayIntent.GUILD_PRESENCES,
        GatewayIntent.GUILD_MEMBERS,
        GatewayIntent.GUILD_MESSAGES,
        GatewayIntent.DIRECT_MESSAGES,
        GatewayIntent.MESSAGE_CONTENT
    ))

    fun start() {
        if (::jda.isInitialized) {
            throw UnsupportedOperationException("Cannot make JDA instance twice.")
        }
        jda = builder.build()
    }

    fun stop() {
        if (::jda.isInitialized) {
            jda.shutdown()
        }
    }

    fun reload() {
        jda.shutdown()
        jda = builder.build()
    }

    fun addListener(listener: MinecordListener) {
        builder.addEventListeners(listener)
        plugin.server.pluginManager.registerEvents(listener, plugin)
    }

}