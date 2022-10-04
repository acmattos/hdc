package br.com.acmattos.hdc.odontogram.domain.model

import br.com.acmattos.hdc.common.tool.assertion.AssertionFailedException
import br.com.acmattos.hdc.odontogram.config.MessageTrackerIdEnum.CODE_OUT_OF_RANGE
import br.com.acmattos.hdc.odontogram.config.MessageTrackerIdEnum.SURFACE_TYPE_NOT_ALLOWED_DISTAL
import br.com.acmattos.hdc.odontogram.config.MessageTrackerIdEnum.SURFACE_TYPE_NOT_ALLOWED_FACIAL
import br.com.acmattos.hdc.odontogram.config.MessageTrackerIdEnum.SURFACE_TYPE_NOT_ALLOWED_INCISAL
import br.com.acmattos.hdc.odontogram.config.MessageTrackerIdEnum.SURFACE_TYPE_NOT_ALLOWED_LINGUAL
import br.com.acmattos.hdc.odontogram.config.MessageTrackerIdEnum.SURFACE_TYPE_NOT_ALLOWED_MESIAL
import br.com.acmattos.hdc.odontogram.config.MessageTrackerIdEnum.SURFACE_TYPE_NOT_ALLOWED_OCCLUSAL
import br.com.acmattos.hdc.odontogram.config.MessageTrackerIdEnum.SURFACE_TYPE_NOT_ALLOWED_PALATAL
import org.assertj.core.api.AbstractThrowableAssert
import org.assertj.core.api.Assertions
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

private const val MESSAGE_1 = "Code is out of range: 11-18 | 21-28 | 31-38 | 41-48 | 51-55 | 61-65 | 71-75 | 81-85!"
private const val MESSAGE_2 = "Code [11] does not allow surface A [OCCLUSAL]: INCISAL!"
private const val MESSAGE_3 = "Code [18] does not allow surface A [INCISAL]: OCCLUSAL!"
private const val MESSAGE_4 = "Surface B does not allow [DISTAL]: FACIAL!"
private const val MESSAGE_5 = "Surface C does not allow [MESIAL]: DISTAL!"
private const val MESSAGE_6 = "Surface D does not allow [LINGUAL]: MESIAL!"
private const val MESSAGE_7 = "Code [31] does not allow surface E [PALATAL]: LINGUAL!"
private const val MESSAGE_8 = "Code [21] does not allow surface E [LINGUAL]: PALATAL!"

/**
 * @author ACMattos
 * @since 03/10/2022.
 */
object ToothTest: Spek({
    Feature("${Tooth::class.java} usage") {
        Scenario("a valid ${Tooth::class.java.simpleName} creation") {
            lateinit var entity: Tooth
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            When("a successful ${Tooth::class.java.simpleName} instantiation is done") {
                assertion = Assertions.assertThatCode {
                    entity = ToothBuilder.build()
                }
            }
            Then("no exception is raised") {
                assertion.doesNotThrowAnyException()
            }
            And("entity is not null") {
                Assertions.assertThat(entity).isNotNull()
            }
        }

        Scenario("invalid code") {
            var invalid: Int? = null
            var entity: Tooth? = null
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            Given("an invalid code") {
                invalid = 19
            }
            When("an unsuccessful tooth instantiation is done") {
                assertion = Assertions.assertThatCode {
                    entity = ToothBuilder.buildWithInvalidCode(invalid!!)
                }
            }
            Then("""instantiation throws exception""") {
                assertion.hasSameClassAs(AssertionFailedException(MESSAGE_1, CODE_OUT_OF_RANGE.messageTrackerId))
            }
            And("""exception has code ${CODE_OUT_OF_RANGE.messageTrackerId}""") {
                assertion.hasFieldOrPropertyWithValue("code", CODE_OUT_OF_RANGE.messageTrackerId)
            }
            And("""exception has message $MESSAGE_1""") {
                assertion.hasMessage(MESSAGE_1)
            }
            And("""no entity was instantiated""") {
                Assertions.assertThat(entity).isNull()
            }
        }

        Scenario("invalid surface A: ${SurfaceType.OCCLUSAL}") {
            var invalid: SurfaceType? = null
            var entity: Tooth? = null
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            Given("an invalid surface A") {
                invalid = SurfaceType.OCCLUSAL
            }
            When("an unsuccessful tooth instantiation is done") {
                assertion = Assertions.assertThatCode {
                    entity = ToothBuilder.buildWithInvalidSurfaceAOcclusal(surfaceA = invalid!!)
                }
            }
            Then("""instantiation throws exception""") {
                assertion.hasSameClassAs(AssertionFailedException(MESSAGE_2, SURFACE_TYPE_NOT_ALLOWED_INCISAL.messageTrackerId))
            }
            And("""exception has code ${SURFACE_TYPE_NOT_ALLOWED_INCISAL.messageTrackerId}""") {
                assertion.hasFieldOrPropertyWithValue("code", SURFACE_TYPE_NOT_ALLOWED_INCISAL.messageTrackerId)
            }
            And("""exception has message $MESSAGE_2""") {
                assertion.hasMessage(MESSAGE_2)
            }
            And("""no entity was instantiated""") {
                Assertions.assertThat(entity).isNull()
            }
        }

        Scenario("invalid surface A: ${SurfaceType.INCISAL}") {
            var invalid: SurfaceType? = null
            var entity: Tooth? = null
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            Given("an invalid code surface A") {
                invalid = SurfaceType.INCISAL
            }
            When("an unsuccessful tooth instantiation is done") {
                assertion = Assertions.assertThatCode {
                    entity = ToothBuilder.buildWithInvalidSurfaceAIncisal(invalid!!)
                }
            }
            Then("""instantiation throws exception""") {
                assertion.hasSameClassAs(AssertionFailedException(MESSAGE_3, SURFACE_TYPE_NOT_ALLOWED_OCCLUSAL.messageTrackerId))
            }
            And("""exception has code ${SURFACE_TYPE_NOT_ALLOWED_OCCLUSAL.messageTrackerId}""") {
                assertion.hasFieldOrPropertyWithValue("code", SURFACE_TYPE_NOT_ALLOWED_OCCLUSAL.messageTrackerId)
            }
            And("""exception has message $MESSAGE_3""") {
                assertion.hasMessage(MESSAGE_3)
            }
            And("""no entity was instantiated""") {
                Assertions.assertThat(entity).isNull()
            }
        }

        Scenario("invalid surface B: ${SurfaceType.DISTAL}") {
            var invalid: SurfaceType? = null
            var entity: Tooth? = null
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            Given("an invalid code surface B") {
                invalid = SurfaceType.DISTAL
            }
            When("an unsuccessful tooth instantiation is done") {
                assertion = Assertions.assertThatCode {
                    entity = ToothBuilder.buildWithInvalidSurfaceB(invalid!!)
                }
            }
            Then("""instantiation throws exception""") {
                assertion.hasSameClassAs(AssertionFailedException(MESSAGE_4, SURFACE_TYPE_NOT_ALLOWED_FACIAL.messageTrackerId))
            }
            And("""exception has code ${SURFACE_TYPE_NOT_ALLOWED_FACIAL.messageTrackerId}""") {
                assertion.hasFieldOrPropertyWithValue("code", SURFACE_TYPE_NOT_ALLOWED_FACIAL.messageTrackerId)
            }
            And("""exception has message $MESSAGE_4""") {
                assertion.hasMessage(MESSAGE_4)
            }
            And("""no entity was instantiated""") {
                Assertions.assertThat(entity).isNull()
            }
        }

        Scenario("invalid surface C: ${SurfaceType.MESIAL}") {
            var invalid: SurfaceType? = null
            var entity: Tooth? = null
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            Given("an invalid code surface C") {
                invalid = SurfaceType.MESIAL
            }
            When("an unsuccessful tooth instantiation is done") {
                assertion = Assertions.assertThatCode {
                    entity = ToothBuilder.buildWithInvalidSurfaceC(invalid!!)
                }
            }
            Then("""instantiation throws exception""") {
                assertion.hasSameClassAs(AssertionFailedException(MESSAGE_5, SURFACE_TYPE_NOT_ALLOWED_DISTAL.messageTrackerId))
            }
            And("""exception has code ${SURFACE_TYPE_NOT_ALLOWED_DISTAL.messageTrackerId}""") {
                assertion.hasFieldOrPropertyWithValue("code", SURFACE_TYPE_NOT_ALLOWED_DISTAL.messageTrackerId)
            }
            And("""exception has message $MESSAGE_5""") {
                assertion.hasMessage(MESSAGE_5)
            }
            And("""no entity was instantiated""") {
                Assertions.assertThat(entity).isNull()
            }
        }

        Scenario("invalid surface D: ${SurfaceType.LINGUAL}") {
            var invalid: SurfaceType? = null
            var entity: Tooth? = null
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            Given("an invalid code surface D") {
                invalid = SurfaceType.LINGUAL
            }
            When("an unsuccessful tooth instantiation is done") {
                assertion = Assertions.assertThatCode {
                    entity = ToothBuilder.buildWithInvalidSurfaceD(invalid!!)
                }
            }
            Then("""instantiation throws exception""") {
                assertion.hasSameClassAs(AssertionFailedException(MESSAGE_6, SURFACE_TYPE_NOT_ALLOWED_MESIAL.messageTrackerId))
            }
            And("""exception has code ${SURFACE_TYPE_NOT_ALLOWED_MESIAL.messageTrackerId}""") {
                assertion.hasFieldOrPropertyWithValue("code", SURFACE_TYPE_NOT_ALLOWED_MESIAL.messageTrackerId)
            }
            And("""exception has message $MESSAGE_6""") {
                assertion.hasMessage(MESSAGE_6)
            }
            And("""no entity was instantiated""") {
                Assertions.assertThat(entity).isNull()
            }
        }

        Scenario("invalid surface E: ${SurfaceType.LINGUAL}") {
            var invalid: SurfaceType? = null
            var entity: Tooth? = null
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            Given("an invalid surface E") {
                invalid = SurfaceType.PALATAL
            }
            When("an unsuccessful tooth instantiation is done") {
                assertion = Assertions.assertThatCode {
                    entity = ToothBuilder.buildWithInvalidSurfaceELingual(invalid!!)
                }
            }
            Then("""instantiation throws exception""") {
                assertion.hasSameClassAs(AssertionFailedException(MESSAGE_7, SURFACE_TYPE_NOT_ALLOWED_LINGUAL.messageTrackerId))
            }
            And("""exception has code ${SURFACE_TYPE_NOT_ALLOWED_LINGUAL.messageTrackerId}""") {
                assertion.hasFieldOrPropertyWithValue("code", SURFACE_TYPE_NOT_ALLOWED_LINGUAL.messageTrackerId)
            }
            And("""exception has message $MESSAGE_7""") {
                assertion.hasMessage(MESSAGE_7)
            }
            And("""no entity was instantiated""") {
                Assertions.assertThat(entity).isNull()
            }
        }

        Scenario("invalid surface E: ${SurfaceType.PALATAL}") {
            var invalid: SurfaceType? = null
            var entity: Tooth? = null
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            Given("an invalid code surface E") {
                invalid = SurfaceType.LINGUAL
            }
            When("an unsuccessful tooth instantiation is done") {
                assertion = Assertions.assertThatCode {
                    entity = ToothBuilder.buildWithInvalidSurfaceEPalatal(invalid!!)
                }
            }
            Then("""instantiation throws exception""") {
                assertion.hasSameClassAs(AssertionFailedException(MESSAGE_8, SURFACE_TYPE_NOT_ALLOWED_PALATAL.messageTrackerId))
            }
            And("""exception has code ${SURFACE_TYPE_NOT_ALLOWED_PALATAL.messageTrackerId}""") {
                assertion.hasFieldOrPropertyWithValue("code", SURFACE_TYPE_NOT_ALLOWED_PALATAL.messageTrackerId)
            }
            And("""exception has message $MESSAGE_8""") {
                assertion.hasMessage(MESSAGE_8)
            }
            And("""no entity was instantiated""") {
                Assertions.assertThat(entity).isNull()
            }
        }
    }
})
