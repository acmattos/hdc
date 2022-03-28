package br.com.acmattos.hdc.scheduler.domain.model

import br.com.acmattos.hdc.common.context.domain.model.Entity
import br.com.acmattos.hdc.common.context.domain.model.Id
import br.com.acmattos.hdc.common.tool.assertion.Assertion
import br.com.acmattos.hdc.scheduler.config.MessageTrackerCodeEnum.INVALID_DENTIST_FULL_NAME
import br.com.acmattos.hdc.scheduler.config.ScheduleLogEnum.SCHEDULE

/**
 * @author ACMattos
 * @since 07/11/2021.
 */
data class Dentist(
    val dentistId: DentistId,
    val fullName: String
): Entity {
    init {
        assertFullName()
    }

    private fun assertFullName(){
        Assertion.assert(
            "Invalid name for the Dentist",
            SCHEDULE.name,
            INVALID_DENTIST_FULL_NAME.code
        ) {
            fullName.isNotBlank()
        }
    }

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
