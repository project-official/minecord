package org.propercrew.minecord.utils

import org.apache.logging.log4j.core.LogEvent
import org.apache.logging.log4j.core.appender.AbstractAppender
import org.propercrew.minecord.Minecord.Companion.instance
import org.propercrew.minecord.Minecord.Companion.jda
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class LogAppender: AbstractAppender("MinecordSRV", null, null, false, null) {

    init {
        this.start()
    }

    private var queueMessage: Queue<String> = LinkedList()

    override fun append(event: LogEvent?) {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val formatted = current.format(formatter)
        val packageName = "[${ event?.loggerName ?: "" }]"
        val message = "[${formatted}] [${event?.threadName} ${event?.level?.name()}]: $packageName ${event?.message?.formattedMessage as String}"

        if(message.length >= 2000) {
            val split = message.lines()
            for(msg in split) {
                queueMessage.add(msg)
            }
        } else {
            queueMessage.add(message)
        }

        while(queueMessage.size != 0) {
            val msg = queueMessage.poll()
            jda!!.getTextChannelById(instance!!.config.getString("console-srv-channelId").toString())?.sendMessage(msg)?.queue()
        }
    }
}