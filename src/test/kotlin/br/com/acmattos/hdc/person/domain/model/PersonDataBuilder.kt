package br.com.acmattos.hdc.person.domain.model

import br.com.acmattos.hdc.person.domain.model.ContactType.EMAIL
import java.time.LocalDateTime

/**
 * @author ACMattos
 * @since 06/09/2022.
 */
class PersonDataBuilder {
    companion object {
        fun buildAddress(
            street: String = "Street Name",
            number: String = "Number 30",
            complement: String = "Complement 1",
            zipCode: String = "22777-230",
            city: String = "City",
        ) = Address(
            street,
            number,
            complement,
            zipCode,
            "Neighborhood",
            State.RJ,
            city,
            LocalDateTime.of(2022, 4, 22, 11, 2, 0),
            null
        )

        fun buildContact(
            info: String = "info@email.com",
            type: ContactType = EMAIL,
            obs: String = "OBS",
        ) = Contact(
            info,
            type,
            obs,
            LocalDateTime.of(2022, 4, 22, 11, 2, 0),
            null
        )
    }
}