package br.com.acmattos.hdc.procedure.application

import br.com.acmattos.hdc.common.context.domain.cqs.EventStore
import br.com.acmattos.hdc.common.context.domain.model.Repository
import br.com.acmattos.hdc.common.tool.assertion.AssertionFailedException
import br.com.acmattos.hdc.common.tool.page.EqFilter
import br.com.acmattos.hdc.procedure.config.MessageTrackerIdEnum.*
import br.com.acmattos.hdc.procedure.domain.cqs.*
import br.com.acmattos.hdc.procedure.domain.model.CommandBuilder
import br.com.acmattos.hdc.procedure.domain.model.EventBuilder
import br.com.acmattos.hdc.procedure.domain.model.Procedure
import br.com.acmattos.hdc.procedure.domain.model.ProcedureAttributes
import br.com.acmattos.hdc.procedure.port.persistence.mongodb.DocumentIndexedField.EVENT_CODE
import br.com.acmattos.hdc.procedure.port.rest.ProcedureCreateRequest
import br.com.acmattos.hdc.procedure.port.rest.ProcedureUpdateRequest
import io.kotest.core.spec.style.FreeSpec
import io.mockk.*
import org.assertj.core.api.AbstractThrowableAssert
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatCode

private const val EXCEPTION_MESSAGE_1 = "There is a procedure already defined for the given code [81000015]!"
private const val EXCEPTION_MESSAGE_2 = "There is no procedure defined for the given id [01FK96GENJKTN1BYZW6BRHFZFJ]!"
private const val EXCEPTION_MESSAGE_3 = "Description length is out of range (3-120)!"
private const val EXCEPTION_MESSAGE_4 = "There is no procedure defined for the given id [01GQNYQ7TY57RMXBZDKHVT7ZGJ]!"

/**
 * @author ACMattos
 * @since 20/03/2022.
 */
class ProcedureCommandHandlerServiceTest: FreeSpec({
    "Feature: ${ProcedureCommandHandlerService::class.java.simpleName} usage - creating a procedure flows" - {
        "Scenario: handling ProcedureCreateCommand successfully" - {
            lateinit var command: ProcedureCreateCommand
            lateinit var eventStore: EventStore<ProcedureEvent>
            lateinit var repository: Repository<Procedure>
            lateinit var service: ProcedureCommandHandlerService
            lateinit var event: ProcedureCreateEvent
            "Given: a ${EventStore::class.java.simpleName} mock" {
                eventStore = mockk()
            }
            "And: eventStore#findAllByFilter returns empty list" {
                every {
                    eventStore.findAllByFilter(
                        EqFilter<String, Int>(
                            EVENT_CODE.fieldName,
                            ProcedureAttributes.VCODE
                        )
                    )
                } returns listOf()
            }
            "And: eventStore#addEvent just runs" {
                every { eventStore.addEvent(any<ProcedureCreateEvent>()) } just Runs
            }
            "And: a ${Repository::class.java.simpleName} mock" {
                repository = mockk()
            }
            "And: repository#save just runs" {
                every { repository.save(any()) } just Runs
            }
            "And: a ProcedureCreateCommand generated" {
                command = CommandBuilder.buildCreateCommand()
            }
            "And: a ${ProcedureCommandHandlerService::class.java.simpleName} successfully instantiated" {
                service = ProcedureCommandHandlerService(eventStore, repository)
            }
            "When: #handle is executed" {
                event = service.handle(command) as ProcedureCreateEvent
            }
            "Then: ${ProcedureCreateEvent::class.java.simpleName} matches event´s class" {
                assertThat(event::class).hasSameClassAs(ProcedureCreateEvent::class)
            }
            "And: the event store is accessed in the right order" {
                verifyOrder {
                    eventStore.findAllByFilter(
                        EqFilter<String, Int>(
                            EVENT_CODE.fieldName,
                            command.code
                        )
                    )
                    eventStore.addEvent(any<ProcedureCreateEvent>())
                }
            }
            "And: the repository is accessed once" {
                verify(exactly = 1) {
                    repository.save(any())
                }
            }
        }

        "Scenario: handling ProcedureCreateCommand for a already registered procedure" - {
            lateinit var command: ProcedureCreateCommand
            lateinit var eventStore: EventStore<ProcedureEvent>
            lateinit var repository: Repository<Procedure>
            lateinit var service: ProcedureCommandHandlerService
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            "Given: a ${EventStore::class.java.simpleName} mock" {
                eventStore = mockk()
            }
            "And: eventStore#findAllByFilter returns an event" {
                every {
                    eventStore.findAllByFilter(
                        EqFilter<String, Int>(
                            EVENT_CODE.fieldName,
                            ProcedureAttributes.VCODE
                        )
                    )
                } returns listOf(EventBuilder.buildCreateEvent())
            }
            "And: eventStore#addEvent just runs" {
                every { eventStore.addEvent(any<ProcedureCreateEvent>()) } just Runs
            }
            "And: a ${Repository::class.java.simpleName} mock" {
                repository = mockk()
            }
            "And: a ProcedureCreateCommand generated" {
                command = CommandBuilder.buildCreateCommand()
            }
            "And: a ${ProcedureCommandHandlerService::class.java.simpleName} successfully instantiated" {
                service = ProcedureCommandHandlerService(eventStore, repository)
            }
            "When: #handle is executed" {
                assertion = assertThatCode {
                    service.handle(command) as ProcedureCreateEvent
                }
            }
            "Then: ${AssertionFailedException::class.java.simpleName} is raised with message" {
                assertion.hasSameClassAs(
                    AssertionFailedException(
                        EXCEPTION_MESSAGE_1,
                        PROCEDURE_ALREADY_DEFINED.messageTrackerId
                    )
                )
            }
            "And: the message is $EXCEPTION_MESSAGE_1" {
                assertion.hasMessage(EXCEPTION_MESSAGE_1)
            }
            "And: exception has messageTrackerId ${PROCEDURE_ALREADY_DEFINED.messageTrackerId}" {
                assertion.hasFieldOrPropertyWithValue("code", PROCEDURE_ALREADY_DEFINED.messageTrackerId)
            }
            "And: the eventStore#findAllByFilter is accessed" {
                verify(exactly = 1) {
                    eventStore.findAllByFilter(
                        EqFilter<String, Int>(
                            EVENT_CODE.fieldName,
                            ProcedureAttributes.VCODE
                        )
                    )
                }
            }
            "And: the repository#save is not accessed" {
                verify(exactly = 0) {
                    repository.save(any())
                }
            }
            "And: the event store#addEvent is not accessed" {
                verify(exactly = 0) {
                    eventStore.addEvent(any<ProcedureCreateEvent>())
                }
            }
        }

        "Scenario: handling ProcedureCreateCommand for an invalid description" - {
            lateinit var command: ProcedureCreateCommand
            lateinit var eventStore: EventStore<ProcedureEvent>
            lateinit var repository: Repository<Procedure>
            lateinit var service: ProcedureCommandHandlerService
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            "Given: a ${EventStore::class.java.simpleName} mock" {
                eventStore = mockk()
            }
            "And: eventStore#findAllByFilter returns empty list" {
                every {
                    eventStore.findAllByFilter(
                        EqFilter<String, Int>(
                            EVENT_CODE.fieldName,
                            ProcedureAttributes.VCODE
                        )
                    )
                } returns listOf()
            }
            "And: a ${Repository::class.java.simpleName} mock" {
                repository = mockk()
            }
            "And: a ProcedureCreateCommand generated" {
                command =CommandBuilder.buildInvalidCreateCommand(code = ProcedureAttributes.VCODE)
            }
            "And: a ${ProcedureCommandHandlerService::class.java.simpleName} successfully instantiated" {
                service = ProcedureCommandHandlerService(eventStore, repository)
            }
            "When: #handle is executed" {
                assertion = assertThatCode {
                    service.handle(command) as ProcedureCreateEvent
                }
            }
            "Then: ${AssertionFailedException::class.java.simpleName} is raised with message" {
                assertion.hasSameClassAs(
                    AssertionFailedException(
                        EXCEPTION_MESSAGE_3,
                        DESCRIPTION_INVALID_LENGTH.messageTrackerId
                    )
                )
            }
            "And: the message is $EXCEPTION_MESSAGE_3" {
                assertion.hasMessage(EXCEPTION_MESSAGE_3)
            }
            "And: exception has messageTrackerId ${DESCRIPTION_INVALID_LENGTH.messageTrackerId}" {
                assertion.hasFieldOrPropertyWithValue("code", DESCRIPTION_INVALID_LENGTH.messageTrackerId)
            }
            "And: the eventStore#findAllByFilter is accessed" {
                verify(exactly = 1) {
                    eventStore.findAllByFilter(
                        EqFilter<String, Int>(
                            EVENT_CODE.fieldName,
                            ProcedureAttributes.VCODE
                        )
                    )
                }
            }
            "And: the repository#save is not accessed" {
                verify(exactly = 0) {
                    repository.save(any())
                }
            }
            "And: the event store#addEvent is not accessed " {
                verify(exactly = 0) {
                    eventStore.addEvent(any<ProcedureCreateEvent>())
                }
            }
        }
    }

    "Feature: ${ProcedureCommandHandlerService::class.java.simpleName} usage - upserting a procedure flows" - {
        "Scenario: handling ${ProcedureUpsertCommand::class.java.simpleName} successfully" - {
            lateinit var command: ProcedureCreateCommand
            lateinit var eventStore: EventStore<ProcedureEvent>
            lateinit var repository: Repository<Procedure>
            lateinit var service: ProcedureCommandHandlerService
            lateinit var event: ProcedureUpsertEvent
            "Given: a ${EventStore::class.java.simpleName} mock" {
                eventStore = mockk()
            }
            "And: eventStore#findAllByFilter returns delete event" {
                every {
                    eventStore.findAllByFilter(
                        EqFilter<String, Int>(
                            EVENT_CODE.fieldName,
                            ProcedureAttributes.VCODE
                        )
                    )
                } returns listOf(EventBuilder.buildDeleteEvent())
            }
            "And: eventStore#addEvent just runs" {
                every { eventStore.addEvent(any<ProcedureCreateEvent>()) } just Runs
            }
            "And: a ${Repository::class.java.simpleName} mock" {
                repository = mockk()
            }
            "And: repository#save just runs" {
                every { repository.save(any()) } just Runs
            }
            "And: a ProcedureCreateCommand generated" {
                command = CommandBuilder.buildCreateCommand()
            }
            "And: a ${ProcedureCommandHandlerService::class.java.simpleName} successfully instantiated" {
                service = ProcedureCommandHandlerService(eventStore, repository)
            }
            "When: #handle is executed" {
                event = service.handle(command) as ProcedureUpsertEvent
            }
            "Then: ${ProcedureUpsertEvent::class.java.simpleName} matches event´s class" {
                assertThat(event::class).hasSameClassAs(ProcedureUpsertEvent::class)
            }
            "And: the event store is accessed in the right order" {
                verifyOrder {
                    eventStore.findAllByFilter(
                        EqFilter<String, Int>(
                            EVENT_CODE.fieldName,
                            command.code
                        )
                    )
                    eventStore.addEvent(any<ProcedureCreateEvent>())
                }
            }
            "And: the repository is accessed once" {
                verify(exactly = 1) {
                    repository.save(any())
                }
            }
        }

        "Scenario: handling ${ProcedureUpsertCommand::class.java.simpleName} for a already registered procedure" - {
            lateinit var command: ProcedureCreateCommand
            lateinit var eventStore: EventStore<ProcedureEvent>
            lateinit var repository: Repository<Procedure>
            lateinit var service: ProcedureCommandHandlerService
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            "Given: a ${EventStore::class.java.simpleName} mock" {
                eventStore = mockk()
            }
            "And: eventStore#findAllByFilter returns an event" {
                every {
                    eventStore.findAllByFilter(
                        EqFilter<String, Int>(
                            EVENT_CODE.fieldName,
                            ProcedureAttributes.VCODE
                        )
                    )
                } returns listOf(EventBuilder.buildCreateEvent())
            }
            "And: a ${Repository::class.java.simpleName} mock" {
                repository = mockk()
            }
            "And: a ProcedureCreateCommand generated" {
                command = CommandBuilder.buildCreateCommand()
            }
            "And: a ${ProcedureCommandHandlerService::class.java.simpleName} successfully instantiated" {
                service = ProcedureCommandHandlerService(eventStore, repository)
            }
            "When: #handle is executed" {
                assertion = assertThatCode {
                    service.handle(command) as ProcedureCreateEvent
                }
            }
            "Then: ${AssertionFailedException::class.java.simpleName} is raised with message" {
                assertion.hasSameClassAs(
                    AssertionFailedException(
                        EXCEPTION_MESSAGE_1,
                        PROCEDURE_ALREADY_DEFINED.messageTrackerId
                    )
                )
            }
            "And: the message is $EXCEPTION_MESSAGE_1" {
                assertion.hasMessage(EXCEPTION_MESSAGE_1)
            }
            "And: exception has messageTrackerId ${PROCEDURE_ALREADY_DEFINED.messageTrackerId}" {
                assertion.hasFieldOrPropertyWithValue("code", PROCEDURE_ALREADY_DEFINED.messageTrackerId)
            }
            "And: the eventStore#findAllByFilter is accessed" {
                verify(exactly = 1) {
                    eventStore.findAllByFilter(
                        EqFilter<String, Int>(
                            EVENT_CODE.fieldName,
                            command.code
                        )
                    )
                }
            }
            "And: the repository#save is not accessed" {
                verify(exactly = 0) {
                    repository.save(any())
                }
            }
            "And: the event store#addEvent is not accessed" {
                verify(exactly = 0) {
                    eventStore.addEvent(any<ProcedureCreateEvent>())
                }
            }
        }

        "Scenario: handling ${ProcedureUpsertCommand::class.java.simpleName} for an invalid description" - {
            lateinit var command: ProcedureCreateCommand
            lateinit var eventStore: EventStore<ProcedureEvent>
            lateinit var repository: Repository<Procedure>
            lateinit var service: ProcedureCommandHandlerService
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            "Given: a ${EventStore::class.java.simpleName} mock" {
                eventStore = mockk()
            }
            "And: eventStore#findAllByFilter returns delete event" {
                every {
                    eventStore.findAllByFilter(
                        EqFilter<String, Int>(
                            EVENT_CODE.fieldName,
                            ProcedureAttributes.VCODE
                        )
                    )
                } returns listOf(EventBuilder.buildDeleteEvent())
            }
            "And: a ${Repository::class.java.simpleName} mock" {
                repository = mockk()
            }
            "And: a ProcedureCreateCommand generated" {
                command = CommandBuilder.buildInvalidCreateCommand(code = ProcedureAttributes.VCODE)
            }
            "And: a ${ProcedureCommandHandlerService::class.java.simpleName} successfully instantiated" {
                service = ProcedureCommandHandlerService(eventStore, repository)
            }
            "When: #handle is executed" {
                assertion = assertThatCode {
                    service.handle(command)
                }
            }
            "Then: ${AssertionFailedException::class.java.simpleName} is raised with message" {
                assertion.hasSameClassAs(
                    AssertionFailedException(
                        EXCEPTION_MESSAGE_3,
                        DESCRIPTION_INVALID_LENGTH.messageTrackerId
                    )
                )
            }
            "And: the message is $EXCEPTION_MESSAGE_3" {
                assertion.hasMessage(EXCEPTION_MESSAGE_3)
            }
            "And: exception has messageTrackerId ${DESCRIPTION_INVALID_LENGTH.messageTrackerId}" {
                assertion.hasFieldOrPropertyWithValue("code", DESCRIPTION_INVALID_LENGTH.messageTrackerId)
            }
            "And: the eventStore#findAllByFilter is accessed" {
                verify(exactly = 1) {
                    eventStore.findAllByFilter(
                        EqFilter<String, Int>(
                            EVENT_CODE.fieldName,
                            command.code
                        )
                    )
                }
            }
            "And: the repository#save is not accessed" {
                verify(exactly = 0) {
                    repository.save(any())
                }
            }
            "And: the event store#addEvent is not accessed " {
                verify(exactly = 0) {
                    eventStore.addEvent(any<ProcedureCreateEvent>())
                }
            }
        }
    }

    "Feature: ${ProcedureCommandHandlerService::class.java.simpleName} usage - updating a procedure flows" - {
        "Scenario: handling ${ProcedureUpdateCommand::class.java.simpleName} successfully" - {
            lateinit var command: ProcedureUpdateCommand
            lateinit var eventStore: EventStore<ProcedureEvent>
            lateinit var repository: Repository<Procedure>
            lateinit var service: ProcedureCommandHandlerService
            lateinit var event: ProcedureUpdateEvent
            "Given: a ${EventStore::class.java.simpleName} mock" {
                eventStore = mockk()
            }
            "And: eventStore#findAllByFilter returns an event" {
                every {
                    eventStore.findAllByFilter(
                        EqFilter<String, Int>(
                            EVENT_CODE.fieldName,
                            ProcedureAttributes.VCODE
                        )
                    )
                } returns listOf(EventBuilder.buildCreateEvent())
            }
            "And: eventStore#addEvent just runs" {
                every { eventStore.addEvent(any<ProcedureUpdateEvent>()) } just Runs
            }
            "And: a ${Repository::class.java.simpleName} mock" {
                repository = mockk()
            }
            "And: repository#update just runs" {
                every { repository.update(any(), any()) } just Runs
            }
            "And: a ${ProcedureUpdateCommand::class.java.simpleName} generated from ${ProcedureCreateRequest::class.java.simpleName}" {
                command = CommandBuilder.buildUpdateCommand()
            }
            "And: a ${ProcedureCommandHandlerService::class.java.simpleName} successfully instantiated" {
                service = ProcedureCommandHandlerService(eventStore, repository)
            }
            "When: #handle is executed" {
                event = service.handle(command) as ProcedureUpdateEvent
            }
            "Then: ${ProcedureUpdateEvent::class.java.simpleName}  matches event´s class" {
                assertThat(event::class).hasSameClassAs(ProcedureUpdateEvent::class)
            }
            "And: the event store is accessed in the right order" {
                verifyOrder {
                    eventStore.findAllByFilter(
                        EqFilter<String, Int>(
                            EVENT_CODE.fieldName,
                            command.code
                        )
                    )
                    eventStore.addEvent(any<ProcedureUpdateEvent>())
                }
            }
            "And: the repository is accessed once" {
                verify(exactly = 1) {
                    repository.update(any(), any())
                }
            }
        }

        "Scenario: handling ${ProcedureUpdateCommand::class.java.simpleName} for non registered procedure" - {
            lateinit var command: ProcedureUpdateCommand
            lateinit var eventStore: EventStore<ProcedureEvent>
            lateinit var repository: Repository<Procedure>
            lateinit var service: ProcedureCommandHandlerService
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            "Given: a ${EventStore::class.java.simpleName} mock" {
                eventStore = mockk()
            }
            "And: eventStore#findAllByFilter returns empty list" {
                every {
                    eventStore.findAllByFilter(
                        EqFilter<String, Int>(
                            EVENT_CODE.fieldName,
                            ProcedureAttributes.VCODE
                        )
                    )
                } returns listOf()
            }
            "And: a ${Repository::class.java.simpleName} mock" {
                repository = mockk()
            }
            "And: a ${ProcedureUpdateCommand::class.java.simpleName} generated" {
                command = CommandBuilder.buildUpdateCommand()
            }
            "And: a ${ProcedureCommandHandlerService::class.java.simpleName} successfully instantiated" {
                service = ProcedureCommandHandlerService(eventStore, repository)
            }
            "When: #handle is executed" {
                assertion = assertThatCode {
                    service.handle(command) as ProcedureUpdateEvent
                }
            }
            "Then: ${AssertionFailedException::class.java.simpleName} is raised with message" {
                assertion.hasSameClassAs(
                    AssertionFailedException(
                        EXCEPTION_MESSAGE_4,
                        PROCEDURE_NOT_DEFINED.messageTrackerId
                    )
                )
            }
            "And: the message is $EXCEPTION_MESSAGE_4" {
                assertion.hasMessage(EXCEPTION_MESSAGE_4)
            }
            "And: exception has messageTrackerId ${PROCEDURE_NOT_DEFINED.messageTrackerId}" {
                assertion.hasFieldOrPropertyWithValue("code", PROCEDURE_NOT_DEFINED.messageTrackerId)
            }
            "And: the eventStore#findAllByFilter is accessed" {
                verify(exactly = 1) {
                    eventStore.findAllByFilter(
                        EqFilter<String, Int>(
                            EVENT_CODE.fieldName,
                            ProcedureAttributes.VCODE
                        )
                    )
                }
            }
            "And: the repository#update is not accessed" {
                verify(exactly = 0) {
                    repository.update(any(), any())
                }
            }
            "And: the event store#addEvent is not accessed" {
                verify(exactly = 0) {
                    eventStore.addEvent(any<ProcedureCreateEvent>())
                }
            }
        }

        "Scenario: handling ${ProcedureUpdateCommand::class.java.simpleName} for an invalid description" - {
            lateinit var command: ProcedureUpdateCommand
            lateinit var eventStore: EventStore<ProcedureEvent>
            lateinit var repository: Repository<Procedure>
            lateinit var service: ProcedureCommandHandlerService
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            "Given: a ${EventStore::class.java.simpleName} mock" {
                eventStore = mockk()
            }
            "And: eventStore#findAllByFilter returns create event" {
                every {
                    eventStore.findAllByFilter(
                        EqFilter<String, Int>(
                            EVENT_CODE.fieldName,
                            ProcedureAttributes.VCODE
                        )
                    )
                } returns listOf(EventBuilder.buildCreateEvent())
            }
            "And: a ${Repository::class.java.simpleName} mock" {
                repository = mockk()
            }
            "And: a ProcedureCreateCommand generated}" {
                command = CommandBuilder.buildInvalidUpdateCommand(procedureId = ProcedureAttributes.VPRID, code = ProcedureAttributes.VCODE)
            }
            "And: a ${ProcedureCommandHandlerService::class.java.simpleName} successfully instantiated" {
                service = ProcedureCommandHandlerService(eventStore, repository)
            }
            "When: #handle is executed" {
                assertion = assertThatCode {
                    service.handle(command)
                }
            }
            "Then: ${AssertionFailedException::class.java.simpleName} is raised with message" {
                assertion.hasSameClassAs(
                    AssertionFailedException(
                        EXCEPTION_MESSAGE_3,
                        DESCRIPTION_INVALID_LENGTH.messageTrackerId
                    )
                )
            }
            "And: the message is $EXCEPTION_MESSAGE_3" {
                assertion.hasMessage(EXCEPTION_MESSAGE_3)
            }
            "And: exception has messageTrackerId ${DESCRIPTION_INVALID_LENGTH.messageTrackerId}" {
                assertion.hasFieldOrPropertyWithValue("code", DESCRIPTION_INVALID_LENGTH.messageTrackerId)
            }
            "And: the eventStore#findAllByFilter is accessed" {
                verify(exactly = 1) {
                    eventStore.findAllByFilter(
                        EqFilter<String, Int>(
                            EVENT_CODE.fieldName,
                            command.code
                        )
                    )
                }
            }
            "And: the repository#save is not accessed" {
                verify(exactly = 0) {
                    repository.save(any())
                }
            }
            "And: the event store#addEvent is not accessed " {
                verify(exactly = 0) {
                    eventStore.addEvent(any<ProcedureCreateEvent>())
                }
            }
        }
    }

    "Feature: ${ProcedureCommandHandlerService::class.java.simpleName} usage - deleting a procedure flows" - {
        "Scenario: handling ${ProcedureDeleteCommand::class.java.simpleName} successfully" - {
            lateinit var command: ProcedureDeleteCommand
            lateinit var eventStore: EventStore<ProcedureEvent>
            lateinit var repository: Repository<Procedure>
            lateinit var service: ProcedureCommandHandlerService
            lateinit var event: ProcedureDeleteEvent
            "Given: a ${EventStore::class.java.simpleName} mock" {
                eventStore = mockk()
            }
            "And: eventStore#findAllByFilter returns an event" {
                every {
                    eventStore.findAllByFilter(
                        any<EqFilter<String, String>>()
//        EqFilter<String, String>(
//            EVENT_PROCEDURE_ID_ID.fieldName,
//            ProcedureAttributes.VPRID
//        )
                    )
                } returns listOf(EventBuilder.buildCreateEvent())
            }
            "And: eventStore#addEvent just runs" {
                every { eventStore.addEvent(any<ProcedureDeleteEvent>()) } just Runs
            }
            "And: a ${Repository::class.java.simpleName} mock" {
                repository = mockk()
            }
            "And: repository#delete just runs" {
                every { repository.delete(any()) } just Runs
            }
            "And: a ${ProcedureDeleteCommand::class.java.simpleName} generated" {
                command = CommandBuilder.buildDeleteCommand()
            }
            "And: a ${ProcedureCommandHandlerService::class.java.simpleName} successfully instantiated" {
                service = ProcedureCommandHandlerService(eventStore, repository)
            }
            "When: #handle is executed" {
                event = service.handle(command) as ProcedureDeleteEvent
            }
            "Then: ${ProcedureDeleteEvent::class.java.simpleName}  matches event´s class" {
                assertThat(event::class).hasSameClassAs(ProcedureDeleteEvent::class)
            }
            "And: the event store is accessed in the right order" {
                verifyOrder {
                    eventStore.findAllByFilter(
                        any<EqFilter<String, String>>()
                    )
                    eventStore.addEvent(any<ProcedureDeleteEvent>())
                }
            }
            "And: the repository is accessed once" {
                verify(exactly = 1) {
                    repository.delete(any())
                }
            }
        }

        "Scenario: handling ${ProcedureDeleteCommand::class.java.simpleName} for non registered procedure" - {
            lateinit var command: ProcedureDeleteCommand
            lateinit var eventStore: EventStore<ProcedureEvent>
            lateinit var repository: Repository<Procedure>
            lateinit var service: ProcedureCommandHandlerService
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            "Given: a ${EventStore::class.java.simpleName} mock" {
                eventStore = mockk()
            }
            "And: eventStore#findAllByFilter returns empty list" {
                every {
                    eventStore.findAllByFilter(
                        any<EqFilter<String, String>>()
//                        EqFilter<String, String>(
//                            EVENT_PROCEDURE_ID_ID.fieldName,
//                            ProcedureAttributes.VPRID
//                        )
                    )
                } returns listOf()
            }
            "And: a ${Repository::class.java.simpleName} mock" {
                repository = mockk()
            }
            "And: a ${ProcedureDeleteCommand::class.java.simpleName} generated from ${ProcedureUpdateRequest::class.java.simpleName}" {
                command = CommandBuilder.buildDeleteCommand()
            }
            "And: a ${ProcedureCommandHandlerService::class.java.simpleName} successfully instantiated" {
                service = ProcedureCommandHandlerService(eventStore, repository)
            }
            "When: #handle is executed" {
                assertion = assertThatCode {
                    service.handle(command)
                }
            }
            "Then: ${AssertionFailedException::class.java.simpleName} is raised with message" {
                assertion.hasSameClassAs(
                    AssertionFailedException(
                        EXCEPTION_MESSAGE_2,
                        PROCEDURE_NOT_DEFINED.messageTrackerId
                    )
                )
            }
            "And: the message is $EXCEPTION_MESSAGE_2" {
                assertion.hasMessage(EXCEPTION_MESSAGE_2)
            }
            "And: exception has messageTrackerId ${PROCEDURE_NOT_DEFINED.messageTrackerId}" {
                assertion.hasFieldOrPropertyWithValue("code", PROCEDURE_NOT_DEFINED.messageTrackerId)
            }
            "And: the eventStore#findAllByFilter is accessed" {
                verify(exactly = 1) {
                    eventStore.findAllByFilter(
                        any<EqFilter<String, String>>()
//                        EqFilter<String, String>(
//                            EVENT_PROCEDURE_ID_ID.fieldName,
//                            ProcedureAttributes.VPRID
//                        )
                    )
                }
            }
            "And: the repository#delete is not accessed" {
                verify(exactly = 0) {
                    repository.delete(any())
                }
            }
            "And: the event store#addEvent is not accessed" {
                verify(exactly = 0) {
                    eventStore.addEvent(any<ProcedureCreateEvent>())
                }
            }
        }
    }
})
