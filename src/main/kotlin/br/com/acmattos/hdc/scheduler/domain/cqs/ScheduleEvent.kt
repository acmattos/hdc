package br.com.acmattos.hdc.scheduler.domain.cqs

import br.com.acmattos.hdc.common.context.domain.cqs.Event
import br.com.acmattos.hdc.common.context.domain.model.AuditLog
import br.com.acmattos.hdc.scheduler.domain.model.Dentist
import br.com.acmattos.hdc.scheduler.domain.model.Period
import br.com.acmattos.hdc.scheduler.domain.model.ScheduleId
import com.fasterxml.jackson.annotation.JsonTypeName
import java.time.LocalDateTime

/**
 * @author ACMattos
 * @since 02/07/2020.
 */
open class ScheduleEvent(
    open val scheduleId: ScheduleId,
    override val auditLog: AuditLog
): Event(auditLog)

/**
 * @author ACMattos
 * @since 02/07/2020.
 */
@JsonTypeName("CreateAScheduleForTheDentistEvent")
data class CreateAScheduleForTheDentistEvent(
    override val scheduleId: ScheduleId,
    val dentist: Dentist,
    val periods: List<Period>,
    val enabled: Boolean,
    val createdAt: LocalDateTime,
    override val auditLog: AuditLog
): ScheduleEvent(scheduleId, auditLog) {
    constructor(
        command: CreateAScheduleForTheDentistCommand
    ): this(
        scheduleId = command.scheduleId,
        dentist = command.dentist!!,
        periods = command.periods,
        enabled = command.enabled,
        createdAt = command.createdAt,
        auditLog = command.auditLog
    )
}
