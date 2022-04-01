//package br.com.acmattos.hdc.scheduler.port.persistence.mongodb
//
//import br.com.acmattos.hdc.common.context.domain.cqs.AndFilter
//import br.com.acmattos.hdc.common.context.domain.cqs.EqFilter
//import br.com.acmattos.hdc.common.context.port.persistence.mongodb.MdbCriteria
//import br.com.acmattos.hdc.scheduler.domain.model.ScheduleId
//import com.mongodb.client.model.Filters
//import java.time.LocalDate
//import java.time.LocalTime
//import org.bson.conversions.Bson
//
///**
// * @author ACMattos
// * @since 21/11/2021.
// */
//@Deprecated("Change it to Filter")
//data class OverlappingAppointmentCriteria(// TODO VERIFY AND REMOVE
//    val scheduleId: ScheduleId,
//    val date: LocalDate,
//    val time: LocalTime
//): MdbCriteria {
//    override val filters: Bson
//        get() = buildFilters()
//
//    private fun buildFilters() =
//        AndFilter<String, Any>(
//            listOf(
//                EqFilter<String, String>("event.schedule_id.id", scheduleId.id), // TODO TRACK THIS FIELD
//                EqFilter<String, LocalDate>("event.date", date), // TODO TRACK THIS FIELD
//                EqFilter<String, LocalTime>("event.time", time) // TODO TRACK THIS FIELD
//            )
//        )
////        Filters.and(
////            Filters.eq(),
////            Filters.eq(),// TODO TRACK THIS FIELD
////            Filters.eq(),// TODO TRACK THIS FIELD
////        )
//}
