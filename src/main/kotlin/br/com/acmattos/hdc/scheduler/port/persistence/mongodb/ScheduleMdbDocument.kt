package br.com.acmattos.hdc.scheduler.port.persistence.mongodb

import br.com.acmattos.hdc.common.context.port.persistence.mongodb.MdbDocument
import br.com.acmattos.hdc.scheduler.domain.model.Period
import br.com.acmattos.hdc.scheduler.domain.model.Schedule
import br.com.acmattos.hdc.scheduler.domain.model.ScheduleDentistId
import br.com.acmattos.hdc.scheduler.domain.model.ScheduleId
import java.time.LocalDateTime

/**
 * @author ACMattos
 * @since 08/07/2020.
 */
data class ScheduleMdbDocument(
    val scheduleId: String,
    val scheduleDentistId: String,
    val periods: List<Period>,
    val enabled: Boolean,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime?
): MdbDocument() {
    constructor(
        schedule: Schedule
    ): this(
        scheduleId = schedule.scheduleId.id,
        scheduleDentistId = schedule.scheduleDentistId!!.id,
        periods = schedule.periods,
        enabled = schedule.enabled,
        createdAt = schedule.createdAt,
        updatedAt = schedule.updatedAt
    )

    override fun toType(): Schedule =
        Schedule(
            scheduleIdData = ScheduleId(scheduleId),
            scheduleDentistIdData = ScheduleDentistId(scheduleDentistId),
            periodsData = periods,
            enabledData = enabled,
            createdAtData = createdAt,
            updatedAtData = updatedAt
        )
}
