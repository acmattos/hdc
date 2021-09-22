package br.com.acmattos.hdc.common.tool.loggable

import ch.qos.logback.classic.Level
import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

const val TRACE_MESSAGE = "This is a trace message sample"
const val DEBUG_MESSAGE = "This is a debug message sample"
const val INFO_MESSAGE = "This is a info message sample"
const val WARN_MESSAGE = "This is a warn message sample"
const val ERROR_MESSAGE = "This is a error message sample"

/**
 * @author ACMattos
 * @since 03/08/2021.
 */
object HdcLoggerTest: Spek({
    Feature("${HdcLogger::class.java} only message usage") {
        Scenario("trace message generation") {
            lateinit var logger: HdcLogger
            lateinit var appender: LogEventsAppender
            Given("""a prepared ${LogEventsAppender::class.java}""") {
                appender = LogEventsAppender(HdcLogger::class.java)
            }
            And("""a successful ${HdcLogger::class.java} instantiation""") {
                logger = HdcLogger(HdcLogger::class.java.name)
            }
            When("""#trace is executed""") {
                logger.trace(TRACE_MESSAGE)
            }
            Then("""the message is $TRACE_MESSAGE""") {
                assertThat(appender.getMessage(0)).isEqualTo(TRACE_MESSAGE)
            }
            And("the level is ${Level.TRACE}") {
                assertThat(appender.getLoggingEvent(0).level).isEqualTo(Level.TRACE)
            }
        }

        Scenario("debug message generation") {
            lateinit var logger: HdcLogger
            lateinit var appender: LogEventsAppender
            Given("""a prepared ${LogEventsAppender::class.java}""") {
                appender = LogEventsAppender(HdcLogger::class.java)
            }
            And("""a successful ${HdcLogger::class.java} instantiation""") {
                logger = HdcLogger(HdcLogger::class.java.name)
            }
            When("""#debug is executed""") {
                logger.debug(DEBUG_MESSAGE)
            }
            Then("""the message is $DEBUG_MESSAGE""") {
                assertThat(appender.getMessage(0)).isEqualTo(DEBUG_MESSAGE)
            }
            And("the level is ${Level.DEBUG}") {
                assertThat(appender.getLoggingEvent(0).level).isEqualTo(Level.DEBUG)
            }
        }

        Scenario("info message generation") {
            lateinit var logger: HdcLogger
            lateinit var appender: LogEventsAppender
            Given("""a prepared ${LogEventsAppender::class.java}""") {
                appender = LogEventsAppender(HdcLogger::class.java)
            }
            And("""a successful ${HdcLogger::class.java} instantiation""") {
                logger = HdcLogger(HdcLogger::class.java.name)
            }
            When("""#info is executed""") {
                logger.info(INFO_MESSAGE)
            }
            Then("""the message is $INFO_MESSAGE""") {
                assertThat(appender.getMessage(0)).isEqualTo(INFO_MESSAGE)
            }
            And("the level is ${Level.INFO}") {
                assertThat(appender.getLoggingEvent(0).level).isEqualTo(Level.INFO)
            }
        }

        Scenario("warn message generation") {
            lateinit var logger: HdcLogger
            lateinit var appender: LogEventsAppender
            Given("""a prepared ${LogEventsAppender::class.java}""") {
                appender = LogEventsAppender(HdcLogger::class.java)
            }
            And("""a successful ${HdcLogger::class.java} instantiation""") {
                logger = HdcLogger(HdcLogger::class.java.name)
            }
            When("""#warn is executed""") {
                logger.warn(WARN_MESSAGE)
            }
            Then("""the message is $WARN_MESSAGE""") {
                assertThat(appender.getMessage(0)).isEqualTo(WARN_MESSAGE)
            }
            And("the level is ${Level.WARN}") {
                assertThat(appender.getLoggingEvent(0).level).isEqualTo(Level.WARN)
            }
        }

        Scenario("error message generation") {
            lateinit var logger: HdcLogger
            lateinit var appender: LogEventsAppender
            Given("""a prepared ${LogEventsAppender::class.java}""") {
                appender = LogEventsAppender(HdcLogger::class.java)
            }
            And("""a successful ${HdcLogger::class.java} instantiation""") {
                logger = HdcLogger(HdcLogger::class.java.name)
            }
            When("""#error is executed""") {
                logger.error(ERROR_MESSAGE)
            }
            Then("""the message is $ERROR_MESSAGE""") {
                assertThat(appender.getMessage(0)).isEqualTo(ERROR_MESSAGE)
            }
            And("the level is ${Level.ERROR}") {
                assertThat(appender.getLoggingEvent(0).level).isEqualTo(Level.ERROR)
            }
        }
    }

    Feature("${HdcLogger::class.java} message with exception usage") {
        Scenario("trace message generation") {
            lateinit var logger: HdcLogger
            lateinit var appender: LogEventsAppender
            lateinit var exception: Exception
            Given("""a prepared ${LogEventsAppender::class.java}""") {
                appender = LogEventsAppender(HdcLogger::class.java)
            }
            And("""a successful ${HdcLogger::class.java} instantiation""") {
                logger = HdcLogger(HdcLogger::class.java.name)
            }
            And("an exception is generated") {
                exception = Exception("This is an exception")
            }
            When("""#trace is executed""") {
                logger.trace(TRACE_MESSAGE, exception)
            }
            Then("""the message is $TRACE_MESSAGE""") {
                assertThat(appender.getMessage(0)).isEqualTo(TRACE_MESSAGE)
            }
            And("the level is ${Level.TRACE}") {
                assertThat(appender.getLoggingEvent(0).level).isEqualTo(Level.TRACE)
            }
        }

        Scenario("debug message generation") {
            lateinit var logger: HdcLogger
            lateinit var appender: LogEventsAppender
            lateinit var exception: Exception
            Given("""a prepared ${LogEventsAppender::class.java}""") {
                appender = LogEventsAppender(HdcLogger::class.java)
            }
            And("""a successful ${HdcLogger::class.java} instantiation""") {
                logger = HdcLogger(HdcLogger::class.java.name)
            }
            And("an exception is generated") {
                exception = Exception("This is an exception")
            }
            When("""#debug is executed""") {
                logger.debug(DEBUG_MESSAGE, exception)
            }
            Then("""the message is $DEBUG_MESSAGE""") {
                assertThat(appender.getMessage(0)).isEqualTo(DEBUG_MESSAGE)
            }
            And("the level is ${Level.DEBUG}") {
                assertThat(appender.getLoggingEvent(0).level).isEqualTo(Level.DEBUG)
            }
        }

        Scenario("info message generation") {
            lateinit var logger: HdcLogger
            lateinit var appender: LogEventsAppender
            lateinit var exception: Exception
            Given("""a prepared ${LogEventsAppender::class.java}""") {
                appender = LogEventsAppender(HdcLogger::class.java)
            }
            And("""a successful ${HdcLogger::class.java} instantiation""") {
                logger = HdcLogger(HdcLogger::class.java.name)
            }
            And("an exception is generated") {
                exception = Exception("This is an exception")
            }
            When("""#info is executed""") {
                logger.info(INFO_MESSAGE, exception)
            }
            Then("""the message is $INFO_MESSAGE""") {
                assertThat(appender.getMessage(0)).isEqualTo(INFO_MESSAGE)
            }
            And("the level is ${Level.INFO}") {
                assertThat(appender.getLoggingEvent(0).level).isEqualTo(Level.INFO)
            }
        }

        Scenario("warn message generation") {
            lateinit var logger: HdcLogger
            lateinit var appender: LogEventsAppender
            lateinit var exception: Exception
            Given("""a prepared ${LogEventsAppender::class.java}""") {
                appender = LogEventsAppender(HdcLogger::class.java)
            }
            And("""a successful ${HdcLogger::class.java} instantiation""") {
                logger = HdcLogger(HdcLogger::class.java.name)
            }
            And("an exception is generated") {
                exception = Exception("This is an exception")
            }
            When("""#warn is executed""") {
                logger.warn(WARN_MESSAGE, exception)
            }
            Then("""the message is $WARN_MESSAGE""") {
                assertThat(appender.getMessage(0)).isEqualTo(WARN_MESSAGE)
            }
            And("the level is ${Level.WARN}") {
                assertThat(appender.getLoggingEvent(0).level).isEqualTo(Level.WARN)
            }
        }

        Scenario("error message generation") {
            lateinit var logger: HdcLogger
            lateinit var appender: LogEventsAppender
            lateinit var exception: Exception
            Given("""a prepared ${LogEventsAppender::class.java}""") {
                appender = LogEventsAppender(HdcLogger::class.java)
            }
            And("""a successful ${HdcLogger::class.java} instantiation""") {
                logger = HdcLogger(HdcLogger::class.java.name)
            }
            And("an exception is generated") {
                exception = Exception("This is an exception")
            }
            When("""#error is executed""") {
                logger.error(ERROR_MESSAGE, exception)
            }
            Then("""the message is $ERROR_MESSAGE""") {
                assertThat(appender.getMessage(0)).isEqualTo(ERROR_MESSAGE)
            }
            And("the level is ${Level.ERROR}") {
                assertThat(appender.getLoggingEvent(0).level).isEqualTo(Level.ERROR)
            }
        }
    }
})
