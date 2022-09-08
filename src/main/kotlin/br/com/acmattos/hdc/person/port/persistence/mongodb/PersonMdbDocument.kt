package br.com.acmattos.hdc.person.port.persistence.mongodb

import br.com.acmattos.hdc.common.context.port.persistence.mongodb.MdbDocument
import br.com.acmattos.hdc.person.domain.model.Gender
import br.com.acmattos.hdc.person.domain.model.MaritalStatus
import br.com.acmattos.hdc.person.domain.model.Person
import br.com.acmattos.hdc.person.domain.model.PersonId
import br.com.acmattos.hdc.person.domain.model.PersonType
import br.com.acmattos.hdc.person.domain.model.Status
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * @author ACMattos
 * @since 04/10/2021.
 */
data class PersonMdbDocument(
    val personId: String,
    val fullName: String,
    val dob: LocalDate,
    val maritalStatus: String,
    val gender: String,
    val personType: String,
    val cpf: String,
    val personalId: String?,
    val occupation: String?,
    val addresses: List<AddressMdbDocument>,
    val contacts: List<ContactMdbDocument>,
    val dentalPlan: DentalPlanMdbDocument?,
    val responsibleFor: String?,
    val indicatedBy: String?,
    val familyGroup: List<String>,
    val status: String?,
    val lastAppointment: LocalDate?,
    val enabled: Boolean,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime?
): MdbDocument() {
    constructor(
        person: Person
    ): this(
        personId = person.personId.id,
        fullName = person.fullName,
        dob = person.dob,
        maritalStatus = person.maritalStatus.name,
        gender = person.gender.name,
        personType = person.personType.name,
        cpf = person.cpf,
        personalId = person.personalId,
        occupation = person.occupation,
        addresses = person.addresses.map { AddressMdbDocument(it) },
        contacts = person.contacts.map { ContactMdbDocument(it) },
        dentalPlan = if(person.dentalPlan != null) {
            DentalPlanMdbDocument(person.dentalPlan!!)
        } else {
            null
        },
        responsibleFor = person.responsibleFor?.id,
        indicatedBy = person.indicatedBy,
        familyGroup = person.familyGroup.map { it.id },
        status = person.status?.name,
        lastAppointment = person.lastAppointment,
        enabled = person.enabled,
        createdAt = person.createdAt,
        updatedAt = person.updatedAt,
    )

    override fun toType(): Person =
        Person(
            personIdData = PersonId(personId),
            fullNameData = fullName,
            dobData = dob,
            maritalStatusData = MaritalStatus.convert(maritalStatus),
            genderData = Gender.convert(gender),
            personTypeData = PersonType.convert(personType),
            cpfData = cpf,
            personalIdData = personalId,
            occupationData = occupation,
            addressesData = addresses.map { it.toType() },
            contactsData = contacts.map { it.toType() },
            dentalPlanData = dentalPlan?.toType(),
            responsibleForData = if(responsibleFor != null) {
                PersonId(responsibleFor)
            } else {
                null
            },
            indicatedByData = indicatedBy,
            familyGroupData = familyGroup.map { PersonId(it) },
            statusData = if(status != null) {
                Status.convert(status)
            } else {
                null
            },
            lastAppointmentData = lastAppointment,
            enabledData = enabled,
            createdAtData = createdAt,
            updatedAtData = updatedAt,
        )
}
