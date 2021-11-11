package br.com.acmattos.hdc.scheduler.port.persistence.mongodb

import br.com.acmattos.hdc.common.context.port.persistence.mongodb.MdbDocument
import br.com.acmattos.hdc.scheduler.domain.model.Dentist
import br.com.acmattos.hdc.scheduler.domain.model.DentistId

/**
 * @author ACMattos
 * @since 11/11/2021.
 */
data class DentistMdbDocument(
    val dentistId: String,
    val fullName: String,
): MdbDocument() {
    constructor(
        dentist: Dentist
    ): this(
        dentistId = dentist.dentistId.id,
        fullName = dentist.fullName,
    )

    override fun toType(): Dentist =
        Dentist(
            DentistId(dentistId),
            fullName,
        )
}
