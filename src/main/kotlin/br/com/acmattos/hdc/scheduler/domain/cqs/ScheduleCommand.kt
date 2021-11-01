package br.com.acmattos.hdc.scheduler.domain.cqs

import br.com.acmattos.hdc.common.context.domain.cqs.Command
import br.com.acmattos.hdc.common.context.domain.model.AuditLog
import br.com.acmattos.hdc.scheduler.domain.model.Period
import br.com.acmattos.hdc.scheduler.domain.model.ScheduleDentistId
import br.com.acmattos.hdc.scheduler.domain.model.ScheduleId
import java.time.LocalDateTime

/**
* @author ACMattos
* @since 02/07/2020.
*/
open class ScheduleCommand(
    open val scheduleId: ScheduleId,
    override val auditLog: AuditLog
): Command(auditLog)

/**
 * @author ACMattos
 * @since 16/06/2020.
 */
data class CreateAScheduleForTheDentistCommand(
    override val scheduleId: ScheduleId,
    val scheduleDentistId: ScheduleDentistId,
    val periods: List<Period>,
    val enabled: Boolean,
    val createdAt: LocalDateTime,
    override val auditLog: AuditLog
): ScheduleCommand(scheduleId, auditLog) {
    constructor(
        scheduleDentistId: String,
        periods: List<Period>,
        auditLog: AuditLog
    ): this(
        ScheduleId(),
        ScheduleDentistId(scheduleDentistId),
        periods,
        true,
        LocalDateTime.now(),
        auditLog
    )
}
