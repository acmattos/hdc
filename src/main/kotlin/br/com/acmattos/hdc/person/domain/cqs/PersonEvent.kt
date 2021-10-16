package br.com.acmattos.hdc.person.domain.cqs

import br.com.acmattos.hdc.common.context.domain.cqs.Event
import br.com.acmattos.hdc.common.context.domain.model.AuditLog
import br.com.acmattos.hdc.person.domain.model.Address
import br.com.acmattos.hdc.person.domain.model.Contact
import br.com.acmattos.hdc.person.domain.model.PersonId
import br.com.acmattos.hdc.person.domain.model.PersonType
import com.fasterxml.jackson.annotation.JsonTypeName
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
    open val personType: PersonType,
    open val cpf: String,
    open val personalId: String?,
    open val addresses: List<Address>,
    open val contacts: List<Contact>,
    open val enabled: Boolean,
    open val createdAt: LocalDateTime,
    override val auditLog: AuditLog
): PersonEvent(personId, auditLog)

/**
 * @author ACMattos
 * @since 30/09/2021.
 */
@JsonTypeName("CreateADentistEvent")
data class CreateADentistEvent(
    override val personId: PersonId,
    override val fullName: String,
    override val personType: PersonType,
    override val cpf: String,
    override val personalId: String?,
    override val addresses: List<Address>,
    override val contacts: List<Contact>,
    override val enabled: Boolean,
    override val createdAt: LocalDateTime,
    override val auditLog: AuditLog
): CreateAPersonEvent(
    personId,
    fullName,
    personType,
    cpf,
    personalId,
    addresses,
    contacts,
    enabled,
    createdAt,
    auditLog
) {
    constructor(
        command: CreateADentistCommand
    ): this(
        personId = command.personId,
        fullName = command.fullName,
        personType = command.personType,
        cpf = command.cpf,
        personalId = command.personalId,
        addresses = command.addresses,
        contacts = command.contacts,
        enabled = command.enabled,
        createdAt = command.createdAt,
        auditLog = command.auditLog
    )
}
