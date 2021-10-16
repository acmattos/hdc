package br.com.acmattos.hdc.person.port.persistence.mongodb

import br.com.acmattos.hdc.common.context.port.persistence.mongodb.MdbDocument
import br.com.acmattos.hdc.person.domain.model.Person
import br.com.acmattos.hdc.person.domain.model.PersonId
import br.com.acmattos.hdc.person.domain.model.PersonType
import java.time.LocalDateTime

/**
 * @author ACMattos
 * @since 04/10/2021.
 */
class PersonMdbDocument(
    val personId: String,
    val fullName: String,
    val personType: String,
    val cpf: String,
    val personalId: String?,
    val addresses: List<AddressMdbDocument>,
    val contacts: List<ContactMdbDocument>,
    val enabled: Boolean,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime?
): MdbDocument() {
    constructor(
        person: Person
    ): this(
        personId = person.personId.id,
        fullName = person.fullName,
        personType = person.personType.name,
        cpf = person.cpf,
        personalId = person.personalId,
        addresses = person.addresses.map { AddressMdbDocument(it) },
        contacts = person.contacts.map { ContactMdbDocument(it) },
        enabled = person.enabled,
        createdAt = person.createdAt,
        updatedAt = person.updatedAt,
    )

    override fun toType(): Person =
        Person(
            personIdData = PersonId(personId),
            fullNameData = fullName,
            personTypeData = PersonType.convert(personType),
            cpfData = cpf,
            personalIdData = personalId,
            addressesData = addresses.map { it.toType() },
            contactsData = contacts.map { it.toType() },
            enabledData = enabled,
            createdAtData = createdAt,
            updatedAtData = updatedAt
        )
}
