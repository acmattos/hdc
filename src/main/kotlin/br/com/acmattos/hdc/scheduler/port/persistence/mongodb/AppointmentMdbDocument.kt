package br.com.acmattos.hdc.scheduler.port.persistence.mongodb

import br.com.acmattos.hdc.common.context.port.persistence.mongodb.MdbDocument
import br.com.acmattos.hdc.scheduler.domain.model.Appointment
import br.com.acmattos.hdc.scheduler.domain.model.AppointmentId
import br.com.acmattos.hdc.scheduler.domain.model.AppointmentStatus
import br.com.acmattos.hdc.scheduler.domain.model.ScheduleId
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

/**
 * @author ACMattos
 * @since 03/08/2021.
 */
data class AppointmentMdbDocument(
    val appointmentId: String,
    val scheduleId: String,
    val patient: PatientMdbDocument?,
    val date: LocalDate,
    val time: LocalTime,
    val duration: Long,
    val status: AppointmentStatus,
    val enabled: Boolean,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime?,
): MdbDocument() {
    constructor(
        appointment: Appointment
    ): this(
        appointmentId = appointment.appointmentId.id,
        scheduleId = appointment.scheduleId.id,
        patient = if (appointment.patient == null) {
            null
        } else {
            PatientMdbDocument(appointment.patient!!)
        },
        date = appointment.date,
        time = appointment.time,
        duration = appointment.duration,
        status = appointment.status,
        enabled = appointment.enabled,
        createdAt = appointment.createdAt,
        updatedAt = appointment.updatedAt,
    )

    override fun toType(): Appointment =
        Appointment(
            appointmentIdData = AppointmentId(appointmentId),
            scheduleIdData = ScheduleId(scheduleId),
            patientData = patient?.toType(),
            dateData = date,
            timeData = time,
            durationData = duration,
            statusData = status,
            enabledData = enabled,
            createdAtData = createdAt,
            updatedAtData = updatedAt,
        )
}