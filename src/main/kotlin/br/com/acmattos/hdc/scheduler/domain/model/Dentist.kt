package br.com.acmattos.hdc.scheduler.domain.model

import br.com.acmattos.hdc.common.context.domain.model.Entity
import br.com.acmattos.hdc.common.context.domain.model.Id

/**
 * @author ACMattos
 * @since 07/11/2021.
 */
data class Dentist(
    private var dentistIdData: DentistId? = null,
    private var fullNameData: String? = null,
): Entity {
    val dentistId get() = dentistIdData
    val fullName get() = fullNameData
}

/**
 * @author ACMattos
 * @since 07/11/2021.
 */
class DentistId: Id {
    constructor(id: String): super(id)
    constructor(): super()
}
