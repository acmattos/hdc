//package br.com.acmattos.hdc.scheduler.application
//
//import br.com.acmattos.hdc.common.context.domain.cqs.EventStore
//import br.com.acmattos.hdc.common.context.domain.model.Repository
//import br.com.acmattos.hdc.scheduler.domain.cqs.AppointmentEvent
//import br.com.acmattos.hdc.scheduler.domain.cqs.CreateAppointmentForTheScheduleCommand
//import br.com.acmattos.hdc.scheduler.domain.cqs.CreateAppointmentForTheScheduleEvent
//import br.com.acmattos.hdc.scheduler.domain.cqs.ScheduleEvent
//import br.com.acmattos.hdc.scheduler.domain.model.Appointment
//import br.com.acmattos.hdc.scheduler.domain.model.Dentist
//import br.com.acmattos.hdc.scheduler.domain.model.DentistBuilder
//import br.com.acmattos.hdc.scheduler.port.rest.CreateAScheduleForTheDentistRequest
//import br.com.acmattos.hdc.scheduler.port.rest.ScheduleRequestBuilder
//import io.mockk.Runs
//import io.mockk.every
//import io.mockk.just
//import io.mockk.mockk
//import io.mockk.verify
//import io.mockk.verifyOrder
//import java.util.Optional
//import org.assertj.core.api.Assertions
//import org.spekframework.spek2.Spek
//import org.spekframework.spek2.style.gherkin.Feature
//
//private const val EXCEPTION_MESSAGE_1 = "There is a schedule already defined for the given dentistId [12345678901234567890123456]!"
//private const val EXCEPTION_MESSAGE_2 = "There is no dentist defined for the given dentistId [12345678901234567890123456]!"
//
///**
// * @author ACMattos
// * @since 27/11/2021.
// */
//object AppointmentCommandHandlerServiceTest: Spek({
//    Feature("${AppointmentCommandHandlerService::class.java} usage") {
//        Scenario("handling ${CreateAppointmentForTheScheduleCommand::class.java} successfully") {
//            lateinit var eventStore: EventStore<AppointmentEvent>
//            lateinit var repository: Repository<Appointment>
//            lateinit var scheduleEventStore: EventStore<ScheduleEvent>
//            lateinit var service: AppointmentCommandHandlerService
//            lateinit var command: CreateAppointmentForTheScheduleCommand
//            lateinit var event: CreateAppointmentForTheScheduleEvent
//            Given("""a ${EventStore::class.java} mock""") {
//                eventStore = mockk()
//            }
//            And("""eventStore#findAllByField returns null""") {
//                every {
//                    eventStore.findAllByField(
//                        "event.schedule_id.id",// TODO TRACK THIS FIELD
//                        "12345678901234567890123456"
//                    )
//                } returns listOf()
//            }
//            And("""eventStore#addEvent just runs""") {
//                every { eventStore.addEvent(any<CreateAppointmentForTheScheduleEvent>()) } just Runs
//            }
//            And("""a ${Repository::class.java} mock""") {
//                repository = mockk()
//            }
//            And("""repository#save just runs""") {
//                every { repository.save(any()) } just Runs
//            }
//            And("""a ${EventStore::class.java} mock""") {
//                scheduleEventStore = mockk()
//            }
//            And("""scheduleEventStore#findTheDentist returns ${Dentist::class.java}""") {
//                every {
//                    scheduleEventStore.findTheDentist(any())
//                } returns Optional.of(DentistBuilder.build())
//            }
//            And("""a ${CreateAppointmentForTheScheduleCommand::class.java} generated from ${CreateAScheduleForTheDentistRequest::class.java}""") {
//                command = ScheduleRequestBuilder.buildCreateAScheduleForTheDentistRequest()
//                    .toType() as CreateAppointmentForTheScheduleCommand
//            }
//            And("""a ${AppointmentCommandHandlerService::class.java} successfully instantiated""") {
//                service = AppointmentCommandHandlerService(eventStore, repository, scheduleEventStore)
//            }
//            When("""#handle is executed""") {
//                event = service.handle(command) as CreateAppointmentForTheScheduleEvent
//            }
//            Then("""${CreateAppointmentForTheScheduleEvent::class.java} is not null""") {
//                Assertions.assertThat(event).isNotNull()
//            }
//            And("""the scheduleEventStore is accessed once""") {
//                verify(exactly = 1) {
//                    scheduleEventStore.findTheDentist(any())
//                }
//            }
//            And("""the repository is accessed once""") {
//                verify(exactly = 1) {
//                    repository.save(any())
//                }
//            }
//            And("""the event store is accessed in the right order""") {
//                verifyOrder {
//                    eventStore.findAllByField(
//                        "event.dentist.dentist_id.id",// TODO TRACK THIS FIELD
//                        command.dentistId.id
//                    )
//                    eventStore.addEvent(any<CreateAppointmentForTheScheduleEvent>())
//                }
//            }
//        }
//
////        Scenario("handling ${CreateAppointmentForTheScheduleCommand::class.java} for a already registered dentist's schedule ") {
////            lateinit var command: CreateAppointmentForTheScheduleCommand
////            lateinit var eventStore: EventStore<AppointmentEvent>
////            lateinit var repository: Repository<Appointment>
////            lateinit var scheduleEventStore: DentistRestService
////            lateinit var service: AppointmentCommandHandlerService
////            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
////            Given("""a ${EventStore::class.java} mock""") {
////                eventStore = mockk()
////            }
////            And("""eventStore#findAllByField returns an event""") {
////                every {
////                    eventStore.findAllByField(
////                        "event.dentist.dentist_id.id",// TODO TRACK THIS FIELD
////                        "12345678901234567890123456"
////                    )
////                } returns listOf(
////                    CreateAppointmentForTheScheduleEvent(
////                        (ScheduleRequestBuilder.buildCreateAScheduleForTheDentistRequest().toType()
////                            as CreateAppointmentForTheScheduleCommand).copy(dentist = DentistBuilder.build())
////                    )
////                )
////            }
////            And("""eventStore#addEvent just runs""") {
////                every { eventStore.addEvent(any<CreateAppointmentForTheScheduleEvent>()) } just Runs
////            }
////            And("""a ${Repository::class.java} mock""") {
////                repository = mockk()
////            }
////            And("""a ${CreateAppointmentForTheScheduleCommand::class.java} generated from ${CreateAScheduleForTheDentistRequest::class.java}""") {
////                command = ScheduleRequestBuilder.buildCreateAScheduleForTheDentistRequest()
////                    .toType() as CreateAppointmentForTheScheduleCommand
////            }
////            And("""a ${DentistRestService::class.java} mock""") {
////                scheduleEventStore = mockk()
////            }
////            And("""a ${AppointmentCommandHandlerService::class.java} successfully instantiated""") {
////                service = AppointmentCommandHandlerService(eventStore, repository, scheduleEventStore)
////            }
////            When("""#handle is executed""") {
////                assertion = Assertions.assertThatCode {
////                    service.handle(command) as CreateAppointmentForTheScheduleEvent
////                }
////            }
////            Then("""${AssertionFailedException::class.java} is raised with message""") {
////                assertion.hasSameClassAs(AssertionFailedException(EXCEPTION_MESSAGE_1, SCHEDULE_ALREADY_DEFINED.code))
////            }
////            And("""the message is $EXCEPTION_MESSAGE_1""") {
////                assertion.hasMessage(EXCEPTION_MESSAGE_1)
////            }
////            And("""exception has code ${SCHEDULE_ALREADY_DEFINED.code}""") {
////                assertion.hasFieldOrPropertyWithValue("code", SCHEDULE_ALREADY_DEFINED.code)
////            }
////            And("""the repository#save is not accessed""") {
////                verify(exactly = 0) {
////                    repository.save(any())
////                }
////            }
////            And("""the scheduleEventStore is not accessed""") {
////                verify(exactly = 0) {
////                    scheduleEventStore.findTheDentist(any())
////                }
////            }
////            And("""the event store#addEvent is not accessed""") {
////                verify(exactly = 0){
////                    eventStore.addEvent(any<CreateAppointmentForTheScheduleEvent>())
////                }
////            }
////            And("""the eventStore#findAllByField is accessed""") {
////                verify(exactly = 1) {
////                    eventStore.findAllByField(
////                        "event.dentist.dentist_id.id",// TODO TRACK THIS FIELD
////                        "12345678901234567890123456"
////                    )
////                }
////            }
////        }
////
////        Scenario("handling ${CreateAppointmentForTheScheduleCommand::class.java} for a non-existant dentist") {
////            lateinit var command: CreateAppointmentForTheScheduleCommand
////            lateinit var eventStore: EventStore<AppointmentEvent>
////            lateinit var repository: Repository<Appointment>
////            lateinit var scheduleEventStore: DentistRestService
////            lateinit var service: AppointmentCommandHandlerService
////            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
////            Given("""a ${EventStore::class.java} mock""") {
////                eventStore = mockk()
////            }
////            And("""eventStore#findAllByField returns null""") {
////                every {
////                    eventStore.findAllByField(
////                        "event.dentist.dentist_id.id",// TODO TRACK THIS FIELD
////                        "12345678901234567890123456"
////                    )
////                } returns listOf()
////            }
////            And("""a ${Repository::class.java} mock""") {
////                repository = mockk()
////            }
////            And("""a ${CreateAppointmentForTheScheduleCommand::class.java} generated from ${CreateAScheduleForTheDentistRequest::class.java}""") {
////                command = ScheduleRequestBuilder.buildCreateAScheduleForTheDentistRequest()
////                    .toType() as CreateAppointmentForTheScheduleCommand
////            }
////            And("""a ${DentistRestService::class.java} mock""") {
////                scheduleEventStore = mockk()
////            }
////            And("""scheduleEventStore#findTheDentist returns ${Dentist::class.java}""") {
////                every {
////                    scheduleEventStore.findTheDentist(any())
////                } returns Optional.empty<Dentist>()
////            }
////            And("""a ${AppointmentCommandHandlerService::class.java} successfully instantiated""") {
////                service = AppointmentCommandHandlerService(eventStore, repository, scheduleEventStore)
////            }
////            When("""#handle is executed""") {
////                assertion = Assertions.assertThatCode {
////                    service.handle(command) as CreateAppointmentForTheScheduleEvent
////                }
////            }
////            Then("""${AssertionFailedException::class.java} is raised with message""") {
////                assertion.hasSameClassAs(AssertionFailedException(EXCEPTION_MESSAGE_2, DENTIST_NOT_REGISTERED.code))
////            }
////            And("""the message is $EXCEPTION_MESSAGE_2""") {
////                assertion.hasMessage(EXCEPTION_MESSAGE_2)
////            }
////            And("""exception has code ${DENTIST_NOT_REGISTERED.code}""") {
////                assertion.hasFieldOrPropertyWithValue("code", DENTIST_NOT_REGISTERED.code)
////            }
////            And("""the repository#save is not accessed""") {
////                verify(exactly = 0) {
////                    repository.save(any())
////                }
////            }
////            And("""the scheduleEventStore is accessed once""") {
////                verify(exactly = 1) {
////                    scheduleEventStore.findTheDentist(any())
////                }
////            }
////            And("""the event store#addEvent is not accessed """) {
////                verify(exactly = 0){
////                    eventStore.addEvent(any<CreateAppointmentForTheScheduleEvent>())
////                }
////            }
////            And("""the eventStore#findAllByField is accessed""") {
////                verify(exactly = 1) {
////                    eventStore.findAllByField(
////                        "event.dentist.dentist_id.id",// TODO TRACK THIS FIELD
////                        "12345678901234567890123456"
////                    )
////                }
////            }
////        }
//    }
//})
