package br.com.acmattos.hdc.common.context.domain.cqs

import br.com.acmattos.hdc.common.context.domain.model.AuditLog
import br.com.acmattos.hdc.common.context.domain.model.Entity
import br.com.acmattos.hdc.common.context.domain.model.Id
import br.com.acmattos.hdc.procedure.domain.cqs.CreateDentalProcedureEvent
import br.com.acmattos.hdc.procedure.domain.cqs.DeleteDentalProcedureEvent
import br.com.acmattos.hdc.procedure.domain.cqs.UpdateDentalProcedureEvent
import br.com.acmattos.hdc.scheduler.domain.cqs.CreateAScheduleForTheDentistEvent
import br.com.acmattos.hdc.scheduler.domain.cqs.CreateAppointmentForTheScheduleEvent
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

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
    JsonSubTypes.Type(value = CreateDentalProcedureEvent::class, name = "CreateDentalProcedureEvent"),
    JsonSubTypes.Type(value = UpdateDentalProcedureEvent::class, name = "UpdateDentalProcedureEvent"),
    JsonSubTypes.Type(value = DeleteDentalProcedureEvent::class, name = "DeleteDentalProcedureEvent"),
)
open class Event(
    open val eventId: EventId,
    open val auditLog: AuditLog
): Entity {
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
class EventId: Id {
    constructor(id: String): super(id)
    constructor(): super()
}
