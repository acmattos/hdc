package br.com.acmattos.hdc.common.tool.loggable

import ch.qos.logback.classic.Level
import io.kotest.core.spec.style.FreeSpec
import org.assertj.core.api.Assertions.assertThat

const val TRACE_MESSAGE = "This is a trace message sample"
const val DEBUG_MESSAGE = "This is a debug message sample"
const val INFO_MESSAGE = "This is a info message sample"
const val WARN_MESSAGE = "This is a warn message sample"
const val ERROR_MESSAGE = "This is a error message sample"

/**
 * @author ACMattos
 * @since 03/08/2021.
 */
class HdcLoggerTest: FreeSpec({
    "Feature: ${HdcLogger::class.java} only message usage" - {
        "Scenario: trace message generation" - {
            lateinit var logger: HdcLogger
            lateinit var appender: LogEventsAppender
             "Given: a prepared ${LogEventsAppender::class.java}" - {
                appender = LogEventsAppender(HdcLogger::class.java)
            }
            "And: a successful ${HdcLogger::class.java} instantiation" {
                logger = HdcLogger(HdcLogger::class.java.name)
            }
            "When:#trace is executed" {
                logger.trace(TRACE_MESSAGE)
            }
            "Then:the message is $TRACE_MESSAGE" {
                assertThat(appender.containsMessage(TRACE_MESSAGE)).isTrue()
            }
            "And: the level is ${Level.TRACE}" {
                assertThat(appender.getMessageLevel(TRACE_MESSAGE)).isEqualTo(Level.TRACE)
            }
        }

        "Scenario: trace message not being logged due to Level is higher" - {
            lateinit var logger: HdcLogger
            lateinit var appender: LogEventsAppender
             "Given: a prepared ${LogEventsAppender::class.java} with level higher" {
                appender = LogEventsAppender(HdcLogger::class.java)
                    .setLevel(Level.ERROR)
            }
            "And: a successful ${HdcLogger::class.java} instantiation" {
                logger = HdcLogger(HdcLogger::class.java.name)
            }
            "When:#trace is executed" {
                logger.trace(TRACE_MESSAGE)
            }
            "Then:the size of events is zero" {
                assertThat(appender.eventsSize()).isEqualTo(0)
            }
        }

        "Scenario: debug message generation" - {
            lateinit var logger: HdcLogger
            lateinit var appender: LogEventsAppender
             "Given: a prepared ${LogEventsAppender::class.java}" {
                appender = LogEventsAppender(HdcLogger::class.java)
            }
            "And: a successful ${HdcLogger::class.java} instantiation" {
                logger = HdcLogger(HdcLogger::class.java.name)
            }
            "When:#debug is executed" {
                logger.debug(DEBUG_MESSAGE)
            }
            "Then:the message is $DEBUG_MESSAGE" {
                assertThat(appender.containsMessage(DEBUG_MESSAGE)).isTrue()
            }
            "And: the level is ${Level.DEBUG}" {
                assertThat(appender.getMessageLevel(DEBUG_MESSAGE)).isEqualTo(Level.DEBUG)
            }
        }

        "Scenario: info message generation" - {
            lateinit var logger: HdcLogger
            lateinit var appender: LogEventsAppender
             "Given: a prepared ${LogEventsAppender::class.java}" {
                appender = LogEventsAppender(HdcLogger::class.java)
            }
            "And: a successful ${HdcLogger::class.java} instantiation" {
                logger = HdcLogger(HdcLogger::class.java.name)
            }
            "When:#info is executed" {
                logger.info(INFO_MESSAGE)
            }
            "Then:the message is $INFO_MESSAGE" {
                assertThat(appender.containsMessage(INFO_MESSAGE)).isTrue()
            }
            "And: the level is ${Level.INFO}" {
                assertThat(appender.getMessageLevel(INFO_MESSAGE)).isEqualTo(Level.INFO)
            }
        }

        "Scenario: warn message generation" - {
            lateinit var logger: HdcLogger
            lateinit var appender: LogEventsAppender
             "Given: a prepared ${LogEventsAppender::class.java}" {
                appender = LogEventsAppender(HdcLogger::class.java)
            }
            "And: a successful ${HdcLogger::class.java} instantiation" {
                logger = HdcLogger(HdcLogger::class.java.name)
            }
            "When:#warn is executed" {
                logger.warn(WARN_MESSAGE)
            }
            "Then:the message is $WARN_MESSAGE" {
                assertThat(appender.containsMessage(WARN_MESSAGE)).isTrue()
            }
            "And: the level is ${Level.WARN}" {
                assertThat(appender.getMessageLevel(WARN_MESSAGE)).isEqualTo(Level.WARN)
            }
        }

        "Scenario: error message generation" - {
            lateinit var logger: HdcLogger
            lateinit var appender: LogEventsAppender
             "Given: a prepared ${LogEventsAppender::class.java}" {
                appender = LogEventsAppender(HdcLogger::class.java)
            }
            "And: a successful ${HdcLogger::class.java} instantiation" {
                logger = HdcLogger(HdcLogger::class.java.name)
            }
            "When:#error is executed" {
                logger.error(ERROR_MESSAGE)
            }
            "Then:the message is $ERROR_MESSAGE" {
                assertThat(appender.containsMessage(ERROR_MESSAGE)).isTrue()
            }
            "And: the level is ${Level.ERROR}" {
                assertThat(appender.getMessageLevel(ERROR_MESSAGE)).isEqualTo(Level.ERROR)
            }
        }
    }

    "Feature: ${HdcLogger::class.java} message with exception usage" - {
        "Scenario: trace message generation" - {
            lateinit var logger: HdcLogger
            lateinit var appender: LogEventsAppender
            lateinit var exception: Exception
             "Given: a prepared ${LogEventsAppender::class.java}" {
                appender = LogEventsAppender(HdcLogger::class.java)
            }
            "And: a successful ${HdcLogger::class.java} instantiation" {
                logger = HdcLogger(HdcLogger::class.java.name)
            }
            "And: an exception is generated" {
                exception = Exception("This is an exception")
            }
            "When:#trace is executed" {
                logger.trace(TRACE_MESSAGE, exception)
            }
            "Then:the message is $TRACE_MESSAGE" {
                assertThat(appender.containsMessage(TRACE_MESSAGE)).isTrue()
            }
            "And: the level is ${Level.TRACE}" {
                assertThat(appender.getMessageLevel(TRACE_MESSAGE)).isEqualTo(Level.TRACE)
            }
        }

        "Scenario: debug message generation" - {
            lateinit var logger: HdcLogger
            lateinit var appender: LogEventsAppender
            lateinit var exception: Exception
             "Given: a prepared ${LogEventsAppender::class.java}" {
                appender = LogEventsAppender(HdcLogger::class.java)
            }
            "And: a successful ${HdcLogger::class.java} instantiation" {
                logger = HdcLogger(HdcLogger::class.java.name)
            }
            "And: an exception is generated" {
                exception = Exception("This is an exception")
            }
            "When:#debug is executed" {
                logger.debug(DEBUG_MESSAGE, exception)
            }
            "Then:the message is $DEBUG_MESSAGE" {
                assertThat(appender.containsMessage(DEBUG_MESSAGE)).isTrue()
            }
            "And: the level is ${Level.DEBUG}" {
                assertThat(appender.getMessageLevel(DEBUG_MESSAGE)).isEqualTo(Level.DEBUG)
            }
        }

        "Scenario: info message generation" - {
            lateinit var logger: HdcLogger
            lateinit var appender: LogEventsAppender
            lateinit var exception: Exception
             "Given: a prepared ${LogEventsAppender::class.java}" {
                appender = LogEventsAppender(HdcLogger::class.java)
            }
            "And: a successful ${HdcLogger::class.java} instantiation" {
                logger = HdcLogger(HdcLogger::class.java.name)
            }
            "And: an exception is generated" {
                exception = Exception("This is an exception")
            }
            "When:#info is executed" {
                logger.info(INFO_MESSAGE, exception)
            }
            "Then:the message is $INFO_MESSAGE" {
                assertThat(appender.containsMessage(INFO_MESSAGE)).isTrue()
            }
            "And: the level is ${Level.INFO}" {
                assertThat(appender.getMessageLevel(INFO_MESSAGE)).isEqualTo(Level.INFO)
            }
        }

        "Scenario: warn message generation" - {
            lateinit var logger: HdcLogger
            lateinit var appender: LogEventsAppender
            lateinit var exception: Exception
             "Given: a prepared ${LogEventsAppender::class.java}" {
                appender = LogEventsAppender(HdcLogger::class.java)
            }
            "And: a successful ${HdcLogger::class.java} instantiation" {
                logger = HdcLogger(HdcLogger::class.java.name)
            }
            "And: an exception is generated" {
                exception = Exception("This is an exception")
            }
            "When:#warn is executed" {
                logger.warn(WARN_MESSAGE, exception)
            }
            "Then:the message is $WARN_MESSAGE" {
                assertThat(appender.containsMessage(WARN_MESSAGE)).isTrue()
            }
            "And: the level is ${Level.WARN}" {
                assertThat(appender.getMessageLevel(WARN_MESSAGE)).isEqualTo(Level.WARN)
            }
        }

        "Scenario: error message generation" - {
            lateinit var logger: HdcLogger
            lateinit var appender: LogEventsAppender
            lateinit var exception: Exception
            "Given: a prepared ${LogEventsAppender::class.java}" {
                appender = LogEventsAppender(HdcLogger::class.java)
            }
            "And: a successful ${HdcLogger::class.java} instantiation" {
                logger = HdcLogger(HdcLogger::class.java.name)
            }
            "And: an exception is generated" {
                exception = Exception("This is an exception")
            }
            "When:#error is executed" {
                logger.error(ERROR_MESSAGE, exception)
            }
            "Then:the message is $ERROR_MESSAGE" {
                assertThat(appender.containsMessage(ERROR_MESSAGE)).isTrue()
            }
            "And: the level is ${Level.ERROR}" {
                assertThat(appender.getMessageLevel(ERROR_MESSAGE)).isEqualTo(Level.ERROR)
            }
        }
    }
})
