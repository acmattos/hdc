package br.com.acmattos.hdc.common.tool.assertion

import br.com.acmattos.hdc.common.tool.loggable.LogEventsAppender
import br.com.acmattos.hdc.common.tool.server.javalin.ErrorTrackerCodeBuilder
import ch.qos.logback.classic.Level
import org.assertj.core.api.AbstractThrowableAssert
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

private const val MESSAGE = "Condition has failed!"
private const val CONTEXT = "TEST"
private const val LOG_MESSAGE_1 = "[$CONTEXT ASSERTION] - Assertion to be evaluated..."
private const val LOG_MESSAGE_2 = "[$CONTEXT ASSERTION] - Assertion evaluated successfully!"
private const val LOG_MESSAGE_3 = "[$CONTEXT ASSERTION FAILURE]: -> '$MESSAGE' <-"

/**
 * @author ACMattos
 * @since 22/09/2021.
 */
object AssertionTest: Spek({
    Feature("${Assertion::class.java} usage") {
        Scenario("assertion succeed") {
            lateinit var appender: LogEventsAppender
            var condition = false
            Given("""a prepared ${LogEventsAppender::class.java}""") {
                appender = LogEventsAppender(Assertion::class.java)
            }
            And("""a condition that evaluates to true""") {
                condition = true
            }
            When("""#assert is executed""") {
                Assertion.assert(MESSAGE, CONTEXT, ErrorTrackerCodeBuilder.build()) {
                    condition
                }
            }
            Then("""the message is $LOG_MESSAGE_1""") {
                assertThat(appender.containsMessage(LOG_MESSAGE_1)).isTrue()
            }
            And("the level is ${Level.TRACE}") {
                assertThat(appender.getLoggingEvent(0).level).isEqualTo(Level.TRACE)
            }
            And("""the message is $LOG_MESSAGE_2""") {
                assertThat(appender.containsMessage(LOG_MESSAGE_2)).isTrue()
            }
            And("the level is ${Level.TRACE}") {
                assertThat(appender.getLoggingEvent(1).level).isEqualTo(Level.TRACE)
            }
        }

        Scenario("assertion fails") {
            lateinit var appender: LogEventsAppender
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            var condition = true
            Given("""a prepared ${LogEventsAppender::class.java}""") {
                appender = LogEventsAppender(Assertion::class.java)
            }
            And("""a condition that evaluates to false""") {
                condition = false
            }
            When("""#assert is executed""") {
                assertion = Assertions.assertThatCode {
                    Assertion.assert(MESSAGE, CONTEXT, ErrorTrackerCodeBuilder.build()) {
                        condition
                    }
                }
            }
            Then("""the message is $LOG_MESSAGE_1""") {
                assertThat(appender.containsMessage(LOG_MESSAGE_1)).isTrue()
            }
            And("the level is ${Level.TRACE}") {
                assertThat(appender.getLoggingEvent(0).level).isEqualTo(Level.TRACE)
            }
            And("""the message is $LOG_MESSAGE_3""") {
                assertThat(appender.containsMessage(LOG_MESSAGE_3)).isTrue()
            }
            And("the level is ${Level.INFO}") {
                assertThat(appender.getMessageLevel(LOG_MESSAGE_3)).isEqualTo(Level.INFO)
            }
            Then("""#assert throws exception""") {
                assertion.hasSameClassAs(AssertionFailedException(MESSAGE, ErrorTrackerCodeBuilder.build()))
            }
            And("""code is ${ErrorTrackerCodeBuilder.build()}""") {
                assertion.hasFieldOrPropertyWithValue("code", ErrorTrackerCodeBuilder.build())
            }
            And("""exception has message $MESSAGE""") {
                assertion.hasMessage(MESSAGE)
            }
            And("""exception has code ${ErrorTrackerCodeBuilder.build()}""") {
                assertion.hasFieldOrPropertyWithValue("code", ErrorTrackerCodeBuilder.build())
            }
        }
    }
})
