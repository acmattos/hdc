package br.com.acmattos.hdc.scheduler.domain.cqs

import br.com.acmattos.hdc.common.context.domain.cqs.AuditableCommand
import br.com.acmattos.hdc.common.context.domain.model.AuditLog
import br.com.acmattos.hdc.scheduler.domain.model.AppointmentId
import br.com.acmattos.hdc.scheduler.domain.model.AppointmentStatus
import br.com.acmattos.hdc.scheduler.domain.model.AppointmentStatus.FREED
import br.com.acmattos.hdc.scheduler.domain.model.ScheduleId
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

/**
 * @author ACMattos
 * @since 17/09/2020.
 */
open class AppointmentCommand(
    open val appointmentId: AppointmentId,
    open val scheduleId: ScheduleId,
    override val auditLog: AuditLog
): AuditableCommand(auditLog)

/**
 * @author ACMattos
 * @since 19/08/2021.
 */
data class CreateAppointmentsForTheScheduleCommand(
    override val appointmentId: AppointmentId,
    override val scheduleId: ScheduleId,
    val from: LocalDate,
    val to: LocalDate,
    override val auditLog: AuditLog
): AppointmentCommand(appointmentId, scheduleId, auditLog) {
    constructor(
        scheduleId: String,
        fromDate: LocalDate,
        toDate: LocalDate,
        auditLog: AuditLog
    ): this(
        AppointmentId(),
        ScheduleId(scheduleId),
        fromDate,
        toDate,
        auditLog
    )
}

/**
 * @author ACMattos
 * @since 19/08/2021.
 */
data class CreateAppointmentForTheScheduleCommand(
    override val appointmentId: AppointmentId,
    override val scheduleId: ScheduleId,
    val acronym: String,
    val date: LocalDate,
    val time: LocalTime,
    val duration: Long,
    val status: AppointmentStatus,
    val enabled: Boolean,
    val createdAt: LocalDateTime,
    override val auditLog: AuditLog
): AppointmentCommand(appointmentId, scheduleId, auditLog) {
    constructor(
        scheduleId: ScheduleId,
        acronym: String,
        date: LocalDate,
        time: LocalTime,
        duration: Long,
        auditLog: AuditLog
    ): this(
        AppointmentId(),
        scheduleId,
        acronym,
        date,
        time,
        duration,
        FREED,
        true,
        LocalDateTime.now(),
        auditLog
    )
}
