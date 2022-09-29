package br.com.acmattos.hdc.procedure.application

import br.com.acmattos.hdc.common.context.domain.cqs.EventStore
import br.com.acmattos.hdc.common.context.domain.model.Repository
import br.com.acmattos.hdc.common.tool.assertion.AssertionFailedException
import br.com.acmattos.hdc.common.tool.page.EqFilter
import br.com.acmattos.hdc.procedure.config.MessageTrackerCodeEnum.CODE_OUT_OF_RANGE
import br.com.acmattos.hdc.procedure.config.MessageTrackerCodeEnum.DESCRIPTION_INVALID_LENGTH
import br.com.acmattos.hdc.procedure.config.MessageTrackerCodeEnum.PROCEDURE_ALREADY_DEFINED
import br.com.acmattos.hdc.procedure.config.MessageTrackerCodeEnum.PROCEDURE_NOT_DEFINED
import br.com.acmattos.hdc.procedure.domain.cqs.CreateDentalProcedureCommand
import br.com.acmattos.hdc.procedure.domain.cqs.CreateDentalProcedureEvent
import br.com.acmattos.hdc.procedure.domain.cqs.DeleteDentalProcedureCommand
import br.com.acmattos.hdc.procedure.domain.cqs.DeleteDentalProcedureEvent
import br.com.acmattos.hdc.procedure.domain.cqs.ProcedureEvent
import br.com.acmattos.hdc.procedure.domain.cqs.UpdateDentalProcedureCommand
import br.com.acmattos.hdc.procedure.domain.cqs.UpdateDentalProcedureEvent
import br.com.acmattos.hdc.procedure.domain.model.Procedure
import br.com.acmattos.hdc.procedure.port.persistence.mongodb.DocumentIndexedField.EVENT_CODE
import br.com.acmattos.hdc.procedure.port.rest.CreateDentalProcedureRequest
import br.com.acmattos.hdc.procedure.port.rest.ProcedureRequestBuilder
import br.com.acmattos.hdc.procedure.port.rest.UpdateDentalProcedureRequest
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import org.assertj.core.api.AbstractThrowableAssert
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatCode
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

private const val EXCEPTION_MESSAGE_1 = "There is a dental procedure already defined for the given code [81000014]!"
private const val EXCEPTION_MESSAGE_2 = "Code is out of range (81000014-87000199)!"
private const val EXCEPTION_MESSAGE_3 = "Description length is out of range (3-120)!"
private const val EXCEPTION_MESSAGE_4 = "There is no dental procedure defined for the given id [12345678901234567890123456]!"

/**
 * @author ACMattos
 * @since 20/03/2022.
 */
object ProcedureCommandHandlerServiceTest: Spek({
    Feature("${ProcedureCommandHandlerService::class.java.simpleName} usage - creating a procedure flows") {
        Scenario("handling ${CreateDentalProcedureCommand::class.java.simpleName} successfully") {
            lateinit var command: CreateDentalProcedureCommand
            lateinit var eventStore: EventStore<ProcedureEvent>
            lateinit var repository: Repository<Procedure>
            lateinit var service: ProcedureCommandHandlerService
            lateinit var event: CreateDentalProcedureEvent
            Given("""a ${EventStore::class.java.simpleName} mock""") {
                eventStore = mockk()
            }
            And("""eventStore#findAllByFilter returns null""") {
                every {
                    eventStore.findAllByFilter(
                        EqFilter<String, Int>(
                            EVENT_CODE.fieldName,
                            81000014
                        )
                    )
                } returns listOf()
            }
            And("""eventStore#addEvent just runs""") {
                every { eventStore.addEvent(any<CreateDentalProcedureEvent>()) } just Runs
            }
            And("""a ${Repository::class.java.simpleName} mock""") {
                repository = mockk()
            }
            And("""repository#save just runs""") {
                every { repository.save(any()) } just Runs
            }
            And("""a ${CreateDentalProcedureCommand::class.java.simpleName} generated from ${CreateDentalProcedureRequest::class.java.simpleName}""") {
                command = ProcedureRequestBuilder.buildCreateDentalProcedureRequest()
                    .toType() as CreateDentalProcedureCommand
            }
            And("""a ${ProcedureCommandHandlerService::class.java.simpleName} successfully instantiated""") {
                service = ProcedureCommandHandlerService(
                    eventStore,
                    repository
                )
            }
            When("""#handle is executed""") {
                event = service.handle(command) as CreateDentalProcedureEvent
            }
            Then("""${CreateDentalProcedureEvent::class.java.simpleName} is not null""") {
                assertThat(event).isNotNull()
            }
            And("""the repository is accessed once""") {
                verify(exactly = 1) {
                    repository.save(any())
                }
            }
            And("""the event store is accessed in the right order""") {
                verifyOrder {
                    eventStore.findAllByFilter(
                        EqFilter<String, Int>(
                            EVENT_CODE.fieldName,
                            command.code
                        )
                    )
                    eventStore.addEvent(any<CreateDentalProcedureEvent>())
                }
            }
        }

        Scenario("handling ${CreateDentalProcedureCommand::class.java.simpleName} for a already registered procedure") {
            lateinit var command: CreateDentalProcedureCommand
            lateinit var eventStore: EventStore<ProcedureEvent>
            lateinit var repository: Repository<Procedure>
            lateinit var service: ProcedureCommandHandlerService
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            Given("""a ${EventStore::class.java.simpleName} mock""") {
                eventStore = mockk()
            }
            And("""eventStore#findAllByFilter returns an event""") {
                every {
                    eventStore.findAllByFilter(
                        EqFilter<String, Int>(
                            EVENT_CODE.fieldName,
                            81000014
                        )
                    )
                } returns listOf(
                    CreateDentalProcedureEvent(
                        (ProcedureRequestBuilder.buildCreateDentalProcedureRequest()).toType()
                            as CreateDentalProcedureCommand
                    )
                )
            }
            And("""eventStore#addEvent just runs""") {
                every { eventStore.addEvent(any<CreateDentalProcedureEvent>()) } just Runs
            }
            And("""a ${Repository::class.java.simpleName} mock""") {
                repository = mockk()
            }
            And("""a ${CreateDentalProcedureCommand::class.java.simpleName} generated from ${CreateDentalProcedureRequest::class.java.simpleName}""") {
                command = ProcedureRequestBuilder.buildCreateDentalProcedureRequest()
                    .toType() as CreateDentalProcedureCommand
            }
            And("""a ${ProcedureCommandHandlerService::class.java.simpleName} successfully instantiated""") {
                service = ProcedureCommandHandlerService(eventStore, repository)
            }
            When("""#handle is executed""") {
                assertion = assertThatCode {
                    service.handle(command) as CreateDentalProcedureEvent
                }
            }
            Then("""${AssertionFailedException::class.java.simpleName} is raised with message""") {
                assertion.hasSameClassAs(
                    AssertionFailedException(
                        EXCEPTION_MESSAGE_1,
                        PROCEDURE_ALREADY_DEFINED.code
                    )
                )
            }
            And("""the message is $EXCEPTION_MESSAGE_1""") {
                assertion.hasMessage(EXCEPTION_MESSAGE_1)
            }
            And("""exception has code ${PROCEDURE_ALREADY_DEFINED.code}""") {
                assertion.hasFieldOrPropertyWithValue("code", PROCEDURE_ALREADY_DEFINED.code)
            }
            And("""the repository#save is not accessed""") {
                verify(exactly = 0) {
                    repository.save(any())
                }
            }
            And("""the event store#addEvent is not accessed""") {
                verify(exactly = 0) {
                    eventStore.addEvent(any<CreateDentalProcedureEvent>())
                }
            }
            And("""the eventStore#findAllByFilter is accessed""") {
                verify(exactly = 1) {
                    eventStore.findAllByFilter(
                        EqFilter<String, Int>(
                            EVENT_CODE.fieldName,
                            81000014
                        )
                    )
                }
            }
        }

        Scenario("handling ${CreateDentalProcedureCommand::class.java.simpleName} for an invalid code") {
            lateinit var command: CreateDentalProcedureCommand
            lateinit var eventStore: EventStore<ProcedureEvent>
            lateinit var repository: Repository<Procedure>
            lateinit var service: ProcedureCommandHandlerService
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            Given("""a ${EventStore::class.java.simpleName} mock""") {
                eventStore = mockk()
            }
            And("""eventStore#findAllByFilter returns null""") {
                every {
                    eventStore.findAllByFilter(
                        EqFilter<String, Int>(
                            EVENT_CODE.fieldName,
                            8100001
                        )
                    )
                } returns listOf()
            }
            And("""a ${Repository::class.java.simpleName} mock""") {
                repository = mockk()
            }
            And("""a ${CreateDentalProcedureCommand::class.java.simpleName} generated from ${CreateDentalProcedureRequest::class.java.simpleName}""") {
                command = ProcedureRequestBuilder.buildCreateDentalProcedureRequestInvalidCode()
                    .toType() as CreateDentalProcedureCommand
            }
            And("""a ${ProcedureCommandHandlerService::class.java.simpleName} successfully instantiated""") {
                service = ProcedureCommandHandlerService(eventStore, repository)
            }
            When("""#handle is executed""") {
                assertion = assertThatCode {
                    service.handle(command) as CreateDentalProcedureEvent
                }
            }
            Then("""${AssertionFailedException::class.java.simpleName} is raised with message""") {
                assertion.hasSameClassAs(
                    AssertionFailedException(
                        EXCEPTION_MESSAGE_2,
                        CODE_OUT_OF_RANGE.code
                    )
                )
            }
            And("""the message is $EXCEPTION_MESSAGE_2""") {
                assertion.hasMessage(EXCEPTION_MESSAGE_2)
            }
            And("""exception has code ${CODE_OUT_OF_RANGE.code}""") {
                assertion.hasFieldOrPropertyWithValue("code", CODE_OUT_OF_RANGE.code)
            }
            And("""the repository#save is not accessed""") {
                verify(exactly = 0) {
                    repository.save(any())
                }
            }
            And("""the event store#addEvent is not accessed """) {
                verify(exactly = 0) {
                    eventStore.addEvent(any<CreateDentalProcedureEvent>())
                }
            }
            And("""the eventStore#findAllByFilter is accessed""") {
                verify(exactly = 1) {
                    eventStore.findAllByFilter(
                        EqFilter<String, Int>(
                            EVENT_CODE.fieldName,
                            8100001
                        )
                    )
                }
            }
        }

        Scenario("handling ${CreateDentalProcedureCommand::class.java.simpleName} for an invalid description") {
            lateinit var command: CreateDentalProcedureCommand
            lateinit var eventStore: EventStore<ProcedureEvent>
            lateinit var repository: Repository<Procedure>
            lateinit var service: ProcedureCommandHandlerService
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            Given("""a ${EventStore::class.java.simpleName} mock""") {
                eventStore = mockk()
            }
            And("""eventStore#findAllByFilter returns null""") {
                every {
                    eventStore.findAllByFilter(
                        EqFilter<String, Int>(
                            EVENT_CODE.fieldName,
                            81000014
                        )
                    )
                } returns listOf()
            }
            And("""a ${Repository::class.java.simpleName} mock""") {
                repository = mockk()
            }
            And("""a ${CreateDentalProcedureCommand::class.java.simpleName} generated from ${CreateDentalProcedureRequest::class.java.simpleName}""") {
                command =
                    ProcedureRequestBuilder.buildCreateDentalProcedureRequestInvalidDescription()
                        .toType() as CreateDentalProcedureCommand
            }
            And("""a ${ProcedureCommandHandlerService::class.java.simpleName} successfully instantiated""") {
                service = ProcedureCommandHandlerService(eventStore, repository)
            }
            When("""#handle is executed""") {
                assertion = assertThatCode {
                    service.handle(command) as CreateDentalProcedureEvent
                }
            }
            Then("""${AssertionFailedException::class.java.simpleName} is raised with message""") {
                assertion.hasSameClassAs(
                    AssertionFailedException(
                        EXCEPTION_MESSAGE_3,
                        DESCRIPTION_INVALID_LENGTH.code
                    )
                )
            }
            And("""the message is $EXCEPTION_MESSAGE_3""") {
                assertion.hasMessage(EXCEPTION_MESSAGE_3)
            }
            And("""exception has code ${DESCRIPTION_INVALID_LENGTH.code}""") {
                assertion.hasFieldOrPropertyWithValue("code", DESCRIPTION_INVALID_LENGTH.code)
            }
            And("""the repository#save is not accessed""") {
                verify(exactly = 0) {
                    repository.save(any())
                }
            }
            And("""the event store#addEvent is not accessed """) {
                verify(exactly = 0) {
                    eventStore.addEvent(any<CreateDentalProcedureEvent>())
                }
            }
            And("""the eventStore#findAllByFilter is accessed""") {
                verify(exactly = 1) {
                    eventStore.findAllByFilter(
                        EqFilter<String, Int>(
                            EVENT_CODE.fieldName,
                            81000014
                        )
                    )
                }
            }
        }
    }

        Feature("${ProcedureCommandHandlerService::class.java.simpleName} usage - updating a procedure flows") {
            Scenario("handling ${UpdateDentalProcedureCommand::class.java.simpleName} successfully") {
                lateinit var command: UpdateDentalProcedureCommand
                lateinit var eventStore: EventStore<ProcedureEvent>
                lateinit var repository: Repository<Procedure>
                lateinit var service: ProcedureCommandHandlerService
                lateinit var event: UpdateDentalProcedureEvent
                lateinit var storedEvent: CreateDentalProcedureEvent
                Given("""a ${EventStore::class.java.simpleName} mock""") {
                    eventStore = mockk()
                }
                And("""a ${CreateDentalProcedureEvent::class.java.simpleName} generated from ${CreateDentalProcedureRequest::class.java.simpleName}""") {
                    storedEvent = CreateDentalProcedureEvent(
                        ProcedureRequestBuilder.buildCreateDentalProcedureRequest()
                            .toType() as CreateDentalProcedureCommand
                    )
                }
                And("""eventStore#findAllByFilter returns an event""") {
                    every {
                        eventStore.findAllByFilter(
                            EqFilter<String, Int>(
                                EVENT_CODE.fieldName,
                                81000014
                            )
                        )
                    } returns listOf(storedEvent)
                }
                And("""eventStore#addEvent just runs""") {
                    every { eventStore.addEvent(any<UpdateDentalProcedureEvent>()) } just Runs
                }
                And("""a ${Repository::class.java.simpleName} mock""") {
                    repository = mockk()
                }
                And("""repository#update just runs""") {
                    every { repository.update(any(), any()) } just Runs
                }
                And("""a ${UpdateDentalProcedureCommand::class.java.simpleName} generated from ${CreateDentalProcedureRequest::class.java.simpleName}""") {
                    command = ProcedureRequestBuilder.buildUpdateDentalProcedureRequest()
                        .toType() as UpdateDentalProcedureCommand
                }
                And("""a ${ProcedureCommandHandlerService::class.java.simpleName} successfully instantiated""") {
                    service = ProcedureCommandHandlerService(
                        eventStore,
                        repository
                    )
                }
                When("""#handle is executed""") {
                    event = service.handle(command) as UpdateDentalProcedureEvent
                }
                Then("""${UpdateDentalProcedureEvent::class.java.simpleName} is not null""") {
                    assertThat(event).isNotNull()
                }
                And("""the repository is accessed once""") {
                    verify(exactly = 1) {
                        repository.update(any(), any())
                    }
                }
                And("""the event store is accessed in the right order""") {
                    verifyOrder {
                        eventStore.findAllByFilter(
                            EqFilter<String, Int>(
                                EVENT_CODE.fieldName,
                                command.code
                            )
                        )
                        eventStore.addEvent(any<UpdateDentalProcedureEvent>())
                    }
                }
            }

            Scenario("handling ${UpdateDentalProcedureCommand::class.java.simpleName} for non registered procedure") {
                lateinit var command: UpdateDentalProcedureCommand
                lateinit var eventStore: EventStore<ProcedureEvent>
                lateinit var repository: Repository<Procedure>
                lateinit var service: ProcedureCommandHandlerService
                lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
                Given("""a ${EventStore::class.java.simpleName} mock""") {
                    eventStore = mockk()
                }
                And("""eventStore#findAllByFilter returns empty list""") {
                    every {
                        eventStore.findAllByFilter(
                            EqFilter<String, Int>(
                                EVENT_CODE.fieldName,
                                81000014
                            )
                        )
                    } returns listOf()
                }
                And("""a ${Repository::class.java.simpleName} mock""") {
                    repository = mockk()
                }
                And("""a ${UpdateDentalProcedureCommand::class.java.simpleName} generated from ${UpdateDentalProcedureRequest::class.java.simpleName}""") {
                    command = ProcedureRequestBuilder.buildUpdateDentalProcedureRequest()
                        .toType() as UpdateDentalProcedureCommand
                }
                And("""a ${ProcedureCommandHandlerService::class.java.simpleName} successfully instantiated""") {
                    service = ProcedureCommandHandlerService(eventStore, repository)
                }
                When("""#handle is executed""") {
                    assertion = assertThatCode {
                        service.handle(command) as UpdateDentalProcedureEvent
                    }
                }
                Then("""${AssertionFailedException::class.java.simpleName} is raised with message""") {
                    assertion.hasSameClassAs(
                        AssertionFailedException(
                            EXCEPTION_MESSAGE_4,
                            PROCEDURE_NOT_DEFINED.code
                        )
                    )
                }
                And("""the message is $EXCEPTION_MESSAGE_4""") {
                    assertion.hasMessage(EXCEPTION_MESSAGE_4)
                }
                And("""exception has code ${PROCEDURE_NOT_DEFINED.code}""") {
                    assertion.hasFieldOrPropertyWithValue("code", PROCEDURE_NOT_DEFINED.code)
                }
                And("""the repository#update is not accessed""") {
                    verify(exactly = 0) {
                        repository.update(any(), any())
                    }
                }
                And("""the event store#addEvent is not accessed""") {
                    verify(exactly = 0) {
                        eventStore.addEvent(any<CreateDentalProcedureEvent>())
                    }
                }
                And("""the eventStore#findAllByFilter is accessed""") {
                    verify(exactly = 1) {
                        eventStore.findAllByFilter(
                            EqFilter<String, Int>(
                                EVENT_CODE.fieldName,
                                81000014
                            )
                        )
                    }
                }
            }
        }

        Feature("${ProcedureCommandHandlerService::class.java.simpleName} usage - deleting a procedure flows") {
            Scenario("handling ${DeleteDentalProcedureCommand::class.java.simpleName} successfully") {
                lateinit var command: DeleteDentalProcedureCommand
                lateinit var eventStore: EventStore<ProcedureEvent>
                lateinit var repository: Repository<Procedure>
                lateinit var service: ProcedureCommandHandlerService
                lateinit var event: DeleteDentalProcedureEvent
                lateinit var storedEvent: CreateDentalProcedureEvent
                Given("""a ${EventStore::class.java.simpleName} mock""") {
                    eventStore = mockk()
                }
                And("""a ${CreateDentalProcedureEvent::class.java.simpleName} generated from ${CreateDentalProcedureRequest::class.java.simpleName}""") {
                    storedEvent = CreateDentalProcedureEvent(
                        ProcedureRequestBuilder.buildCreateDentalProcedureRequest()
                            .toType() as CreateDentalProcedureCommand
                    )
                }
                And("""eventStore#findAllByFilter returns an event""") {
                    every {
                        eventStore.findAllByFilter(
                            any<EqFilter<String, String>>()
                        )
                    } returns listOf(storedEvent)
                }
                And("""eventStore#addEvent just runs""") {
                    every { eventStore.addEvent(any<DeleteDentalProcedureEvent>()) } just Runs
                }
                And("""a ${Repository::class.java.simpleName} mock""") {
                    repository = mockk()
                }
                And("""repository#delete just runs""") {
                    every { repository.delete(any()) } just Runs
                }
                And("""a ${DeleteDentalProcedureCommand::class.java.simpleName} generated from ${CreateDentalProcedureRequest::class.java.simpleName}""") {
                    command = ProcedureRequestBuilder.buildDeleteDentalProcedureRequest()
                        .toType() as DeleteDentalProcedureCommand
                }
                And("""a ${ProcedureCommandHandlerService::class.java.simpleName} successfully instantiated""") {
                    service = ProcedureCommandHandlerService(
                        eventStore,
                        repository
                    )
                }
                When("""#handle is executed""") {
                    event = service.handle(command) as DeleteDentalProcedureEvent
                }
                Then("""${DeleteDentalProcedureEvent::class.java.simpleName} is not null""") {
                    assertThat(event).isNotNull()
                }
                And("""the repository is accessed once""") {
                    verify(exactly = 1) {
                        repository.delete(any())
                    }
                }
                And("""the event store is accessed in the right order""") {
                    verifyOrder {
                        eventStore.findAllByFilter(
                            any<EqFilter<String, String>>()
                        )
                        eventStore.addEvent(any<UpdateDentalProcedureEvent>())
                    }
                }
            }
        }
})
