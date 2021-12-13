package br.com.acmattos.hdc.scheduler.domain.cqs

import br.com.acmattos.hdc.common.context.domain.cqs.Event
import br.com.acmattos.hdc.common.context.domain.model.AuditLog
import br.com.acmattos.hdc.scheduler.domain.model.AppointmentId
import br.com.acmattos.hdc.scheduler.domain.model.AppointmentStatus
import br.com.acmattos.hdc.scheduler.domain.model.ScheduleId
import com.fasterxml.jackson.annotation.JsonTypeName
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

/**
 * @author ACMattos
 * @since 06/10/2020.
 */
open class AppointmentEvent(
    open val appointmentId: AppointmentId,
    open val scheduleId: ScheduleId,
    override val auditLog: AuditLog
): Event(auditLog)

/**
 * @author ACMattos
 * @since 03/09/2021.
 */
data class CreateAppointmentsForTheScheduleEvent(
    override val appointmentId: AppointmentId,
    override val scheduleId: ScheduleId,
    val eventsCreated: Int,
    override val auditLog: AuditLog
): AppointmentEvent(appointmentId, scheduleId, auditLog) {
    constructor(
        command: CreateAppointmentsForTheScheduleCommand,
        events: List<CreateAppointmentForTheScheduleEvent>,
    ): this(
        appointmentId = command.appointmentId,
        scheduleId = command.scheduleId,
        eventsCreated = events.size,
        auditLog = command.auditLog
    )
}

/**
 * @author ACMattos
 * @since 19/08/2021.
 */
@JsonTypeName("CreateAppointmentForTheScheduleEvent")
data class CreateAppointmentForTheScheduleEvent(
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
): AppointmentEvent(appointmentId, scheduleId, auditLog) {
    constructor(
        command: CreateAppointmentForTheScheduleCommand
    ): this(
        appointmentId = command.appointmentId,
        scheduleId = command.scheduleId,
        acronym = command.acronym,
        date = command.date,
        time = command.time,
        duration = command.duration,
        status = command.status,
        enabled = command.enabled,
        createdAt = command.createdAt,
        auditLog = command.auditLog
    )
}
