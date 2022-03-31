package br.com.acmattos.hdc.procedure.port.rest

import br.com.acmattos.hdc.common.context.domain.cqs.QueryResult
import br.com.acmattos.hdc.common.tool.server.javalin.ContextBuilder
import br.com.acmattos.hdc.common.tool.server.javalin.Response
import br.com.acmattos.hdc.common.tool.server.javalin.getRequest
import br.com.acmattos.hdc.common.tool.server.mapper.JacksonObjectMapperFactory
import br.com.acmattos.hdc.procedure.application.ProcedureQueryHandlerService
import br.com.acmattos.hdc.procedure.domain.cqs.FindAllProceduresQuery
import br.com.acmattos.hdc.procedure.domain.cqs.FindTheProcedureQuery
import br.com.acmattos.hdc.procedure.domain.model.Procedure
import br.com.acmattos.hdc.procedure.domain.model.ProcedureBuilder
import io.javalin.http.Context
import io.javalin.plugin.json.JavalinJackson
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import java.util.Optional
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

private const val STATUS = 200

/**
 * @author ACMattos
 * @since 30/03/2022.
 */
object ProcedureQueryControllerTest: Spek({
    Feature("${ProcedureQueryController::class.java} usage") {
        Scenario("handling ${FindAllProceduresQuery::class.java} successfully") {
            lateinit var request: FindAllProceduresRequest
            lateinit var query: FindAllProceduresQuery
            lateinit var result: QueryResult<Procedure>
            lateinit var service: ProcedureQueryHandlerService
            lateinit var context: Context
            lateinit var controller: ProcedureQueryController
            Given("""a ${FindAllProceduresRequest::class.java} successfully instantiated""") {
                request = ProcedureRequestBuilder.buildFindAllProceduresRequest()
            }
            And("""a ${FindAllProceduresQuery::class.java} successfully generated""") {
                query = request.toType() as FindAllProceduresQuery
            }
            And("""a ${QueryResult::class.java} successfully instantiated""") {
                result = QueryResult.build(listOf(ProcedureBuilder.build()))
            }
            And("""a ${ProcedureQueryHandlerService::class.java} mock""") {
                service = mockk()
            }
            And("""service#handle returning the ${QueryResult::class.java}""") {
                every { service.handle(any<FindAllProceduresQuery>()) } returns result
            }
            And("""a ${Context::class.java} mock""") {
                context = ContextBuilder().mockContext("procedure_id")
            }
            And("""${JavalinJackson::class.java} is properly configured""") {
                JavalinJackson.configure(JacksonObjectMapperFactory.build())
            }
            And("""context#fullUrl returns fullUrl""") {
                every { context.fullUrl() } returns "fullUrl"
            }
            And("""context#status returns ${Context::class.java}""") {
                every { context.status(STATUS) } returns context
            }
            And("""context#status returns $STATUS""") {
                every { context.status() } returns STATUS
            }
            And("""context#json returns ${Context::class.java}""") {
                every { context.json(Response("", "", 0, "")) } returns context
            }
            And("""a ${ProcedureQueryController::class.java} successfully instantiated""") {
                controller = ProcedureQueryController(service)
            }
            When("""#findTheProcedure is executed""") {
                controller.findAllProcedures(context)
            }
            Then("""status is $STATUS""") {
                assertThat(context.status()).isEqualTo(STATUS)
            }
            And("""the context is accessed in the right order""") {
                verifyOrder {
                    context.getRequest(::FindAllProceduresRequest)
                    context.fullUrl()
                    context.status(STATUS)
                    context.status()
                    context.json(Response("", "", 0, ""))
                }
            }
            And("""the service store is accessed as well""") {
                verify(exactly = 1) {
                    service.handle(any<FindAllProceduresQuery>())
                }
            }
        }

        Scenario("handling ${FindTheProcedureQuery::class.java} successfully") {
            lateinit var request: FindTheProcedureRequest
            lateinit var query: FindTheProcedureQuery
            lateinit var result: QueryResult<Procedure>
            lateinit var service: ProcedureQueryHandlerService
            lateinit var context: Context
            lateinit var controller: ProcedureQueryController
            Given("""a ${FindTheProcedureRequest::class.java} successfully instantiated""") {
                request = ProcedureRequestBuilder.buildFindTheProcedureRequest()
            }
            And("""a ${FindTheProcedureQuery::class.java} successfully generated""") {
                query = request.toType() as FindTheProcedureQuery
            }
            And("""a ${QueryResult::class.java} successfully instantiated""") {
                result = QueryResult.build(Optional.of(ProcedureBuilder.build()))
            }
            And("""a ${ProcedureQueryHandlerService::class.java} mock""") {
                service = mockk()
            }
            And("""service#handle returning the ${QueryResult::class.java}""") {
                every { service.handle(any<FindTheProcedureQuery>()) } returns result
            }
            And("""a ${Context::class.java} mock""") {
                context = ContextBuilder().mockContext("procedure_id")
            }
            And("""${JavalinJackson::class.java} is properly configured""") {
                JavalinJackson.configure(JacksonObjectMapperFactory.build())
            }
            And("""context#fullUrl returns fullUrl""") {
                every { context.fullUrl() } returns "fullUrl"
            }
            And("""context#status returns ${Context::class.java}""") {
                every { context.status(STATUS) } returns context
            }
            And("""context#status returns $STATUS""") {
                every { context.status() } returns STATUS
            }
            And("""context#json returns ${Context::class.java}""") {
                every { context.json(Response("", "", 0, "")) } returns context
            }
            And("""a ${ProcedureQueryController::class.java} successfully instantiated""") {
                controller = ProcedureQueryController(service)
            }
            When("""#findTheProcedure is executed""") {
                controller.findTheProcedure(context)
            }
            Then("""status is $STATUS""") {
                assertThat(context.status()).isEqualTo(STATUS)
            }
            And("""the context is accessed in the right order""") {
                verifyOrder {
                    context.getRequest(::FindTheProcedureRequest)
                    context.fullUrl()
                    context.status(STATUS)
                    context.status()
                    context.json(Response("", "", 0, ""))
                }
            }
            And("""the service store is accessed as well""") {
                verify(exactly = 1) {
                    service.handle(any<FindTheProcedureQuery>())
                }
            }
        }
    }
})
