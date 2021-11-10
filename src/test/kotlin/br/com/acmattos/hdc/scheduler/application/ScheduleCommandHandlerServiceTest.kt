package br.com.acmattos.hdc.scheduler.application

import br.com.acmattos.hdc.common.context.domain.cqs.EventStore
import br.com.acmattos.hdc.common.context.domain.model.Repository
import br.com.acmattos.hdc.common.tool.assertion.AssertionFailedException
import br.com.acmattos.hdc.scheduler.config.ErrorTrackerCodeEnum.DENTIST_NOT_REGISTERED
import br.com.acmattos.hdc.scheduler.config.ErrorTrackerCodeEnum.SCHEDULE_ALREADY_DEFINED
import br.com.acmattos.hdc.scheduler.domain.cqs.CreateAScheduleForTheDentistCommand
import br.com.acmattos.hdc.scheduler.domain.cqs.CreateAScheduleForTheDentistEvent
import br.com.acmattos.hdc.scheduler.domain.cqs.ScheduleEvent
import br.com.acmattos.hdc.scheduler.domain.model.Dentist
import br.com.acmattos.hdc.scheduler.domain.model.DentistBuilder
import br.com.acmattos.hdc.scheduler.domain.model.Schedule
import br.com.acmattos.hdc.scheduler.port.rest.CreateAScheduleForTheDentistRequest
import br.com.acmattos.hdc.scheduler.port.rest.ScheduleRequestBuilder
import br.com.acmattos.hdc.scheduler.port.service.DentistRestService
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import java.util.Optional
import org.assertj.core.api.AbstractThrowableAssert
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatCode
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

private const val EXCEPTION_MESSAGE_1 = "There is a schedule already defined for the given dentistId [12345678901234567890123456]!"
private const val EXCEPTION_MESSAGE_2 = "There is no dentist defined for the given dentistId [12345678901234567890123456]!"

/**
 * @author ACMattos
 * @since 21/10/2021.
 */
object ScheduleCommandHandlerServiceTest: Spek({
    Feature("${ScheduleCommandHandlerService::class.java} usage") {
        Scenario("handling ${CreateAScheduleForTheDentistCommand::class.java} successfully") {
            lateinit var command: CreateAScheduleForTheDentistCommand
            lateinit var eventStore: EventStore<ScheduleEvent>
            lateinit var repository: Repository<Schedule>
            lateinit var dentistService: DentistRestService
            lateinit var service: ScheduleCommandHandlerService
            lateinit var event: CreateAScheduleForTheDentistEvent
            Given("""a ${EventStore::class.java} mock""") {
                eventStore = mockk()
            }
            And("""eventStore#findAllByField returns null""") {
                every {
                    eventStore.findAllByField(
                        "event.dentist.dentist_id.id",// TODO TRACK THIS FIELD
                        "12345678901234567890123456"
                    )
                } returns listOf()
            }
            And("""eventStore#addEvent just runs""") {
                every { eventStore.addEvent(any<CreateAScheduleForTheDentistEvent>()) } just Runs
            }
            And("""a ${Repository::class.java} mock""") {
                repository = mockk()
            }
            And("""repository#save just runs""") {
                every { repository.save(any()) } just Runs
            }
            And("""a ${DentistRestService::class.java} mock""") {
                dentistService = mockk()
            }
            And("""dentistService#findTheDentist returns ${Dentist::class.java}""") {
                every {
                    dentistService.findTheDentist(any())
                } returns Optional.of(DentistBuilder.build())
            }
            And("""a ${CreateAScheduleForTheDentistCommand::class.java} generated from ${CreateAScheduleForTheDentistRequest::class.java}""") {
                command = ScheduleRequestBuilder.buildCreateAScheduleForTheDentistRequest()
                    .toType() as CreateAScheduleForTheDentistCommand
            }
            And("""a ${ScheduleCommandHandlerService::class.java} successfully instantiated""") {
                service = ScheduleCommandHandlerService(eventStore, repository, dentistService)
            }
            When("""#handle is executed""") {
                event = service.handle(command) as CreateAScheduleForTheDentistEvent
            }
            Then("""${CreateAScheduleForTheDentistEvent::class.java} is not null""") {
                assertThat(event).isNotNull()
            }
            And("""the dentistService is accessed once""") {
                verify(exactly = 1) {
                    dentistService.findTheDentist(any())
                }
            }
            And("""the repository is accessed once""") {
                verify(exactly = 1) {
                    repository.save(any())
                }
            }
            And("""the event store is accessed in the right order""") {
                verifyOrder {
                    eventStore.findAllByField(
                        "event.dentist.dentist_id.id",// TODO TRACK THIS FIELD
                        command.dentistId.id
                    )
                    eventStore.addEvent(any<CreateAScheduleForTheDentistEvent>())
                }
            }
        }

        Scenario("handling ${CreateAScheduleForTheDentistCommand::class.java} for a already registered dentist's schedule ") {
            lateinit var command: CreateAScheduleForTheDentistCommand
            lateinit var eventStore: EventStore<ScheduleEvent>
            lateinit var repository: Repository<Schedule>
            lateinit var dentistService: DentistRestService
            lateinit var service: ScheduleCommandHandlerService
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            Given("""a ${EventStore::class.java} mock""") {
                eventStore = mockk()
            }
            And("""eventStore#findAllByField returns an event""") {
                every {
                    eventStore.findAllByField(
                        "event.dentist.dentist_id.id",// TODO TRACK THIS FIELD
                        "12345678901234567890123456"
                    )
                } returns listOf(
                    CreateAScheduleForTheDentistEvent(
                        (ScheduleRequestBuilder.buildCreateAScheduleForTheDentistRequest().toType()
                            as CreateAScheduleForTheDentistCommand).copy(dentist = DentistBuilder.build())
                    )
                )
            }
            And("""eventStore#addEvent just runs""") {
                every { eventStore.addEvent(any<CreateAScheduleForTheDentistEvent>()) } just Runs
            }
            And("""a ${Repository::class.java} mock""") {
                repository = mockk()
            }
            And("""a ${CreateAScheduleForTheDentistCommand::class.java} generated from ${CreateAScheduleForTheDentistRequest::class.java}""") {
                command = ScheduleRequestBuilder.buildCreateAScheduleForTheDentistRequest()
                    .toType() as CreateAScheduleForTheDentistCommand
            }
            And("""a ${DentistRestService::class.java} mock""") {
                dentistService = mockk()
            }
            And("""a ${ScheduleCommandHandlerService::class.java} successfully instantiated""") {
                service = ScheduleCommandHandlerService(eventStore, repository, dentistService)
            }
            When("""#handle is executed""") {
                assertion = assertThatCode {
                    service.handle(command) as CreateAScheduleForTheDentistEvent
                }
            }
            Then("""${AssertionFailedException::class.java} is raised with message""") {
                assertion.hasSameClassAs(AssertionFailedException(EXCEPTION_MESSAGE_1, SCHEDULE_ALREADY_DEFINED.code))
            }
            And("""the message is $EXCEPTION_MESSAGE_1""") {
                assertion.hasMessage(EXCEPTION_MESSAGE_1)
            }
            And("""exception has code ${SCHEDULE_ALREADY_DEFINED.code}""") {
                assertion.hasFieldOrPropertyWithValue("code", SCHEDULE_ALREADY_DEFINED.code)
            }
            And("""the repository#save is not accessed""") {
                verify(exactly = 0) {
                    repository.save(any())
                }
            }
            And("""the dentistService is not accessed""") {
                verify(exactly = 0) {
                    dentistService.findTheDentist(any())
                }
            }
            And("""the event store#addEvent is not accessed""") {
                verify(exactly = 0){
                    eventStore.addEvent(any<CreateAScheduleForTheDentistEvent>())
                }
            }
            And("""the eventStore#findAllByField is accessed""") {
                verify(exactly = 1) {
                    eventStore.findAllByField(
                        "event.dentist.dentist_id.id",// TODO TRACK THIS FIELD
                        "12345678901234567890123456"
                    )
                }
            }
        }




        Scenario("handling ${CreateAScheduleForTheDentistCommand::class.java} for a non-existant dentist") {
            lateinit var command: CreateAScheduleForTheDentistCommand
            lateinit var eventStore: EventStore<ScheduleEvent>
            lateinit var repository: Repository<Schedule>
            lateinit var dentistService: DentistRestService
            lateinit var service: ScheduleCommandHandlerService
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            Given("""a ${EventStore::class.java} mock""") {
                eventStore = mockk()
            }
            And("""eventStore#findAllByField returns null""") {
                every {
                    eventStore.findAllByField(
                        "event.dentist.dentist_id.id",// TODO TRACK THIS FIELD
                        "12345678901234567890123456"
                    )
                } returns listOf()
            }
            And("""a ${Repository::class.java} mock""") {
                repository = mockk()
            }
            And("""a ${CreateAScheduleForTheDentistCommand::class.java} generated from ${CreateAScheduleForTheDentistRequest::class.java}""") {
                command = ScheduleRequestBuilder.buildCreateAScheduleForTheDentistRequest()
                    .toType() as CreateAScheduleForTheDentistCommand
            }
            And("""a ${DentistRestService::class.java} mock""") {
                dentistService = mockk()
            }
            And("""dentistService#findTheDentist returns ${Dentist::class.java}""") {
                every {
                    dentistService.findTheDentist(any())
                } returns Optional.empty<Dentist>()
            }
            And("""a ${ScheduleCommandHandlerService::class.java} successfully instantiated""") {
                service = ScheduleCommandHandlerService(eventStore, repository, dentistService)
            }
            When("""#handle is executed""") {
                assertion = assertThatCode {
                    service.handle(command) as CreateAScheduleForTheDentistEvent
                }
            }
            Then("""${AssertionFailedException::class.java} is raised with message""") {
                assertion.hasSameClassAs(AssertionFailedException(EXCEPTION_MESSAGE_2, DENTIST_NOT_REGISTERED.code))
            }
            And("""the message is $EXCEPTION_MESSAGE_2""") {
                assertion.hasMessage(EXCEPTION_MESSAGE_2)
            }
            And("""exception has code ${DENTIST_NOT_REGISTERED.code}""") {
                assertion.hasFieldOrPropertyWithValue("code", DENTIST_NOT_REGISTERED.code)
            }
            And("""the repository#save is not accessed""") {
                verify(exactly = 0) {
                    repository.save(any())
                }
            }
            And("""the dentistService is accessed once""") {
                verify(exactly = 1) {
                    dentistService.findTheDentist(any())
                }
            }
            And("""the event store#addEvent is not accessed """) {
                verify(exactly = 0){
                    eventStore.addEvent(any<CreateAScheduleForTheDentistEvent>())
                }
            }
            And("""the eventStore#findAllByField is accessed""") {
                verify(exactly = 1) {
                    eventStore.findAllByField(
                        "event.dentist.dentist_id.id",// TODO TRACK THIS FIELD
                        "12345678901234567890123456"
                    )
                }
            }
        }
    }
})
