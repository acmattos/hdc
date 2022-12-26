package br.com.acmattos.hdc.odontogram.port.rest

import br.com.acmattos.hdc.common.context.domain.cqs.QueryHandler
import br.com.acmattos.hdc.common.context.domain.model.AuditLog
import br.com.acmattos.hdc.common.context.port.rest.EndpointLogEnum.ENDPOINT
import br.com.acmattos.hdc.common.context.port.rest.Request
import br.com.acmattos.hdc.common.tool.loggable.Loggable
import br.com.acmattos.hdc.common.tool.server.javalin.Response
import br.com.acmattos.hdc.common.tool.server.javalin.getRequest
import br.com.acmattos.hdc.odontogram.config.OdontogramLogEnum.ODONTOGRAM
import br.com.acmattos.hdc.odontogram.domain.cqs.GetAnOdontogramQuery
import br.com.acmattos.hdc.odontogram.domain.cqs.OdontogramQuery
import br.com.acmattos.hdc.odontogram.domain.model.Odontogram
import io.javalin.http.Context
import io.javalin.plugin.openapi.annotations.HttpMethod
import io.javalin.plugin.openapi.annotations.OpenApi
import io.javalin.plugin.openapi.annotations.OpenApiContent
import io.javalin.plugin.openapi.annotations.OpenApiResponse
import org.eclipse.jetty.http.HttpStatus

/**
 * @author ACMattos
 * @since 18/10/2022.
 */
class OdontogramQueryController(
    private val handler: QueryHandler<Odontogram>
) {
    @OpenApi(
        summary = "Get a basic odontogram.",
        operationId = "getABasicOdontogram",
        tags = ["Odontogram"],
        responses = [
            OpenApiResponse("200", [OpenApiContent(Response::class)]),
        ],
        method = HttpMethod.GET
    )
    fun getABasicOdontogram(context: Context) {
        logger.debug(
            "[{} {}] - Getting the odontogram...",
            ODONTOGRAM.name,
            ENDPOINT.name
        )
        context.getRequest(::GetAnOdontogramRequest)
        .toType(what = context.fullUrl())
        .also { query ->
            context.status(HttpStatus.OK_200).json(
                Response.create(
                    context.status(),
                    handler.handle(query)
                )
            )
        }
        logger.info(
            "[{} {}] - Getting the odontogram: -> !DONE! <-",
            ODONTOGRAM.name,
            ENDPOINT.name
        )
    }

    companion object: Loggable()
}

/**
 * @author ACMattos
 * @since 18/10/2022.
 */
class GetAnOdontogramRequest(
    val context: Context
): Request<OdontogramQuery>(context) {
    override fun toType(who: String, what: String): OdontogramQuery {
        return GetAnOdontogramQuery(AuditLog(who, what))
    }
}
