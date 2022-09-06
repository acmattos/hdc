package br.com.acmattos.hdc.person.domain.model

import br.com.acmattos.hdc.common.context.domain.model.ValueObject
import br.com.acmattos.hdc.common.tool.assertion.Assertion
import br.com.acmattos.hdc.common.tool.enum.assertThatTerm
import br.com.acmattos.hdc.person.config.MessageTrackerCodeEnum.CONTACT_TYPE_CONVERT_FAILED
import br.com.acmattos.hdc.person.config.MessageTrackerCodeEnum.INVALID_PERSON_ADDRESS_CITY
import br.com.acmattos.hdc.person.config.MessageTrackerCodeEnum.INVALID_PERSON_ADDRESS_COMPLEMENT
import br.com.acmattos.hdc.person.config.MessageTrackerCodeEnum.INVALID_PERSON_ADDRESS_NEIGHBORHOOD
import br.com.acmattos.hdc.person.config.MessageTrackerCodeEnum.INVALID_PERSON_ADDRESS_NUMBER
import br.com.acmattos.hdc.person.config.MessageTrackerCodeEnum.INVALID_PERSON_ADDRESS_STREET
import br.com.acmattos.hdc.person.config.MessageTrackerCodeEnum.INVALID_PERSON_ADDRESS_ZIP_CODE
import br.com.acmattos.hdc.person.config.MessageTrackerCodeEnum.INVALID_PERSON_CONTACT_INFO
import br.com.acmattos.hdc.person.config.MessageTrackerCodeEnum.INVALID_PERSON_CONTACT_OBS
import br.com.acmattos.hdc.person.config.MessageTrackerCodeEnum.INVALID_PERSON_DENTAL_PLAN_NAME
import br.com.acmattos.hdc.person.config.MessageTrackerCodeEnum.INVALID_PERSON_DENTAL_PLAN_NUMBER
import br.com.acmattos.hdc.person.config.MessageTrackerCodeEnum.MARITAL_STATUS_CONVERT_FAILED
import br.com.acmattos.hdc.person.config.MessageTrackerCodeEnum.PERSON_TYPE_CONVERT_FAILED
import br.com.acmattos.hdc.person.config.MessageTrackerCodeEnum.STATE_CONVERT_FAILED
import br.com.acmattos.hdc.person.config.MessageTrackerCodeEnum.STATUS_CONVERT_FAILED
import br.com.acmattos.hdc.person.config.PersonLogEnum.PERSON
import br.com.acmattos.hdc.person.domain.model.ContactType.CELLULAR
import br.com.acmattos.hdc.person.domain.model.ContactType.EMAIL
import br.com.acmattos.hdc.person.domain.model.ContactType.EMERGENCY
import java.time.LocalDateTime

/**
 * @author ACMattos
 * @since 30/09/2021.
 */
data class Address(
    val street: String,
    val number: String?,
    val complement: String?,
    val zipCode: String,
    val neighborhood: String,
    val state: State,
    val city: String,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime? = null,
): ValueObject {
    init {
        assertValidStreet()
        assertValidNumber()
        assertValidComplement()
        assertValidZipCode()
        assertValidNeighborhood()
        assertValidCity()
    }

    private fun assertValidStreet() {
        Assertion.assert(
            "Invalid address street: 3 <= street.length <= 100!",
            PERSON.name,
            INVALID_PERSON_ADDRESS_STREET.code
        ) {
            street.length in 3..100
        }
    }

    private fun assertValidNumber() {
        if(number != null) {
            Assertion.assert(
                "Invalid address number: 1 <= number.length <= 10!",
                PERSON.name,
                INVALID_PERSON_ADDRESS_NUMBER.code
            ) {
                number.length in 1..10
            }
        }
    }

    private fun assertValidComplement() {
        if(complement != null) {
            Assertion.assert(
                "Invalid address complement: 1 <= complement.length <= 50!",
                PERSON.name,
                INVALID_PERSON_ADDRESS_COMPLEMENT.code
            ) {
                complement!!.length in 1..50
            }
        }
    }
    private fun assertValidZipCode() {
        Assertion.assert(
            "Invalid address zip code: format 12345-678!",
            PERSON.name,
            INVALID_PERSON_ADDRESS_ZIP_CODE.code
        ) {
            zipCode.matches(Regex("^\\d{5}[-]\\d{3}$"))
        }
    }

    private fun assertValidNeighborhood() {
        Assertion.assert(
            "Invalid address neighborhood: 3 <= neighborhood.length <= 100!",
            PERSON.name,
            INVALID_PERSON_ADDRESS_NEIGHBORHOOD.code
        ) {
            neighborhood.length in 3..100

        }
    }
    private fun assertValidCity() {
        Assertion.assert(
            "Invalid address city: 3 <= city.length <= 100!",
            PERSON.name,
            INVALID_PERSON_ADDRESS_CITY.code
        ) {
            city.length in 3..100
        }
    }
}

/**
 * @author ACMattos
 * @since 30/09/2021.
 */
data class Contact(
    val info: String,
    val type: ContactType,
    val obs: String?,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime? = null,
): ValueObject {
    init {
        assertValidInfo()
        assertValidObs()
    }

    private fun assertValidInfo() {
        Assertion.assert(
            "Invalid contact info: [$type]!",
            PERSON.name,
            INVALID_PERSON_CONTACT_INFO.code
        ) {
            info != null && info.matches(getEquivalentPattern())
        }
    }

    private fun getEquivalentPattern() =
        when {
            EMAIL == type -> Regex("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
            EMERGENCY == type -> Regex("^((\\(\\d{2}\\))|\\d{2})[- .]?\\d{4,5}[- .]?\\d{4}$")
            CELLULAR == type -> Regex("^((\\(\\d{2}\\))|\\d{2})[- .]?\\d{5}[- .]?\\d{4}$")
            else /*PHONE */-> Regex("^((\\(\\d{2}\\))|\\d{2})[- .]?\\d{4}[- .]?\\d{4}$")
        }

    private fun assertValidObs() {
        if(obs != null) {
            Assertion.assert(
                "Invalid contact obs: 3 <= obs.length <= 20!",
                PERSON.name,
                INVALID_PERSON_CONTACT_OBS.code
            ) {
                obs!!.length in 3..20
            }
        }
    }
}

/**
 * @author ACMattos
 * @since 12/11/2021.
 */
data class DentalPlan(
    val name: String,
    val number: String,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime? = null,
):ValueObject {
    init {
        assertValidName()
        assertValidNumber()
    }

    private fun assertValidName() {
        Assertion.assert(
            "Invalid dental plan name: 2 <= name.length <= 30!",
            PERSON.name,
            INVALID_PERSON_DENTAL_PLAN_NAME.code
        ) {
            name.isNotBlank() && name.length in 2..30
        }
    }

    private fun assertValidNumber() {
        Assertion.assert(
            "Invalid dental plan number: 5 <= number.length <= 20!",
            PERSON.name,
            INVALID_PERSON_DENTAL_PLAN_NUMBER.code
        ) {
            number.isNotBlank() && number.length in 5..20
        }
    }
}

/**
 * @author ACMattos
 * @since 10/03/2022.
 */
enum class ContactType {
    EMAIL, CELLULAR, PHONE, EMERGENCY;
    companion object {
        fun convert(term: String): ContactType = assertThatTerm(
            term,
            "[$term] does not correspond to a valid contact type!",
            PERSON.name,
            CONTACT_TYPE_CONVERT_FAILED.code
        )
    }
}

/**
 * @author ACMattos
 * @since 20/04/2022.
 */
enum class Gender {
    FEMALE, MALE;
    companion object {
        fun convert(term: String): Gender = assertThatTerm(
            term,
            "[$term] does not correspond to a valid gender!",
            PERSON.name,
            MARITAL_STATUS_CONVERT_FAILED.code
        )
    }
}

/**
 * @author ACMattos
 * @since 20/04/2022.
 */
enum class MaritalStatus {
    MARRIED, SINGLE, DIVORCED, SEPARATED, WIDOWED;
    companion object {
        fun convert(term: String): MaritalStatus = assertThatTerm(
            term,
            "[$term] does not correspond to a valid marital status!",
            PERSON.name,
            MARITAL_STATUS_CONVERT_FAILED.code
        )
    }
}

/**
 * @author ACMattos
 * @since 30/09/2021.
 */
enum class PersonType {
    DENTIST, PATIENT;

    companion object {
        fun convert(term: String): PersonType = assertThatTerm(
            term,
            "[$term] does not correspond to a valid person type!",
            PERSON.name,
            PERSON_TYPE_CONVERT_FAILED.code
        )
    }
}

/**
 * @author ACMattos
 * @since 30/09/2021.
 */
enum class State {
    RJ;
    companion object {
        fun convert(term: String): State = assertThatTerm(
            term,
            "[$term] does not correspond to a valid state!",
            PERSON.name,
            STATE_CONVERT_FAILED.code
        )
    }
}

/**
 * @author ACMattos
 * @since 18/04/2022.
 */
enum class Status {
    SPECIAL, REGULAR, AVOID;

    companion object {
        fun convert(term: String): Status = assertThatTerm(
            term,
            "[$term] does not correspond to a valid status!",
            PERSON.name,
            STATUS_CONVERT_FAILED.code
        )
    }
}
