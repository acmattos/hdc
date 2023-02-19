package br.com.acmattos.hdc.odontogram.application

import br.com.acmattos.hdc.common.context.domain.cqs.EventStore
import br.com.acmattos.hdc.common.context.domain.model.Repository
import br.com.acmattos.hdc.common.tool.assertion.AssertionFailedException
import br.com.acmattos.hdc.common.tool.page.EqFilter
import br.com.acmattos.hdc.odontogram.config.MessageTrackerIdEnum.ODONTOGRAM_ALREADY_DEFINED
import br.com.acmattos.hdc.odontogram.config.MessageTrackerIdEnum.ODONTOGRAM_NOT_DEFINED
import br.com.acmattos.hdc.odontogram.domain.cqs.OdontogramCreateCommand
import br.com.acmattos.hdc.odontogram.domain.cqs.OdontogramCreateEvent
import br.com.acmattos.hdc.odontogram.domain.cqs.OdontogramDeleteCommand
import br.com.acmattos.hdc.odontogram.domain.cqs.OdontogramDeleteEvent
import br.com.acmattos.hdc.odontogram.domain.cqs.OdontogramEvent
import br.com.acmattos.hdc.odontogram.domain.cqs.OdontogramUpdateCommand
import br.com.acmattos.hdc.odontogram.domain.cqs.OdontogramUpdateEvent
import br.com.acmattos.hdc.odontogram.domain.cqs.OdontogramUpsertCommand
import br.com.acmattos.hdc.odontogram.domain.cqs.OdontogramUpsertEvent
import br.com.acmattos.hdc.odontogram.domain.model.CommandBuilder
import br.com.acmattos.hdc.odontogram.domain.model.EventBuilder
import br.com.acmattos.hdc.odontogram.domain.model.Odontogram
import br.com.acmattos.hdc.odontogram.domain.model.OdontogramAttributes
import br.com.acmattos.hdc.odontogram.port.persistence.mongodb.DocumentIndexedField.EVENT_ODONTOGRAM_ID_ID
import br.com.acmattos.hdc.odontogram.port.rest.OdontogramCreateRequest
import br.com.acmattos.hdc.odontogram.port.rest.OdontogramUpdateRequest
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

private const val EXCEPTION_MESSAGE_1 = "There is an odontogram already defined for the given id [01FK96GENJKTN1BYZW6BRHFZFJ]!"
private const val EXCEPTION_MESSAGE_2 = "There is no odontogram defined for the given id [01FK96GENJKTN1BYZW6BRHFZFJ]!"
private const val EXCEPTION_MESSAGE_3 = "Upper left must contain exactly 8 teeth!"
private const val EXCEPTION_MESSAGE_4 = "There is no odontogram defined for the given id [01FK96GENJKTN1BYZW6BRHFZFJ]!"

/**
 * @author ACMattos
 * @since 20/03/2022.
 */
object OdontogramCommandHandlerServiceTest: Spek({
    Feature("${OdontogramCommandHandlerService::class.java.simpleName} usage - creating a odontogram flows") {
        Scenario("handling ${OdontogramCreateCommand::class.java.simpleName} successfully") {
            lateinit var command: OdontogramCreateCommand
            lateinit var eventStore: EventStore<OdontogramEvent>
            lateinit var repository: Repository<Odontogram>
            lateinit var service: OdontogramCommandHandlerService
            lateinit var event: OdontogramCreateEvent
            Given("""a ${EventStore::class.java.simpleName} mock""") {
                eventStore = mockk()
            }
            And("""eventStore#findAllByFilter returns empty list""") {
                every {
                    eventStore.findAllByFilter(
                        EqFilter<String, String>(
                            EVENT_ODONTOGRAM_ID_ID.fieldName,
                            OdontogramAttributes.VOMID
                        )
                    )
                } returns listOf()
            }
            And("""eventStore#addEvent just runs""") {
                every { eventStore.addEvent(any<OdontogramCreateEvent>()) } just Runs
            }
            And("""a ${Repository::class.java.simpleName} mock""") {
                repository = mockk()
            }
            And("""repository#save just runs""") {
                every { repository.save(any()) } just Runs
            }
            And("""a ${OdontogramCreateCommand::class.java.simpleName} generated""") {
                command = CommandBuilder.buildCreateCommand()
            }
            And("""a ${OdontogramCommandHandlerService::class.java.simpleName} successfully instantiated""") {
                service = OdontogramCommandHandlerService(eventStore, repository)
            }
            When("""#handle is executed""") {
                event = service.handle(command) as OdontogramCreateEvent
            }
            Then("""${OdontogramCreateEvent::class.java.simpleName} matches event´s class""") {
                assertThat(event::class).hasSameClassAs(OdontogramCreateEvent::class)
            }
            And("""the event store is accessed in the right order""") {
                verifyOrder {
                    eventStore.findAllByFilter(
                        EqFilter<String, String>(
                            EVENT_ODONTOGRAM_ID_ID.fieldName,
                            command.odontogramId.id
                        )
                    )
                    eventStore.addEvent(any<OdontogramCreateEvent>())
                }
            }
            And("""the repository is accessed once""") {
                verify(exactly = 1) {
                    repository.save(any())
                }
            }
        }

        Scenario("handling ${OdontogramCreateCommand::class.java.simpleName} for a already registered odontogram") {
            lateinit var command: OdontogramCreateCommand
            lateinit var eventStore: EventStore<OdontogramEvent>
            lateinit var repository: Repository<Odontogram>
            lateinit var service: OdontogramCommandHandlerService
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            Given("""a ${EventStore::class.java.simpleName} mock""") {
                eventStore = mockk()
            }
            And("""eventStore#findAllByFilter returns an event""") {
                every {
                    eventStore.findAllByFilter(
                        EqFilter<String, String>(
                            EVENT_ODONTOGRAM_ID_ID.fieldName,
                            OdontogramAttributes.VOMID
                        )
                    )
                } returns listOf(EventBuilder.buildCreateEvent())
            }
            And("""eventStore#addEvent just runs""") {
                every { eventStore.addEvent(any<OdontogramCreateEvent>()) } just Runs
            }
            And("""a ${Repository::class.java.simpleName} mock""") {
                repository = mockk()
            }
            And("""a ${OdontogramCreateCommand::class.java.simpleName} generated""") {
                command = CommandBuilder.buildCreateCommand()
            }
            And("""a ${OdontogramCommandHandlerService::class.java.simpleName} successfully instantiated""") {
                service = OdontogramCommandHandlerService(eventStore, repository)
            }
            When("""#handle is executed""") {
                assertion = assertThatCode {
                    service.handle(command) as OdontogramCreateEvent
                }
            }
            Then("""${AssertionFailedException::class.java.simpleName} is raised with message""") {
                assertion.hasSameClassAs(
                    AssertionFailedException(
                        EXCEPTION_MESSAGE_1,
                        ODONTOGRAM_ALREADY_DEFINED.messageTrackerId
                    )
                )
            }
            And("""the message is $EXCEPTION_MESSAGE_1""") {
                assertion.hasMessage(EXCEPTION_MESSAGE_1)
            }
            And("""exception has messageTrackerId ${ODONTOGRAM_ALREADY_DEFINED.messageTrackerId}""") {
                assertion.hasFieldOrPropertyWithValue("code", ODONTOGRAM_ALREADY_DEFINED.messageTrackerId)
            }
            And("""the eventStore#findAllByFilter is accessed""") {
                verify(exactly = 1) {
                    eventStore.findAllByFilter(
                        EqFilter<String, String>(
                            EVENT_ODONTOGRAM_ID_ID.fieldName,
                            OdontogramAttributes.VOMID
                        )
                    )
                }
            }
            And("""the repository#save is not accessed""") {
                verify(exactly = 0) {
                    repository.save(any())
                }
            }
            And("""the event store#addEvent is not accessed""") {
                verify(exactly = 0) {
                    eventStore.addEvent(any<OdontogramCreateEvent>())
                }
            }
        }

        Scenario("handling ${OdontogramCreateCommand::class.java.simpleName} for an invalid description") {
            lateinit var command: OdontogramCreateCommand
            lateinit var eventStore: EventStore<OdontogramEvent>
            lateinit var repository: Repository<Odontogram>
            lateinit var service: OdontogramCommandHandlerService
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            Given("""a ${EventStore::class.java.simpleName} mock""") {
                eventStore = mockk()
            }
            And("""eventStore#findAllByFilter returns empty list""") {
                every {
                    eventStore.findAllByFilter(
                        EqFilter<String, String>(
                            EVENT_ODONTOGRAM_ID_ID.fieldName,
                            OdontogramAttributes.VOMID
                        )
                    )
                } returns listOf()
            }
            And("""a ${Repository::class.java.simpleName} mock""") {
                repository = mockk()
            }
            And("""a ${OdontogramCreateCommand::class.java.simpleName} generated""") {
                command = CommandBuilder.buildInvalidCreateCommand()
            }
            And("""a ${OdontogramCommandHandlerService::class.java.simpleName} successfully instantiated""") {
                service = OdontogramCommandHandlerService(eventStore, repository)
            }
            When("""#handle is executed""") {
                assertion = assertThatCode {
                    service.handle(command) as OdontogramCreateEvent
                }
            }
            Then("""${AssertionFailedException::class.java.simpleName} is raised with message""") {
//                assertion.hasSameClassAs(
//                    AssertionFailedException(
//                        EXCEPTION_MESSAGE_3,
//                        DESCRIPTION_INVALID_LENGTH.messageTrackerId
//                    )
//                )
            }
            And("""the message is $EXCEPTION_MESSAGE_3""") {
                assertion.hasMessage(EXCEPTION_MESSAGE_3)
            }
//            And("""exception has messageTrackerId ${DESCRIPTION_INVALID_LENGTH.messageTrackerId}""") {
//                assertion.hasFieldOrPropertyWithValue("code", DESCRIPTION_INVALID_LENGTH.messageTrackerId)
//            }
            And("""the eventStore#findAllByFilter is accessed""") {
                verify(exactly = 1) {
                    eventStore.findAllByFilter(
                        EqFilter<String, String>(
                            EVENT_ODONTOGRAM_ID_ID.fieldName,
                            OdontogramAttributes.VOMID
                        )
                    )
                }
            }
            And("""the repository#save is not accessed""") {
                verify(exactly = 0) {
                    repository.save(any())
                }
            }
            And("""the event store#addEvent is not accessed """) {
                verify(exactly = 0) {
                    eventStore.addEvent(any<OdontogramCreateEvent>())
                }
            }
        }
    }

    Feature("${OdontogramCommandHandlerService::class.java.simpleName} usage - upserting a odontogram flows") {
        Scenario("handling ${OdontogramUpsertCommand::class.java.simpleName} successfully") {
            lateinit var command: OdontogramCreateCommand
            lateinit var eventStore: EventStore<OdontogramEvent>
            lateinit var repository: Repository<Odontogram>
            lateinit var service: OdontogramCommandHandlerService
            lateinit var event: OdontogramUpsertEvent
            Given("""a ${EventStore::class.java.simpleName} mock""") {
                eventStore = mockk()
            }
            And("""eventStore#findAllByFilter returns delete event""") {
                every {
                    eventStore.findAllByFilter(
                        EqFilter<String, String>(
                            EVENT_ODONTOGRAM_ID_ID.fieldName,
                            OdontogramAttributes.VOMID
                        )
                    )
                } returns listOf(EventBuilder.buildDeleteEvent())
            }
            And("""eventStore#addEvent just runs""") {
                every { eventStore.addEvent(any<OdontogramCreateEvent>()) } just Runs
            }
            And("""a ${Repository::class.java.simpleName} mock""") {
                repository = mockk()
            }
            And("""repository#save just runs""") {
                every { repository.save(any()) } just Runs
            }
            And("""a ${OdontogramCreateCommand::class.java.simpleName} generated""") {
                command = CommandBuilder.buildCreateCommand()
            }
            And("""a ${OdontogramCommandHandlerService::class.java.simpleName} successfully instantiated""") {
                service = OdontogramCommandHandlerService(eventStore, repository)
            }
            When("""#handle is executed""") {
                event = service.handle(command) as OdontogramUpsertEvent
            }
            Then("""${OdontogramUpsertEvent::class.java.simpleName} matches event´s class""") {
                assertThat(event::class).hasSameClassAs(OdontogramUpsertEvent::class)
            }
            And("""the event store is accessed in the right order""") {
                verifyOrder {
                    eventStore.findAllByFilter(
                        EqFilter<String, String>(
                            EVENT_ODONTOGRAM_ID_ID.fieldName,
                            command.odontogramId.id
                        )
                    )
                    eventStore.addEvent(any<OdontogramCreateEvent>())
                }
            }
            And("""the repository is accessed once""") {
                verify(exactly = 1) {
                    repository.save(any())
                }
            }
        }

        Scenario("handling ${OdontogramUpsertCommand::class.java.simpleName} for a already registered odontogram") {
            lateinit var command: OdontogramCreateCommand
            lateinit var eventStore: EventStore<OdontogramEvent>
            lateinit var repository: Repository<Odontogram>
            lateinit var service: OdontogramCommandHandlerService
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            Given("""a ${EventStore::class.java.simpleName} mock""") {
                eventStore = mockk()
            }
            And("""eventStore#findAllByFilter returns an event""") {
                every {
                    eventStore.findAllByFilter(
                        EqFilter<String, String>(
                            EVENT_ODONTOGRAM_ID_ID.fieldName,
                            OdontogramAttributes.VOMID
                        )
                    )
                } returns listOf(EventBuilder.buildCreateEvent())
            }
            And("""a ${Repository::class.java.simpleName} mock""") {
                repository = mockk()
            }
            And("""a ${OdontogramCreateCommand::class.java.simpleName} generated""") {
                command = CommandBuilder.buildCreateCommand()
            }
            And("""a ${OdontogramCommandHandlerService::class.java.simpleName} successfully instantiated""") {
                service = OdontogramCommandHandlerService(eventStore, repository)
            }
            When("""#handle is executed""") {
                assertion = assertThatCode {
                    service.handle(command) as OdontogramCreateEvent
                }
            }
            Then("""${AssertionFailedException::class.java.simpleName} is raised with message""") {
                assertion.hasSameClassAs(
                    AssertionFailedException(
                        EXCEPTION_MESSAGE_1,
                        ODONTOGRAM_ALREADY_DEFINED.messageTrackerId
                    )
                )
            }
            And("""the message is $EXCEPTION_MESSAGE_1""") {
                assertion.hasMessage(EXCEPTION_MESSAGE_1)
            }
            And("""exception has messageTrackerId ${ODONTOGRAM_ALREADY_DEFINED.messageTrackerId}""") {
                assertion.hasFieldOrPropertyWithValue("code", ODONTOGRAM_ALREADY_DEFINED.messageTrackerId)
            }
            And("""the eventStore#findAllByFilter is accessed""") {
                verify(exactly = 1) {
                    eventStore.findAllByFilter(
                        EqFilter<String, String>(
                            EVENT_ODONTOGRAM_ID_ID.fieldName,
                            command.odontogramId.id
                        )
                    )
                }
            }
            And("""the repository#save is not accessed""") {
                verify(exactly = 0) {
                    repository.save(any())
                }
            }
            And("""the event store#addEvent is not accessed""") {
                verify(exactly = 0) {
                    eventStore.addEvent(any<OdontogramCreateEvent>())
                }
            }
        }

        Scenario("handling ${OdontogramUpsertCommand::class.java.simpleName} for an invalid description") {
            lateinit var command: OdontogramCreateCommand
            lateinit var eventStore: EventStore<OdontogramEvent>
            lateinit var repository: Repository<Odontogram>
            lateinit var service: OdontogramCommandHandlerService
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            Given("""a ${EventStore::class.java.simpleName} mock""") {
                eventStore = mockk()
            }
            And("""eventStore#findAllByFilter returns delete event""") {
                every {
                    eventStore.findAllByFilter(
                        EqFilter<String, String>(
                            EVENT_ODONTOGRAM_ID_ID.fieldName,
                            OdontogramAttributes.VOMID
                        )
                    )
                } returns listOf(EventBuilder.buildDeleteEvent())
            }
            And("""a ${Repository::class.java.simpleName} mock""") {
                repository = mockk()
            }
            And("""a ${OdontogramCreateCommand::class.java.simpleName} generated""") {
                command = CommandBuilder.buildInvalidCreateCommand()
            }
            And("""a ${OdontogramCommandHandlerService::class.java.simpleName} successfully instantiated""") {
                service = OdontogramCommandHandlerService(eventStore, repository)
            }
            When("""#handle is executed""") {
                assertion = assertThatCode {
                    service.handle(command)
                }
            }
            Then("""${AssertionFailedException::class.java.simpleName} is raised with message""") {
//                assertion.hasSameClassAs(
//                    AssertionFailedException(
//                        EXCEPTION_MESSAGE_3,
//                        DESCRIPTION_INVALID_LENGTH.messageTrackerId
//                    )
//                )
            }
            And("""the message is $EXCEPTION_MESSAGE_3""") {
                assertion.hasMessage(EXCEPTION_MESSAGE_3)
            }
//            And("""exception has messageTrackerId ${DESCRIPTION_INVALID_LENGTH.messageTrackerId}""") {
//                assertion.hasFieldOrPropertyWithValue("code", DESCRIPTION_INVALID_LENGTH.messageTrackerId)
//            }
            And("""the eventStore#findAllByFilter is accessed""") {
                verify(exactly = 1) {
                    eventStore.findAllByFilter(
                        EqFilter<String, String>(
                            EVENT_ODONTOGRAM_ID_ID.fieldName,
                            command.odontogramId.id
                        )
                    )
                }
            }
            And("""the repository#save is not accessed""") {
                verify(exactly = 0) {
                    repository.save(any())
                }
            }
            And("""the event store#addEvent is not accessed """) {
                verify(exactly = 0) {
                    eventStore.addEvent(any<OdontogramCreateEvent>())
                }
            }
        }
    }

    Feature("${OdontogramCommandHandlerService::class.java.simpleName} usage - updating a odontogram flows") {
        Scenario("handling ${OdontogramUpdateCommand::class.java.simpleName} successfully") {
            lateinit var command: OdontogramUpdateCommand
            lateinit var eventStore: EventStore<OdontogramEvent>
            lateinit var repository: Repository<Odontogram>
            lateinit var service: OdontogramCommandHandlerService
            lateinit var event: OdontogramUpdateEvent
            Given("""a ${EventStore::class.java.simpleName} mock""") {
                eventStore = mockk()
            }
            And("""eventStore#findAllByFilter returns an event""") {
                every {
                    eventStore.findAllByFilter(
                        EqFilter<String, String>(
                            EVENT_ODONTOGRAM_ID_ID.fieldName,
                            OdontogramAttributes.VOMID
                        )
                    )
                } returns listOf(EventBuilder.buildCreateEvent())
            }
            And("""eventStore#addEvent just runs""") {
                every { eventStore.addEvent(any<OdontogramUpdateEvent>()) } just Runs
            }
            And("""a ${Repository::class.java.simpleName} mock""") {
                repository = mockk()
            }
            And("""repository#update just runs""") {
                every { repository.update(any(), any()) } just Runs
            }
            And("""a ${OdontogramUpdateCommand::class.java.simpleName} generated from ${OdontogramCreateRequest::class.java.simpleName}""") {
                command = CommandBuilder.buildUpdateCommand()
            }
            And("""a ${OdontogramCommandHandlerService::class.java.simpleName} successfully instantiated""") {
                service = OdontogramCommandHandlerService(eventStore, repository)
            }
            When("""#handle is executed""") {
                event = service.handle(command) as OdontogramUpdateEvent
            }
            Then("""${OdontogramUpdateEvent::class.java.simpleName}  matches event´s class""") {
                assertThat(event::class).hasSameClassAs(OdontogramUpdateEvent::class)
            }
            And("""the event store is accessed in the right order""") {
                verifyOrder {
                    eventStore.findAllByFilter(
                        EqFilter<String, String>(
                            EVENT_ODONTOGRAM_ID_ID.fieldName,
                            command.odontogramId.id
                        )
                    )
                    eventStore.addEvent(any<OdontogramUpdateEvent>())
                }
            }
            And("""the repository is accessed once""") {
                verify(exactly = 1) {
                    repository.update(any(), any())
                }
            }
        }

        Scenario("handling ${OdontogramUpdateCommand::class.java.simpleName} for non registered odontogram") {
            lateinit var command: OdontogramUpdateCommand
            lateinit var eventStore: EventStore<OdontogramEvent>
            lateinit var repository: Repository<Odontogram>
            lateinit var service: OdontogramCommandHandlerService
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            Given("""a ${EventStore::class.java.simpleName} mock""") {
                eventStore = mockk()
            }
            And("""eventStore#findAllByFilter returns empty list""") {
                every {
                    eventStore.findAllByFilter(
                        EqFilter<String, String>(
                            EVENT_ODONTOGRAM_ID_ID.fieldName,
                            OdontogramAttributes.VOMID
                        )
                    )
                } returns listOf()
            }
            And("""a ${Repository::class.java.simpleName} mock""") {
                repository = mockk()
            }
            And("""a ${OdontogramUpdateCommand::class.java.simpleName} generated""") {
                command = CommandBuilder.buildUpdateCommand()
            }
            And("""a ${OdontogramCommandHandlerService::class.java.simpleName} successfully instantiated""") {
                service = OdontogramCommandHandlerService(eventStore, repository)
            }
            When("""#handle is executed""") {
                assertion = assertThatCode {
                    service.handle(command) as OdontogramUpdateEvent
                }
            }
            Then("""${AssertionFailedException::class.java.simpleName} is raised with message""") {
                assertion.hasSameClassAs(
                    AssertionFailedException(
                        EXCEPTION_MESSAGE_4,
                        ODONTOGRAM_NOT_DEFINED.messageTrackerId
                    )
                )
            }
            And("""the message is $EXCEPTION_MESSAGE_4""") {
                assertion.hasMessage(EXCEPTION_MESSAGE_4)
            }
            And("""exception has messageTrackerId ${ODONTOGRAM_NOT_DEFINED.messageTrackerId}""") {
                assertion.hasFieldOrPropertyWithValue("code", ODONTOGRAM_NOT_DEFINED.messageTrackerId)
            }
            And("""the eventStore#findAllByFilter is accessed""") {
                verify(exactly = 1) {
                    eventStore.findAllByFilter(
                        EqFilter<String, String>(
                            EVENT_ODONTOGRAM_ID_ID.fieldName,
                            OdontogramAttributes.VOMID
                        )
                    )
                }
            }
            And("""the repository#update is not accessed""") {
                verify(exactly = 0) {
                    repository.update(any(), any())
                }
            }
            And("""the event store#addEvent is not accessed""") {
                verify(exactly = 0) {
                    eventStore.addEvent(any<OdontogramCreateEvent>())
                }
            }
        }

        Scenario("handling ${OdontogramUpdateCommand::class.java.simpleName} for an invalid description") {
            lateinit var command: OdontogramUpdateCommand
            lateinit var eventStore: EventStore<OdontogramEvent>
            lateinit var repository: Repository<Odontogram>
            lateinit var service: OdontogramCommandHandlerService
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            Given("""a ${EventStore::class.java.simpleName} mock""") {
                eventStore = mockk()
            }
            And("""eventStore#findAllByFilter returns create event""") {
                every {
                    eventStore.findAllByFilter(
                        EqFilter<String, String>(
                            EVENT_ODONTOGRAM_ID_ID.fieldName,
                            OdontogramAttributes.VOMID
                        )
                    )
                } returns listOf(EventBuilder.buildCreateEvent())
            }
            And("""a ${Repository::class.java.simpleName} mock""") {
                repository = mockk()
            }
            And("""a ${OdontogramCreateCommand::class.java.simpleName} generated}""") {
                command = CommandBuilder.buildInvalidUpdateCommand()
            }
            And("""a ${OdontogramCommandHandlerService::class.java.simpleName} successfully instantiated""") {
                service = OdontogramCommandHandlerService(eventStore, repository)
            }
            When("""#handle is executed""") {
                assertion = assertThatCode {
                    service.handle(command)
                }
            }
            Then("""${AssertionFailedException::class.java.simpleName} is raised with message""") {
//                assertion.hasSameClassAs(
//                    AssertionFailedException(
//                        EXCEPTION_MESSAGE_3,
//                        DESCRIPTION_INVALID_LENGTH.messageTrackerId
//                    )
//                )
            }
            And("""the message is $EXCEPTION_MESSAGE_3""") {
                assertion.hasMessage(EXCEPTION_MESSAGE_3)
            }
//            And("""exception has messageTrackerId ${DESCRIPTION_INVALID_LENGTH.messageTrackerId}""") {
//                assertion.hasFieldOrPropertyWithValue("code", DESCRIPTION_INVALID_LENGTH.messageTrackerId)
//            }
            And("""the eventStore#findAllByFilter is accessed""") {
                verify(exactly = 1) {
                    eventStore.findAllByFilter(
                        EqFilter<String, String>(
                            EVENT_ODONTOGRAM_ID_ID.fieldName,
                            command.odontogramId.id
                        )
                    )
                }
            }
            And("""the repository#save is not accessed""") {
                verify(exactly = 0) {
                    repository.save(any())
                }
            }
            And("""the event store#addEvent is not accessed """) {
                verify(exactly = 0) {
                    eventStore.addEvent(any<OdontogramCreateEvent>())
                }
            }
        }
    }

    Feature("${OdontogramCommandHandlerService::class.java.simpleName} usage - deleting a odontogram flows") {
        Scenario("handling ${OdontogramDeleteCommand::class.java.simpleName} successfully") {
            lateinit var command: OdontogramDeleteCommand
            lateinit var eventStore: EventStore<OdontogramEvent>
            lateinit var repository: Repository<Odontogram>
            lateinit var service: OdontogramCommandHandlerService
            lateinit var event: OdontogramDeleteEvent
            Given("""a ${EventStore::class.java.simpleName} mock""") {
                eventStore = mockk()
            }
            And("""eventStore#findAllByFilter returns an event""") {
                every {
                    eventStore.findAllByFilter(
                        any<EqFilter<String, String>>()
//        EqFilter<String, String>(
//            EVENT_ODONTOGRAM_ID_ID.fieldName,
//            OdontogramAttributes.VPRID
//        )
                    )
                } returns listOf(EventBuilder.buildCreateEvent())
            }
            And("""eventStore#addEvent just runs""") {
                every { eventStore.addEvent(any<OdontogramDeleteEvent>()) } just Runs
            }
            And("""a ${Repository::class.java.simpleName} mock""") {
                repository = mockk()
            }
            And("""repository#delete just runs""") {
                every { repository.delete(any()) } just Runs
            }
            And("""a ${OdontogramDeleteCommand::class.java.simpleName} generated""") {
                command = CommandBuilder.buildDeleteCommand()
            }
            And("""a ${OdontogramCommandHandlerService::class.java.simpleName} successfully instantiated""") {
                service = OdontogramCommandHandlerService(eventStore, repository)
            }
            When("""#handle is executed""") {
                event = service.handle(command) as OdontogramDeleteEvent
            }
            Then("""${OdontogramDeleteEvent::class.java.simpleName}  matches event´s class""") {
                assertThat(event::class).hasSameClassAs(OdontogramDeleteEvent::class)
            }
            And("""the event store is accessed in the right order""") {
                verifyOrder {
                    eventStore.findAllByFilter(
                        any<EqFilter<String, String>>()
                    )
                    eventStore.addEvent(any<OdontogramDeleteEvent>())
                }
            }
            And("""the repository is accessed once""") {
                verify(exactly = 1) {
                    repository.delete(any())
                }
            }
        }

        Scenario("handling ${OdontogramDeleteCommand::class.java.simpleName} for non registered odontogram") {
            lateinit var command: OdontogramDeleteCommand
            lateinit var eventStore: EventStore<OdontogramEvent>
            lateinit var repository: Repository<Odontogram>
            lateinit var service: OdontogramCommandHandlerService
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            Given("""a ${EventStore::class.java.simpleName} mock""") {
                eventStore = mockk()
            }
            And("""eventStore#findAllByFilter returns empty list""") {
                every {
                    eventStore.findAllByFilter(
                        any<EqFilter<String, String>>()
                    )
                } returns listOf()
            }
            And("""a ${Repository::class.java.simpleName} mock""") {
                repository = mockk()
            }
            And("""a ${OdontogramDeleteCommand::class.java.simpleName} generated from ${OdontogramUpdateRequest::class.java.simpleName}""") {
                command = CommandBuilder.buildDeleteCommand()
            }
            And("""a ${OdontogramCommandHandlerService::class.java.simpleName} successfully instantiated""") {
                service = OdontogramCommandHandlerService(eventStore, repository)
            }
            When("""#handle is executed""") {
                assertion = assertThatCode {
                    service.handle(command)
                }
            }
            Then("""${AssertionFailedException::class.java.simpleName} is raised with message""") {
                assertion.hasSameClassAs(
                    AssertionFailedException(
                        EXCEPTION_MESSAGE_2,
                        ODONTOGRAM_NOT_DEFINED.messageTrackerId
                    )
                )
            }
            And("""the message is $EXCEPTION_MESSAGE_2""") {
                assertion.hasMessage(EXCEPTION_MESSAGE_2)
            }
            And("""exception has messageTrackerId ${ODONTOGRAM_NOT_DEFINED.messageTrackerId}""") {
                assertion.hasFieldOrPropertyWithValue("code", ODONTOGRAM_NOT_DEFINED.messageTrackerId)
            }
            And("""the eventStore#findAllByFilter is accessed""") {
                verify(exactly = 1) {
                    eventStore.findAllByFilter(
                        any<EqFilter<String, String>>()
//                        EqFilter<String, String>(
//                            EVENT_ODONTOGRAM_ID_ID.fieldName,
//                            OdontogramAttributes.VPRID
//                        )
                    )
                }
            }
            And("""the repository#delete is not accessed""") {
                verify(exactly = 0) {
                    repository.delete(any())
                }
            }
            And("""the event store#addEvent is not accessed""") {
                verify(exactly = 0) {
                    eventStore.addEvent(any<OdontogramCreateEvent>())
                }
            }
        }
    }
})
