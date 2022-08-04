package br.com.acmattos.hdc.person.domain.cqs

import br.com.acmattos.hdc.common.context.domain.cqs.Event
import br.com.acmattos.hdc.common.context.domain.model.AuditLog
import br.com.acmattos.hdc.person.domain.model.Address
import br.com.acmattos.hdc.person.domain.model.Contact
import br.com.acmattos.hdc.person.domain.model.DentalPlan
import br.com.acmattos.hdc.person.domain.model.Gender
import br.com.acmattos.hdc.person.domain.model.MaritalStatus
import br.com.acmattos.hdc.person.domain.model.PersonId
import br.com.acmattos.hdc.person.domain.model.PersonType
import br.com.acmattos.hdc.person.domain.model.Status
import com.fasterxml.jackson.annotation.JsonTypeName
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * @author ACMattos
 * @since 30/09/2021.
 */
open class PersonEvent(
    open val personId: PersonId,
    override val auditLog: AuditLog
): Event(auditLog)

/**
 * @author ACMattos
 * @since 30/09/2021.
 */
open class CreateAPersonEvent(
    override val personId: PersonId,
    open val fullName: String,
    open val dob: LocalDate,
    open val maritalStatus: MaritalStatus,
    open val gender: Gender,
    open val personType: PersonType,
    open val cpf: String,
    open val personalId: String?,
    open val occupation: String?,
    open val addresses: List<Address>,
    open val contacts: List<Contact>,
    open val dentalPlan: DentalPlan?,
    open val responsibleFor: PersonId?,
    open val indicatedBy: String?,
    open val familyGroup: List<PersonId>,
    open val status: Status?,
    open val enabled: Boolean,
    open val createdAt: LocalDateTime,
    override val auditLog: AuditLog
): PersonEvent(personId, auditLog)

/**
 * @author ACMattos
 * @since 23/05/2022.
 */
open class UpdateAPersonEvent(
    override val personId: PersonId,
    open val fullName: String,
    open val dob: LocalDate,
    open val maritalStatus: MaritalStatus,
    open val gender: Gender,
    open val personType: PersonType,
    open val cpf: String,
    open val personalId: String?,
    open val occupation: String?,
    open val addresses: List<Address>,
    open val contacts: List<Contact>,
    open val dentalPlan: DentalPlan?,
    open val responsibleFor: PersonId?,
    open val indicatedBy: String?,
    open val familyGroup: List<PersonId>,
    open val status: Status?,
    open val enabled: Boolean,
    open val createdAt: LocalDateTime,
    override val auditLog: AuditLog
): PersonEvent(personId, auditLog)

/**
 * @author ACMattos
 * @since 30/09/2021.
 */
@JsonTypeName("CreateADentistEvent")
data class CreateDentistEvent(
    override val personId: PersonId,
    override val fullName: String,
    override val dob: LocalDate,
    override val maritalStatus: MaritalStatus,
    override val gender: Gender,
    override val personType: PersonType,
    override val cpf: String,
    override val personalId: String?,
    override val addresses: List<Address>,
    override val contacts: List<Contact>,
    override val responsibleFor: PersonId?,
    override val familyGroup: List<PersonId>,
    override val enabled: Boolean,
    override val createdAt: LocalDateTime,
    override val auditLog: AuditLog
): CreateAPersonEvent(
    personId,
    fullName,
    dob,
    maritalStatus,
    gender,
    personType,
    cpf,
    personalId,
    null,
    addresses,
    contacts,
    null,
    responsibleFor,
    null,
    familyGroup,
    null,
    enabled,
    createdAt,
    auditLog
) {
    constructor(
        command: CreateDentistCommand
    ): this(
        personId = command.personId,
        fullName = command.fullName,
        dob = command.dob,
        maritalStatus = command.maritalStatus,
        gender = command.gender,
        personType = command.personType,
        cpf = command.cpf,
        personalId = command.personalId,
        addresses = command.addresses,
        contacts = command.contacts,
        responsibleFor = command.responsibleFor,
        familyGroup = command.familyGroup,
        enabled = command.enabled,
        createdAt = command.createdAt,
        auditLog = command.auditLog
    )
}

/**
 * @author ACMattos
 * @since 18/04/2022.
 */
@JsonTypeName("CreatePatientEvent")
data class CreatePatientEvent(
    override val personId: PersonId,
    override val fullName: String,
    override val dob: LocalDate,
    override val maritalStatus: MaritalStatus,
    override val gender: Gender,
    override val personType: PersonType,
    override val cpf: String,
    override val personalId: String?,
    override val occupation: String?,
    override val addresses: List<Address>,
    override val contacts: List<Contact>,
    override val dentalPlan: DentalPlan?,
    override val responsibleFor: PersonId?,
    override val indicatedBy: String?,
    override val familyGroup: List<PersonId>,
    override val status: Status,
    override val enabled: Boolean,
    override val createdAt: LocalDateTime,
    override val auditLog: AuditLog
): CreateAPersonEvent(
    personId,
    fullName,
    dob,
    maritalStatus,
    gender,
    personType,
    cpf,
    personalId,
    occupation,
    addresses,
    contacts,
    dentalPlan,
    responsibleFor,
    indicatedBy,
    familyGroup,
    status,
    enabled,
    createdAt,
    auditLog
) {
    constructor(
        command: CreatePatientCommand
    ): this(
        personId = command.personId,
        fullName = command.fullName,
        dob = command.dob,
        maritalStatus = command.maritalStatus,
        gender = command.gender,
        personType = command.personType,
        cpf = command.cpf,
        personalId = command.personalId,
        occupation = command.occupation,
        addresses = command.addresses,
        contacts = command.contacts,
        dentalPlan = command.dentalPlan,
        responsibleFor = command.responsibleFor,
        indicatedBy = command.indicatedBy,
        familyGroup = command.familyGroup,
        status = command.status,
        enabled = command.enabled,
        createdAt = command.createdAt,
        auditLog = command.auditLog
    )
}

/**
 * @author ACMattos
 * @since 23/05/2022.
 */
@JsonTypeName("UpdatePatientEvent")
data class UpdatePatientEvent(
    override val personId: PersonId,
    override val fullName: String,
    override val dob: LocalDate,
    override val maritalStatus: MaritalStatus,
    override val gender: Gender,
    override val personType: PersonType,
    override val cpf: String,
    override val personalId: String?,
    override val occupation: String?,
    override val addresses: List<Address>,
    override val contacts: List<Contact>,
    override val dentalPlan: DentalPlan?,
    override val responsibleFor: PersonId?,
    override val indicatedBy: String?,
    override val familyGroup: List<PersonId>,
    override val status: Status,
    override val enabled: Boolean,
    override val createdAt: LocalDateTime,
    override val auditLog: AuditLog
): UpdateAPersonEvent(
    personId,
    fullName,
    dob,
    maritalStatus,
    gender,
    personType,
    cpf,
    personalId,
    occupation,
    addresses,
    contacts,
    dentalPlan,
    responsibleFor,
    indicatedBy,
    familyGroup,
    status,
    enabled,
    createdAt,
    auditLog
) {
    constructor(
        command: UpdatePatientCommand
    ): this(
        personId = command.personId,
        fullName = command.fullName,
        dob = command.dob,
        maritalStatus = command.maritalStatus,
        gender = command.gender,
        personType = command.personType,
        cpf = command.cpf,
        personalId = command.personalId,
        occupation = command.occupation,
        addresses = command.addresses,
        contacts = command.contacts,
        dentalPlan = command.dentalPlan,
        responsibleFor = command.responsibleFor,
        indicatedBy = command.indicatedBy,
        familyGroup = command.familyGroup,
        status = command.status,
        enabled = command.enabled,
        createdAt = command.createdAt,
        auditLog = command.auditLog
    )
}
