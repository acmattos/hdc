package br.com.acmattos.hdc.common.tool.assertion

import br.com.acmattos.hdc.common.tool.loggable.LogEventsAppender
import br.com.acmattos.hdc.common.tool.server.javalin.MessageTrackerCodeBuilder
import ch.qos.logback.classic.Level
import io.kotest.core.spec.style.FreeSpec
import org.assertj.core.api.AbstractThrowableAssert
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat

private const val MESSAGE = "Condition has failed!"
private const val CONTEXT = "TEST"
private const val LOG_MESSAGE_1 = "[$CONTEXT ASSERTION <MTI>] - Assertion to be evaluated..."
private const val LOG_MESSAGE_2 = "[$CONTEXT ASSERTION <MTI>] - Assertion evaluated successfully!"
private const val LOG_MESSAGE_3 = "[$CONTEXT ASSERTION <MTI> FAILURE]: -> '$MESSAGE' <-"

/**
 * @author ACMattos
 * @since 22/09/2021.
 */
class AssertionTest: FreeSpec({
    "Feature: Assertion usage" - {
        "Scenario: assertion succeed" - {
            lateinit var appender: LogEventsAppender
            var condition = false
            "Given: a prepared ${LogEventsAppender::class.java}" {
                appender = LogEventsAppender(Assertion::class.java)
            }
            "And: a condition that evaluates to true" {
                condition = true
            }
            "When: #assert is executed" {
                Assertion.assert(MESSAGE, CONTEXT, MessageTrackerCodeBuilder.build()) {
                    condition
                }
            }
            "Then: the message is $LOG_MESSAGE_1" {
                assertThat(appender.containsMessage(LOG_MESSAGE_1)).isTrue()
            }
            "And: the level is ${Level.TRACE}" {
                assertThat(appender.getMessageLevel(LOG_MESSAGE_1)).isEqualTo(Level.TRACE)
            }
            "And: the message is $LOG_MESSAGE_2" {
                assertThat(appender.containsMessage(LOG_MESSAGE_2)).isTrue()
            }
            "And: the level is ${Level.DEBUG}" {
                assertThat(appender.getMessageLevel(LOG_MESSAGE_2)).isEqualTo(Level.DEBUG)
            }
        }

        "Scenario: assertion fails" - {
            lateinit var appender: LogEventsAppender
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            var condition = true
            "Given: a prepared ${LogEventsAppender::class.java}" {
                appender = LogEventsAppender(Assertion::class.java)
            }
            "And: a condition that evaluates to false" {
                condition = false
            }
            "When: #assert is executed" {
                assertion = Assertions.assertThatCode {
                    Assertion.assert(MESSAGE, CONTEXT, MessageTrackerCodeBuilder.build()) {
                        condition
                    }
                }
            }
            "Then: the message is $LOG_MESSAGE_1" {
                assertThat(appender.containsMessage(LOG_MESSAGE_1)).isTrue()
            }
            "And: the level is ${Level.TRACE}" {
                assertThat(appender.getMessageLevel(LOG_MESSAGE_1)).isEqualTo(Level.TRACE)
            }
            "And: the message is $LOG_MESSAGE_3" {
                assertThat(appender.containsMessage(LOG_MESSAGE_3)).isTrue()
            }
            "And: the level is ${Level.INFO}" {
                assertThat(appender.getMessageLevel(LOG_MESSAGE_3)).isEqualTo(Level.INFO)
            }
            "Then: #assert throws exception" {
                assertion.hasSameClassAs(AssertionFailedException(MESSAGE, MessageTrackerCodeBuilder.build().messageTrackerId()))
            }
            "And: code is ${MessageTrackerCodeBuilder.build()}" {
                assertion.hasFieldOrPropertyWithValue("code", MessageTrackerCodeBuilder.build().messageTrackerId)
            }
            "And: exception has message $MESSAGE" {
                assertion.hasMessage(MESSAGE)
            }
            "And: exception has code ${MessageTrackerCodeBuilder.build()}" {
                assertion.hasFieldOrPropertyWithValue("code", MessageTrackerCodeBuilder.build().messageTrackerId)
            }
        }
    }
})
