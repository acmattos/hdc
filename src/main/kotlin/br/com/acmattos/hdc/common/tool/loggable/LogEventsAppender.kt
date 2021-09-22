package br.com.acmattos.hdc.common.tool.loggable

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.AppenderBase
import org.slf4j.LoggerFactory

/**
 * This object helps you to track logged messages.
 * @author ACMattos
 * @since 21/09/2021.
 */
class LogEventsAppender(
    private val clazz: Class<*>
): AppenderBase<ILoggingEvent>() {
    private lateinit var logger: ch.qos.logback.classic.Logger
    private val events: MutableList<ILoggingEvent> = mutableListOf()

    init {
        setup()
    }

    /**
     * Prepares the appender to start monitoring.
     */
    fun setup(): LogEventsAppender {
        logger = LoggerFactory.getLogger(clazz)
            as ch.qos.logback.classic.Logger
        logger.addAppender(this)
        logger.level = Level.ALL
        this.start()
        return this
    }

    /**
     * Prepares the appender to stop monitoring.
     */
    fun tearDown() {
        events.clear()
        logger.detachAppender(this)
        this.stop()
    }

    /**
     * Get the logging event by index.
     * @param index
     */
    fun getLoggingEvent(index: Int) = events[index]

    /**
     * Get message by index.
     * @param index
     */
    fun getMessage(index: Int): String = getLoggingEvent(index).message

    override fun append(eventObject: ILoggingEvent) {
        events.add(eventObject)
    }
}