package br.com.acmattos.hdc.procedure.port.rest

import br.com.acmattos.hdc.common.tool.server.javalin.ContextBuilder
import br.com.acmattos.hdc.common.tool.server.javalin.Response
import br.com.acmattos.hdc.common.tool.server.javalin.getRequest
import br.com.acmattos.hdc.common.tool.server.mapper.JacksonObjectMapperFactory
import br.com.acmattos.hdc.person.application.PersonCommandHandlerService
import br.com.acmattos.hdc.procedure.application.ProcedureCommandHandlerService
import br.com.acmattos.hdc.procedure.domain.cqs.CreateDentalProcedureCommand
import br.com.acmattos.hdc.procedure.domain.cqs.CreateDentalProcedureEvent
import br.com.acmattos.hdc.procedure.domain.cqs.DeleteDentalProcedureCommand
import br.com.acmattos.hdc.procedure.domain.cqs.DeleteDentalProcedureEvent
import br.com.acmattos.hdc.procedure.domain.cqs.UpdateDentalProcedureCommand
import br.com.acmattos.hdc.procedure.domain.cqs.UpdateDentalProcedureEvent
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
object ProcedureCommandControllerTest: Spek({
    Feature("${ProcedureCommandController::class.java.simpleName} usage") {
        Scenario("handling ${CreateDentalProcedureCommand::class.java} successfully") {
            lateinit var request: CreateDentalProcedureRequest
            lateinit var command: CreateDentalProcedureCommand
            lateinit var event: CreateDentalProcedureEvent
            lateinit var service: ProcedureCommandHandlerService
            lateinit var context: Context
            lateinit var controller: ProcedureCommandController

            Given("""a ${CreateDentalProcedureRequest::class.java.simpleName} successfully instantiated""") {
                request = ProcedureRequestBuilder.buildCreateDentalProcedureRequest()
            }
            And("""a ${CreateDentalProcedureCommand::class.java.simpleName} successfully generated""") {
                command = request.toType() as CreateDentalProcedureCommand
            }
            And("""a ${CreateDentalProcedureEvent::class.java.simpleName} successfully instantiated""") {
                event = CreateDentalProcedureEvent(command)
            }
            And("""a ${ProcedureCommandHandlerService::class.java.simpleName} mock""") {
                service = mockk()
            }
            And("""service#handle returning the ${CreateDentalProcedureCommand::class.java.simpleName}""") {
                every { service.handle(any<CreateDentalProcedureCommand>()) } returns event
            }
            And("""a ${Context::class.java.simpleName} mock""") {
                context = mockk()
            }
            And("""context#bodyValidator#get returns ${CreateDentalProcedureRequest::class.java.simpleName}""") {
                every { context.bodyValidator<CreateDentalProcedureRequest>().get() } returns request
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
            And("""a ${PersonCommandHandlerService::class.java.simpleName} successfully instantiated""") {
                controller = ProcedureCommandController(service)
            }
            When("""#createDentalProcedure is executed""") {
                controller.createDentalProcedure(context)
            }
            Then("""status is $STATUS_201""") {
                assertThat(context.status()).isEqualTo(STATUS_201)
            }
            And("""the context is accessed in the right order""") {
                verifyOrder {
                    context.bodyValidator<CreateDentalProcedureRequest>().get()
                    context.fullUrl()
                    context.status(STATUS_201)
                    context.status()
                    context.json(any<Response>())
                }
            }
            And("""the service is accessed as well""") {
                verify(exactly = 1) {
                    service.handle(any<CreateDentalProcedureCommand>())
                }
            }
        }

        Scenario("handling ${UpdateDentalProcedureCommand::class.java.simpleName} successfully") {
            lateinit var request: UpdateDentalProcedureRequest
            lateinit var command: UpdateDentalProcedureCommand
            lateinit var event: UpdateDentalProcedureEvent
            lateinit var service: ProcedureCommandHandlerService
            lateinit var context: Context
            lateinit var controller: ProcedureCommandController

            Given("""a ${UpdateDentalProcedureRequest::class.java.simpleName} successfully instantiated""") {
                request = ProcedureRequestBuilder.buildUpdateDentalProcedureRequest()
            }
            And("""a ${UpdateDentalProcedureCommand::class.java.simpleName} successfully generated""") {
                command = request.toType() as UpdateDentalProcedureCommand
            }
            And("""a ${UpdateDentalProcedureEvent::class.java.simpleName} successfully instantiated""") {
                event = UpdateDentalProcedureEvent(command)
            }
            And("""a ${ProcedureCommandHandlerService::class.java.simpleName} mock""") {
                service = mockk()
            }
            And("""service#handle returning the ${UpdateDentalProcedureCommand::class.java.simpleName}""") {
                every { service.handle(any<UpdateDentalProcedureCommand>()) } returns event
            }
            And("""a ${Context::class.java.simpleName} mock""") {
                context = mockk()
            }
            And("""context#bodyValidator#get returns ${UpdateDentalProcedureRequest::class.java.simpleName}""") {
                every { context.bodyValidator<UpdateDentalProcedureRequest>().get() } returns request
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
            And("""a ${PersonCommandHandlerService::class.java.simpleName} successfully instantiated""") {
                controller = ProcedureCommandController(service)
            }
            When("""#updateDentalProcedure is executed""") {
                controller.updateDentalProcedure(context)
            }
            Then("""status is $STATUS_200""") {
                assertThat(context.status()).isEqualTo(STATUS_200)
            }
            And("""the context is accessed in the right order""") {
                verifyOrder {
                    context.bodyValidator<UpdateDentalProcedureRequest>().get()
                    context.fullUrl()
                    context.status(STATUS_200)
                    context.status()
                    context.json(any<Response>())
                }
            }
            And("""the service is accessed as well""") {
                verify(exactly = 1) {
                    service.handle(any<UpdateDentalProcedureCommand>())
                }
            }
        }

        Scenario("handling ${DeleteDentalProcedureCommand::class.java.simpleName} successfully") {
            lateinit var request: DeleteDentalProcedureRequest
            lateinit var command: DeleteDentalProcedureCommand
            lateinit var event: DeleteDentalProcedureEvent
            lateinit var service: ProcedureCommandHandlerService
            lateinit var context: Context
            lateinit var controller: ProcedureCommandController

            Given("""a ${DeleteDentalProcedureRequest::class.java.simpleName} successfully instantiated""") {
                request = ProcedureRequestBuilder.buildDeleteDentalProcedureRequest()
            }
            And("""a ${DeleteDentalProcedureCommand::class.java.simpleName} successfully generated""") {
                command = request.toType() as DeleteDentalProcedureCommand
            }
            And("""a ${DeleteDentalProcedureEvent::class.java.simpleName} successfully instantiated""") {
                event = DeleteDentalProcedureEvent(command)
            }
            And("""a ${ProcedureCommandHandlerService::class.java.simpleName} mock""") {
                service = mockk()
            }
            And("""service#handle returning the ${DeleteDentalProcedureCommand::class.java.simpleName}""") {
                every { service.handle(any<DeleteDentalProcedureCommand>()) } returns event
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
            And("""context#status returns ${Context::class.java.simpleName}""") {
                every { context.status(STATUS_200) } returns context
            }
            And("""context#status returns $STATUS_200""") {
                every { context.status() } returns STATUS_200
            }
            And("""context#json returns ${Context::class.java.simpleName}""") {
                every { context.json(Response("", "", 0, "")) } returns context
            }
            And("""a ${PersonCommandHandlerService::class.java.simpleName} successfully instantiated""") {
                controller = ProcedureCommandController(service)
            }
            When("""#deleteDentalProcedure is executed""") {
                controller.deleteDentalProcedure(context)
            }
            Then("""status is $STATUS_200""") {
                assertThat(context.status()).isEqualTo(STATUS_200)
            }
            And("""the context is accessed in the right order""") {
                verifyOrder {
                    context.getRequest(::DeleteDentalProcedureRequest)
                    context.fullUrl()
                    context.status(STATUS_200)
                    context.status()
                    context.json(Response("", "", 0, ""))
                }
            }
            And("""the service is accessed as well""") {
                verify(exactly = 1) {
                    service.handle(any<DeleteDentalProcedureCommand>())
                }
            }
        }
    }
})
