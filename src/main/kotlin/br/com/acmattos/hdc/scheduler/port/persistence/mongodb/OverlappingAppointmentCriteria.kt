package br.com.acmattos.hdc.scheduler.port.persistence.mongodb

import br.com.acmattos.hdc.common.context.port.persistence.mongodb.MdbCriteria
import br.com.acmattos.hdc.scheduler.domain.model.ScheduleId
import com.mongodb.client.model.Filters
import java.time.LocalDate
import java.time.LocalTime
import org.bson.conversions.Bson

/**
 * @author ACMattos
 * @since 21/11/2021.
 */
@Deprecated("Change it to Filter")
data class OverlappingAppointmentCriteria(
    val scheduleId: ScheduleId,
    val date: LocalDate,
    val time: LocalTime
): MdbCriteria {
    override val filters: Bson
        get() = buildFilters()

    private fun buildFilters() =
        Filters.and(
            Filters.eq("event.schedule_id.id", scheduleId.id), // TODO TRACK THIS FIELD
            Filters.eq("event.date", date),// TODO TRACK THIS FIELD
            Filters.eq("event.time", time),// TODO TRACK THIS FIELD
        )
}
