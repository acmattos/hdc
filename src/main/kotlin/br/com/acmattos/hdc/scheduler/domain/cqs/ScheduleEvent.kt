package br.com.acmattos.hdc.scheduler.domain.cqs

import br.com.acmattos.hdc.common.context.domain.cqs.Event
import br.com.acmattos.hdc.common.context.domain.model.AuditLog
import br.com.acmattos.hdc.scheduler.domain.model.Period
import br.com.acmattos.hdc.scheduler.domain.model.ScheduleDentistId
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
    val scheduleDentistId: ScheduleDentistId,
    val periods: List<Period>,
    val enabled: Boolean,
    val createdAt: LocalDateTime,
    override val auditLog: AuditLog
): ScheduleEvent(scheduleId, auditLog) {
    constructor(
        command: CreateAScheduleForTheDentistCommand
    ): this(
        scheduleId = command.scheduleId,
        scheduleDentistId = command.scheduleDentistId,
        periods = command.periods,
        enabled = command.enabled,
        createdAt = command.createdAt,
        auditLog = command.auditLog
    )
}















//
///**
// * @author ACMattos
// * @since 10/07/2020.
// */
//@JsonTypeName("ModifySchedulePeriodsEvent")
//data class ModifySchedulePeriodsEvent(
//    override val tenantId: TenantId,
//    override val dentistId: DentistId,
//    override val scheduleId: ScheduleId,
//    @JsonIgnore override val events: MutableList<ScheduleEvent> = mutableListOf<ScheduleEvent>(),
//    val periods: List<Period>,
//    val updatedAt: LocalDateTime,
//    override val auditEntry: AuditEntry
//): ScheduleEvent(tenantId, dentistId, scheduleId, events, auditEntry) {
//    constructor(
//        command: ModifySchedulePeriodsCommand
//    ): this(
//        tenantId = command.tenantId,
//        dentistId = command.dentistId,
//        scheduleId = command.scheduleId,
//        periods = command.periods,
//        updatedAt = command.updatedAt,
//        auditEntry = command.auditEntry
//    )
//}
//
///**
// * @author ACMattos
// * @since 01/11/2020.
// */
//@JsonTypeName("ModifyScheduleEnabledStatusEvent")
//data class ModifyScheduleEnabledStatusEvent(
//    override val tenantId: TenantId,
//    override val dentistId: DentistId,
//    override val scheduleId: ScheduleId,
//    @JsonIgnore override val events: MutableList<ScheduleEvent> = mutableListOf<ScheduleEvent>(),
//    val enabled: Boolean,
//    val updatedAt: LocalDateTime,
//    override val auditEntry: AuditEntry
//): ScheduleEvent(tenantId, dentistId, scheduleId, events, auditEntry) {
//    constructor(
//        command: ModifyScheduleEnabledStatusCommand
//    ): this(
//        tenantId = command.tenantId,
//        dentistId = command.dentistId,
//        scheduleId = command.scheduleId,
//        enabled = command.enabled,
//        updatedAt = command.updatedAt,
//        auditEntry = command.auditEntry
//    )
//}