package dev.cube1.minecord.plugin

import dev.cube1.minecord.plugin.CorePlugin.Companion.instance
import kotlin.reflect.KProperty

object Config {

    private val config by lazy { instance.config }

    private fun <T> useConfig(): ConfigDelegate<T> {
        return ConfigDelegate()
    }

    data object Discord {
        val token by useConfig<String>()
        val guild_id by useConfig<String>()
        val chat_id by  useConfig<String>()
        val webhook_url by useConfig<String>()
    }

    data object Settings {
        data object Activity {
            val enable by useConfig<Boolean>()
            val context by useConfig<String>()
        }
    }

    data object Color {
        val default by useConfig<Int>()
        val join by  useConfig<Int>()
        val quit by useConfig<Int>()
        val death by useConfig<Int>()
        val kick by useConfig<Int>()
    }

    data object Format {
        val user_join by useConfig<String>()
        val user_quit by useConfig<String>()
        val forbidden_channel by useConfig<String>()
        val discord_user_chat by useConfig<String>()
    }
}



class ConfigDelegate<T> {
    operator fun getValue(thisRef: Any, property: KProperty<*>): T {
        val packageName = thisRef::class.java.packageName
        val className = thisRef::class.qualifiedName!!.removePrefix("$packageName.Config.").lowercase()
        val propertyName = property.name
        val path = "${className}.${propertyName.lowercase()}"
        return instance.config.get(path) as T
    }
}