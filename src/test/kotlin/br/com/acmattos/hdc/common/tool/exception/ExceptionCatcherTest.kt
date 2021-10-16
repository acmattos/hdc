package br.com.acmattos.hdc.common.tool.exception

import br.com.acmattos.hdc.common.tool.exception.ExceptionCatcher.catch
import br.com.acmattos.hdc.common.tool.loggable.LogEventsAppender
import ch.qos.logback.classic.Level
import org.assertj.core.api.AbstractThrowableAssert
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatCode
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

private const val LOGGER_MESSAGE = "[TEST] - Trace Message"
private const val EXCEPTION_MESSAGE = "Exception Message"
private const val LOGGER_EXCEPTION_MESSAGE = "$EXCEPTION_MESSAGE > [CATCHER] $LOGGER_MESSAGE"
private const val CAUSE_MESSAGE = "Cause Message"
private const val LOGGER_CAUSE_MESSAGE = "$CAUSE_MESSAGE > [CATCHER] $LOGGER_MESSAGE"
private const val NO_MESSAGE = "No message was provided for this exception!"
private const val LOGGER_NO_MESSAGE = "$NO_MESSAGE > [CATCHER] $LOGGER_MESSAGE"

/**
 * @author ACMattos
 * @since 01/10/2021.
 */
object ExceptionCatcherTest: Spek({
    Feature("${ExceptionCatcher::class.java} usage") {
        Scenario("ex.message was found") {
            lateinit var block: () -> Exception
            lateinit var appender: LogEventsAppender
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            Given("""a block that throws an ${Exception::class.java}""") {
                block = { throw Exception(EXCEPTION_MESSAGE) }
            }
            And("""a prepared ${LogEventsAppender::class.java}""") {
                appender = LogEventsAppender(ExceptionCatcher::class.java)
            }
            When("""#catch is executed""") {
                assertion = assertThatCode {
                    catch("[{}] - Trace Message", "TEST") {
                        block()
                    }
                }
            }
            Then("""${InternalServerErrorException::class.java} is raised""") {
                assertion.hasSameClassAs(InternalServerErrorException(
                    EXCEPTION_MESSAGE,
                    Exception(EXCEPTION_MESSAGE)
                ))
            }
            And("""message is $EXCEPTION_MESSAGE""") {
                assertion.hasMessage(EXCEPTION_MESSAGE)
            }
            And("""the message is $LOGGER_MESSAGE""") {
                assertThat(appender.getMessage(0)).isEqualTo(LOGGER_MESSAGE)
            }
            And("the level is ${Level.TRACE}") {
                assertThat(appender.getLoggingEvent(0).level).isEqualTo(Level.TRACE)
            }
            And("""the message is $LOGGER_EXCEPTION_MESSAGE""") {
                assertThat(appender.getMessage(1)).isEqualTo(LOGGER_EXCEPTION_MESSAGE)
            }
            And("the level is ${Level.ERROR}") {
                assertThat(appender.getLoggingEvent(1).level).isEqualTo(Level.ERROR)
            }
            And("there is no more log messages") {
                assertThat(appender.eventsSize()).isEqualTo(2)
            }
        }

        Scenario("ex.cause.message was found") {
            lateinit var block: () -> Exception
            lateinit var appender: LogEventsAppender
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            Given("""a block that throws an ${Exception::class.java}""") {
                block = { throw Exception(Exception(CAUSE_MESSAGE)) }
            }
            And("""a prepared ${LogEventsAppender::class.java}""") {
                appender = LogEventsAppender(ExceptionCatcher::class.java)
            }
            When("""#catch is executed""") {
                assertion = assertThatCode {
                    catch("[{}] - Trace Message", "TEST") {
                        block()
                    }
                }
            }
            Then("""${InternalServerErrorException::class.java} is raised""") {
                assertion.hasSameClassAs(InternalServerErrorException(
                    CAUSE_MESSAGE,
                    Exception(Exception(CAUSE_MESSAGE))
                ))
            }
            And("""message is $CAUSE_MESSAGE""") {
                assertion.hasMessage(CAUSE_MESSAGE)
            }
            And("""the message is $LOGGER_MESSAGE""") {
                assertThat(appender.getMessage(0)).isEqualTo(LOGGER_MESSAGE)
            }
            And("the level is ${Level.TRACE}") {
                assertThat(appender.getLoggingEvent(0).level).isEqualTo(Level.TRACE)
            }
            And("""the message is $LOGGER_CAUSE_MESSAGE""") {
                assertThat(appender.getMessage(1)).isEqualTo(LOGGER_CAUSE_MESSAGE)
            }
            And("the level is ${Level.ERROR}") {
                assertThat(appender.getLoggingEvent(1).level).isEqualTo(Level.ERROR)
            }
            And("there is no more log messages") {
                assertThat(appender.eventsSize()).isEqualTo(2)
            }
        }

        Scenario("no ex message was found") {
            lateinit var block: () -> Exception
            lateinit var appender: LogEventsAppender
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            Given("""a block that throws an ${Exception::class.java}""") {
                block = { throw Exception() }
            }
            And("""a prepared ${LogEventsAppender::class.java}""") {
                appender = LogEventsAppender(ExceptionCatcher::class.java)
            }
            When("""#catch is executed""") {
                assertion = assertThatCode {
                    catch("[{}] - Trace Message", "TEST") {
                        block()
                    }
                }
            }
            Then("""${InternalServerErrorException::class.java} is raised""") {
                assertion.hasSameClassAs(InternalServerErrorException(
                    NO_MESSAGE,
                    Exception()
                ))
            }
            And("""message is $NO_MESSAGE""") {
                assertion.hasMessage(NO_MESSAGE)
            }
            And("""the message is $LOGGER_MESSAGE""") {
                assertThat(appender.getMessage(0)).isEqualTo(LOGGER_MESSAGE)
            }
            And("the level is ${Level.TRACE}") {
                assertThat(appender.getLoggingEvent(0).level).isEqualTo(Level.TRACE)
            }
            And("""the message is $LOGGER_NO_MESSAGE""") {
                assertThat(appender.getMessage(1)).isEqualTo(LOGGER_NO_MESSAGE)
            }
            And("the level is ${Level.ERROR}") {
                assertThat(appender.getLoggingEvent(1).level).isEqualTo(Level.ERROR)
            }
            And("there is no more log messages") {
                assertThat(appender.eventsSize()).isEqualTo(2)
            }
        }

        Scenario("no ${InternalServerErrorException::class.java} thrown") {
            lateinit var block: () -> Unit
            lateinit var appender: LogEventsAppender
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            Given("""a block that does not throw any ${Exception::class.java}""") {
                block = { true }
            }
            And("""a prepared ${LogEventsAppender::class.java}""") {
                appender = LogEventsAppender(ExceptionCatcher::class.java)
            }
            When("""#catch is executed""") {
                assertion = assertThatCode {
                    catch("[{}] - Trace Message", "TEST") {
                        block()
                    }
                }
            }
            Then("""${InternalServerErrorException::class.java} is raised""") {
                assertion.doesNotThrowAnyException()
            }
            And("""the message is $LOGGER_MESSAGE""") {
                assertThat(appender.getMessage(0)).isEqualTo(LOGGER_MESSAGE)
            }
            And("the level is ${Level.TRACE}") {
                assertThat(appender.getLoggingEvent(0).level).isEqualTo(Level.TRACE)
            }
            And("there is no more log messages") {
                assertThat(appender.eventsSize()).isEqualTo(1)
            }
        }
    }
})
