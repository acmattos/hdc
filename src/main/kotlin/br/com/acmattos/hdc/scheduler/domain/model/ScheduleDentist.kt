package br.com.acmattos.hdc.scheduler.domain.model

import br.com.acmattos.hdc.common.context.domain.model.Id
import java.time.LocalDateTime

/**
 * @author ACMattos
 * @since 07/08/2021.
 */
class ScheduleDentist(
    private var scheduleDentistIdData: ScheduleDentistId? = null,
    private var scheduleDentistNameData: String? = null,
    private var enabledData: Boolean = true,
    private var createdAtData: LocalDateTime = LocalDateTime.now(),
    private var updatedAtData: LocalDateTime? = null
){
    val scheduleDentistId get() = scheduleDentistIdData
    val scheduleDentistName get() = scheduleDentistNameData
    val enabled get() = enabledData
    val createdAt get() = createdAtData
    val updatedAt get() = updatedAtData
}

/**
 * @author ACMattos
 * @since 07/08/2021.
 */
class ScheduleDentistId: Id {
    constructor(id: String): super(id)
    constructor(): super()
}
