package br.com.acmattos.hdc.person.port.rest

import br.com.acmattos.hdc.common.context.domain.cqs.QueryHandler
import br.com.acmattos.hdc.common.context.domain.cqs.QueryResult
import br.com.acmattos.hdc.common.context.domain.model.AuditLog
import br.com.acmattos.hdc.common.context.port.rest.EndpointLogEnum.ENDPOINT
import br.com.acmattos.hdc.common.context.port.rest.Request
import br.com.acmattos.hdc.common.tool.assertion.Assertion
import br.com.acmattos.hdc.common.tool.loggable.Loggable
import br.com.acmattos.hdc.common.tool.server.javalin.Response
import br.com.acmattos.hdc.common.tool.server.javalin.getRequest
import br.com.acmattos.hdc.person.config.MessageTrackerCodeEnum.DENTIST_ID_INVALID
import br.com.acmattos.hdc.person.config.PersonLogEnum.DENTIST
import br.com.acmattos.hdc.person.config.PersonLogEnum.PERSON
import br.com.acmattos.hdc.person.domain.cqs.FindAllContactTypesQuery
import br.com.acmattos.hdc.person.domain.cqs.FindAllPersonTypesQuery
import br.com.acmattos.hdc.person.domain.cqs.FindAllPersonsQuery
import br.com.acmattos.hdc.person.domain.cqs.FindAllStatesQuery
import br.com.acmattos.hdc.person.domain.cqs.FindTheDentistQuery
import br.com.acmattos.hdc.person.domain.cqs.PersonQuery
import br.com.acmattos.hdc.person.domain.model.ContactType
import br.com.acmattos.hdc.person.domain.model.Person
import br.com.acmattos.hdc.person.domain.model.PersonId
import br.com.acmattos.hdc.person.domain.model.PersonType
import br.com.acmattos.hdc.person.domain.model.State
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
        summary = "Find all persons.",
        operationId = "findAllPersons",
        tags = ["Person"],
        responses = [
            OpenApiResponse("200", [OpenApiContent(Response::class)]),
            OpenApiResponse("404", [OpenApiContent(Response::class)])
        ],
        method = HttpMethod.GET
    )
    fun findAllPersons(context: Context) {// TODO Test
        logger.debug(
            "[{} {}] - Finding all persons...",
            PERSON.name,
            ENDPOINT.name
        )
        context.getRequest(::FindAllPersonsRequest)
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
            "[{} {}] - Finding all persons: -> !DONE! <-",
            PERSON.name,
            ENDPOINT.name
        )
    }

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
            OpenApiResponse("200", [OpenApiContent(Response::class)]),
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
        context.getRequest(::FindTheDentistRequest)
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
            "[{} {}] - Finding the dentist: -> !DONE! <-",
            DENTIST.name,
            ENDPOINT.name
        )
    }

    @OpenApi(
        summary = "Find all person types",
        operationId = "findAllPersonTypes",
        tags = ["Person"],
        responses = [
            OpenApiResponse("200", [OpenApiContent(Response::class)]),
            OpenApiResponse("404", [OpenApiContent(Response::class)])
        ],
        method = HttpMethod.GET
    )
    fun findAllPersonTypes(context: Context) {// TODO Test
        logger.debug(
            "[{} {}] - Finding all persons types...",
            PERSON.name,
            ENDPOINT.name
        )
        context.getRequest(::FindAllPersonTypesRequest)
            .toType(what = context.fullUrl())
            .also { query ->
                context.status(HttpStatus.OK_200).json(
                    Response.create(
                        context.status(),
                        QueryResult.build(PersonType.values().toList())
                    )
                )
            }
        logger.info(
            "[{} {}] - Finding all persons types: -> !DONE! <-",
            PERSON.name,
            ENDPOINT.name
        )
    }

    @OpenApi(
        summary = "Find all states",
        operationId = "findAllStates",
        tags = ["Person"],
        responses = [
            OpenApiResponse("200", [OpenApiContent(Response::class)]),
            OpenApiResponse("404", [OpenApiContent(Response::class)])
        ],
        method = HttpMethod.GET
    )
    fun findAllStates(context: Context) {// TODO Test
        logger.debug(
            "[{} {}] - Finding all states...",
            PERSON.name,
            ENDPOINT.name
        )
        context.getRequest(::FindAllStatesRequest)
            .toType(what = context.fullUrl())
            .also { query ->
                context.status(HttpStatus.OK_200).json(
                    Response.create(
                        context.status(),
                        QueryResult.build(State.values().toList())
                    )
                )
            }
        logger.info(
            "[{} {}] - Finding all states: -> !DONE! <-",
            PERSON.name,
            ENDPOINT.name
        )
    }

    @OpenApi(
        summary = "Find all contact types",
        operationId = "findAllContactTypes",
        tags = ["Person"],
        responses = [
            OpenApiResponse("200", [OpenApiContent(Response::class)]),
            OpenApiResponse("404", [OpenApiContent(Response::class)])
        ],
        method = HttpMethod.GET
    )
    fun findAllContactTypes(context: Context) {// TODO Test
        logger.debug(
            "[{} {}] - Finding all contact types...",
            PERSON.name,
            ENDPOINT.name
        )
        context.getRequest(::FindAllContactTypesRequest)
            .toType(what = context.fullUrl())
            .also { query ->
                context.status(HttpStatus.OK_200).json(
                    Response.create(
                        context.status(),
                        QueryResult.build(ContactType.values().toList())
                    )
                )
            }
        logger.info(
            "[{} {}] - Finding all contact types: -> !DONE! <-",
            PERSON.name,
            ENDPOINT.name
        )
    }

    companion object: Loggable()
}

/**
 * @author ACMattos
 * @since 04/02/2022.
 */
class FindAllPersonsRequest(val context: Context): Request<PersonQuery>(context) {
    override fun toType(who: String, what: String): PersonQuery {
        return FindAllPersonsQuery(
            AuditLog(who, what)
        )
    }
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
            DENTIST.name,
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

/**
 * @author ACMattos
 * @since 23/02/2022.
 */
class FindAllPersonTypesRequest(val context: Context): Request<PersonQuery>(context) {
    override fun toType(who: String, what: String): PersonQuery {
        return FindAllPersonTypesQuery(
            AuditLog(who, what)
        )
    }
}

/**
 * @author ACMattos
 * @since 10/03/2022.
 */
class FindAllStatesRequest(val context: Context): Request<PersonQuery>(context) {
    override fun toType(who: String, what: String): PersonQuery {
        return FindAllStatesQuery(
            AuditLog(who, what)
        )
    }
}

/**
 * @author ACMattos
 * @since 10/03/2022.
 */
class FindAllContactTypesRequest(val context: Context): Request<PersonQuery>(context) {
    override fun toType(who: String, what: String): PersonQuery {
        return FindAllContactTypesQuery(
            AuditLog(who, what)
        )
    }
}