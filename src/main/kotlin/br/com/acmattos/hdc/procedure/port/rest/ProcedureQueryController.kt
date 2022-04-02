package br.com.acmattos.hdc.procedure.port.rest

import br.com.acmattos.hdc.common.context.domain.cqs.QueryHandler
import br.com.acmattos.hdc.common.context.domain.model.AuditLog
import br.com.acmattos.hdc.common.context.port.rest.EndpointLogEnum.ENDPOINT
import br.com.acmattos.hdc.common.context.port.rest.Request
import br.com.acmattos.hdc.common.tool.loggable.Loggable
import br.com.acmattos.hdc.common.tool.server.javalin.Response
import br.com.acmattos.hdc.common.tool.server.javalin.getRequest
import br.com.acmattos.hdc.person.config.PersonLogEnum.PERSON
import br.com.acmattos.hdc.procedure.config.ProcedureLogEnum.PROCEDURE
import br.com.acmattos.hdc.procedure.domain.cqs.FindAllProceduresQuery
import br.com.acmattos.hdc.procedure.domain.cqs.FindTheProcedureQuery
import br.com.acmattos.hdc.procedure.domain.cqs.ProcedureQuery
import br.com.acmattos.hdc.procedure.domain.model.Procedure
import br.com.acmattos.hdc.procedure.domain.model.ProcedureId
import io.javalin.http.Context
import io.javalin.plugin.openapi.annotations.HttpMethod
import io.javalin.plugin.openapi.annotations.OpenApi
import io.javalin.plugin.openapi.annotations.OpenApiContent
import io.javalin.plugin.openapi.annotations.OpenApiResponse
import org.eclipse.jetty.http.HttpStatus

/**
 * @author ACMattos
 * @since 20/03/2022.
 */
class ProcedureQueryController(
    private val handler: QueryHandler<Procedure>
) {
    @OpenApi(
        summary = "Find all procedures.",
        operationId = "findAllProcedures",
        tags = ["Procedure"],
        responses = [
            OpenApiResponse("200", [OpenApiContent(Response::class)]),
            OpenApiResponse("404", [OpenApiContent(Response::class)])
        ],
        method = HttpMethod.GET
    )
    fun findAllProcedures(context: Context) {
        logger.debug(
            "[{} {}] - Finding all procedures...",
            PROCEDURE.name,
            ENDPOINT.name
        )
        context.getRequest(::FindAllProceduresRequest)
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
            "[{} {}] - Finding all procedures: -> !DONE! <-",
            PROCEDURE.name,
            ENDPOINT.name
        )
    }

    @OpenApi(
        summary = "Find the procedure identified by the given ID.",
        operationId = "findTheProcedure",
        tags = ["Procedure"],
//        pathParams = [OpenApiParam(
//            name = "procedure_id",
//            type = String::class,
//            description = "The dentist ID",
//            deprecated = false,
//            required = true,
//            allowEmptyValue = false,
//            isRepeatable = false
//        )],
        responses = [
            OpenApiResponse("200", [OpenApiContent(Response::class)]),
            OpenApiResponse("404", [OpenApiContent(Response::class)])
        ],
        method = HttpMethod.GET
    )
    fun findTheProcedure(context: Context) {
        logger.debug(
            "[{} {}] - Finding the procedure...",
            PROCEDURE.name,
            ENDPOINT.name
        )
        context.getRequest(::FindTheProcedureRequest)
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
            "[{} {}] - Finding the procedure: -> !DONE! <-",
            PROCEDURE.name,
            ENDPOINT.name
        )
    }

    companion object: Loggable()
}

/**
 * @author ACMattos
 * @since 20/03/2022.
 */
class FindAllProceduresRequest(val context: Context): Request<ProcedureQuery>(context) {
    override fun toType(who: String, what: String): ProcedureQuery {
        return FindAllProceduresQuery(
            AuditLog(who, what)
        )
    }
}

/**
 * @author ACMattos
 * @since 24/03/2022.
 */
class FindTheProcedureRequest(val context: Context): Request<ProcedureQuery>(context) {
    override fun toType(who: String, what: String): ProcedureQuery {
        val procedureId = context.pathParam("procedure_id")
        return FindTheProcedureQuery(
            ProcedureId(procedureId),
            AuditLog(who, what)
        )
    }
}
