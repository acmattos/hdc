package br.com.acmattos.hdc.odontogram.domain.model

import org.assertj.core.api.AbstractThrowableAssert
import org.assertj.core.api.Assertions
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

private const val MESSAGE_1 = "Code is out of range (81000014-87000199)!"
private const val MESSAGE_2 = "Description length is out of range (3-120)!"

/**
 * @author ACMattos
 * @since 29/08/2022.
 */
object MetaOdontogramTest: Spek({
    Feature("${MetaOdontogram::class.java} usage") {
        Scenario("a valid MetaOdontogram creation") {
            lateinit var entity: MetaOdontogram
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            When("a successful MetaOdontogram instantiation is done") {
                assertion = Assertions.assertThatCode {
                    entity = MetaOdontogramBuilder.build()
                }
            }
            Then("no exception is raised") {
                assertion.doesNotThrowAnyException()
            }
            And("entity is not null") {
                Assertions.assertThat(entity).isNotNull()
            }
        }

//        Scenario("invalid tooth") {
//            var code: Int? = null
//            var entity: MetaOdontogram? = null
//            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
//            Given("an invalid code") {
//                code = 1
//            }
//            When("an unsuccessful MetaOdontogram instantiation is done") {
//                assertion = Assertions.assertThatCode {
//                    entity = MetaOdontogramBuilder.buildWithInvalidCode(code!!)
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
    }
})
