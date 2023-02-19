package br.com.acmattos.hdc.odontogram.port.rest

import br.com.acmattos.hdc.common.tool.server.javalin.ContextBuilder
import br.com.acmattos.hdc.common.tool.server.javalin.Response
import br.com.acmattos.hdc.common.tool.server.javalin.getRequest
import br.com.acmattos.hdc.common.tool.server.mapper.JacksonObjectMapperFactory
import br.com.acmattos.hdc.odontogram.application.OdontogramCommandHandlerService
import br.com.acmattos.hdc.odontogram.domain.cqs.OdontogramCreateCommand
import br.com.acmattos.hdc.odontogram.domain.cqs.OdontogramCreateEvent
import br.com.acmattos.hdc.odontogram.domain.cqs.OdontogramDeleteCommand
import br.com.acmattos.hdc.odontogram.domain.cqs.OdontogramDeleteEvent
import br.com.acmattos.hdc.odontogram.domain.cqs.OdontogramUpdateCommand
import br.com.acmattos.hdc.odontogram.domain.cqs.OdontogramUpdateEvent
import br.com.acmattos.hdc.odontogram.domain.model.EventBuilder
import br.com.acmattos.hdc.odontogram.domain.model.RequestBuilder
import io.javalin.http.Context
import io.javalin.plugin.json.JavalinJackson
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

private const val STATUS_201 = 201
private const val STATUS_200 = 200

/**
 * @author ACMattos
 * @since 19/03/2022.
 */
object OdontogramCommandControllerTest: Spek({
    Feature("${OdontogramCommandController::class.java.simpleName} usage") {
        Scenario("handling ${OdontogramCreateCommand::class.java} successfully") {
            lateinit var request: OdontogramCreateRequest
            lateinit var event: OdontogramCreateEvent
            lateinit var service: OdontogramCommandHandlerService
            lateinit var context: Context
            lateinit var controller: OdontogramCommandController

            Given("""a ${OdontogramCreateRequest::class.java.simpleName} successfully instantiated""") {
                request = RequestBuilder.buildCreateRequest()
            }
            And("""a ${OdontogramCreateEvent::class.java.simpleName} successfully instantiated""") {
                event = EventBuilder.buildCreateEvent()
            }
            And("""a ${OdontogramCommandHandlerService::class.java.simpleName} mock""") {
                service = mockk()
            }
            And("""service#handle returning the ${OdontogramCreateCommand::class.java.simpleName}""") {
                every { service.handle(any<OdontogramCreateCommand>()) } returns event
            }
            And("""a ${Context::class.java.simpleName} mock""") {
                context = mockk()
            }
            And("""context#bodyValidator#get returns ${OdontogramCreateRequest::class.java.simpleName}""") {
                every { context.bodyValidator<OdontogramCreateRequest>().get() } returns request
            }
            And("""context#fullUrl returns fullUrl""") {
                every { context.fullUrl() } returns "fullUrl"
            }
            And("""context#status returns ${Context::class.java.simpleName}""") {
                every { context.status(STATUS_201) } returns context
            }
            And("""context#status returns $STATUS_201""") {
                every { context.status() } returns STATUS_201
            }
            And("""context#json returns ${Context::class.java.simpleName}""") {
                every { context.json(any<Response>()) } returns context
            }
            And("""a ${OdontogramCommandController::class.java.simpleName} successfully instantiated""") {
                controller = OdontogramCommandController(service)
            }
            When("""#createDentalOdontogram is executed""") {
                controller.create(context)
            }
            Then("""status is $STATUS_201""") {
                assertThat(context.status()).isEqualTo(STATUS_201)
            }
            And("""the context is accessed in the right order""") {
                verifyOrder {
                    context.bodyValidator<OdontogramCreateRequest>().get()
                    context.fullUrl()
                    context.status(STATUS_201)
                    context.status()
                    context.json(any<Response>())
                }
            }
            And("""the service is accessed as well""") {
                verify(exactly = 1) {
                    service.handle(any<OdontogramCreateCommand>())
                }
            }
        }

        Scenario("handling ${OdontogramUpdateCommand::class.java.simpleName} successfully") {
            lateinit var request: OdontogramUpdateRequest
            lateinit var event: OdontogramUpdateEvent
            lateinit var service: OdontogramCommandHandlerService
            lateinit var context: Context
            lateinit var controller: OdontogramCommandController

            Given("""a ${OdontogramUpdateRequest::class.java.simpleName} successfully instantiated""") {
                request = RequestBuilder.buildUpdateRequest()
            }
            And("""a ${OdontogramUpdateEvent::class.java.simpleName} successfully instantiated""") {
                event = EventBuilder.buildUpdateEvent()
            }
            And("""a ${OdontogramCommandHandlerService::class.java.simpleName} mock""") {
                service = mockk()
            }
            And("""service#handle returning the ${OdontogramUpdateCommand::class.java.simpleName}""") {
                every { service.handle(any<OdontogramUpdateCommand>()) } returns event
            }
            And("""a ${Context::class.java.simpleName} mock""") {
                context = mockk()
            }
            And("""context#bodyValidator#get returns ${OdontogramUpdateRequest::class.java.simpleName}""") {
                every { context.bodyValidator<OdontogramUpdateRequest>().get() } returns request
            }
            And("""context#fullUrl returns fullUrl""") {
                every { context.fullUrl() } returns "fullUrl"
            }
            And("""context#status returns ${Context::class.java.simpleName}""") {
                every { context.status(STATUS_200) } returns context
            }
            And("""context#status returns $STATUS_200""") {
                every { context.status() } returns STATUS_200
            }
            And("""context#json returns ${Context::class.java.simpleName}""") {
                every { context.json(any<Response>()) } returns context
            }
            And("""a ${OdontogramCommandController::class.java.simpleName} successfully instantiated""") {
                controller = OdontogramCommandController(service)
            }
            When("""#updateDentalOdontogram is executed""") {
                controller.update(context)
            }
            Then("""status is $STATUS_200""") {
                assertThat(context.status()).isEqualTo(STATUS_200)
            }
            And("""the context is accessed in the right order""") {
                verifyOrder {
                    context.bodyValidator<OdontogramUpdateRequest>().get()
                    context.fullUrl()
                    context.status(STATUS_200)
                    context.status()
                    context.json(any<Response>())
                }
            }
            And("""the service is accessed as well""") {
                verify(exactly = 1) {
                    service.handle(any<OdontogramUpdateCommand>())
                }
            }
        }

        Scenario("handling ${OdontogramDeleteCommand::class.java.simpleName} successfully") {
            lateinit var event: OdontogramDeleteEvent
            lateinit var service: OdontogramCommandHandlerService
            lateinit var context: Context
            lateinit var controller: OdontogramCommandController

            Given(""""a ${OdontogramDeleteEvent::class.java.simpleName} successfully instantiated""") {
                event = EventBuilder.buildDeleteEvent()
            }
            And("""a ${OdontogramCommandHandlerService::class.java.simpleName} mock""") {
                service = mockk()
            }
            And("""service#handle returning the ${OdontogramDeleteCommand::class.java.simpleName}""") {
                every { service.handle(any<OdontogramDeleteCommand>()) } returns event
            }
            And("""a ${Context::class.java} mock""") {
                context = ContextBuilder().mockContext("odontogram_id", "01FJJDJKDXN4K558FMCKEMQE6B")
            }
            And("""${JavalinJackson::class.java} is properly configured""") {
                JavalinJackson.configure(JacksonObjectMapperFactory.build())
            }
            And("""context#fullUrl returns fullUrl""") {
                every { context.fullUrl() } returns "fullUrl"
            }
            And("""context#status returns ${Context::class.java.simpleName}""") {
                every { context.status(STATUS_200) } returns context
            }
            And("""context#status returns $STATUS_200""") {
                every { context.status() } returns STATUS_200
            }
            And("""context#json returns ${Context::class.java.simpleName}""") {
                every { context.json(Response("", "", 0, "")) } returns context
            }
            And("""a ${OdontogramCommandController::class.java.simpleName} successfully instantiated""") {
                controller = OdontogramCommandController(service)
            }
            When("""#deleteDentalOdontogram is executed""") {
                controller.delete(context)
            }
            Then("""status is $STATUS_200""") {
                assertThat(context.status()).isEqualTo(STATUS_200)
            }
            And("""the context is accessed in the right order""") {
                verifyOrder {
                    context.getRequest(::OdontogramDeleteRequest)
                    context.fullUrl()
                    context.status(STATUS_200)
                    context.status()
                    context.json(Response("", "", 0, ""))
                }
            }
            And("""the service is accessed as well""") {
                verify(exactly = 1) {
                    service.handle(any<OdontogramDeleteCommand>())
                }
            }
        }
    }
})
