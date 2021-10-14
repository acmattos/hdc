package br.com.acmattos.hdc.person.port.persistence.mongodb

import br.com.acmattos.hdc.common.context.port.persistence.mongodb.MdbDocument
import br.com.acmattos.hdc.person.domain.model.Address
import br.com.acmattos.hdc.person.domain.model.Contact
import br.com.acmattos.hdc.person.domain.model.State
import java.time.LocalDateTime

/**
 * @author ACMattos
 * @since 04/10/2021.
 */
data class AddressMdbDocument (
    val street: String,
    val number: String,
    val complement: String?,
    val zipCode: String,
    val neighborhood: String,
    val state: String,
    val city: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime?
): MdbDocument() {
    constructor(
        address: Address
    ): this(
        street = address.street,
        number = address.number,
        complement = address.complement,
        zipCode = address.zipCode,
        neighborhood = address.neighborhood,
        state = address.state.name,
        city = address.city,
        createdAt = address.createdAt,
        updatedAt = address.updatedAt,
    )

    override fun toType(): Address =
        Address(
            streetData = street,
            numberData = number,
            complementData = complement,
            zipCodeData = zipCode,
            neighborhoodData = neighborhood,
            stateData = State.convert(state),
            cityData = city,
            createdAtData = createdAt,
            updatedAtData = updatedAt,
        )
}

/**
 * @author ACMattos
 * @since 04/10/2021.
 */
data class ContactMdbDocument (
    val info: String,
    val type: String?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime?
): MdbDocument() {
    constructor(
        contact: Contact
    ): this(
        info = contact.info,
        type = contact.type,
        createdAt = contact.createdAt,
        updatedAt = contact.updatedAt,
    )

    override fun toType(): Contact =
        Contact(
            infoData = info,
            typeData = type,
            createdAtData = createdAt,
            updatedAtData = updatedAt,
        )
}
