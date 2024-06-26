package br.com.acmattos.hdc.common.context.domain.model

import io.kotest.core.spec.style.FreeSpec
import org.assertj.core.api.AbstractThrowableAssert
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatCode

private const val VALID_ID = "01EAQK096DJ7T2642FM17RTJJ3"
private const val INVALID_ID = "A1EAQK096DJ7T2642FINVALID"
private const val EXCEEDED_MESSAGE = "ULID [8ZZZZZZZZZZZZZZZZZZZZZZZZZ] must not exceed '7ZZZZZZZZZZZZZZZZZZZZZZZZZ'!"

/**
 * @author ACMattos
 * @since 29/09/2021.
 */
class IdTest: FreeSpec({
    "Feature: ${Id::class.java} usage" - {
        "Scenario: ${Id::class.java} is valid and given" - {
            lateinit var id: String
            lateinit var sid: Id
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            "Given: a valid id exists: $VALID_ID " {
                id = VALID_ID
            }
            "When: a new ${Id::class.java} is created" {
                assertion = assertThatCode {
                    sid = Id(id)
                }
            }
            "Then: no Exception is thrown" {
                assertion.doesNotThrowAnyException()
            }
            "And: 'id.id is equal to '$VALID_ID" {
                assertThat(sid.id).isEqualTo(VALID_ID)
            }
            "And: 'id is equal to Id('$VALID_ID)" {
                assertThat(Id(VALID_ID).equals(sid)).isTrue()
            }
            "And: 'id hashcode is equal to -599353911" {
                assertThat(sid.hashCode()).isEqualTo(-599353911)
            }
        }

        "Scenario: ${Id::class.java} is valid and not given" - {
            lateinit var sid: Id
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>

            "When: an new ${Id::class.java} is created without" {
                assertion = assertThatCode {
                    sid = Id()
                }
            }
            "Then: no Exception is thrown" {
                assertion.doesNotThrowAnyException()
            }
            "And: 'tenantId.id' is similar to $VALID_ID" {
                assertThat(sid.id).isNotBlank()
            }
        }

        "Scenario: ${Id::class.java} is invalid and id is not 26 chars long" - {
            lateinit var id: String
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            "Given: an invalid id exists" {
                id = INVALID_ID
            }
            "When: an new ${Id::class.java} is created with an invalid id" {
                assertion = assertThatCode {
                    Id(id)
                }
            }
            "Then: ${IllegalArgumentException::class.java} is thrown" {
                assertion.hasSameClassAs(IllegalArgumentException(""))
            }
            "And: exception has message 'id [X] must be exactly 26 chars long!'" {
                assertion.hasMessage("ULID [A1EAQK096DJ7T2642FINVALID] does not match the expected size: 26 chars long!")
            }
        }

        "Scenario: ${Id::class.java} id is invalid and id is bigger than '7ZZZZZZZZZZZZZZZZZZZZZZZZZ'" - {
            lateinit var id: String
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            "Given: an invalid id exists" {
                id = "8ZZZZZZZZZZZZZZZZZZZZZZZZZ"
            }
            "When: an new ${Id::class.java} is created with an invalid id" {
                assertion = assertThatCode {
                    Id(id)
                }
            }
            "Then: ${IllegalArgumentException::class.java} is thrown" {
                assertion.hasSameClassAs(IllegalArgumentException(""))
            }
            "And: exception has message $EXCEEDED_MESSAGE" {
                assertion.hasMessage(EXCEEDED_MESSAGE)
            }
        }
    }
})
