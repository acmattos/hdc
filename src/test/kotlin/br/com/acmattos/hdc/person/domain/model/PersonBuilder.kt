package br.com.acmattos.hdc.person.domain.model

import java.time.LocalDateTime

/**
 * @author ACMattos
 * @since 04/11/2021.
 */
class PersonBuilder {
    companion object {
        fun buildDentist() = Person(
            PersonId("01FJJDJKDXN4K558FMCKEMQE6B"),
            "H DC",
            PersonType.DENTIST,
            "07108976923",
            "20734-29",
            listOf(Address(
                "Rua A",
                "30",
                "BL1",
                "22777-230",
                "Freg",
                 State.RJ,
                "Rio de Janeiro",
                LocalDateTime.now(),
                null
            )),
            listOf(Contact(
                "email@email.com",
                "email",
                LocalDateTime.now(),
                null
            )),
            null,
            true,
            LocalDateTime.now(),
            null
        )
    }
}
