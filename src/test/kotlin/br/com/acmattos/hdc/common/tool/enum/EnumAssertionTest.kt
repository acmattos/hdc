package br.com.acmattos.hdc.common.tool.enum

import br.com.acmattos.hdc.common.tool.assertion.AssertionFailedException
import br.com.acmattos.hdc.common.tool.server.javalin.MessageTrackerCodeBuilder
import org.assertj.core.api.AbstractThrowableAssert
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

private const val CONTEXT = "TEST"
private const val MESSAGE = "[invalid] does not correspond to a valid ValidEnum!"
private const val MESSAGE_MATCHES = "[invalid] does not correspond to a valid ValidMatchEnum!"
private const val VALID = "valid"
private const val VALID_MATCHES = "v"
private const val INVALID = "invalid"

/**
 * @author ACMattos
 * @since 26/10/2021.
 */
object EnumAssertionTest: Spek({
    Feature("EnumAssertionTest usage") {
        Scenario("#assertThatTerm succeed") {
            lateinit var term: String
            lateinit var enum: ValidEnum
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            Given("""a valid term: $VALID""") {
                term = VALID
            }
            When("""#convert is executed""") {
                assertion = Assertions.assertThatCode {
                    enum = ValidEnum.convert(term)
                }
            }
            Then("""#convert throws no exception""") {
                assertion.doesNotThrowAnyException()
            }
            And("""term is equal to ${ValidEnum.VALID}""") {
                assertThat(term.toUpperCase()).isEqualTo(ValidEnum.VALID.name)
            }
            And("""enum is not null""") {
                assertThat(enum).isNotNull()
            }
        }

        Scenario("#assertThatTerm throws exception") {
            lateinit var term: String
            var enum: ValidEnum? = null
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            Given("""a invalid term: $INVALID""") {
                term = INVALID
            }
            When("""#convert is executed""") {
                assertion = Assertions.assertThatCode {
                    enum = ValidEnum.convert(term)
                }
            }
            Then("""#convert throws exception""") {
                assertion.hasSameClassAs(AssertionFailedException(MESSAGE, MessageTrackerCodeBuilder.build().messageTrackerId))
            }
            And("""exception has message $MESSAGE""") {
                assertion.hasMessage(MESSAGE)
            }
            And("""exception has code ${MessageTrackerCodeBuilder.build()}""") {
                assertion.hasFieldOrPropertyWithValue("code", MessageTrackerCodeBuilder.build().messageTrackerId)
            }
            And("""no enum was retrieved""") {
                assertThat(enum).isNull()
            }
        }

        Scenario("#assertThatTermMatches succeed") {
            lateinit var term: String
            lateinit var enum: ValidMatchEnum
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            Given("""a valid term: $VALID_MATCHES""") {
                term = VALID_MATCHES
            }
            When("""#convert is executed""") {
                assertion = Assertions.assertThatCode {
                    enum = ValidMatchEnum.convert(term)
                }
            }
            Then("""#convert throws no exception""") {
                assertion.doesNotThrowAnyException()
            }
            And("""term is equal to ${ValidMatchEnum.VALID}""") {
                assertThat(term.toUpperCase()).isEqualTo(ValidMatchEnum.VALID.anyName)
            }
            And("""enum is not null""") {
                assertThat(enum).isNotNull()
            }
        }

        Scenario("#assertThatTermMatches throws exception") {
            lateinit var term: String
            var enum: ValidMatchEnum? = null
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            Given("""a invalid term: $INVALID""") {
                term = INVALID
            }
            When("""#convert is executed""") {
                assertion = Assertions.assertThatCode {
                    enum = ValidMatchEnum.convert(term)
                }
            }
            Then("""#convert throws exception""") {
                assertion.hasSameClassAs(AssertionFailedException(MESSAGE_MATCHES, MessageTrackerCodeBuilder.build().messageTrackerId))
            }
            And("""exception has message $MESSAGE_MATCHES""") {
                assertion.hasMessage(MESSAGE_MATCHES)
            }
            And("""exception has code ${MessageTrackerCodeBuilder.build()}""") {
                assertion.hasFieldOrPropertyWithValue("code", MessageTrackerCodeBuilder.build().messageTrackerId)
            }
            And("""no enum was retrieved""") {
                assertThat(enum).isNull()
            }
        }
    }
})

enum class ValidEnum {
    VALID;
    companion object {
        fun convert(term: String): ValidEnum = assertThatTerm(
            term,
            "[$term] does not correspond to a valid ValidEnum!",
            CONTEXT,
            MessageTrackerCodeBuilder.build()
        )
    }
}

enum class ValidMatchEnum(val anyName: String) {
    VALID("V");
    companion object {
        fun convert(term: String): ValidMatchEnum = assertThatTermMatches(
            term,
            "[$term] does not correspond to a valid ValidMatchEnum!",
            CONTEXT,
            MessageTrackerCodeBuilder.build()
        ) { validMatchEnum, anyNameParam ->
            validMatchEnum.anyName == anyNameParam.toUpperCase()
        }
    }
}