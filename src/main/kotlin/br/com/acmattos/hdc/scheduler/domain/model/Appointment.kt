package br.com.acmattos.hdc.scheduler.domain.model

import br.com.acmattos.hdc.common.context.domain.cqs.EntityEvent
import br.com.acmattos.hdc.common.context.domain.model.AppliableEntity
import br.com.acmattos.hdc.common.context.domain.model.Id
import br.com.acmattos.hdc.common.tool.assertion.Assertion
import br.com.acmattos.hdc.common.tool.enum.assertThatTerm
import br.com.acmattos.hdc.scheduler.config.MessageTrackerIdEnum.APPOINTMENT_STATUS_CONVERT_FAILED
import br.com.acmattos.hdc.scheduler.config.MessageTrackerIdEnum.ENTITY_APPOINTMENT_ENABLED_MUST_BE_TRUE
import br.com.acmattos.hdc.scheduler.config.MessageTrackerIdEnum.ENTITY_APPOINTMENT_STATUS_MUST_BE_FREED
import br.com.acmattos.hdc.scheduler.config.ScheduleLogEnum.APPOINTMENT
import br.com.acmattos.hdc.scheduler.domain.cqs.AppointmentEvent
import br.com.acmattos.hdc.scheduler.domain.cqs.CreateAppointmentForTheScheduleEvent
import br.com.acmattos.hdc.scheduler.domain.model.AppointmentStatus.FREED
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

/**
 * @author ACMattos
 * @since 09/07/2019.
 */
data class Appointment(
    private var appointmentIdData: AppointmentId? = null,
    private var scheduleIdData: ScheduleId? = null,
    private var patientData: Patient? = null,
    private var dateData: LocalDate? = null,
    private var timeData: LocalTime? = null,
    private var durationData: Long? = null,
    private var statusData: AppointmentStatus = FREED,
    private var enabledData: Boolean = true,
    override var createdAtData: LocalDateTime = LocalDateTime.now(),
    override var updatedAtData: LocalDateTime? = null,
    override var deletedAtData: LocalDateTime? = null,
): AppliableEntity {
    val appointmentId get() = appointmentIdData!!
    val scheduleId get() = scheduleIdData!!
    val patient get() = patientData
    val date get() = dateData!!
    val time get() = timeData!!
    val duration get() = durationData!!
    val status get() = statusData
    val enabled get() = enabledData

    override fun apply(event: EntityEvent): Appointment {
        when(event) {
            is CreateAppointmentForTheScheduleEvent -> apply(event)
            else -> apply(event as CreateAppointmentForTheScheduleEvent)
        }
        return this
    }

    private fun apply(event: CreateAppointmentForTheScheduleEvent) {
        appointmentIdData = event.appointmentId
        scheduleIdData = event.scheduleId
        dateData = event.date
        timeData = event.time
        durationData = event.duration
        statusData = event.status
        enabledData = event.enabled
        createdAtData = event.createdAt
        assertCreateAppointmentForTheScheduleEvent()
    }

    private fun assertCreateAppointmentForTheScheduleEvent() {
        Assertion.assert(
            "Status must be $FREED!",
            APPOINTMENT.name,
            ENTITY_APPOINTMENT_STATUS_MUST_BE_FREED
        ) {
            FREED == statusData
        }
        Assertion.assert(
            "Enabled must be TRUE!",
            APPOINTMENT.name,
            ENTITY_APPOINTMENT_ENABLED_MUST_BE_TRUE
        ) {
            enabledData
        }
    }

    companion object {
        fun apply(event: AppointmentEvent) = Appointment().apply(event)
        fun apply(events: List<AppointmentEvent>) = Appointment().apply(events)
    }
}

/**
 * @author ACMattos
 * @since 17/09/2020.
 */
class AppointmentId: Id {
    constructor(id: String): super(id)
    constructor(): super()
}

/**
 * @author ACMattos
 * @since 10/11/2021.
 */
enum class AppointmentStatus {
    FREED, SCHEDULED, CONFIRMED, RELEASED, MISSED;
    companion object {
        fun convert(term: String): WeekDay = assertThatTerm(
            term,
            "[$term] does not correspond to a valid AppointmentStatus!",
            APPOINTMENT.name,
            APPOINTMENT_STATUS_CONVERT_FAILED
        )
    }
}
