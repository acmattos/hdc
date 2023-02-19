package br.com.acmattos.hdc.odontogram.port.rest

import br.com.acmattos.hdc.common.context.config.ContextLogEnum.ENDPOINT
import br.com.acmattos.hdc.common.context.domain.cqs.CommandHandler
import br.com.acmattos.hdc.common.context.domain.model.AuditLog
import br.com.acmattos.hdc.common.context.port.rest.Request
import br.com.acmattos.hdc.common.tool.loggable.Loggable
import br.com.acmattos.hdc.common.tool.server.javalin.Response
import br.com.acmattos.hdc.common.tool.server.javalin.getRequest
import br.com.acmattos.hdc.odontogram.config.OdontogramLogEnum.ODONTOGRAM
import br.com.acmattos.hdc.odontogram.domain.cqs.OdontogramCommand
import br.com.acmattos.hdc.odontogram.domain.cqs.OdontogramCreateCommand
import br.com.acmattos.hdc.odontogram.domain.cqs.OdontogramDeleteCommand
import br.com.acmattos.hdc.odontogram.domain.cqs.OdontogramEvent
import br.com.acmattos.hdc.odontogram.domain.cqs.OdontogramUpdateCommand
import br.com.acmattos.hdc.odontogram.domain.model.Tooth
import io.javalin.http.Context
import io.javalin.plugin.openapi.annotations.HttpMethod
import io.javalin.plugin.openapi.annotations.OpenApi
import io.javalin.plugin.openapi.annotations.OpenApiContent
import io.javalin.plugin.openapi.annotations.OpenApiRequestBody
import io.javalin.plugin.openapi.annotations.OpenApiResponse
import org.eclipse.jetty.http.HttpStatus

/**
 * @author ACMattos
 * @since 27/09/2022.
 */
class OdontogramCommandController(
    private val handler: CommandHandler<OdontogramEvent>
) {
    @OpenApi(
        summary = "Create odontogram",
        operationId = "createOdontogram",
        tags = ["Odontogram"],
        requestBody = OpenApiRequestBody(
            content = [OpenApiContent(from = OdontogramCreateRequest::class)],
            required = true,
            description = "CreateOdontogramRequest Sample"
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
            "[{} {}] - Creating odontogram...",
            ODONTOGRAM.name,
            ENDPOINT.name
        )
        context.bodyValidator<OdontogramCreateRequest>()
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
            "[{} {}] - Creating odontogram... -> !DONE! <-",
            ODONTOGRAM.name,
            ENDPOINT.name
        )
    }

    @OpenApi(
        summary = "Update odontogram",
        operationId = "updateOdontogram",
        tags = ["Odontogram"],
        requestBody = OpenApiRequestBody(
            [OpenApiContent(OdontogramUpdateRequest::class)],
            true,
            "UpdateOdontogramRequest Sample"
        ),
        responses = [
            OpenApiResponse("200", [
                OpenApiContent(Response::class)
            ]),
            OpenApiResponse("400", [OpenApiContent(Response::class)])
        ],
        method = HttpMethod.POST
    )
    fun update(context: Context) {
        logger.debug(
            "[{} {}] - Update odontogram...",
            ODONTOGRAM.name,
            ENDPOINT.name
        )
        context.bodyValidator<OdontogramUpdateRequest>()
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
            ODONTOGRAM.name,
            ENDPOINT.name
        )
    }

    @OpenApi(
        summary = "Delete odontogram",
        operationId = "deleteOdontogram",
        tags = ["Odontogram"],
        requestBody = OpenApiRequestBody(
            [OpenApiContent(OdontogramDeleteRequest::class)],
            true,
            "OdontogramDeleteRequest Sample"
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
            ODONTOGRAM.name,
            ENDPOINT.name
        )
        context.getRequest(::OdontogramDeleteRequest)
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
            ODONTOGRAM.name,
            ENDPOINT.name
        )
    }

    companion object: Loggable()
}

/**
 * @author ACMattos
 * @since 27/09/2022.
 */
data class OdontogramCreateRequest(
    val upperLeft: List<Tooth>,
    val upperRight: List<Tooth>,
    val lowerLeft: List<Tooth>,
    val lowerRight: List<Tooth>,
    val upperLeftChild: List<Tooth>,
    val upperRightChild: List<Tooth>,
    val lowerLeftChild: List<Tooth>,
    val lowerRightChild: List<Tooth>,
): Request<OdontogramCommand>() {
    override fun toType(who: String, what: String): OdontogramCommand =
        OdontogramCreateCommand(
            upperLeft,
            upperRight,
            lowerLeft,
            lowerRight,
            upperLeftChild,
            upperRightChild,
            lowerLeftChild,
            lowerRightChild,
            AuditLog(who = who, what = what)
        )
}

/**
 * @author ACMattos
 * @since 03/01/2023.
 */
data class OdontogramUpdateRequest(
    val odontogramId: String,
    val upperLeft: List<Tooth>,
    val upperRight: List<Tooth>,
    val lowerLeft: List<Tooth>,
    val lowerRight: List<Tooth>,
    val upperLeftChild: List<Tooth>,
    val upperRightChild: List<Tooth>,
    val lowerLeftChild: List<Tooth>,
    val lowerRightChild: List<Tooth>,
    val enabled: Boolean,
): Request<OdontogramCommand>() {
    override fun toType(who: String, what: String): OdontogramCommand =
        OdontogramUpdateCommand(
            odontogramId,
            upperLeft,
            upperRight,
            lowerLeft,
            lowerRight,
            upperLeftChild,
            upperRightChild,
            lowerLeftChild,
            lowerRightChild,
            enabled,
            AuditLog(who = who, what = what)
        )
}

/**
 * @author ACMattos
 * @since 15/02/2023.
 */
data class OdontogramDeleteRequest(
    val context: Context
): Request<OdontogramCommand>(context) {
    override fun toType(who: String, what: String): OdontogramCommand {
        val odontogramId = context.pathParam("odontogram_id")
        return OdontogramDeleteCommand(
            odontogramId,
            AuditLog(who = who, what = what)
        )
    }
}
