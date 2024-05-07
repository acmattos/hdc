package br.com.acmattos.hdc.procedure.domain.model

import br.com.acmattos.hdc.common.tool.assertion.AssertionFailedException
import br.com.acmattos.hdc.procedure.config.MessageTrackerIdEnum.DESCRIPTION_INVALID_LENGTH
import br.com.acmattos.hdc.procedure.config.MessageTrackerIdEnum.Id_OUT_OF_RANGE
import br.com.acmattos.hdc.procedure.domain.model.ProcedureAttributes.ICODE
import br.com.acmattos.hdc.procedure.domain.model.ProcedureAttributes.IDESC
import br.com.acmattos.hdc.procedure.domain.model.ProcedureAttributes.VCODE
import br.com.acmattos.hdc.procedure.domain.model.ProcedureAttributes.VDESC
import io.kotest.core.spec.style.FreeSpec
import org.assertj.core.api.AbstractThrowableAssert
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatCode

private const val MESSAGE_1 = "Code is out of range (81000014-87000199)!"
private const val MESSAGE_2 = "Description length is out of range (3-120)!"

/**
 * @author ACMattos
 * @since 30/03/2022.
 */
class ProcedureTest: FreeSpec({
    "Feature: Procedure create usage" - {
        "Scenario: create - valid procedure" - {
            lateinit var entity: Procedure
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            "When: a successful procedure instantiation is done" {
                assertion = assertThatCode {
                    entity = ProcedureBuilder.buildCreate()
                }
            }
            "Then: no exception is raised" {
                assertion.doesNotThrowAnyException()
            }
            "And: entity is not null" {
                assertThat(entity).isNotNull()
            }
        }

        "Scenario: create - invalid code" - {
            var code: Int? = null
            var entity: Procedure? = null
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            "Given: an invalid code" {
                code = ICODE
            }
            "When: an unsuccessful procedure instantiation is done" {
                assertion = assertThatCode {
                    entity = ProcedureBuilder.buildInvalidCreate(code!!, VDESC)
                }
            }
            "Then: instantiation throws exception" {
                assertion.hasSameClassAs(AssertionFailedException(
                    MESSAGE_1,
                    Id_OUT_OF_RANGE.messageTrackerId
                ))
            }
            "And: exception has messageTrackerId ${Id_OUT_OF_RANGE.messageTrackerId}" {
                assertion.hasFieldOrPropertyWithValue(
                    "code",
                    Id_OUT_OF_RANGE.messageTrackerId
                )
            }
            "And: exception has message $MESSAGE_1" {
                assertion.hasMessage(MESSAGE_1)
            }
            "And: no entity was instantiated" {
                assertThat(entity).isNull()
            }
        }

        "Scenario: create - invalid description" - {
            var description: String? = null
            var entity: Procedure? = null
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            "Given: an invalid description" {
                description = IDESC
            }
            "When: an unsuccessful procedure instantiation is done" {
                assertion = assertThatCode {
                    entity = ProcedureBuilder.buildInvalidCreate(VCODE, description!!)
                }
            }
            "Then: instantiation throws exception" {
                assertion.hasSameClassAs(AssertionFailedException(
                    MESSAGE_2,
                    DESCRIPTION_INVALID_LENGTH.messageTrackerId
                ))
            }
            "And: exception has messageTrackerId ${DESCRIPTION_INVALID_LENGTH.messageTrackerId}" {
                assertion.hasFieldOrPropertyWithValue(
                    "code",
                    DESCRIPTION_INVALID_LENGTH.messageTrackerId
                )
            }
            "And: exception has message $MESSAGE_2" {
                assertion.hasMessage(MESSAGE_2)
            }
            "And: no entity was instantiated" {
                assertThat(entity).isNull()
            }
        }
    }

    "Feature: Procedure upsert usage" - {
        "Scenario: upsert - valid procedure" - {
            lateinit var entity: Procedure
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            "When: a successful procedure instantiation is done" {
                assertion = assertThatCode {
                    entity = ProcedureBuilder.buildUpsert()
                }
            }
            "Then: no exception is raised" {
                assertion.doesNotThrowAnyException()
            }
            "And: entity is not null" {
                assertThat(entity).isNotNull()
            }
        }

        "Scenario: upsert - invalid code" - {
            var code: Int? = null
            var entity: Procedure? = null
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            "Given: : an invalid code" {
                code = ICODE
            }
            "When: an unsuccessful procedure instantiation is done" {
                assertion = assertThatCode {
                    entity = ProcedureBuilder.buildInvalidUpsert(code!!, VDESC)
                }
            }
            "Then: instantiation throws exception" {
                assertion.hasSameClassAs(AssertionFailedException(
                    MESSAGE_1,
                    Id_OUT_OF_RANGE.messageTrackerId
                ))
            }
            "And: exception has messageTrackerId ${Id_OUT_OF_RANGE.messageTrackerId}" {
                assertion.hasFieldOrPropertyWithValue(
                    "code",
                    Id_OUT_OF_RANGE.messageTrackerId
                )
            }
            "And: exception has message $MESSAGE_1" {
                assertion.hasMessage(MESSAGE_1)
            }
            "And: no entity was instantiated" {
                assertThat(entity).isNull()
            }
        }

        "Scenario: upsert - invalid description" - {
            var description: String? = null
            var entity: Procedure? = null
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            "Given: : an invalid description" {
                description = IDESC
            }
            "When: an unsuccessful procedure instantiation is done" {
                assertion = assertThatCode {
                    entity = ProcedureBuilder.buildInvalidUpdate(VCODE, description!!)
                }
            }
            "Then: instantiation throws exception" {
                assertion.hasSameClassAs(AssertionFailedException(
                    MESSAGE_2,
                    DESCRIPTION_INVALID_LENGTH.messageTrackerId
                ))
            }
            "And: exception has messageTrackerId ${DESCRIPTION_INVALID_LENGTH.messageTrackerId}" {
                assertion.hasFieldOrPropertyWithValue(
                    "code",
                    DESCRIPTION_INVALID_LENGTH.messageTrackerId
                )
            }
            "And: exception has message $MESSAGE_2" {
                assertion.hasMessage(MESSAGE_2)
            }
            "And: no entity was instantiated" {
                assertThat(entity).isNull()
            }
        }
    }

    "Feature: Procedure update usage" - {
        "Scenario: update - valid procedure" - {
            lateinit var entity: Procedure
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            "When: a successful procedure instantiation is done" {
                assertion = assertThatCode {
                    entity = ProcedureBuilder.buildUpdate()
                }
            }
            "Then: no exception is raised" {
                assertion.doesNotThrowAnyException()
            }
            "And: entity is not null" {
                assertThat(entity).isNotNull()
            }
        }

        "Scenario: update - invalid code" - {
            var code: Int? = null
            var entity: Procedure? = null
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            "Given: : an invalid code" {
                code = ICODE
            }
            "When: an unsuccessful procedure instantiation is done" {
                assertion = assertThatCode {
                    entity = ProcedureBuilder.buildInvalidUpdate(code!!, VDESC)
                }
            }
            "Then: instantiation throws exception" {
                assertion.hasSameClassAs(AssertionFailedException(
                    MESSAGE_1,
                    Id_OUT_OF_RANGE.messageTrackerId
                ))
            }
            "And: exception has messageTrackerId ${Id_OUT_OF_RANGE.messageTrackerId}" {
                assertion.hasFieldOrPropertyWithValue(
                    "code",
                    Id_OUT_OF_RANGE.messageTrackerId
                )
            }
            "And: exception has message $MESSAGE_1" {
                assertion.hasMessage(MESSAGE_1)
            }
            "And: no entity was instantiated" {
                assertThat(entity).isNull()
            }
        }

        "Scenario: update - invalid description" - {
            var description: String? = null
            var entity: Procedure? = null
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            "Given: : an invalid description" {
                description = IDESC
            }
            "When: an unsuccessful procedure instantiation is done" {
                assertion = assertThatCode {
                    entity = ProcedureBuilder.buildInvalidUpdate(VCODE, description!!)
                }
            }
            "Then: instantiation throws exception" {
                assertion.hasSameClassAs(AssertionFailedException(
                    MESSAGE_2,
                    DESCRIPTION_INVALID_LENGTH.messageTrackerId
                ))
            }
            "And: exception has messageTrackerId ${DESCRIPTION_INVALID_LENGTH.messageTrackerId}" {
                assertion.hasFieldOrPropertyWithValue(
                    "code",
                    DESCRIPTION_INVALID_LENGTH.messageTrackerId
                )
            }
            "And: exception has message $MESSAGE_2" {
                assertion.hasMessage(MESSAGE_2)
            }
            "And: no entity was instantiated" {
                assertThat(entity).isNull()
            }
        }
    }

    "Feature: Procedure delete usage" - {
        "Scenario: delete - valid procedure" - {
            lateinit var entity: Procedure
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            "When: a successful procedure instantiation is done" {
                assertion = assertThatCode {
                    entity = ProcedureBuilder.buildDelete()
                }
            }
            "Then: no exception is raised" {
                assertion.doesNotThrowAnyException()
            }
            "And: entity is not null" {
                assertThat(entity).isNotNull()
            }
            "And: entity.deletedAt is not null" {
                assertThat(entity.deletedAt).isNotNull()
            }
        }
    }
})
