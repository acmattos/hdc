package br.com.acmattos.hdc.procedure.port.rest

import br.com.acmattos.hdc.common.context.config.ContextLogEnum.ENDPOINT
import br.com.acmattos.hdc.common.context.domain.cqs.CommandHandler
import br.com.acmattos.hdc.common.context.domain.model.AuditLog
import br.com.acmattos.hdc.common.context.port.rest.Request
import br.com.acmattos.hdc.common.tool.loggable.Loggable
import br.com.acmattos.hdc.common.tool.server.javalin.Response
import br.com.acmattos.hdc.common.tool.server.javalin.getRequest
import br.com.acmattos.hdc.procedure.config.ProcedureLogEnum.PROCEDURE
import br.com.acmattos.hdc.procedure.domain.cqs.ProcedureCommand
import br.com.acmattos.hdc.procedure.domain.cqs.ProcedureCreateCommand
import br.com.acmattos.hdc.procedure.domain.cqs.ProcedureDeleteCommand
import br.com.acmattos.hdc.procedure.domain.cqs.ProcedureEvent
import br.com.acmattos.hdc.procedure.domain.cqs.ProcedureUpdateCommand
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
        summary = "Create procedure",
        operationId = "createProcedure",
        tags = ["Procedure"],
        requestBody = OpenApiRequestBody(
            [OpenApiContent(ProcedureCreateRequest::class)],
            true,
            "ProcedureCreateRequest Sample"
        ),
        responses = [
            OpenApiResponse("201",[
                OpenApiContent(Response::class)
            ]),
            OpenApiResponse("400", [OpenApiContent(Response::class)])
        ],
        method = HttpMethod.POST
    )
    fun create(context: Context) {
        logger.debug(
            "[{} {}] - Create procedure...",
            PROCEDURE.name,
            ENDPOINT.name
        )
        context.bodyValidator<ProcedureCreateRequest>()
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
            "[{} {}] - Procedure created successfully!",
            PROCEDURE.name,
            ENDPOINT.name
        )
    }

    @OpenApi(
        summary = "Update procedure",
        operationId = "updateProcedure",
        tags = ["Procedure"],
        requestBody = OpenApiRequestBody(
            [OpenApiContent(ProcedureUpdateRequest::class)],
            true,
            "ProcedureUpdateRequest Sample"
        ),
        responses = [
            OpenApiResponse("200",[
                OpenApiContent(Response::class)
            ]),
            OpenApiResponse("400", [OpenApiContent(Response::class)])
        ],
        method = HttpMethod.POST
    )
    fun update(context: Context) {
        logger.debug(
            "[{} {}] - Update procedure...",
            PROCEDURE.name,
            ENDPOINT.name
        )
        context.bodyValidator<ProcedureUpdateRequest>()
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
            "[{} {}] - Procedure updated successfully!",
            PROCEDURE.name,
            ENDPOINT.name
        )
    }

    @OpenApi(
        summary = "Delete procedure",
        operationId = "deleteProcedure",
        tags = ["Procedure"],
        requestBody = OpenApiRequestBody(
            [OpenApiContent(ProcedureDeleteRequest::class)],
            true,
            "ProcedureDeleteRequest Sample"
        ),
        responses = [
            OpenApiResponse("200",[
                OpenApiContent(Response::class)
            ]),
            OpenApiResponse("400", [OpenApiContent(Response::class)])
        ],
        method = HttpMethod.POST
    )
    fun delete(context: Context) {
        logger.debug(
            "[{} {}] - Delete procedure...",
            PROCEDURE.name,
            ENDPOINT.name
        )
        context.getRequest(::ProcedureDeleteRequest)
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
            "[{} {}] - Procedure deleted successfully!",
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
data class ProcedureCreateRequest(
    val code: Int,
    val description: String
): Request<ProcedureCommand>() {
    override fun toType(who: String, what: String): ProcedureCommand =
        ProcedureCreateCommand(
            code,
            description,
            AuditLog(who = who, what = what)
        )
}

/**
 * @author ACMattos
 * @since 24/03/2022.
 */
data class ProcedureUpdateRequest(
    val procedureId: String,
    val code: Int,
    val description: String,
    val enabled: Boolean
): Request<ProcedureCommand>() {
    override fun toType(who: String, what: String): ProcedureCommand =
        ProcedureUpdateCommand(
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
data class ProcedureDeleteRequest(
    val context: Context
): Request<ProcedureCommand>(context) {
    override fun toType(who: String, what: String): ProcedureCommand {
        val procedureIds = context.pathParam("procedure_ids")
            .split(";")// TODO ASSERT VALUES
        return ProcedureDeleteCommand(
            procedureIds[0],
            procedureIds[1],
            AuditLog(who = who, what = what)
        )
    }
}
