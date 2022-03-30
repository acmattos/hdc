package br.com.acmattos.hdc.procedure.port.rest

import br.com.acmattos.hdc.common.tool.server.javalin.Response
import br.com.acmattos.hdc.person.application.PersonCommandHandlerService
import br.com.acmattos.hdc.person.domain.cqs.CreateADentistCommand
import br.com.acmattos.hdc.procedure.application.ProcedureCommandHandlerService
import br.com.acmattos.hdc.procedure.domain.cqs.CreateDentalProcedureCommand
import br.com.acmattos.hdc.procedure.domain.cqs.CreateDentalProcedureEvent
import io.javalin.http.Context
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import org.assertj.core.api.Assertions
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

private const val STATUS = 201

/**
 * @author ACMattos
 * @since 19/03/2022.
 */
object ProcedureCommandControllerTest: Spek({
    Feature("${ProcedureCommandController::class.java} usage") {
        Scenario("handling ${CreateDentalProcedureCommand::class.java} successfully") {
            lateinit var request: CreateDentalProcedureRequest
            lateinit var command: CreateDentalProcedureCommand
            lateinit var event: CreateDentalProcedureEvent
            lateinit var service: ProcedureCommandHandlerService
            lateinit var context: Context
            lateinit var controller: ProcedureCommandController

            Given("""a ${CreateDentalProcedureRequest::class.java} successfully instantiated""") {
                request = ProcedureRequestBuilder.buildCreateDentalProcedureRequest()
            }
            And("""a ${CreateDentalProcedureCommand::class.java} successfully generated""") {
                command = request.toType() as CreateDentalProcedureCommand
            }
            And("""a ${CreateDentalProcedureEvent::class.java} successfully instantiated""") {
                event = CreateDentalProcedureEvent(command)
            }
            And("""a ${ProcedureCommandHandlerService::class.java} mock""") {
                service = mockk()
            }
            And("""service#handle returning the ${CreateADentistCommand::class.java}""") {
                every { service.handle(any<CreateADentistCommand>()) } returns event
            }
            And("""a ${Context::class.java} mock""") {
                context = mockk()
            }
            And("""context#bodyValidator#get returns ${CreateDentalProcedureRequest::class.java}""") {
                every { context.bodyValidator<CreateDentalProcedureRequest>().get() } returns request
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
                every { context.json(any<Response>()) } returns context
            }
            And("""a ${PersonCommandHandlerService::class.java} successfully instantiated""") {
                controller = ProcedureCommandController(service)
            }
            When("""#createADentist is executed""") {
                controller.createDentalProcedure(context)
            }
            Then("""status is $STATUS""") {
                Assertions.assertThat(context.status()).isEqualTo(STATUS)
            }
            And("""the context is accessed in the right order""") {
                verifyOrder {
                    context.bodyValidator<CreateDentalProcedureRequest>().get()
                    context.fullUrl()
                    context.status(STATUS)
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
    }
})
