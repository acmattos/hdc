//package br.com.acmattos.hdc.scheduler.port.service
//
//import br.com.acmattos.hdc.common.tool.http.isEmpty
//import br.com.acmattos.hdc.common.tool.http.toId
//import br.com.acmattos.hdc.common.tool.http.toResult
//import br.com.acmattos.hdc.common.tool.server.javalin.Response
//import br.com.acmattos.hdc.common.tool.server.javalin.getRequest
//import br.com.acmattos.hdc.common.tool.server.mapper.JacksonObjectMapperFactory
//import br.com.acmattos.hdc.person.application.PersonCommandHandlerService
//import br.com.acmattos.hdc.person.domain.cqs.CreateADentistCommand
//import br.com.acmattos.hdc.scheduler.application.AppointmentCommandHandlerService
//import br.com.acmattos.hdc.scheduler.domain.cqs.CreateAppointmentsForTheScheduleCommand
//import br.com.acmattos.hdc.scheduler.domain.cqs.CreateAppointmentsForTheScheduleEvent
//import br.com.acmattos.hdc.scheduler.domain.model.DentistId
//import br.com.acmattos.hdc.scheduler.port.rest.AppointmentCommandController
//import br.com.acmattos.hdc.scheduler.port.rest.AppointmentRequestBuilder
//import br.com.acmattos.hdc.scheduler.port.rest.CreateAScheduleForTheDentistRequest
//import br.com.acmattos.hdc.scheduler.port.rest.CreateAppointmentsForTheScheduleRequestBuilder
//import br.com.acmattos.hdc.scheduler.port.rest.STATUS
//import io.javalin.http.Context
//import io.javalin.plugin.json.JavalinJackson
//import io.mockk.every
//import io.mockk.mockk
//import io.mockk.verify
//import io.mockk.verifyOrder
//import org.assertj.core.api.Assertions
//import org.spekframework.spek2.Spek
//import org.spekframework.spek2.style.gherkin.Feature
//
///**
// * @author ACMattos
// * @since 30/11/2021.
// */
//object DentistRestServiceTest: Spek({
////    afterEachTest { // Avoids error when all tests are executed at the same time
////        JavalinJackson.configure(JacksonObjectMapperFactory.build())
////    }
//    Feature("${DentistRestService::class.java} usage") {
//        Scenario("handling ${CreateADentistCommand::class.java} successfully") {
//            lateinit var request: CreateAppointmentsForTheScheduleRequestBuilder
//            lateinit var command: CreateAppointmentsForTheScheduleCommand
//            lateinit var event: CreateAppointmentsForTheScheduleEvent
//
//            lateinit var dentistId: DentistId
//            lateinit var response: Response?// = null
//            lateinit var service: PersonRestApiService
//
//            Given("""a ${PersonRestApiService::class.java} mock""") {
//                dentistId = DentistId("12345678901234567890123456")
//                response = mockk(relaxed = true)
//                every { response!!.isEmpty() } returns false
//                every { response!!.toId("person_id") } returns dentistId
//                every { response!!.toResult()["full_name"] } returns "Full Name"
//
//                service = mockk(relaxed = true)
//                every { service.findTheDentist(dentistId.id).execute().body() } returns event
//
//                val response: Response? = personRestApiService
//                    .findTheDentist(dentistId.id).execute().body()
//                if (response != null && !response.isEmpty()) {
//            }
//            And("""a ${CreateAppointmentsForTheScheduleCommand::class.java} successfully generated""") {
//                command = (request.toType() as CreateAppointmentsForTheScheduleCommand)
//            }
//            And("""a ${CreateAppointmentsForTheScheduleEvent::class.java} successfully instantiated""") {
//                event = CreateAppointmentsForTheScheduleEvent(command, listOf())
//            }
//            And("""a ${AppointmentCommandHandlerService::class.java} mock""") {
//                service = mockk()
//            }
//            And("""service#handle returning the ${CreateADentistCommand::class.java}""") {
//                every { service.handle(any<CreateADentistCommand>()) } returns event
//            }
//            And("""a ${Context::class.java} mock""") {
//                context = AppointmentRequestBuilder.getContext()
//            }
//            And("""${JavalinJackson::class.java} is properly configured""") {
//                JavalinJackson.configure(JacksonObjectMapperFactory.build())
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
//                every { context.json(Response("", "", 0, "")) } returns context
//            }
//            And("""a ${PersonCommandHandlerService::class.java} successfully instantiated""") {
//                controller = AppointmentCommandController(service)
//            }
//            When("""#createADentist is executed""") {
//                controller.createAppointmentsForTheSchedule(context)
//            }
//            Then("""status is $STATUS""") {
//                Assertions.assertThat(context.status()).isEqualTo(STATUS)
//            }
//            And("""the context is accessed in the right order""") {
//                verifyOrder {
//                    context.getRequest(br.com.acmattos.hdc.scheduler.port.rest::CreateAppointmentsForTheScheduleRequestBuilder)
//                    context.fullUrl()
//                    context.status(STATUS)
//                    context.status()
//                    context.json(Response("", "", 0, ""))
//                }
//            }
//            And("""the service is accessed as well""") {
//                verify(exactly = 1) {
//                    service.handle(any<CreateAppointmentsForTheScheduleCommand>())
//                }
//            }
//        }
//    }
//})
