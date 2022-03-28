package br.com.acmattos.hdc.person.domain.model

import br.com.acmattos.hdc.common.context.domain.model.ValueObject
import br.com.acmattos.hdc.common.tool.enum.assertThatTerm
import br.com.acmattos.hdc.person.config.MessageTrackerCodeEnum.CONTACT_TYPE_CONVERT_FAILED
import br.com.acmattos.hdc.person.config.MessageTrackerCodeEnum.STATE_CONVERT_FAILED
import br.com.acmattos.hdc.person.config.PersonLogEnum.PERSON
import java.time.LocalDateTime

/**
 * @author ACMattos
 * @since 30/09/2021.
 */
data class Address(
    private var streetData: String? = null,
    private var numberData: String? = null,
    private var complementData: String? = null,
    private var zipCodeData: String? = null,
    private var neighborhoodData: String? = null,
    private var stateData: State? = null,
    private var cityData: String? = null,
    private var createdAtData: LocalDateTime = LocalDateTime.now(),
    private var updatedAtData: LocalDateTime? = null
): ValueObject {
    val street get() = streetData!!
    val number get() = numberData!!
    val complement get() = complementData
    val zipCode get() = zipCodeData!!
    val neighborhood get() = neighborhoodData!!
    val state get() = stateData!!
    val city get() = cityData!!
    val createdAt get() = createdAtData
    val updatedAt get() = updatedAtData
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
            "[$term] does not correspond to a valid State!",
            PERSON.name,
            STATE_CONVERT_FAILED.code
        )
    }
}

/**
 * @author ACMattos
 * @since 30/09/2021.
 */
data class Contact(
    private var infoData: String? = null,
    private var typeData: String? = null,// TODO CONTACT TYPE HERE
    private var createdAtData: LocalDateTime = LocalDateTime.now(),
    private var updatedAtData: LocalDateTime? = null
): ValueObject {
    val info get() = infoData!!
    val type get() = typeData!!
    val createdAt get() = createdAtData
    val updatedAt get() = updatedAtData
}


/**
 * @author ACMattos
 * @since 10/03/2022.
 */
enum class ContactType {
    EMAIL, CELULAR, PHONE;
    companion object {
        fun convert(term: String): State = assertThatTerm(
            term,
            "[$term] does not correspond to a valid contact type!",
            PERSON.name,
            CONTACT_TYPE_CONVERT_FAILED.code
        )
    }
}
/**
 * @author ACMattos
 * @since 12/11/2021.
 */
data class HealthInsurance(
    private var companyNameData: String? = null,
    private var planNumberData: String? = null,
    private var planNameData: String? = null,
    private var createdAtData: LocalDateTime = LocalDateTime.now(),
    private var updatedAtData: LocalDateTime? = null
):ValueObject {
    val companyName get() = companyNameData!!
    val planNumber get() = planNumberData!!
    val planName get() = planNameData!!
    val createdAt get() = createdAtData
    val updatedAt get() = updatedAtData
}
