package br.com.acmattos.hdc.common.context.domain.model

import org.assertj.core.api.AbstractThrowableAssert
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatCode
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

private const val VALID_ID = "01EAQK096DJ7T2642FM17RTJJ3"
private const val INVALID_ID = "A1EAQK096DJ7T2642FINVALID"
private const val EXCEEDED_MESSAGE = "[8ZZZZZZZZZZZZZZZZZZZZZZZZZ] must not exceed '7ZZZZZZZZZZZZZZZZZZZZZZZZZ'!"

/**
 * @author ACMattos
 * @since 29/09/2021.
 */
object IdTest: Spek({
    Feature("${Id::class.java} usage") {
        Scenario("""${Id::class.java} is valid and given""") {
            lateinit var id: String
            lateinit var sid: Id
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            Given("""a valid id exists: $VALID_ID """) {
                id = VALID_ID
            }
            When("""a new ${Id::class.java} is created""") {
                assertion = assertThatCode {
                    sid = Id(id)
                }
            }
            Then("""no Exception is thrown""") {
                assertion.doesNotThrowAnyException()
            }
            And("""'id.id is equal to '$VALID_ID""") {
                assertThat(sid.id).isEqualTo(VALID_ID)
            }
        }

        Scenario("""${Id::class.java} is valid and not given""") {
            lateinit var sid: Id
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>

            When("""an new ${Id::class.java} is created without""") {
                assertion = assertThatCode {
                    sid = Id()
                }
            }
            Then("""no Exception is thrown""") {
                assertion.doesNotThrowAnyException()
            }
            And("""'tenantId.id' is similar to $VALID_ID""") {
                assertThat(sid.id).isNotBlank()
            }
        }

        Scenario("""${Id::class.java} is invalid and id is not 26 chars long""") {
            lateinit var id: String
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            Given("""an invalid id exists""") {
                id = INVALID_ID
            }
            When("""an new ${Id::class.java} is created with an invalid id""") {
                assertion = assertThatCode {
                    Id(id)
                }
            }
            Then("""${IllegalArgumentException::class.java} is thrown""") {
                assertion.hasSameClassAs(IllegalArgumentException(""))
            }
            And("""exception has message 'id [X] must be exactly 26 chars long!'""") {
                assertion.hasMessage("[A1EAQK096DJ7T2642FINVALID] does not match the expected size: 26 chars long!")
            }
        }

        Scenario("""${Id::class.java} id is invalid and id is bigger than '7ZZZZZZZZZZZZZZZZZZZZZZZZZ'""") {
            lateinit var id: String
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            Given("""an invalid id exists""") {
                id = "8ZZZZZZZZZZZZZZZZZZZZZZZZZ"
            }
            When("""an new ${Id::class.java} is created with an invalid id""") {
                assertion = assertThatCode {
                    Id(id)
                }
            }
            Then("""${IllegalArgumentException::class.java} is thrown""") {
                assertion.hasSameClassAs(IllegalArgumentException(""))
            }
            And("""exception has message $EXCEEDED_MESSAGE""") {
                assertion.hasMessage(EXCEEDED_MESSAGE)
            }
        }
    }
})
