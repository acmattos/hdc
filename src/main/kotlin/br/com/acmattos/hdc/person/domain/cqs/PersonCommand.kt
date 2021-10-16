package br.com.acmattos.hdc.person.domain.cqs

import br.com.acmattos.hdc.common.context.domain.cqs.Command
import br.com.acmattos.hdc.common.context.domain.model.AuditLog
import br.com.acmattos.hdc.person.domain.model.Address
import br.com.acmattos.hdc.person.domain.model.Contact
import br.com.acmattos.hdc.person.domain.model.PersonId
import br.com.acmattos.hdc.person.domain.model.PersonType
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
    open val personType: PersonType,
    open val cpf: String,
    open val personalId: String?,
    open val addresses: List<Address>,
    open val contacts: List<Contact>,
    open val enabled: Boolean,
    open val createdAt: LocalDateTime,
    override val auditLog: AuditLog
): PersonCommand(personId, fullName, auditLog)

/**
 * @author ACMattos
 * @since 30/09/2021.
 */
data class CreateADentistCommand(
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
): CreateAPersonCommand(
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
        fullName: String,
        cpf: String,
        personalId: String?,
        addresses: List<Address>,
        contacts: List<Contact>,
        auditLog: AuditLog
    ): this(
        PersonId(),
        fullName,
        PersonType.DENTIST,
        cpf,
        personalId,
        addresses,
        contacts,
        true,
        LocalDateTime.now(),
        auditLog
    )
}
