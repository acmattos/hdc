package br.com.acmattos.hdc.person.domain.model

import org.assertj.core.api.AbstractThrowableAssert
import org.assertj.core.api.Assertions
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

private const val MESSAGE_1 = "Code is out of range (81000014-87000199)!"
private const val MESSAGE_2 = "Description length is out of range (3-120)!"

/**
 * @author ACMattos
 * @since 06/09/2022.
 */
object PersonTest: Spek({
    Feature("${Person::class.java} usage") {
        Scenario("a valid person creation") {
            lateinit var entity: Person
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            When("a successful person instantiation is done") {
                assertion = Assertions.assertThatCode {
                    entity = PersonBuilder.buildPatient()
                }
            }
            Then("no exception is raised") {
                assertion.doesNotThrowAnyException()
            }
            And("entity is not null") {
                Assertions.assertThat(entity).isNotNull()
            }
        }

//        Scenario("invalid code") {
//            var code: Int? = null
//            var entity: Person? = null
//            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
//            Given("an invalid code") {
//                code = 1
//            }
//            When("an unsuccessful Person instantiation is done") {
//                assertion = Assertions.assertThatCode {
//                    entity = PersonBuilder.buildWithInvalidCode(code!!)
//                }
//            }
//            Then("""instantiation throws exception""") {
//                assertion.hasSameClassAs(AssertionFailedException(MESSAGE_1, CODE_OUT_OF_RANGE.code))
//            }
//            And("""exception has code ${CODE_OUT_OF_RANGE.code}""") {
//                assertion.hasFieldOrPropertyWithValue("code", CODE_OUT_OF_RANGE.code)
//            }
//            And("""exception has message $MESSAGE_1""") {
//                assertion.hasMessage(MESSAGE_1)
//            }
//            And("""no entity was instantiated""") {
//                Assertions.assertThat(entity).isNull()
//            }
//        }
//
//        Scenario("invalid description") {
//            var description: String? = null
//            var entity: Person? = null
//            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
//            Given("an invalid description") {
//                description = "I"
//            }
//            When("an unsuccessful Person instantiation is done") {
//                assertion = Assertions.assertThatCode {
//                    entity = PersonBuilder.buildWithInvalidDescription(description!!)
//                }
//            }
//            Then("""instantiation throws exception""") {
//                assertion.hasSameClassAs(AssertionFailedException(MESSAGE_2, DESCRIPTION_INVALID_LENGTH.code))
//            }
//            And("""exception has code ${DESCRIPTION_INVALID_LENGTH.code}""") {
//                assertion.hasFieldOrPropertyWithValue("code", DESCRIPTION_INVALID_LENGTH.code)
//            }
//            And("""exception has message $MESSAGE_2""") {
//                assertion.hasMessage(MESSAGE_2)
//            }
//            And("""no entity was instantiated""") {
//                Assertions.assertThat(entity).isNull()
//            }
//        }
    }
})
