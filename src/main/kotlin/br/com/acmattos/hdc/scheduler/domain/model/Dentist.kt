package br.com.acmattos.hdc.scheduler.domain.model

import br.com.acmattos.hdc.common.context.domain.model.Entity
import br.com.acmattos.hdc.common.context.domain.model.Id

/**
 * @author ACMattos
 * @since 07/11/2021.
 */
data class Dentist(
    val dentistId: DentistId,
    val fullName: String
): Entity {
    fun getAcronym() = fullName
        .split(" ")
        .map { it[0] }
        .joinToString(",")
        .replace(",","")
}

/**
 * @author ACMattos
 * @since 07/11/2021.
 */
class DentistId: Id {
    constructor(id: String): super(id)
    constructor(): super()
}
