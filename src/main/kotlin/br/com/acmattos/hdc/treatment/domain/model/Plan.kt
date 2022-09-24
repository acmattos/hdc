package br.com.acmattos.hdc.treatment.domain.model

import br.com.acmattos.hdc.common.context.domain.model.Id
import java.math.BigDecimal
import java.time.LocalDateTime

/**
 * @author ACMattos
 * @since 24/08/2022.
 */
data class Plan(
    private var planIdData: PlanId? = null,
    private var descriptionData: String? = null,
    private var treatmentsData: List<Treatment>? = null,
    private var costData: BigDecimal? = null,
    private var createdAtData: LocalDateTime = LocalDateTime.now(),
    private var updatedAtData: LocalDateTime? = null
) {
//    private var treatmentIdData: TreatmentId? = n
    val description get() = descriptionData!!
    val cost get() = costData!!
    val createdAt get() = createdAtData
    val updatedAt get() = updatedAtData
}

/**
 * @author ACMattos
 * @since 24/08/2022.
 */
class PlanId: Id {
    constructor(id: String): super(id)
    constructor(): super()
}
