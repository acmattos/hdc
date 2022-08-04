package br.com.acmattos.hdc.person.domain.model

import br.com.acmattos.hdc.common.context.domain.model.Entity
import br.com.acmattos.hdc.common.context.domain.model.Id
import br.com.acmattos.hdc.common.tool.assertion.Assertion
import br.com.acmattos.hdc.person.config.MessageTrackerCodeEnum.INVALID_PERSON_ADDRESSES
import br.com.acmattos.hdc.person.config.MessageTrackerCodeEnum.INVALID_PERSON_CONTACTS
import br.com.acmattos.hdc.person.config.MessageTrackerCodeEnum.INVALID_PERSON_CPF
import br.com.acmattos.hdc.person.config.MessageTrackerCodeEnum.INVALID_PERSON_DOB
import br.com.acmattos.hdc.person.config.MessageTrackerCodeEnum.INVALID_PERSON_FULL_NAME
import br.com.acmattos.hdc.person.config.MessageTrackerCodeEnum.INVALID_PERSON_OCCUPATION
import br.com.acmattos.hdc.person.config.MessageTrackerCodeEnum.INVALID_PERSON_PERSONAL_ID
import br.com.acmattos.hdc.person.config.MessageTrackerCodeEnum.INVALID_PERSON_PERSON_TYPE_DENTIST
import br.com.acmattos.hdc.person.config.MessageTrackerCodeEnum.INVALID_PERSON_PERSON_TYPE_PATIENT
import br.com.acmattos.hdc.person.config.MessageTrackerCodeEnum.INVALID_PERSON_STATUS
import br.com.acmattos.hdc.person.config.PersonLogEnum.PERSON
import br.com.acmattos.hdc.person.domain.cqs.CreateAPersonEvent
import br.com.acmattos.hdc.person.domain.cqs.CreateDentistEvent
import br.com.acmattos.hdc.person.domain.cqs.CreatePatientEvent
import br.com.acmattos.hdc.person.domain.cqs.PersonEvent
import br.com.acmattos.hdc.person.domain.cqs.UpdateAPersonEvent
import br.com.acmattos.hdc.person.domain.cqs.UpdatePatientEvent
import br.com.acmattos.hdc.person.domain.model.PersonType.DENTIST
import br.com.acmattos.hdc.person.domain.model.PersonType.PATIENT
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * @author ACMattos
 * @since 30/09/2021.
 */
data class Person(
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
    private var enabledData: Boolean = true,
    private var createdAtData: LocalDateTime = LocalDateTime.now(),
    private var updatedAtData: LocalDateTime? = null
): Entity {
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
    val enabled get() = enabledData
    val createdAt get() = createdAtData
    val updatedAt get() = updatedAtData

    fun apply(events: List<PersonEvent>): Person {
        for (event in events) {
            apply(event)
        }
        return this
    }

    fun apply(event: PersonEvent): Person {
        when(event) {
            is CreateDentistEvent -> apply(event)
            is CreatePatientEvent -> apply(event)
            else -> apply(event as UpdatePatientEvent)
        }
        return this
    }

    private fun apply(event: CreateDentistEvent) {
        applyCreateEvent(event)
        //assertDentist()
    }

    private fun apply(event: CreatePatientEvent) {
        applyCreateEvent(event)
        //assertPatient()
        assertValidOccupation()
        assertValidStatus()
    }

    private fun apply(event: UpdatePatientEvent) {
        applyUpdateEvent(event)
       // assertPatient()
        assertValidOccupation()
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
            PERSON.name,
            INVALID_PERSON_FULL_NAME.code
        ) {
            fullName.length in 3..100
        }
    }

    private fun assertValidDob() {
        Assertion.assert(
            "Invalid dob: dob <= NOW!",
            PERSON.name,
            INVALID_PERSON_DOB.code
        ) {
            dob.isBefore(LocalDate.now()) || dob.isEqual(LocalDate.now())
        }
    }

//    private fun assertDentist() {
//        Assertion.assert(
//            "Invalid person type: MUST BE DENTIST!",
//            PERSON.name,
//            INVALID_PERSON_PERSON_TYPE_DENTIST.code
//        ) {
//            personType == DENTIST
//        }
//    }

    /*private fun assertPatient() {
        Assertion.assert(
            "Invalid person type: MUST BE PATIENT!",
            PERSON.name,
            INVALID_PERSON_PERSON_TYPE_PATIENT.code
        ) {
            personType == PATIENT
        }
    }*/

    private fun assertValidCpf() {
        val rawCpf = cpf.replace(".", "").replace("-", "")
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
            INVALID_PERSON_CPF.code
        ) {
            valid
        }
    }

    private fun assertValidPersonId() {
        if(personalId != null) {
            Assertion.assert(
                "Invalid personal id: 5 <= name.length <= 15!",
                PERSON.name,
                INVALID_PERSON_PERSONAL_ID.code
            ) {
                personalId!!.length in 5..15
            }
        }
    }

    private fun assertValidOccupation()  {
        if(occupation != null) {
            Assertion.assert(
                "Invalid occupation: 3 <= occupation.length <= 50!",
                PERSON.name,
                INVALID_PERSON_OCCUPATION.code
            ) {
                occupation!!.length in 3..50
            }
        }
    }

    private fun assertValidAddresses() {
        Assertion.assert(
            "Invalid addresses size: one at least!",
            PERSON.name,
            INVALID_PERSON_ADDRESSES.code
        ) {
            addresses.isNotEmpty()
        }
    }

    private fun assertValidContacts() {
        Assertion.assert(
            "Invalid contacts size: one at least!",
            PERSON.name,
            INVALID_PERSON_CONTACTS.code
        ) {
            contacts.isNotEmpty()
        }
    }

    private fun assertValidStatus()  {
        Assertion.assert(
            "Invalid status: PATIENT needs a defined status!",
            PERSON.name,
            INVALID_PERSON_STATUS.code
        ) {
            PersonType.PATIENT == personType && status != null
        }
    }

    companion object {
        fun apply(events: List<PersonEvent>): Person = Person().apply(events)
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
