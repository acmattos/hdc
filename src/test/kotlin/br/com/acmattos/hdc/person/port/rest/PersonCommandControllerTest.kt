//package br.com.acmattos.hdc.person.port.rest
//
//import br.com.acmattos.hdc.common.tool.server.javalin.Response
//import br.com.acmattos.hdc.person.application.PersonCommandHandlerService
//import br.com.acmattos.hdc.person.domain.cqs.CreateADentistCommand
//import br.com.acmattos.hdc.person.domain.cqs.CreateADentistEvent
//import io.javalin.http.Context
//import io.mockk.every
//import io.mockk.mockk
//import io.mockk.verify
//import io.mockk.verifyOrder
//import org.assertj.core.api.Assertions.assertThat
//import org.spekframework.spek2.Spek
//import org.spekframework.spek2.style.gherkin.Feature
//
//private const val STATUS = 201
//
///**
// * @author ACMattos
// * @since 16/10/2021.
// */
//object PersonCommandControllerTest: Spek({
//    Feature("${PersonCommandController::class.java} usage") {
//        Scenario("handling ${CreateADentistCommand::class.java} successfully") {
//            lateinit var request: CreateADentistRequest
//            lateinit var command: CreateADentistCommand
//            lateinit var event: CreateADentistEvent
//            lateinit var service: PersonCommandHandlerService
//            lateinit var context: Context
//            lateinit var controller: PersonCommandController
//            Given("""a ${CreateADentistRequest::class.java} successfully instantiated""") {
//                request = PersonRequestBuilder.buildCreateADentistRequest()
//            }
//            And("""a ${CreateADentistCommand::class.java} successfully generated""") {
//                command = request.toType() as CreateADentistCommand
//            }
//            And("""a ${CreateADentistEvent::class.java} successfully instantiated""") {
//                event = CreateADentistEvent(command)
//            }
//            And("""a ${PersonCommandHandlerService::class.java} mock""") {
//                service = mockk()
//            }
//            And("""service#handle returning the ${CreateADentistCommand::class.java}""") {
//                every { service.handle(any<CreateADentistCommand>()) } returns event
//            }
//            And("""a ${Context::class.java} mock""") {
//                context = mockk()
//            }
//            And("""context#bodyValidator#get returns ${CreateADentistRequest::class.java}""") {
//                every { context.bodyValidator<CreateADentistRequest>().get() } returns request
//            }
//            And("""context#fullUrl returns fullUrl""") {
//                every { context.fullUrl() } returns "fullUrl"
//            }
//            And("""context#status returns ${Context::class.java}""") {
//                every { context.status(STATUS) } returns context
//            }
//            And("""context#status returns $STATUS""") {
//                every { context.status() } returns STATUS
//            }
//            And("""context#json returns ${Context::class.java}""") {
//                every { context.json(any<Response>()) } returns context
//            }
//            And("""a ${PersonCommandController::class.java} successfully instantiated""") {
//                controller = PersonCommandController(service)
//            }
//            When("""#createADentist is executed""") {
//                controller.createADentist(context)
//            }
//            Then("""status is $STATUS""") {
//                assertThat(context.status()).isEqualTo(STATUS)
//            }
//            And("""the context is accessed in the right order""") {
//                verifyOrder {
//                    context.bodyValidator<CreateADentistRequest>().get()
//                    context.fullUrl()
//                    context.status(STATUS)
//                    context.status()
//                    context.json(any<Response>())
//                }
//            }
//            And("""the service store is accessed as well""") {
//                verify(exactly = 1) {
//                    service.handle(any<CreateADentistCommand>())
//                }
//            }
//        }
//    }
//})
