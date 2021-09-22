package br.com.acmattos.hdc.common.tool.loggable

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.slf4j.helpers.MessageFormatter

/**
 * @author ACMattos
 * @since 12/06/2020.
 */
open class Loggable {
    val logger: HdcLogger = HdcLogger(
        this.javaClass.name.substringBefore("\$Companion")
    )
}

/**
 * @author ACMattos
 * @since 03/08/2021.
 */
class HdcLogger(className: String) {
    private val logger: Logger = LoggerFactory.getLogger(className)

    /**
     * Log TRACE messages. Samples:
     * logger.trace("Message")
     * logger.trace("Message Format {}", "usage")
     *
     * @param message or message format ("This is a sample {}, {}")
     * @param values to be placed in the message format or just don't send it.
     */
    fun trace(message: String, vararg values: String) {
        trace(message, null, *values)
    }

    /**
     * Log TRACE messages. Samples:
     * logger.trace("Message")
     * logger.trace("Message Format {}", "usage")
     * logger.trace("Message Format {}", myException, "with exception usage")
     *
     * @param message or message format ("This is a sample {}, {}")
     * @param throwable if some exception occurs.
     * @param values to be placed in the message format or just don't send it.
     */
    fun trace(message: String, throwable: Throwable?, vararg values: String) {
        log(logger.isTraceEnabled, getMessage(message, values), throwable) {
                m, t -> logger.trace(m, t)
        }
    }
    
    /**
     * Log DEBUG messages. Samples:
     * logger.debug("Message")
     * logger.debug("Message Format {}", "usage")
     *
     * @param message or message format ("This is a sample {}, {}")
     * @param values to be placed in the message format or just don't send it.
     */
    fun debug(message: String, vararg values: String) {
        debug(message, null, *values)
    }

    /**
     * Log DEBUG messages. Samples:
     * logger.debug("Message")
     * logger.debug("Message Format {}", "usage")
     * logger.debug("Message Format {}", myException, "with exception usage")
     *
     * @param message or message format ("This is a sample {}, {}")
     * @param throwable if some exception occurs.
     * @param values to be placed in the message format or just don't send it.
     */
    fun debug(message: String, throwable: Throwable?, vararg values: String) {
        log(logger.isDebugEnabled, getMessage(message, values), throwable) {
            m, t -> logger.debug(m, t)
        }
    }

    /**
     * Log INFO messages. Samples:
     * logger.info("Message")
     * logger.info("Message Format {}", "usage")
     *
     * @param message or message format ("This is a sample {}, {}")
     * @param values to be placed in the message format or just don't send it.
     */
    fun info(message: String, vararg values: String) {
        info(message, null, *values)
    }

    /**
     * Log DEBUG messages. Samples:
     * logger.info("Message")
     * logger.info("Message Format {}", "usage")
     * logger.info("Message Format {}", myException, "with exception usage")
     *
     * @param message or message format ("This is a sample {}, {}")
     * @param throwable if some exception occurs.
     * @param values to be placed in the message format or just don't send it.
     */
    fun info(message: String, throwable: Throwable?, vararg values: String) {
        log(logger.isInfoEnabled, getMessage(message, values), throwable) {
            m, t -> logger.info(m, t)
        }
    }
    
    /**
     * Log WARN messages. Samples:
     * logger.warn("Message")
     * logger.warn("Message Format {}", "usage")
     *
     * @param message or message format ("This is a sample {}, {}")
     * @param values to be placed in the message format or just don't send it.
     */
    fun warn(message: String, vararg values: String) {
        warn(message, null, *values)
    }

    /**
     * Log WARN messages. Samples:
     * logger.warn("Message")
     * logger.warn("Message Format {}", "usage")
     * logger.warn("Message Format {}", myException, "with exception usage")
     *
     * @param message or message format ("This is a sample {}, {}")
     * @param throwable if some exception occurs.
     * @param values to be placed in the message format or just don't send it.
     */
    fun warn(message: String, throwable: Throwable?, vararg values: String) {
        log(logger.isWarnEnabled, getMessage(message, values), throwable) {
            m, t -> logger.warn(m, t)
        }
    }

    /**
     * Log ERROR messages. Samples:
     * logger.error("Message")
     * logger.error("Message Format {}", "usage")
     *
     * @param message or message format ("This is a sample {}, {}")
     * @param values to be placed in the message format or just don't send it.
     */
    fun error(message: String, vararg values: String) {
        error(message, null, *values)
    }

    /**
     * Log ERROR messages. Samples:
     * logger.error("Message")
     * logger.error("Message Format {}", "usage")
     * logger.error("Message Format {}", myException, "with exception usage")
     *
     * @param message or message format ("This is a sample {}, {}")
     * @param throwable if some exception occurs.
     * @param values to be placed in the message format or just don't send it.
     */
    fun error(message: String, throwable: Throwable?, vararg values: String) {
        log(logger.isErrorEnabled, getMessage(message, values), throwable) {
                m, t -> logger.error(m, t)
        }
    }
    
    private fun getMessage(message: String, values: Array<out String>) =
        MessageFormatter.arrayFormat(message, values).message

    private fun log(
        enabled: Boolean, 
        message: String, 
        throwable: Throwable?,
        block: (String, Throwable?) -> Unit) {
        if(enabled) {
            block(message, throwable)
        }
    }
}
