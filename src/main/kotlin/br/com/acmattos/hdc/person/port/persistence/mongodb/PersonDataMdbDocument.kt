package br.com.acmattos.hdc.person.port.persistence.mongodb

import br.com.acmattos.hdc.common.context.port.persistence.mongodb.MdbDocument
import br.com.acmattos.hdc.person.domain.model.Address
import br.com.acmattos.hdc.person.domain.model.Contact
import br.com.acmattos.hdc.person.domain.model.ContactType
import br.com.acmattos.hdc.person.domain.model.DentalPlan
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
            street = street,
            number = number,
            complement = complement,
            zipCode = zipCode,
            neighborhood = neighborhood,
            state = State.convert(state?:"RJ"),
            city = city,
            createdAt = createdAt,
            updatedAt = updatedAt,
        )
}

/**
 * @author ACMattos
 * @since 04/10/2021.
 */
data class ContactMdbDocument (
    val info: String,
    val type: String,
    val obs: String?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime?
): MdbDocument() {
    constructor(
        contact: Contact
    ): this(
        info = contact.info,
        type = contact.type.name,
        obs = contact.obs,
        createdAt = contact.createdAt,
        updatedAt = contact.updatedAt,
    )

    override fun toType(): Contact =
        Contact(
            info = info,
            type = ContactType.convert(type?:"EMERGENCY"),
            obs = obs,
            createdAt = createdAt,
            updatedAt = updatedAt,
        )
}

/**
 * @author ACMattos
 * @since 21/04/2022.
 */
data class DentalPlanMdbDocument(
    val name: String,
    val number: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime?
): MdbDocument() {
    constructor(
        dentalPlan: DentalPlan
    ): this(
        name = dentalPlan.name,
        number = dentalPlan.number,
        createdAt = dentalPlan.createdAt,
        updatedAt = dentalPlan.updatedAt,
    )

    override fun toType(): DentalPlan =
        DentalPlan(
            name = name,
            number = number,
            createdAt = createdAt,
            updatedAt = updatedAt,
        )
}