package br.com.acmattos.hdc.scheduler.domain.model

import br.com.acmattos.hdc.common.tool.assertion.AssertionFailedException
import br.com.acmattos.hdc.scheduler.config.MessageTrackerIdEnum.INVALID_DENTIST_FULL_NAME
import org.assertj.core.api.AbstractThrowableAssert
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

private const val MESSAGE = "Invalid name for the Dentist"
/**
 * @author ACMattos
 * @since 13/11/2021.
 */
object DentistTest: Spek({
    Feature("${Dentist::class.java} usage") {
        Scenario("${Dentist::class.java} multiple names in dentist.fullName") {
            lateinit var fullName: String
            lateinit var dentist: Dentist
            lateinit var acronym: String
            Given("""multiple names in dentist.fullName""") {
                fullName = "Helena Dental Care"
            }
            And("""a ${Dentist::class.java} successful instantiation""") {
                dentist = Dentist(DentistId("01FM60GR4G9FE3F55670TQCPE3"), fullName)
            }
            When("""dentist#getAcronym is executed""") {
                acronym = dentist.getAcronym()
            }
            Then("""acronym is equal to HDC""") {
                assertThat(acronym).isEqualTo("HDC")
            }
        }

        Scenario("${Dentist::class.java} single name in dentist.fullName") {
            lateinit var fullName: String
            lateinit var dentist: Dentist
            lateinit var acronym: String
            Given("""multiple names in dentist.fullName""") {
                fullName = "hELENA"
            }
            And("""a ${Dentist::class.java} successful instantiation""") {
                dentist = Dentist(DentistId("01FM60GR4G9FE3F55670TQCPE3"), fullName)
            }
            When("""dentist#getAcronym is executed""") {
                acronym = dentist.getAcronym()
            }
            Then("""acronym is equal to HDC""") {
                assertThat(acronym).isEqualTo("h")
            }
        }

        Scenario("${Dentist::class.java}#assertFullName throws exception") {
            lateinit var fullName: String
            var dentist: Dentist? = null
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            Given("""fullName is empty""") {
                fullName = ""
            }
            When("""a ${Dentist::class.java} instantiation is executed""") {
                assertion = Assertions.assertThatCode {
                    dentist = Dentist(DentistId("01FM60GR4G9FE3F55670TQCPE3"), fullName)
                }
            }
            Then("""instantiation throws exception""") {
                assertion.hasSameClassAs(AssertionFailedException(MESSAGE, INVALID_DENTIST_FULL_NAME.messageTrackerId))
            }
            And("""exception has messageTrackerId ${INVALID_DENTIST_FULL_NAME.messageTrackerId}""") {
                assertion.hasFieldOrPropertyWithValue("messageTrackerId", INVALID_DENTIST_FULL_NAME.messageTrackerId)
            }
            And("""exception has message $MESSAGE""") {
                assertion.hasMessage(MESSAGE)
            }
            And("""no dentist was instantiated""") {
                assertThat(dentist).isNull()
            }
        }
    }
})
