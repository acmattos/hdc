package br.com.acmattos.hdc.procedure.port.rest

import br.com.acmattos.hdc.common.tool.server.javalin.ContextBuilder
import br.com.acmattos.hdc.common.tool.server.javalin.Response
import br.com.acmattos.hdc.common.tool.server.javalin.getRequest
import br.com.acmattos.hdc.common.tool.server.mapper.JacksonObjectMapperFactory
import br.com.acmattos.hdc.procedure.application.ProcedureCommandHandlerService
import br.com.acmattos.hdc.procedure.domain.cqs.*
import br.com.acmattos.hdc.procedure.domain.model.EventBuilder
import br.com.acmattos.hdc.procedure.domain.model.RequestBuilder
import io.javalin.http.Context
import io.javalin.plugin.json.JavalinJackson
import io.kotest.core.spec.style.FreeSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import org.assertj.core.api.Assertions.assertThat

private const val STATUS_201 = 201
private const val STATUS_200 = 200

/**
 * @author ACMattos
 * @since 19/03/2022.
 */
class ProcedureCommandControllerTest: FreeSpec({
    "Feature: ProcedureCommandController usage" - {
        "Scenario: handling ProcedureCreateCommand successfully" - {
            lateinit var request: ProcedureCreateRequest
            lateinit var event: ProcedureCreateEvent
            lateinit var service: ProcedureCommandHandlerService
            lateinit var context: Context
            lateinit var controller: ProcedureCommandController

            "Given: a ${ProcedureCreateRequest::class.java.simpleName} successfully instantiated" {
                request = RequestBuilder.buildCreateRequest()
            }
            "And: a ${ProcedureCreateEvent::class.java.simpleName} successfully instantiated" {
                event = EventBuilder.buildCreateEvent()
            }
            "And: a ${ProcedureCommandHandlerService::class.java.simpleName} mock" {
                service = mockk()
            }
            "And: service#handle returning the ${ProcedureCreateCommand::class.java.simpleName}" {
                every { service.handle(any<ProcedureCreateCommand>()) } returns event
            }
            "And: a ${Context::class.java.simpleName} mock" {
                context = mockk()
            }
            "And: context#bodyValidator#get returns ${ProcedureCreateRequest::class.java.simpleName}" {
                every { context.bodyValidator<ProcedureCreateRequest>().get() } returns request
            }
            "And: context#fullUrl returns fullUrl" {
                every { context.fullUrl() } returns "fullUrl"
            }
            "And: context#status returns ${Context::class.java.simpleName}" {
                every { context.status(STATUS_201) } returns context
            }
            "And: context#status returns $STATUS_201" {
                every { context.status() } returns STATUS_201
            }
            "And: context#json returns ${Context::class.java.simpleName}" {
                every { context.json(any<Response>()) } returns context
            }
            "And: a ${ProcedureCommandController::class.java.simpleName} successfully instantiated" {
                controller = ProcedureCommandController(service)
            }
            "When: #createDentalProcedure is executed" {
                controller.create(context)
            }
            "Then: status is $STATUS_201" {
                assertThat(context.status()).isEqualTo(STATUS_201)
            }
            "And: the context is accessed in the right order" {
                verifyOrder {
                    context.bodyValidator<ProcedureCreateRequest>().get()
                    context.fullUrl()
                    context.status(STATUS_201)
                    context.status()
                    context.json(any<Response>())
                }
            }
            "And: the service is accessed as well" {
                verify(exactly = 1) {
                    service.handle(any<ProcedureCreateCommand>())
                }
            }
        }

        "Scenario: handling ProcedureUpdateCommand successfully" - {
            lateinit var request: ProcedureUpdateRequest
            lateinit var event: ProcedureUpdateEvent
            lateinit var service: ProcedureCommandHandlerService
            lateinit var context: Context
            lateinit var controller: ProcedureCommandController

            "Given: a ${ProcedureUpdateRequest::class.java.simpleName} successfully instantiated" {
                request = RequestBuilder.buildUpdateRequest()
            }
            "And: a ${ProcedureUpdateEvent::class.java.simpleName} successfully instantiated" {
                event = EventBuilder.buildUpdateEvent()
            }
            "And: a ${ProcedureCommandHandlerService::class.java.simpleName} mock" {
                service = mockk()
            }
            "And: service#handle returning the ${ProcedureUpdateCommand::class.java.simpleName}" {
                every { service.handle(any<ProcedureUpdateCommand>()) } returns event
            }
            "And: a ${Context::class.java.simpleName} mock" {
                context = mockk()
            }
            "And: context#bodyValidator#get returns ${ProcedureUpdateRequest::class.java.simpleName}" {
                every { context.bodyValidator<ProcedureUpdateRequest>().get() } returns request
            }
            "And: context#fullUrl returns fullUrl" {
                every { context.fullUrl() } returns "fullUrl"
            }
            "And: context#status returns ${Context::class.java.simpleName}" {
                every { context.status(STATUS_200) } returns context
            }
            "And: context#status returns $STATUS_200" {
                every { context.status() } returns STATUS_200
            }
            "And: context#json returns ${Context::class.java.simpleName}" {
                every { context.json(any<Response>()) } returns context
            }
            "And: a ${ProcedureCommandController::class.java.simpleName} successfully instantiated" {
                controller = ProcedureCommandController(service)
            }
            "When: #updateDentalProcedure is executed" {
                controller.update(context)
            }
            "Then: status is $STATUS_200" {
                assertThat(context.status()).isEqualTo(STATUS_200)
            }
            "And: the context is accessed in the right order" {
                verifyOrder {
                    context.bodyValidator<ProcedureUpdateRequest>().get()
                    context.fullUrl()
                    context.status(STATUS_200)
                    context.status()
                    context.json(any<Response>())
                }
            }
            "And: the service is accessed as well" {
                verify(exactly = 1) {
                    service.handle(any<ProcedureUpdateCommand>())
                }
            }
        }

        "Scenario: handling ProcedureDeleteCommand successfully" - {
            lateinit var event: ProcedureDeleteEvent
            lateinit var service: ProcedureCommandHandlerService
            lateinit var context: Context
            lateinit var controller: ProcedureCommandController

            "Given: a ${ProcedureDeleteEvent::class.java.simpleName} successfully instantiated" {
                event = EventBuilder.buildDeleteEvent()
            }
            "And: a ${ProcedureCommandHandlerService::class.java.simpleName} mock" {
                service = mockk()
            }
            "And: service#handle returning the ${ProcedureDeleteCommand::class.java.simpleName}" {
                every { service.handle(any<ProcedureDeleteCommand>()) } returns event
            }
            "And: a ${Context::class.java} mock" {
                context = ContextBuilder().mockContext("procedure_ids", "01FJJDJKDXN4K558FMCKEMQE6B;1")
            }
            "And: ${JavalinJackson::class.java} is properly configured" {
                JavalinJackson.configure(JacksonObjectMapperFactory.build())
            }
            "And: context#fullUrl returns fullUrl" {
                every { context.fullUrl() } returns "fullUrl"
            }
            "And: context#status returns ${Context::class.java.simpleName}" {
                every { context.status(STATUS_200) } returns context
            }
            "And: context#status returns $STATUS_200" {
                every { context.status() } returns STATUS_200
            }
            "And: context#json returns ${Context::class.java.simpleName}" {
                every { context.json(Response("", "", 0, "") )} returns context
            }
            "And: a ${ProcedureCommandController::class.java.simpleName} successfully instantiated" {
                controller = ProcedureCommandController(service)
            }
            "When: #deleteDentalProcedure is executed" {
                controller.delete(context)
            }
            "Then: status is $STATUS_200" {
                assertThat(context.status()).isEqualTo(STATUS_200)
            }
            "And: the context is accessed in the right order" {
                verifyOrder {
                    context.getRequest(::ProcedureDeleteRequest)
                    context.fullUrl()
                    context.status(STATUS_200)
                    context.status()
                    context.json(Response("", "", 0, ""))
                }
            }
            "And: the service is accessed as well" {
                verify(exactly = 1) {
                    service.handle(any<ProcedureDeleteCommand>())
                }
            }
        }
    }
})
