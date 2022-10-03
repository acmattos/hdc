package br.com.acmattos.hdc.person.domain.model

import br.com.acmattos.hdc.common.tool.assertion.AssertionFailedException
import br.com.acmattos.hdc.person.config.MessageTrackerIdEnum.INVALID_PERSON_ADDRESSES
import br.com.acmattos.hdc.person.config.MessageTrackerIdEnum.INVALID_PERSON_CONTACTS
import br.com.acmattos.hdc.person.config.MessageTrackerIdEnum.INVALID_PERSON_CPF
import br.com.acmattos.hdc.person.config.MessageTrackerIdEnum.INVALID_PERSON_DOB
import br.com.acmattos.hdc.person.config.MessageTrackerIdEnum.INVALID_PERSON_FULL_NAME
import br.com.acmattos.hdc.person.config.MessageTrackerIdEnum.INVALID_PERSON_OCCUPATION
import br.com.acmattos.hdc.person.config.MessageTrackerIdEnum.INVALID_PERSON_PERSONAL_ID
import br.com.acmattos.hdc.person.config.MessageTrackerIdEnum.INVALID_PERSON_PERSON_TYPE_DENTIST
import br.com.acmattos.hdc.person.config.MessageTrackerIdEnum.INVALID_PERSON_PERSON_TYPE_PATIENT
import java.time.LocalDate
import org.assertj.core.api.AbstractThrowableAssert
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThatCode
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

private const val MESSAGE_1 = "Invalid full name: 3 <= fullName.length <= 100!"
private const val MESSAGE_2 = "Invalid person type: MUST BE PATIENT!"
private const val MESSAGE_3 = "Invalid person type: MUST BE DENTIST!"
private const val MESSAGE_4 = "Invalid occupation: 3 <= occupation.length <= 50!"
private const val MESSAGE_5 = "Invalid addresses size: one at least!"
private const val MESSAGE_6 = "Invalid contacts size: one at least!"
private const val MESSAGE_7 = "Invalid CPF!"
private const val MESSAGE_8 = "Invalid dob: dob <= NOW!"
private const val MESSAGE_9 = "Invalid personal id: 5 <= name.length <= 15!"
private const val MESSAGE_10 = "Invalid personal id: 5 <= name.length <= 15!"

/**
 * @author ACMattos
 * @since 21/04/2022.
 */
object PersonTest: Spek({
    Feature("${Person::class.java} usage") {
        Scenario("a valid Patient creation") {
            lateinit var entity: Person
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            When("a successful patient instantiation is done") {
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

        Scenario("invalid fullName") {
            var invalid: String? = null
            var entity: Person? = null
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            Given("an invalid fullName") {
                invalid = ""
            }
            When("an unsuccessful person (patient) instantiation is done") {
                assertion = assertThatCode {
                    entity = PersonBuilder.buildPatientWithInvalidFullName(invalid!!)
                }
            }
            Then("""instantiation throws exception""") {
                assertion.hasSameClassAs(AssertionFailedException(MESSAGE_1, INVALID_PERSON_FULL_NAME.messageTrackerId))
            }
            And("""exception has messageTrackerId ${INVALID_PERSON_FULL_NAME.messageTrackerId}""") {
                assertion.hasFieldOrPropertyWithValue("messageTrackerId", INVALID_PERSON_FULL_NAME.messageTrackerId)
            }
            And("""exception has message $MESSAGE_1""") {
                assertion.hasMessage(MESSAGE_1)
            }
            And("""no entity was instantiated""") {
                Assertions.assertThat(entity).isNull()
            }
        }

        Scenario("invalid dob") {
            var invalid: LocalDate? = null
            var entity: Person? = null
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            Given("an invalid dob") {
                invalid = LocalDate.now().plusDays(1)
            }
            When("an unsuccessful person (patient) instantiation is done") {
                assertion = assertThatCode {
                    entity = PersonBuilder.buildPatientWithInvalidDob(invalid!!)
                }
            }
            Then("""instantiation throws exception""") {
                assertion.hasSameClassAs(AssertionFailedException(MESSAGE_8, INVALID_PERSON_DOB.messageTrackerId))
            }
            And("""exception has messageTrackerId ${INVALID_PERSON_DOB.messageTrackerId}""") {
                assertion.hasFieldOrPropertyWithValue("messageTrackerId", INVALID_PERSON_DOB.messageTrackerId)
            }
            And("""exception has message $MESSAGE_8""") {
                assertion.hasMessage(MESSAGE_8)
            }
            And("""no entity was instantiated""") {
                Assertions.assertThat(entity).isNull()
            }
        }

        Scenario("invalid personType (patient)") {
            var invalid: String? = null
            var entity: Person? = null
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            Given("an invalid personType") {
                invalid = "DENTIST"
            }
            When("an unsuccessful person (patient) instantiation is done") {
                assertion = assertThatCode {
                    entity = PersonBuilder.buildPatientWithInvalidPersonType(invalid!!)
                }
            }
            Then("""instantiation throws exception""") {
                assertion.hasSameClassAs(AssertionFailedException(MESSAGE_2, INVALID_PERSON_PERSON_TYPE_PATIENT.messageTrackerId))
            }
            And("""exception has messageTrackerId ${INVALID_PERSON_PERSON_TYPE_PATIENT.messageTrackerId}""") {
                assertion.hasFieldOrPropertyWithValue("messageTrackerId", INVALID_PERSON_PERSON_TYPE_PATIENT.messageTrackerId)
            }
            And("""exception has message $MESSAGE_2""") {
                assertion.hasMessage(MESSAGE_2)
            }
            And("""no entity was instantiated""") {
                Assertions.assertThat(entity).isNull()
            }
        }

        Scenario("invalid personType (dentist)") {
            var invalid: String? = null
            var entity: Person? = null
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            Given("an invalid personType") {
                invalid = "PATIENT"
            }
            When("an unsuccessful person (dentist) instantiation is done") {
                assertion = assertThatCode {
                    entity = PersonBuilder.buildDentistWithInvalidPersonType(invalid!!)
                }
            }
            Then("""instantiation throws exception""") {
                assertion.hasSameClassAs(AssertionFailedException(MESSAGE_3, INVALID_PERSON_PERSON_TYPE_DENTIST.messageTrackerId))
            }
            And("""exception has messageTrackerId ${INVALID_PERSON_PERSON_TYPE_DENTIST.messageTrackerId}""") {
                assertion.hasFieldOrPropertyWithValue("messageTrackerId", INVALID_PERSON_PERSON_TYPE_DENTIST.messageTrackerId)
            }
            And("""exception has message $MESSAGE_3""") {
                assertion.hasMessage(MESSAGE_3)
            }
            And("""no entity was instantiated""") {
                Assertions.assertThat(entity).isNull()
            }
        }

        Scenario("invalid CPF") {
            var invalid: String? = null
            var entity: Person? = null
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            Given("an invalid CPF") {
                invalid = "111.222.444.5550-98"
            }
            When("an unsuccessful person (patient) instantiation is done") {
                assertion = assertThatCode {
                    entity = PersonBuilder.buildPatient(cpf=invalid!!)
                }
            }
            Then("""instantiation throws exception""") {
                assertion.hasSameClassAs(AssertionFailedException(MESSAGE_7, INVALID_PERSON_CPF.messageTrackerId))
            }
            And("""exception has messageTrackerId ${INVALID_PERSON_CPF.messageTrackerId}""") {
                assertion.hasFieldOrPropertyWithValue("messageTrackerId", INVALID_PERSON_CPF.messageTrackerId)
            }
            And("""exception has message $MESSAGE_7""") {
                assertion.hasMessage(MESSAGE_7)
            }
            And("""no entity was instantiated""") {
                Assertions.assertThat(entity).isNull()
            }
        }

        Scenario("invalid personalId") {
            var invalid: String? = null
            var entity: Person? = null
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            Given("an invalid personalId") {
                invalid = "1234"
            }
            When("an unsuccessful person (patient) instantiation is done") {
                assertion = assertThatCode {
                    entity = PersonBuilder.buildPatientWithInvalidPersonalId(invalid!!)
                }
            }
            Then("""instantiation throws exception""") {
                assertion.hasSameClassAs(AssertionFailedException(MESSAGE_9, INVALID_PERSON_PERSONAL_ID.messageTrackerId))
            }
            And("""exception has messageTrackerId ${INVALID_PERSON_PERSONAL_ID.messageTrackerId}""") {
                assertion.hasFieldOrPropertyWithValue("messageTrackerId", INVALID_PERSON_PERSONAL_ID.messageTrackerId)
            }
            And("""exception has message $MESSAGE_9""") {
                assertion.hasMessage(MESSAGE_9)
            }
            And("""no entity was instantiated""") {
                Assertions.assertThat(entity).isNull()
            }
        }

        Scenario("invalid occupation") {
            var invalid: String? = null
            var entity: Person? = null
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            Given("an invalid occupation") {
                invalid = ""
            }
            When("an unsuccessful person (patient) instantiation is done") {
                assertion = assertThatCode {
                    entity = PersonBuilder.buildPatientWithInvalidOccupation(invalid!!)
                }
            }
            Then("""instantiation throws exception""") {
                assertion.hasSameClassAs(AssertionFailedException(MESSAGE_4, INVALID_PERSON_OCCUPATION.messageTrackerId))
            }
            And("""exception has messageTrackerId ${INVALID_PERSON_OCCUPATION.messageTrackerId}""") {
                assertion.hasFieldOrPropertyWithValue("messageTrackerId", INVALID_PERSON_OCCUPATION.messageTrackerId)
            }
            And("""exception has message $MESSAGE_4""") {
                assertion.hasMessage(MESSAGE_4)
            }
            And("""no entity was instantiated""") {
                Assertions.assertThat(entity).isNull()
            }
        }

        Scenario("invalid addresses") {
            var invalid: List<Address>? = null
            var entity: Person? = null
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            Given("an invalid addresses") {
                invalid = listOf()
            }
            When("an unsuccessful person (patient) instantiation is done") {
                assertion = assertThatCode {
                    entity = PersonBuilder.buildPatientWithInvalidAddresses(invalid!!)
                }
            }
            Then("""instantiation throws exception""") {
                assertion.hasSameClassAs(AssertionFailedException(MESSAGE_5, INVALID_PERSON_ADDRESSES.messageTrackerId))
            }
            And("""exception has messageTrackerId ${INVALID_PERSON_ADDRESSES.messageTrackerId}""") {
                assertion.hasFieldOrPropertyWithValue("messageTrackerId", INVALID_PERSON_ADDRESSES.messageTrackerId)
            }
            And("""exception has message $MESSAGE_5""") {
                assertion.hasMessage(MESSAGE_5)
            }
            And("""no entity was instantiated""") {
                Assertions.assertThat(entity).isNull()
            }
        }

        Scenario("invalid contacts") {
            var invalid: List<Contact>? = null
            var entity: Person? = null
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            Given("an invalid contacts") {
                invalid = listOf()
            }
            When("an unsuccessful person (patient) instantiation is done") {
                assertion = assertThatCode {
                    entity = PersonBuilder.buildPatientWithInvalidContacts(invalid!!)
                }
            }
            Then("""instantiation throws exception""") {
                assertion.hasSameClassAs(AssertionFailedException(MESSAGE_6, INVALID_PERSON_CONTACTS.messageTrackerId))
            }
            And("""exception has messageTrackerId ${INVALID_PERSON_CONTACTS.messageTrackerId}""") {
                assertion.hasFieldOrPropertyWithValue("messageTrackerId", INVALID_PERSON_CONTACTS.messageTrackerId)
            }
            And("""exception has message $MESSAGE_6""") {
                assertion.hasMessage(MESSAGE_6)
            }
            And("""no entity was instantiated""") {
                Assertions.assertThat(entity).isNull()
            }
        }

//        Scenario("invalid dentalPlan") {
//            var invalid: DentalPlan? = null
//            var entity: Person? = null
//            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
//            Given("an invalid dentalPlan") {
//                invalid = listOf()
//            }
//            When("an unsuccessful person (patient) instantiation is done") {
//                assertion = assertThatCode {
//                    entity = PersonBuilder.buildPatientWithInvalidContacts(invalid!!)
//                }
//            }
//            Then("""instantiation throws exception""") {
//                assertion.hasSameClassAs(AssertionFailedException(MESSAGE_10, .messageTrackerId))
//            }
//            And("""exception has messageTrackerId ${.messageTrackerId}""") {
//                assertion.hasFieldOrPropertyWithValue("messageTrackerId", .messageTrackerId)
//            }
//            And("""exception has message $MESSAGE_10""") {
//                assertion.hasMessage(MESSAGE_10)
//            }
//            And("""no entity was instantiated""") {
//                Assertions.assertThat(entity).isNull()
//            }
//        }

//        Scenario("invalid status") {
//            var invalid: String? = null
//            var entity: Person? = null
//            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
//            Given("an invalid status") {
//                invalid = ""
//            }
//            When("an unsuccessful person (patient) instantiation is done") {
//                assertion = assertThatCode {
//                    entity = PersonBuilder.buildPatientWithInvalidStatus(invalid!!)
//                }
//            }
//            Then("""instantiation throws exception""") {
//                assertion.hasSameClassAs(AssertionFailedException(MESSAGE_10, INVALID_PERSON_STATUS.messageTrackerId))
//            }
//            And("""exception has messageTrackerId ${INVALID_PERSON_STATUS.messageTrackerId}""") {
//                assertion.hasFieldOrPropertyWithValue("messageTrackerId", INVALID_PERSON_STATUS.messageTrackerId)
//            }
//            And("""exception has message $MESSAGE_10""") {
//                assertion.hasMessage(MESSAGE_10)
//            }
//            And("""no entity was instantiated""") {
//                Assertions.assertThat(entity).isNull()
//            }
//        }
    }
})
