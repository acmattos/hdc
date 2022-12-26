package br.com.acmattos.hdc.odontogram.port.rest

import br.com.acmattos.hdc.common.context.domain.cqs.QueryResult
import br.com.acmattos.hdc.common.tool.server.javalin.ContextBuilder
import br.com.acmattos.hdc.common.tool.server.javalin.Response
import br.com.acmattos.hdc.common.tool.server.javalin.getRequest
import br.com.acmattos.hdc.common.tool.server.mapper.JacksonObjectMapperFactory
import br.com.acmattos.hdc.odontogram.config.OdontogramQueryHandlerService
import br.com.acmattos.hdc.odontogram.domain.cqs.GetAnOdontogramQuery
import br.com.acmattos.hdc.odontogram.domain.model.Odontogram
import io.javalin.http.Context
import io.javalin.plugin.json.JavalinJackson
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import java.util.Optional
import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

private const val STATUS = 200

/**
 * @author ACMattos
 * @since 21/10/2022.
 */
object OdontogramQueryControllerTest: Spek({
    Feature("${OdontogramQueryController::class.java} usage") {
        Scenario("handling ${GetAnOdontogramRequest::class.java} successfully") {
            lateinit var request: GetAnOdontogramRequest
            lateinit var query: GetAnOdontogramQuery
            lateinit var result: QueryResult<Odontogram>
            lateinit var service: OdontogramQueryHandlerService
            lateinit var context: Context
            lateinit var controller: OdontogramQueryController
            Given("""a ${GetAnOdontogramRequest::class.java} successfully instantiated""") {
                request = OdontogramRequestBuilder.buildGetAnOdontogramRequest()
            }
            And("""a ${GetAnOdontogramQuery::class.java} successfully generated""") {
                query = request.toType() as GetAnOdontogramQuery
            }
            And("""a ${QueryResult::class.java} successfully instantiated""") {
                result = QueryResult.build(Optional.of(Odontogram.create()))
            }
            And("""a ${OdontogramQueryHandlerService::class.java} mock""") {
                service = mockk()
            }
            And("""service#handle returning the ${QueryResult::class.java}""") {
                every { service.handle(any<GetAnOdontogramQuery>()) } returns result
            }
            And("""a ${Context::class.java} mock""") {
                context = ContextBuilder().mockContext()
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
            And("""a ${OdontogramQueryController::class.java} successfully instantiated""") {
                controller = OdontogramQueryController(service)
            }
            When("""#getABasicOdontogram is executed""") {
                controller.getABasicOdontogram(context)
            }
            Then("""status is $STATUS""") {
                assertThat(context.status()).isEqualTo(STATUS)
            }
            And("""the context is accessed in the right order""") {
                verifyOrder {
                    context.getRequest(::GetAnOdontogramRequest)
                    context.fullUrl()
                    context.status(STATUS)
                    context.status()
                    context.json(Response("", "", 0, ""))
                }
            }
            And("""the service store is accessed as well""") {
                verify(exactly = 1) {
                    service.handle(any<GetAnOdontogramQuery>())
                }
            }
        }
    }
})
