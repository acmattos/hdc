package br.com.acmattos.hdc.person.domain.model

import br.com.acmattos.hdc.common.context.domain.model.Entity
import br.com.acmattos.hdc.common.context.domain.model.Id
import br.com.acmattos.hdc.common.tool.assertion.Assertion
import br.com.acmattos.hdc.person.domain.cqs.CreateADentistEvent
import br.com.acmattos.hdc.person.domain.cqs.PersonEvent
import java.time.LocalDateTime

/**
 * @author ACMattos
 * @since 30/09/2021.
 */
data class Person (
    private var personIdData: PersonId? = null,
    private var fullNameData: String? = null,
    private var personTypeData: PersonType? = null,
    private var cpfData: String? = null,
    private var personalIdData: String? = null,
    private var addressesData: List<Address>? = null,
    private var contactsData: List<Contact>? = null,
    private var enabledData: Boolean = true,
    private var createdAtData: LocalDateTime = LocalDateTime.now(),
    private var updatedAtData: LocalDateTime? = null
): Entity {
    val personId get() = personIdData!!
    val fullName get() = fullNameData!!
    val personType get() = personTypeData!!
    val cpf get() = cpfData!!
    val personalId get() = personalIdData!!
    val addresses get() = addressesData!!
    val contacts get() = contactsData!!
    val enabled get() = enabledData
    val createdAt get() = createdAtData
    val updatedAt get() = updatedAtData

    fun apply(events: List<PersonEvent>): Person {
        for (event in events) {
            apply(event)
        }
        return this
    }

    fun apply(event: PersonEvent): Person {
        when(event) {
            is CreateADentistEvent -> apply(event)
            else -> apply(event as CreateADentistEvent)
        }
        return this
    }

    private fun apply(event: CreateADentistEvent) {
        personIdData = event.personId
        fullNameData = event.fullName
        personTypeData = event.personType
        cpfData = event.cpf
        personalIdData = event.personalId
        addressesData = event.addresses
        contactsData = event.contacts
        enabledData = event.enabled
        createdAtData = event.createdAt
    }

    companion object {
        fun apply(events: List<PersonEvent>): Person = Person().apply(events)
        fun apply(event: PersonEvent): Person = Person().apply(event)
    }
}

/**
 * @author ACMattos
 * @since 30/09/2021.
 */
class PersonId: Id {
    constructor(id: String): super(id)
    constructor(): super()
}

/**
 * @author ACMattos
 * @since 30/09/2021.
 */
enum class PersonType {
    DENTIST, PATIENT;
    companion object {
        fun convert(term: String): PersonType {
            val type: PersonType? = values().firstOrNull { type ->
                term.toUpperCase() == type.name
            }
            Assertion.assert(
                "[$term] does not correspond to a valid PersonType!"
            ) {
                type != null
            }
            return type!!
        }
    }
}
