package br.com.acmattos.hdc.procedure.domain.model

import br.com.acmattos.hdc.common.tool.assertion.AssertionFailedException
import br.com.acmattos.hdc.procedure.config.MessageTrackerIdEnum.DESCRIPTION_INVALID_LENGTH
import br.com.acmattos.hdc.procedure.config.MessageTrackerIdEnum.Id_OUT_OF_RANGE
import org.assertj.core.api.AbstractThrowableAssert
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatCode
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

private const val MESSAGE_1 = "Code is out of range (81000014-87000199)!"
private const val MESSAGE_2 = "Description length is out of range (3-120)!"

/**
 * @author ACMattos
 * @since 30/03/2022.
 */
object ProcedureTest: Spek({
    Feature("${Procedure::class.java} usage") {
        Scenario("a valid procedure creation") {
            lateinit var entity: Procedure
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            When("a successful procedure instantiation is done") {
                assertion = assertThatCode {
                    entity = ProcedureBuilder.build()
                }
            }
            Then("no exception is raised") {
                assertion.doesNotThrowAnyException()
            }
            And("entity is not null") {
                assertThat(entity).isNotNull()
            }
        }

        Scenario("invalid messageTrackerId") {
            var messageTrackerId: Int? = null
            var entity: Procedure? = null
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            Given("an invalid messageTrackerId") {
                messageTrackerId = 1
            }
            When("an unsuccessful procedure instantiation is done") {
                assertion = assertThatCode {
                    entity = ProcedureBuilder.buildWithInvalidCode(messageTrackerId!!)
                }
            }
            Then("""instantiation throws exception""") {
                assertion.hasSameClassAs(AssertionFailedException(MESSAGE_1, Id_OUT_OF_RANGE.messageTrackerId))
            }
            And("""exception has messageTrackerId ${Id_OUT_OF_RANGE.messageTrackerId}""") {
                assertion.hasFieldOrPropertyWithValue("messageTrackerId", Id_OUT_OF_RANGE.messageTrackerId)
            }
            And("""exception has message $MESSAGE_1""") {
                assertion.hasMessage(MESSAGE_1)
            }
            And("""no entity was instantiated""") {
                assertThat(entity).isNull()
            }
        }

        Scenario("invalid description") {
            var description: String? = null
            var entity: Procedure? = null
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            Given("an invalid description") {
                description = "I"
            }
            When("an unsuccessful procedure instantiation is done") {
                assertion = assertThatCode {
                    entity = ProcedureBuilder.buildWithInvalidDescription(description!!)
                }
            }
            Then("""instantiation throws exception""") {
                assertion.hasSameClassAs(AssertionFailedException(MESSAGE_2, DESCRIPTION_INVALID_LENGTH.messageTrackerId))
            }
            And("""exception has messageTrackerId ${DESCRIPTION_INVALID_LENGTH.messageTrackerId}""") {
                assertion.hasFieldOrPropertyWithValue("messageTrackerId", DESCRIPTION_INVALID_LENGTH.messageTrackerId)
            }
            And("""exception has message $MESSAGE_2""") {
                assertion.hasMessage(MESSAGE_2)
            }
            And("""no entity was instantiated""") {
                assertThat(entity).isNull()
            }
        }
    }
})
