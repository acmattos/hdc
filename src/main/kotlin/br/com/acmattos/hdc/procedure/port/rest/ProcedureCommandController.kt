package br.com.acmattos.hdc.procedure.port.rest

import br.com.acmattos.hdc.common.context.domain.cqs.CommandHandler
import br.com.acmattos.hdc.common.context.domain.model.AuditLog
import br.com.acmattos.hdc.common.context.port.rest.EndpointLogEnum.ENDPOINT
import br.com.acmattos.hdc.common.context.port.rest.Request
import br.com.acmattos.hdc.common.tool.loggable.Loggable
import br.com.acmattos.hdc.common.tool.server.javalin.Response
import br.com.acmattos.hdc.common.tool.server.javalin.getRequest
import br.com.acmattos.hdc.procedure.config.ProcedureLogEnum.PROCEDURE
import br.com.acmattos.hdc.procedure.domain.cqs.CreateDentalProcedureCommand
import br.com.acmattos.hdc.procedure.domain.cqs.DeleteDentalProcedureCommand
import br.com.acmattos.hdc.procedure.domain.cqs.ProcedureCommand
import br.com.acmattos.hdc.procedure.domain.cqs.ProcedureEvent
import br.com.acmattos.hdc.procedure.domain.cqs.UpdateDentalProcedureCommand
import io.javalin.http.Context
import io.javalin.plugin.openapi.annotations.HttpMethod
import io.javalin.plugin.openapi.annotations.OpenApi
import io.javalin.plugin.openapi.annotations.OpenApiContent
import io.javalin.plugin.openapi.annotations.OpenApiRequestBody
import io.javalin.plugin.openapi.annotations.OpenApiResponse
import org.eclipse.jetty.http.HttpStatus

/**
 * @author ACMattos
 * @since 19/03/2022.
 */
class ProcedureCommandController(
    private val handler: CommandHandler<ProcedureEvent>
) {
    @OpenApi(
        summary = "Create dental procedure",
        operationId = "createDentalProcedure",
        tags = ["Procedure"],
        requestBody = OpenApiRequestBody(
            [OpenApiContent(CreateDentalProcedureRequest::class)],
            true,
            "CreateDentalProcedureRequest Sample"
        ),
        responses = [
            OpenApiResponse("201",[
                OpenApiContent(Response::class)
            ]),
            OpenApiResponse("400", [OpenApiContent(Response::class)])
        ],
        method = HttpMethod.POST
    )
    fun createDentalProcedure(context: Context) { // TODO TEST
        logger.debug(
            "[{} {}] - Create dental procedure...",
            PROCEDURE.name,
            ENDPOINT.name
        )
        context.bodyValidator<CreateDentalProcedureRequest>()
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
            "[{} {}] - Dental procedure created successfully!",
            PROCEDURE.name,
            ENDPOINT.name
        )
    }

    @OpenApi(
        summary = "Update dental procedure",
        operationId = "updateDentalProcedure",
        tags = ["Procedure"],
        requestBody = OpenApiRequestBody(
            [OpenApiContent(UpdateDentalProcedureRequest::class)],
            true,
            "UpdateDentalProcedureRequest Sample"
        ),
        responses = [
            OpenApiResponse("200",[
                OpenApiContent(Response::class)
            ]),
            OpenApiResponse("400", [OpenApiContent(Response::class)])
        ],
        method = HttpMethod.POST
    )
    fun updateDentalProcedure(context: Context) { // TODO TEST
        logger.debug(
            "[{} {}] - Update dental procedure...",
            PROCEDURE.name,
            ENDPOINT.name
        )
        context.bodyValidator<UpdateDentalProcedureRequest>()
            .get()
            .toType(what = context.fullUrl())
            .also { command ->
                context.status(HttpStatus.OK_200).json(
                    Response.create(
                        context.status(),
                        handler.handle(command)
                    )
                )
            }
        logger.info(
            "[{} {}] - Dental procedure updated successfully!",
            PROCEDURE.name,
            ENDPOINT.name
        )
    }

    @OpenApi(
        summary = "Delete dental procedure",
        operationId = "deleteDentalProcedure",
        tags = ["Procedure"],
        requestBody = OpenApiRequestBody(
            [OpenApiContent(DeleteDentalProcedureRequest::class)],
            true,
            "DeleteDentalProcedureRequest Sample"
        ),
        responses = [
            OpenApiResponse("200",[
                OpenApiContent(Response::class)
            ]),
            OpenApiResponse("400", [OpenApiContent(Response::class)])
        ],
        method = HttpMethod.POST
    )
    fun deleteDentalProcedure(context: Context) { // TODO TEST
        logger.debug(
            "[{} {}] - Delete dental procedure...",
            PROCEDURE.name,
            ENDPOINT.name
        )
        context.getRequest(::DeleteDentalProcedureRequest)
            .toType(what = context.fullUrl())
            .also { command ->
                context.status(HttpStatus.OK_200).json(
                    Response.create(
                        context.status(),
                        handler.handle(command)
                    )
                )
            }
        logger.info(
            "[{} {}] - Dental procedure deleted successfully!",
            PROCEDURE.name,
            ENDPOINT.name
        )
    }

    companion object: Loggable()
}

/**
 * @author ACMattos
 * @since 19/03/2022.
 */
data class CreateDentalProcedureRequest(
    val code: Int,
    val description: String
): Request<ProcedureCommand>() {
    override fun toType(who: String, what: String): ProcedureCommand =
        CreateDentalProcedureCommand(
            code,
            description,
            AuditLog(who = who, what = what)
        )
}

/**
 * @author ACMattos
 * @since 24/03/2022.
 */
data class UpdateDentalProcedureRequest(
    val procedureId: String,
    val code: Int,
    val description: String,
    val enabled: Boolean
): Request<ProcedureCommand>() {
    override fun toType(who: String, what: String): ProcedureCommand =
        UpdateDentalProcedureCommand(
            procedureId,
            code,
            description,
            enabled,
            AuditLog(who = who, what = what)
        )
}

/**
 * @author ACMattos
 * @since 26/03/2022.
 */
data class DeleteDentalProcedureRequest(
    val context: Context
): Request<ProcedureCommand>(context) {
    override fun toType(who: String, what: String): ProcedureCommand {
        val procedureId = context.pathParam("procedure_id")
//        Assertion.assert(
//            "dentist_id can't be null or empty",
//            PROCEDURE.name,
//            DENTIST_ID_INVALID.code
//        ) {
//            dentistId.isNotBlank()
//        }

        return DeleteDentalProcedureCommand(
            procedureId,
            AuditLog(who = who, what = what)
        )
    }
}