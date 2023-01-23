package br.com.acmattos.hdc.person.domain.model

import br.com.acmattos.hdc.common.context.domain.cqs.EntityEvent
import br.com.acmattos.hdc.common.context.domain.model.AppliableEntity
import br.com.acmattos.hdc.common.context.domain.model.Id
import br.com.acmattos.hdc.common.tool.assertion.Assertion
import br.com.acmattos.hdc.person.config.MessageTrackerIdEnum.INVALID_PERSON_ADDRESSES
import br.com.acmattos.hdc.person.config.MessageTrackerIdEnum.INVALID_PERSON_CONTACTS
import br.com.acmattos.hdc.person.config.MessageTrackerIdEnum.INVALID_PERSON_CPF
import br.com.acmattos.hdc.person.config.MessageTrackerIdEnum.INVALID_PERSON_DOB
import br.com.acmattos.hdc.person.config.MessageTrackerIdEnum.INVALID_PERSON_FULL_NAME
import br.com.acmattos.hdc.person.config.MessageTrackerIdEnum.INVALID_PERSON_INDICATED_BY
import br.com.acmattos.hdc.person.config.MessageTrackerIdEnum.INVALID_PERSON_OCCUPATION
import br.com.acmattos.hdc.person.config.MessageTrackerIdEnum.INVALID_PERSON_PERSONAL_ID
import br.com.acmattos.hdc.person.config.MessageTrackerIdEnum.INVALID_PERSON_STATUS
import br.com.acmattos.hdc.person.config.PersonLogEnum.PERSON
import br.com.acmattos.hdc.person.domain.cqs.CreateAPersonEvent
import br.com.acmattos.hdc.person.domain.cqs.CreateDentistEvent
import br.com.acmattos.hdc.person.domain.cqs.CreatePatientEvent
import br.com.acmattos.hdc.person.domain.cqs.PersonEvent
import br.com.acmattos.hdc.person.domain.cqs.UpdateAPersonEvent
import br.com.acmattos.hdc.person.domain.cqs.UpdatePatientEvent
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * @author ACMattos
 * @since 30/09/2021.
 */
data class Person(// TODO Create tests
    private var personIdData: PersonId? = null,
    private var fullNameData: String? = null,
    private var dobData: LocalDate? = null,
    private var maritalStatusData: MaritalStatus? = null,
    private var genderData: Gender? = null,
    private var personTypeData: PersonType? = null,
    private var cpfData: String? = null,
    private var personalIdData: String? = null,
    private var occupationData: String? = null,
    private var addressesData: List<Address>? = null,
    private var contactsData: List<Contact>? = null,
    private var dentalPlanData: DentalPlan? = null,
    private var responsibleForData: PersonId? = null,
    private var indicatedByData: String? = null,
    private var familyGroupData: List<PersonId>? = null,
    private var statusData: Status? = null,
    private var lastAppointmentData: LocalDate? = null,
    private var enabledData: Boolean = true,
    override var createdAtData: LocalDateTime = LocalDateTime.now(),
    override var updatedAtData: LocalDateTime? = null,
    override var deletedAtData: LocalDateTime? = null,
): AppliableEntity {
    val personId get() = personIdData!!
    val fullName get() = fullNameData!!
    val dob get() = dobData!!
    val maritalStatus get() = maritalStatusData!!
    val gender get() = genderData!!
    val personType get() = personTypeData!!
    val cpf get() = cpfData!!
    val personalId get() = personalIdData
    val occupation get() = occupationData
    val addresses get() = addressesData!!
    val contacts get() = contactsData!!
    val dentalPlan get() = dentalPlanData
    val responsibleFor get() = responsibleForData
    val indicatedBy get() = indicatedByData
    val familyGroup get() = familyGroupData!!
    val status get() = statusData
    val lastAppointment get() = lastAppointmentData
    val enabled get() = enabledData
    val createdAt get() = createdAtData
    val updatedAt get() = updatedAtData

    override fun apply(event: EntityEvent): Person {
        when(event) {
            is CreateDentistEvent -> apply(event)
            is CreatePatientEvent -> apply(event)
            else -> apply(event as UpdatePatientEvent)
        }
        return this
    }

    private fun apply(event: CreateDentistEvent) {
        applyCreateEvent(event)
    }

    private fun apply(event: CreatePatientEvent) {
        applyCreateEvent(event)
        assertValidOccupation()
        assertValidIndicatedBy()
        assertValidStatus()
    }

    private fun apply(event: UpdatePatientEvent) {
        applyUpdateEvent(event)
        assertValidOccupation()
        assertValidIndicatedBy()
        assertValidStatus()
    }

    private fun applyCreateEvent(event: CreateAPersonEvent) {
        personIdData = event.personId
        fullNameData = event.fullName
        dobData = event.dob
        maritalStatusData = event.maritalStatus
        genderData = event.gender
        personTypeData = event.personType
        cpfData = event.cpf
        personalIdData = event.personalId
        addressesData = event.addresses
        contactsData = event.contacts
        responsibleForData = event.responsibleFor
        familyGroupData = event.familyGroup
        enabledData = event.enabled
        createdAtData = event.createdAt
        when(event) {
            is CreatePatientEvent -> {
                occupationData = event.occupation
                dentalPlanData = event.dentalPlan
                indicatedByData = event.indicatedBy
                statusData = event.status
                lastAppointmentData = event.lastAppointment
            }
        }
        assertValidFullName()
        assertValidDob()
        assertValidCpf()
        assertValidPersonId()
        assertValidAddresses()
        assertValidContacts()
    }

    private fun applyUpdateEvent(event: UpdateAPersonEvent) {
        personIdData = event.personId
        fullNameData = event.fullName
        dobData = event.dob
        maritalStatusData = event.maritalStatus
        genderData = event.gender
        personTypeData = event.personType
        cpfData = event.cpf
        personalIdData = event.personalId
        addressesData = event.addresses
        contactsData = event.contacts
        responsibleForData = event.responsibleFor
        familyGroupData = event.familyGroup
        enabledData = event.enabled
        createdAtData = event.createdAt
        when(event) {
            is UpdatePatientEvent -> {
                occupationData = event.occupation
                dentalPlanData = event.dentalPlan
                indicatedByData = event.indicatedBy
                statusData = event.status
                lastAppointmentData = event.lastAppointment
            }
        }
        assertValidFullName()
        assertValidDob()
        assertValidCpf()
        assertValidPersonId()
        assertValidAddresses()
        assertValidContacts()
    }

    private fun assertValidFullName() {
        Assertion.assert(
            "Invalid full name: 3 <= fullName.length <= 100!",
            "${PERSON.name} $INVALID_PERSON_FULL_NAME",
            INVALID_PERSON_FULL_NAME
        ) {
            fullName.length in 3..100
        }
    }

    private fun assertValidDob() {
        Assertion.assert(
            "Invalid dob: dob <= NOW!",
            "${PERSON.name} $INVALID_PERSON_DOB",
            INVALID_PERSON_DOB
        ) {
            dob.isBefore(LocalDate.now()) || dob.isEqual(LocalDate.now())
        }
    }

    // TODO Review when dentist CRUD is done
//    private fun assertDentist() {
//        Assertion.assert(
//            "Invalid person type: MUST BE DENTIST!",
//            PERSON.name,
//            INVALID_PERSON_PERSON_TYPE_DENTIST.code
//        ) {
//            personType == DENTIST
//        }
//    }
//    /*private fun assertPatient() {
//    indicatedBy
//        Assertion.assert(
//            "Invalid person type: MUST BE PATIENT!",
//            PERSON.name,
//            INVALID_PERSON_PERSON_TYPE_PATIENT.code
//        ) {
//            personType == PATIENT
//        }
//    }*/

    private fun assertValidCpf() {
        val rawCpf = cpf.replace(".", "")
            .replace("-", "")
        var valid = false
        for(index in 1..10){
            if(rawCpf[index - 1] != rawCpf[index]) {
                valid = true
            }
        }
        if(valid) {
            var digit1 = 0
            var p = 10
            for(index in 0..8){
                digit1 += rawCpf[index].toString().toInt() * p--
            }
            digit1 = (digit1 * 10) % 11
            if(digit1 == 10) {
                digit1 = 0
            }
            if(digit1 != rawCpf[9].toString().toInt()) {
                valid = false
            } else {
                var digit2 = 0
                p = 11
                for(index in 0..9){
                    digit2 += rawCpf[index].toString().toInt() * p--
                }
                digit2 = (digit2 * 10) % 11
                if(digit2 == 10) {
                    digit2 = 0
                }
                if(digit2 != rawCpf[10].toString().toInt()) {
                    valid = false
                }
            }
        }
        Assertion.assert(
            "Invalid CPF!",
            PERSON.name,
            INVALID_PERSON_CPF
        ) {
            valid
        }
    }

    private fun assertValidPersonId() {
        if(personalId != null) {
            Assertion.assert(
                "Invalid personal id: 5 <= personalId.length <= 15!",
                PERSON.name,
                INVALID_PERSON_PERSONAL_ID
            ) {
                personalId!!.length in 5..15
            }
        }
    }

    private fun assertValidOccupation()  {
        if(occupation != null) {
            Assertion.assert(
                "Invalid occupation: 3 <= occupation.length <= 100!",
                PERSON.name,
                INVALID_PERSON_OCCUPATION
            ) {
                occupation!!.length in 3..100
            }
        }
    }

    private fun assertValidIndicatedBy()  {
        if(indicatedBy != null) {
            Assertion.assert(
                "Invalid indicated by: 3 <= indicatedBy.length <= 100!",
                PERSON.name,
                INVALID_PERSON_INDICATED_BY
            ) {
                indicatedBy!!.length in 3..100
            }
        }
    }

    private fun assertValidAddresses() {
        Assertion.assert(
            "Invalid addresses size: one at least!",
            PERSON.name,
            INVALID_PERSON_ADDRESSES
        ) {
            addresses.isNotEmpty()
        }
    }

    private fun assertValidContacts() {
        Assertion.assert(
            "Invalid contacts size: one at least!",
            PERSON.name,
            INVALID_PERSON_CONTACTS
        ) {
            contacts.isNotEmpty()
        }
    }

    private fun assertValidStatus()  {
        Assertion.assert(
            "Invalid status: PATIENT needs a defined status!",
            PERSON.name,
            INVALID_PERSON_STATUS
        ) {
            PersonType.PATIENT == personType && status != null
        }
    }

    companion object {
        fun apply(events: List<PersonEvent>): Person =
            Person().apply(events) as Person
        fun apply(event: PersonEvent): Person = Person().apply(event)
    }
}

/**
 * @author ACMattos
 * @since 30/09/2021.
 */
class PersonId: Id {
    constructor(id: String): super(id)
    constructor(): super()
}
