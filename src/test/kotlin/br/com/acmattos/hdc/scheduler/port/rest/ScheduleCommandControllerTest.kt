package br.com.acmattos.hdc.scheduler.port.rest

import br.com.acmattos.hdc.common.tool.server.javalin.Response
import br.com.acmattos.hdc.person.application.PersonCommandHandlerService
import br.com.acmattos.hdc.person.domain.cqs.CreateADentistCommand
import br.com.acmattos.hdc.scheduler.application.ScheduleCommandApplicationService
import br.com.acmattos.hdc.scheduler.domain.cqs.CreateAScheduleForTheDentistCommand
import br.com.acmattos.hdc.scheduler.domain.cqs.CreateAScheduleForTheDentistEvent
import br.com.acmattos.hdc.scheduler.domain.model.DentistBuilder
import io.javalin.http.Context
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

const val STATUS = 201

/**
 * @author ACMattos
 * @since 29/09/2021.
 */
object ScheduleCommandControllerTest: Spek({
    Feature("${ScheduleCommandController::class.java} usage") {
        Scenario("handling ${CreateADentistCommand::class.java} successfully") {
            lateinit var request: CreateAScheduleForTheDentistRequest
            lateinit var command: CreateAScheduleForTheDentistCommand
            lateinit var event: CreateAScheduleForTheDentistEvent
            lateinit var service: ScheduleCommandApplicationService
            lateinit var context: Context
            lateinit var controller: ScheduleCommandController

            Given("""a ${CreateAScheduleForTheDentistRequest::class.java} successfully instantiated""") {
                request = ScheduleRequestBuilder.buildCreateAScheduleForTheDentistRequest()
            }
            And("""a ${CreateAScheduleForTheDentistCommand::class.java} successfully generated""") {
                command = (request.toType() as CreateAScheduleForTheDentistCommand).copy(dentist = DentistBuilder.build())
            }
            And("""a ${CreateAScheduleForTheDentistEvent::class.java} successfully instantiated""") {
                event = CreateAScheduleForTheDentistEvent(command)
            }
            And("""a ${ScheduleCommandApplicationService::class.java} mock""") {
                service = mockk()
            }
            And("""service#handle returning the ${CreateADentistCommand::class.java}""") {
                every { service.handle(any<CreateADentistCommand>()) } returns event
            }
            And("""a ${Context::class.java} mock""") {
                context = mockk()
            }
            And("""context#bodyValidator#get returns ${CreateAScheduleForTheDentistRequest::class.java}""") {
                every { context.bodyValidator<CreateAScheduleForTheDentistRequest>().get() } returns request
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
                controller = ScheduleCommandController(service)
            }
            When("""#createADentist is executed""") {
                controller.createAScheduleForTheDentist(context)
            }
            Then("""status is $STATUS""") {
                assertThat(context.status()).isEqualTo(STATUS)
            }
            And("""the context is accessed in the right order""") {
                verifyOrder {
                    context.bodyValidator<CreateAScheduleForTheDentistRequest>().get()
                    context.fullUrl()
                    context.status(STATUS)
                    context.status()
                    context.json(any<Response>())
                }
            }
            And("""the service is accessed as well""") {
                verify(exactly = 1) {
                    service.handle(any<CreateAScheduleForTheDentistCommand>())
                }
            }
        }
    }
})
