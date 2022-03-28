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
private const val VALID = "valid"
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
                assertion.hasSameClassAs(AssertionFailedException(MESSAGE, MessageTrackerCodeBuilder.build()))
            }
            And("""exception has message $MESSAGE""") {
                assertion.hasMessage(MESSAGE)
            }
            And("""exception has code ${MessageTrackerCodeBuilder.build()}""") {
                assertion.hasFieldOrPropertyWithValue("code", MessageTrackerCodeBuilder.build())
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
        fun convert(term: String): ValidEnum {
            return assertThatTerm(
                term,
                "[$term] does not correspond to a valid ValidEnum!",
                CONTEXT,
                MessageTrackerCodeBuilder.build()
            )
        }
    }
}
