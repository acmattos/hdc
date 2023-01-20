package br.com.acmattos.hdc.common.context.domain.cqs

import br.com.acmattos.hdc.common.context.domain.model.AuditLog
import br.com.acmattos.hdc.common.context.domain.model.Entity
import br.com.acmattos.hdc.common.context.domain.model.Id
import br.com.acmattos.hdc.person.domain.cqs.CreatePatientEvent
import br.com.acmattos.hdc.person.domain.cqs.UpdatePatientEvent
import br.com.acmattos.hdc.procedure.domain.cqs.ProcedureCreateEvent
import br.com.acmattos.hdc.procedure.domain.cqs.ProcedureDeleteEvent
import br.com.acmattos.hdc.procedure.domain.cqs.ProcedureUpdateEvent
import br.com.acmattos.hdc.procedure.domain.cqs.ProcedureUpsertEvent
import br.com.acmattos.hdc.scheduler.domain.cqs.CreateAScheduleForTheDentistEvent
import br.com.acmattos.hdc.scheduler.domain.cqs.CreateAppointmentForTheScheduleEvent
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import java.time.LocalDateTime

/**
 * @author ACMattos
 * @since 06/01/2023.
 */
interface Event

/**
 * @author ACMattos
 * @since 06/01/2023.
 */
interface CreateEvent: Event {
    val createdAt: LocalDateTime
}

/**
 * @author ACMattos
 * @since 06/01/2023.
 */
interface UpsertEvent: Event {
    val updatedAt: LocalDateTime
    val deletedAt: LocalDateTime?
}

/**
 * @author ACMattos
 * @since 06/01/2023.
 */
interface UpdateEvent: Event {
    val updatedAt: LocalDateTime?
}

/**
 * @author ACMattos
 * @since 06/01/2023.
 */
interface DeleteEvent: Event {
    val deletedAt: LocalDateTime?
}

/**
 * @author ACMattos
 * @since 29/06/2020.
 */
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "event_type"
)
@JsonSubTypes(
    JsonSubTypes.Type(value = CreateAScheduleForTheDentistEvent::class, name = "CreateAScheduleForTheDentistEvent"),
    JsonSubTypes.Type(value = CreateAppointmentForTheScheduleEvent::class, name = "CreateAppointmentForTheScheduleEvent"),
    JsonSubTypes.Type(value = ProcedureCreateEvent::class, name = "ProcedureCreateEvent"),
    JsonSubTypes.Type(value = ProcedureUpdateEvent::class, name = "ProcedureUpdateEvent"),
    JsonSubTypes.Type(value = ProcedureUpsertEvent::class, name = "ProcedureUpsertEvent"),
    JsonSubTypes.Type(value = ProcedureDeleteEvent::class, name = "ProcedureDeleteEvent"),
    JsonSubTypes.Type(value = CreatePatientEvent::class, name = "CreatePatientEvent"),
    JsonSubTypes.Type(value = UpdatePatientEvent::class, name = "UpdatePatientEvent"),
//    JsonSubTypes.Type(value = DeletePatientEvent::class, name = "DeletePatientEvent"),
)
open class EntityEvent(
    open val eventId: EventId,
    open val auditLog: AuditLog
): Event, Entity {
    constructor(
        auditLog: AuditLog
    ): this(
        EventId(),
        auditLog
    )
}

/**
 * @author ACMattos
 * @since 01/07/2020.
 */
class EventId() : Id()
