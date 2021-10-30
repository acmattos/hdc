package br.com.acmattos.hdc.person.domain.model

import br.com.acmattos.hdc.common.context.domain.model.ValueObject
import br.com.acmattos.hdc.common.tool.enum.assertThatTerm
import br.com.acmattos.hdc.person.config.ErrorTrackerCodeEnum.STATE_CONVERT_FAILED
import java.time.LocalDateTime

/**
 * @author ACMattos
 * @since 30/09/2021.
 */
data class Address (
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
            STATE_CONVERT_FAILED.code
        )
    }
}

/**
 * @author ACMattos
 * @since 30/09/2021.
 */
data class Contact (
    private var infoData: String? = null,
    private var typeData: String? = null,
    private var createdAtData: LocalDateTime = LocalDateTime.now(),
    private var updatedAtData: LocalDateTime? = null
): ValueObject {
    val info get() = infoData!!
    val type get() = typeData
    val createdAt get() = createdAtData
    val updatedAt get() = updatedAtData
}
