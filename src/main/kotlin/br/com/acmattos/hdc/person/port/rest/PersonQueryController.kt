package br.com.acmattos.hdc.person.port.rest

import br.com.acmattos.hdc.common.context.domain.cqs.QueryHandler
import br.com.acmattos.hdc.common.context.domain.model.AuditLog
import br.com.acmattos.hdc.common.context.port.rest.EndpointLogEnum.ENDPOINT
import br.com.acmattos.hdc.common.context.port.rest.Request
import br.com.acmattos.hdc.common.tool.assertion.Assertion
import br.com.acmattos.hdc.common.tool.loggable.Loggable
import br.com.acmattos.hdc.common.tool.server.javalin.Response
import br.com.acmattos.hdc.common.tool.server.javalin.getRequest
import br.com.acmattos.hdc.person.config.ErrorTrackerCodeEnum.DENTIST_ID_INVALID
import br.com.acmattos.hdc.person.config.PersonLogEnum.DENTIST
import br.com.acmattos.hdc.person.domain.cqs.FindTheDentistQuery
import br.com.acmattos.hdc.person.domain.cqs.PersonQuery
import br.com.acmattos.hdc.person.domain.model.Person
import br.com.acmattos.hdc.person.domain.model.PersonId
import br.com.acmattos.hdc.scheduler.domain.model.ScheduleId
import io.javalin.http.Context
import io.javalin.plugin.openapi.annotations.HttpMethod
import io.javalin.plugin.openapi.annotations.OpenApi
import io.javalin.plugin.openapi.annotations.OpenApiContent
import io.javalin.plugin.openapi.annotations.OpenApiParam
import io.javalin.plugin.openapi.annotations.OpenApiResponse
import org.eclipse.jetty.http.HttpStatus

/**
 * @author ACMattos
 * @since 14/08/2021.
 */
class PersonQueryController(
    private val handler: QueryHandler<Person>
) {
    @OpenApi(
        summary = "Find the dentist identified by the given ID.",
        operationId = "findTheDentist",
        tags = ["Dentist"],
        pathParams = [OpenApiParam(
            name = "dentist_id",
            type = String::class,
            description = "The dentist ID",
            deprecated = false,
            required = true,
            allowEmptyValue = false,
            isRepeatable = false
        )],
        responses = [
            OpenApiResponse("200", [OpenApiContent(ScheduleId::class)]),
            OpenApiResponse("404", [OpenApiContent(Response::class)])
        ],
        method = HttpMethod.GET
    )
    fun findTheDentist(context: Context) {
        logger.debug(
            "[{} {}] - Finding the dentist...",
            DENTIST.name,
            ENDPOINT.name
        )
        context.getRequest<FindTheDentistRequest>(::FindTheDentistRequest)
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
            "[{} {}] - Finding the dentist... -> !DONE! <-",
            DENTIST.name,
            ENDPOINT.name
        )
    }

    companion object: Loggable()
}

/**
 * @author ACMattos
 * @since 14/08/2021.
 */
class FindTheDentistRequest(val context: Context): Request<PersonQuery>(context) {
    override fun toType(who: String, what: String): PersonQuery {
        val dentistId = context.pathParam("dentist_id")
        Assertion.assert(
            "dentist_id can't be null or empty",
            DENTIST_ID_INVALID.code
        ) {
            dentistId.isNotBlank()
        }

        return FindTheDentistQuery(
            PersonId(dentistId),
            AuditLog(who, what)
        )
    }
}
