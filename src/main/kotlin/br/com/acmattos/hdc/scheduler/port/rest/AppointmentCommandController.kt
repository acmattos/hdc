package br.com.acmattos.hdc.scheduler.port.rest

import br.com.acmattos.hdc.common.context.domain.cqs.CommandHandler
import br.com.acmattos.hdc.common.context.domain.model.AuditLog
import br.com.acmattos.hdc.common.context.port.rest.EndpointLogEnum.ENDPOINT
import br.com.acmattos.hdc.common.context.port.rest.Request
import br.com.acmattos.hdc.common.tool.loggable.Loggable
import br.com.acmattos.hdc.common.tool.server.javalin.Response
import br.com.acmattos.hdc.common.tool.server.javalin.getRequest
import br.com.acmattos.hdc.scheduler.config.ScheduleLogEnum.APPOINTMENT
import br.com.acmattos.hdc.scheduler.domain.cqs.AppointmentCommand
import br.com.acmattos.hdc.scheduler.domain.cqs.AppointmentEvent
import br.com.acmattos.hdc.scheduler.domain.cqs.CreateAppointmentsForTheScheduleCommand
import br.com.acmattos.hdc.scheduler.domain.model.toLocalDate
import io.javalin.http.Context
import io.javalin.plugin.openapi.annotations.HttpMethod
import io.javalin.plugin.openapi.annotations.OpenApi
import io.javalin.plugin.openapi.annotations.OpenApiContent
import io.javalin.plugin.openapi.annotations.OpenApiRequestBody
import io.javalin.plugin.openapi.annotations.OpenApiResponse
import org.eclipse.jetty.http.HttpStatus

/**
 * @author ACMattos
 * @since 03/08/2021.
 */
class AppointmentCommandController(
    private val handler: CommandHandler<AppointmentEvent>
) {
    @OpenApi(
        summary = "Create appointments for a given period of dates to a certain dentist's schedule",
        operationId = "createAppointmentsForScheduleAndDentist",
        tags = ["Appointment"],
        requestBody = OpenApiRequestBody(
            [OpenApiContent(CreateAppointmentsForTheScheduleRequest::class)],
            true,
            "CreateAppointmentsForTheDentistRequest Sample"
        ),
        responses = [
            OpenApiResponse("201",[
                OpenApiContent(Response::class)
            ]),
            OpenApiResponse("400", [OpenApiContent(Response::class)])
        ],
        method = HttpMethod.POST
    )
    fun createAppointmentsForTheSchedule(context: Context) {
        logger.debug(
            "[{} {}] - Create appointments for date and period...",
            APPOINTMENT.name,
            ENDPOINT.name
        )
        context.getRequest(::CreateAppointmentsForTheScheduleRequestBuilder)
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
            "[{} {}] - Appointments created successfully!",
            APPOINTMENT.name,
            ENDPOINT.name
        )
    }

    companion object: Loggable()
}

/**
 * @author ACMattos
 * @since 11/11/2021.
 */
data class CreateAppointmentsForTheScheduleRequestBuilder(
    val context: Context
): Request<AppointmentCommand>(context) {
    override fun toType(who: String, what: String): AppointmentCommand {
        val request = context.bodyValidator<CreateAppointmentsForTheScheduleRequest>().get()
        return CreateAppointmentsForTheScheduleCommand(
            context.pathParam("schedule_id"),
            request.from.toLocalDate(),
            request.to.toLocalDate(),
            AuditLog(who = who, what = what)
        )
    }
}

/**
 * @author ACMattos
 * @since 19/08/2021.
 */
data class CreateAppointmentsForTheScheduleRequest(
    val from: String,
    val to: String,
): Request<CreateAppointmentsForTheScheduleRequest>() {
    override fun toType(
        who: String,
        what: String
    ): CreateAppointmentsForTheScheduleRequest = this
}
