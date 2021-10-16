package br.com.acmattos.hdc.scheduler.port.rest

import br.com.acmattos.hdc.common.context.domain.model.AuditLog
import br.com.acmattos.hdc.common.context.port.rest.EndpointLogEnum.ENDPOINT
import br.com.acmattos.hdc.common.context.port.rest.Request
import br.com.acmattos.hdc.common.tool.loggable.Loggable
import br.com.acmattos.hdc.scheduler.config.ScheduleLogEnum.SCHEDULE
import br.com.acmattos.hdc.scheduler.domain.cqs.CreateTheScheduleForTheDentistCommand
import br.com.acmattos.hdc.scheduler.domain.cqs.ScheduleCommand
import br.com.acmattos.hdc.scheduler.domain.model.Period
import br.com.acmattos.hdc.scheduler.domain.model.WeekDay
import br.com.acmattos.hdc.scheduler.domain.model.toLocalTime
import io.javalin.http.Context

/**
* @author ACMattos
* @since 26/06/2019.
*/
class ScheduleCommandController() {
    fun createTheScheduleForTheDentist(context: Context) {
        logger.debug(
            "[{} {}] - Creating the schedule for the dentist...",
            SCHEDULE.name,
            ENDPOINT.name
        )
//        context.bodyValidator<CreateTheScheduleForTheDentistRequest>()
//        .get()
//        .toType(context.fullUrl())
//        .also { command ->
//            context.status(HttpStatus.CREATED_201).json(
//                    Response.create(
//                        context.status(),
//                        "DONE!"
//                    )
//            )
//        }
        logger.info(
            "[{} {}] - Creating the schedule for the dentist... -> !DONE! <-",
            SCHEDULE.name,
            ENDPOINT.name
        )
    }

    companion object: Loggable()
}

/**
 * @author ACMattos
 * @since 28/06/2020.
 */
data class CreateTheScheduleForTheDentistRequest(// TODO Test it
    val scheduleDentistId: String,
    val periods: List<PeriodRequest>
): Request<ScheduleCommand> {
    override fun toType(who: String, what: String): ScheduleCommand =
        CreateTheScheduleForTheDentistCommand(
            scheduleDentistId,
            periods.map { it.toType() },
            AuditLog(who = who, what = what)
        )
}

/**
 * @author ACMattos
 * @since 28/06/2020.
 */
data class PeriodRequest(
    val weekDay: String,
    val from: String,
    val to: String,
    val slot: Int
): Request<Period> {
    override fun toType(
        who: String,
        what: String
    ): Period = Period(
            WeekDay.convert(weekDay),
            from.toLocalTime(),
            to.toLocalTime(),
            slot
        )
}
