package br.com.acmattos.hdc.person.domain.cqs

import br.com.acmattos.hdc.common.context.domain.cqs.Command
import br.com.acmattos.hdc.common.context.domain.model.AuditLog
import br.com.acmattos.hdc.person.domain.model.Address
import br.com.acmattos.hdc.person.domain.model.Contact
import br.com.acmattos.hdc.person.domain.model.DentalPlan
import br.com.acmattos.hdc.person.domain.model.Gender
import br.com.acmattos.hdc.person.domain.model.MaritalStatus
import br.com.acmattos.hdc.person.domain.model.PersonId
import br.com.acmattos.hdc.person.domain.model.PersonType
import br.com.acmattos.hdc.person.domain.model.Status
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * @author ACMattos
 * @since 30/09/2021.
 */
open class PersonCommand(
    open val personId: PersonId,
    open val fullName: String,
    override val auditLog: AuditLog
): Command(auditLog)

/**
 * @author ACMattos
 * @since 30/09/2021.
 */
open class CreateAPersonCommand(
    override val personId: PersonId,
    override val fullName: String,
    open val dob: LocalDate,
    open val maritalStatus: MaritalStatus,
    open val gender: Gender,
    open val personType: PersonType,
    open val cpf: String,
    open val personalId: String?,
    open val occupation: String? = null,
    open val addresses: List<Address>,
    open val contacts: List<Contact>,
    open val dentalPlan: DentalPlan?,
    open val responsibleFor: PersonId?,
    open val indicatedBy: String?,
    open val familyGroup: List<PersonId>,
    open val status: Status?,
    open val lastAppointment: LocalDate?,
    open val enabled: Boolean,
    open val createdAt: LocalDateTime,
    override val auditLog: AuditLog
): PersonCommand(personId, fullName, auditLog)

/**
 * @author ACMattos
 * @since 23/05/2021.
 */
open class UpdateAPersonCommand(
    override val personId: PersonId,
    override val fullName: String,
    open val dob: LocalDate,
    open val maritalStatus: MaritalStatus,
    open val gender: Gender,
    open val personType: PersonType,
    open val cpf: String,
    open val personalId: String?,
    open val occupation: String? = null,
    open val addresses: List<Address>,
    open val contacts: List<Contact>,
    open val dentalPlan: DentalPlan?,
    open val responsibleFor: PersonId?,
    open val indicatedBy: String?,
    open val familyGroup: List<PersonId>,
    open val status: Status?,
    open val lastAppointment: LocalDate?,
    open val enabled: Boolean,
    open val createdAt: LocalDateTime,
    override val auditLog: AuditLog
): PersonCommand(personId, fullName, auditLog)

/**
 * @author ACMattos
 * @since 30/09/2021.
 */
data class CreateDentistCommand(
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
): CreateAPersonCommand(
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
    null,
    enabled,
    createdAt,
    auditLog
) {
    constructor(
        fullName: String,
        cpf: String,
        dob: LocalDate,
        maritalStatus: String,
        gender: String,
        personalId: String?,
        addresses: List<Address>,
        contacts: List<Contact>,
        responsibleFor: PersonId?,
        familyGroup: List<PersonId>,
        auditLog: AuditLog
    ): this(
        PersonId(),
        fullName,
        dob,
        MaritalStatus.convert(maritalStatus),
        Gender.convert(gender),
        PersonType.DENTIST,
        cpf,
        personalId,
        addresses,
        contacts,
        responsibleFor,
        familyGroup,
        true,
        LocalDateTime.now(),
        auditLog
    )
}

/**
 * @author ACMattos
 * @since 21/04/2022.
 */
data class CreatePatientCommand(
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
    override val lastAppointment: LocalDate?,
    override val enabled: Boolean,
    override val createdAt: LocalDateTime,
    override val auditLog: AuditLog
): CreateAPersonCommand(
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
    lastAppointment,
    enabled,
    createdAt,
    auditLog
) {
    constructor(
        fullName: String,
        dob: LocalDate,
        cpf: String,
        maritalStatus: String,
        gender: String,
        personalId: String?,
        occupation: String?,
        addresses: List<Address>,
        contacts: List<Contact>,
        dentalPlan: DentalPlan?,
        responsibleFor: PersonId?,
        indicatedBy: String?,
        familyGroup: List<String>?,
        status: String,
        lastAppointment: LocalDate?,
        auditLog: AuditLog
    ): this(
        PersonId(),
        fullName,
        dob,
        MaritalStatus.convert(maritalStatus),
        Gender.convert(gender),
        PersonType.PATIENT,
        cpf,
        personalId,
        occupation,
        addresses,
        contacts,
        dentalPlan,
        responsibleFor,
        indicatedBy,
        familyGroup?.map { PersonId(it) } ?: listOf(),
        Status.convert(status),
        lastAppointment,
        true,
        LocalDateTime.now(),
        auditLog
    )
}

/**
 * @author ACMattos
 * @since 23/05/2022.
 */
data class UpdatePatientCommand(
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
    override val lastAppointment: LocalDate?,
    override val enabled: Boolean,
    override val createdAt: LocalDateTime,
    override val auditLog: AuditLog
): UpdateAPersonCommand(
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
    lastAppointment,
    enabled,
    createdAt,
    auditLog
) {
    constructor(
        personId: String,
        fullName: String,
        dob: LocalDate,
        cpf: String,
        maritalStatus: String,
        gender: String,
        personalId: String?,
        occupation: String?,
        addresses: List<Address>,
        contacts: List<Contact>,
        dentalPlan: DentalPlan?,
        responsibleFor: PersonId?,
        indicatedBy: String?,
        familyGroup: List<PersonId>,
        status: String,
        lastAppointment: LocalDate?,
        enabled: Boolean,
        auditLog: AuditLog
    ): this(
        PersonId(personId),
        fullName,
        dob,
        MaritalStatus.convert(maritalStatus),
        Gender.convert(gender),
        PersonType.PATIENT,
        cpf,
        personalId,
        occupation,
        addresses,
        contacts,
        dentalPlan,
        responsibleFor,
        indicatedBy,
        familyGroup,
        Status.convert(status),
        lastAppointment,
        true,
        LocalDateTime.now(),
        auditLog
    )
}