package br.com.acmattos.hdc.scheduler.port.rest

import br.com.acmattos.hdc.common.context.domain.cqs.CommandHandler
import br.com.acmattos.hdc.common.context.domain.model.AuditLog
import br.com.acmattos.hdc.common.context.port.rest.EndpointLogEnum.ENDPOINT
import br.com.acmattos.hdc.common.context.port.rest.Request
import br.com.acmattos.hdc.common.tool.loggable.Loggable
import br.com.acmattos.hdc.common.tool.server.javalin.Response
import br.com.acmattos.hdc.scheduler.config.ScheduleLogEnum.SCHEDULE
import br.com.acmattos.hdc.scheduler.domain.cqs.CreateAScheduleForTheDentistCommand
import br.com.acmattos.hdc.scheduler.domain.cqs.ScheduleCommand
import br.com.acmattos.hdc.scheduler.domain.cqs.ScheduleEvent
import br.com.acmattos.hdc.scheduler.domain.model.Period
import br.com.acmattos.hdc.scheduler.domain.model.WeekDay
import br.com.acmattos.hdc.scheduler.domain.model.toLocalTime
import io.javalin.http.Context
import io.javalin.plugin.openapi.annotations.HttpMethod
import io.javalin.plugin.openapi.annotations.OpenApi
import io.javalin.plugin.openapi.annotations.OpenApiContent
import io.javalin.plugin.openapi.annotations.OpenApiRequestBody
import io.javalin.plugin.openapi.annotations.OpenApiResponse
import org.eclipse.jetty.http.HttpStatus

/**
* @author ACMattos
* @since 26/06/2019.
*/
class ScheduleCommandController(
    private val handler: CommandHandler<ScheduleEvent>
) {
    @OpenApi(
        summary = "Create a schedule for the defined dentist",
        operationId = "createAScheduleForTheDentist",
        tags = ["Schedule"],
        requestBody = OpenApiRequestBody(
            [OpenApiContent(CreateAScheduleForTheDentistRequest::class)],
            true,
            "CreateAScheduleForTheDentistRequest Sample"
        ),
        responses = [
            OpenApiResponse("201",[
                OpenApiContent(Response::class)
            ]),
            OpenApiResponse("400", [OpenApiContent(Response::class)])
        ],
        method = HttpMethod.POST
    )
    fun createAScheduleForTheDentist(context: Context) {
        logger.debug(
            "[{} {}] - Creating a schedule for the dentist...",
            SCHEDULE.name,
            ENDPOINT.name
        )
        context.bodyValidator<CreateAScheduleForTheDentistRequest>()
        .get()
        .toType(what = context.fullUrl())
        .also { command ->
            context.status(HttpStatus.CREATED_201).json(
                    Response.create(
                        context.status(),
                        handler.handle(command)
                    )
            )
        }
        logger.info(
            "[{} {}] - Creating a schedule for the dentist... -> !DONE! <-",
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
data class CreateAScheduleForTheDentistRequest(
    val scheduleDentistId: String,
    val periods: List<PeriodRequest>
): Request<ScheduleCommand> {
    override fun toType(who: String, what: String): ScheduleCommand =
        CreateAScheduleForTheDentistCommand(
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
