package br.com.acmattos.hdc.user.application

import br.com.acmattos.hdc.common.context.domain.cqs.EventStore
import br.com.acmattos.hdc.common.context.domain.model.Repository
import br.com.acmattos.hdc.common.tool.assertion.AssertionFailedException
import br.com.acmattos.hdc.common.tool.page.EqFilter
import br.com.acmattos.hdc.user.config.MessageTrackerIdEnum
import br.com.acmattos.hdc.user.domain.cqs.RoleCreateEvent
import br.com.acmattos.hdc.user.domain.cqs.RoleDeleteEvent
import br.com.acmattos.hdc.user.domain.cqs.RoleEvent
import br.com.acmattos.hdc.user.domain.cqs.RoleUpdateEvent
import br.com.acmattos.hdc.user.domain.cqs.RoleUpsertEvent
import br.com.acmattos.hdc.user.domain.model.Role
import br.com.acmattos.hdc.user.domain.model.RoleCommandBuilder
import br.com.acmattos.hdc.user.domain.model.RoleEventBuilder
import br.com.acmattos.hdc.user.domain.model.RoleRequest
import br.com.acmattos.hdc.user.port.persistence.mongodb.DocumentIndexedField.EVENT_CODE
import br.com.acmattos.hdc.user.port.persistence.mongodb.DocumentIndexedField.EVENT_ROLE_ID_ID
import io.kotest.core.spec.style.FreeSpec
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowableOfType

/**
 * @author ACMattos
 * @since 16/05/2024.
 */
class RoleCommandHandlerServiceTest: FreeSpec({
    "Feature: RoleCommandHandlerService creating a role" - {
        "Scenario: handling RoleCreateCommand successfully"  {
            // Given
            val request = RoleRequest.build()
            val command = RoleCommandBuilder.buildCreate(request)
            val eventStore: EventStore<RoleEvent> = mockk()
            every {
                eventStore.findAllByFilter(
                    EqFilter<String, String>(
                        EVENT_CODE.fieldName,
                        request.code!!
                    )
                )
            } returns listOf()
            every { eventStore.addEvent(any<RoleCreateEvent>()) } just Runs
            val repository: Repository<Role> = mockk()
            every { repository.save(any<Role>()) } just Runs
            val service = RoleCommandHandlerService(eventStore, repository)
            // When
            val event = service.handle(command)
            // Then
            assertThat(event::class).hasSameClassAs(RoleCreateEvent::class)
            verifyOrder {
                eventStore.findAllByFilter(
                    EqFilter<String, String>(
                        EVENT_CODE.fieldName,
                        command.code
                    )
                )
                eventStore.addEvent(event)
            }
            verify(exactly = 1) {
                repository.save(any<Role>())
            }
        }

        "Scenario: handling RoleCreateCommand for a already registered role"  {
            // Given
            val request = RoleRequest.build()
            val command = RoleCommandBuilder.buildCreate(request)
            val eventStore: EventStore<RoleEvent> = mockk()
            every {
                eventStore.findAllByFilter(
                    EqFilter<String, String>(
                        EVENT_CODE.fieldName,
                        command.code
                    )
                )
            } returns listOf(RoleEventBuilder.buildCreate(command))
            every { eventStore.addEvent(any<RoleCreateEvent>()) } just Runs
            val repository: Repository<Role> = mockk()
            val service = RoleCommandHandlerService(eventStore, repository)
            // When
            val exception: AssertionFailedException = catchThrowableOfType(
                { service.handle(command) },
                AssertionFailedException::class.java
            )
            // Then
            assertThat(exception).hasMessage(
                "There is a role already defined for the given code [CODE_12]!")
            assertThat(exception.code)
                .isEqualTo(MessageTrackerIdEnum.ROLE_ALREADY_DEFINED.messageTrackerId)
            verify(exactly = 1) {
                eventStore.findAllByFilter(
                    EqFilter<String, String>(
                        EVENT_CODE.fieldName,
                        command.code
                    )
                )
            }
            verify(exactly = 0) {
                repository.save(any<Role>())
                eventStore.addEvent(any<RoleCreateEvent>())
            }
        }

        "Scenario: handling RoleCreateCommand for an invalid description"  {
            // Given
            val request = RoleRequest.buildWithCode(value = "_INVALID")
            val command = RoleCommandBuilder.buildCreate(request)
            val eventStore: EventStore<RoleEvent> = mockk()
            every {
                eventStore.findAllByFilter(
                    EqFilter<String, String>(
                        EVENT_CODE.fieldName,
                        command.code
                    )
                )
            } returns listOf()
            every { eventStore.addEvent(any<RoleCreateEvent>()) } just Runs
            val repository: Repository<Role> = mockk()
            val service = RoleCommandHandlerService(eventStore, repository)
            // When
            val exception: AssertionFailedException = catchThrowableOfType(
                { service.handle(command) },
                AssertionFailedException::class.java
            )
            // Then
            assertThat(exception).hasMessage(
                "Invalid code format: A-Z, A-Z0-9_, 3 <= name.length <= 20!")
            assertThat(exception.code)
                .isEqualTo(MessageTrackerIdEnum.INVALID_CODE_FORMAT.messageTrackerId)
            verify(exactly = 1) {
                eventStore.findAllByFilter(
                    EqFilter<String, String>(
                        EVENT_CODE.fieldName,
                        command.code
                    )
                )
            }
            verify(exactly = 0) {
                repository.save(any<Role>())
                eventStore.addEvent(any<RoleCreateEvent>())
            }
        }
    }

    "Feature: RoleCommandHandlerService upserting a role" - {
        "Scenario: handling RoleUpsertCommand successfully" {
            // Given
            val request = RoleRequest.build()
            val command = RoleCommandBuilder.buildCreate(request)
            val eventStore: EventStore<RoleEvent> = mockk<EventStore<RoleEvent>>()
            every {
                eventStore.findAllByFilter(
                    EqFilter<String, String>(
                        EVENT_CODE.fieldName,
                        request.code!!
                    )
                )
            } returns listOf(
                RoleEventBuilder.buildDelete(
                    RoleCommandBuilder.buildDelete(request)
                ))
            every { eventStore.addEvent(any<RoleUpsertEvent>()) } just Runs
            val repository: Repository<Role> = mockk()
            every { repository.save(any<Role>()) } just Runs
            val service = RoleCommandHandlerService(eventStore, repository)
            // When
            val event = service.handle(command)
            // Then
            assertThat(event::class).hasSameClassAs(RoleUpsertEvent::class)
            verifyOrder {
                eventStore.findAllByFilter(
                    EqFilter<String, String>(
                        EVENT_CODE.fieldName,
                        command.code
                    )
                )
                eventStore.addEvent(event)
            }
            verify(exactly = 1) {
                repository.save(any<Role>())
            }
        }

        "Scenario: handling RoleUpsertCommand for an invalid code" {
            // Given
            val request = RoleRequest.buildWithCode(value = "_INVALID")
            val command = RoleCommandBuilder.buildCreate(request)
            val eventStore: EventStore<RoleEvent> = mockk<EventStore<RoleEvent>>()
            every {
                eventStore.findAllByFilter(
                    EqFilter<String, String>(
                        EVENT_CODE.fieldName,
                        request.code!!
                    )
                )
            } returns listOf(
                RoleEventBuilder.buildDelete(
                    RoleCommandBuilder.buildDelete(request)
                ))
            every { eventStore.addEvent(any<RoleUpsertEvent>()) } just Runs
            val repository: Repository<Role> = mockk()
            every { repository.save(any<Role>()) } just Runs
            val service = RoleCommandHandlerService(eventStore, repository)
            // When
            val exception: AssertionFailedException = catchThrowableOfType(
                { service.handle(command) },
                AssertionFailedException::class.java
            )
            // Then
            assertThat(exception).hasMessage(
                "Invalid code format: A-Z, A-Z0-9_, 3 <= name.length <= 20!")
            assertThat(exception.code)
                .isEqualTo(MessageTrackerIdEnum.INVALID_CODE_FORMAT.messageTrackerId)
            verify(exactly = 1) {
                eventStore.findAllByFilter(
                    EqFilter<String, String>(
                        EVENT_CODE.fieldName,
                        command.code
                    )
                )
            }
            verify(exactly = 0) {
                repository.save(any<Role>())
                eventStore.addEvent(any<RoleUpsertEvent>())
            }
        }
    }

    "Feature: RoleCommandHandlerService updating a role" - {
        "Scenario: handling RoleUpdateCommand successfully" {
            // Given
            val request = RoleRequest.build()
            val command = RoleCommandBuilder.buildUpdate(request)
            val eventStore: EventStore<RoleEvent> = mockk<EventStore<RoleEvent>>()
            every {
                eventStore.findAllByFilter(
                    EqFilter<String, String>(
                        EVENT_ROLE_ID_ID.fieldName,
                        command.roleId.id
                    )
                )
            } returns listOf(
                RoleEventBuilder.buildCreate(
                    RoleCommandBuilder.buildCreate(request)
                )
            )
            every { eventStore.addEvent(any<RoleUpdateEvent>()) } just Runs
            val repository: Repository<Role> = mockk()
            every { repository.update(any<EqFilter<String, String>>(), any<Role>()) } just Runs
            val service = RoleCommandHandlerService(eventStore, repository)
            // When
            val event = service.handle(command)
            // Then
            assertThat(event::class).hasSameClassAs(RoleUpsertEvent::class)
            verifyOrder {
                eventStore.findAllByFilter(
                    EqFilter<String, String>(
                        EVENT_ROLE_ID_ID.fieldName,
                        command.roleId.id
                    )
                )
                eventStore.addEvent(event)
            }
            verify(exactly = 1) {
                repository.update(any<EqFilter<String, String>>(), any<Role>())
            }
        }

        "Scenario: handling RoleUpdateCommand for an invalid code" {
            // Given
            val request = RoleRequest.buildWithCode(value = "_INVALID")
            val command = RoleCommandBuilder.buildUpdate(request)
            val eventStore: EventStore<RoleEvent> = mockk<EventStore<RoleEvent>>()
            every {
                eventStore.findAllByFilter(
                    EqFilter<String, String>(
                        EVENT_ROLE_ID_ID.fieldName,
                        command.roleId.id
                    )
                )
            } returns listOf(
                RoleEventBuilder.buildCreate(
                    RoleCommandBuilder.buildCreate(request)
                ))
            every { eventStore.addEvent(any<RoleUpsertEvent>()) } just Runs
            val repository: Repository<Role> = mockk()
            every { repository.update(any<EqFilter<String, String>>(), any<Role>()) } just Runs
            val service = RoleCommandHandlerService(eventStore, repository)
            // When
            val exception: AssertionFailedException = catchThrowableOfType(
                { service.handle(command) },
                AssertionFailedException::class.java
            )
            // Then
            assertThat(exception).hasMessage(
                "Invalid code format: A-Z, A-Z0-9_, 3 <= name.length <= 20!")
            assertThat(exception.code)
                .isEqualTo(MessageTrackerIdEnum.INVALID_CODE_FORMAT.messageTrackerId)
            verify(exactly = 1) {
                eventStore.findAllByFilter(
                    EqFilter<String, String>(
                        EVENT_ROLE_ID_ID.fieldName,
                        command.roleId.id
                    )
                )
            }
            verify(exactly = 0) {
                repository.update(any<EqFilter<String, String>>(), any<Role>())
                eventStore.addEvent(any<RoleUpdateEvent>())
            }
        }
    }

    "Feature: RoleCommandHandlerService deleting a role" - {
        "Scenario: handling RoleDeleteCommand successfully"  {
            // Given
            val request = RoleRequest.build()
            val command = RoleCommandBuilder.buildDelete(request)
            val eventStore: EventStore<RoleEvent> = mockk<EventStore<RoleEvent>>()
            every {
                eventStore.findAllByFilter(
                    EqFilter<String, String>(
                        EVENT_ROLE_ID_ID.fieldName,
                        command.roleId.id
                    )
                )
            } returns listOf(
                RoleEventBuilder.buildCreate(
                    RoleCommandBuilder.buildCreate(request)
                )
            )
            every { eventStore.addEvent(any<RoleDeleteEvent>()) } just Runs
            val repository: Repository<Role> = mockk()
            every { repository.delete(any<EqFilter<String, String>>()) } just Runs
            val service = RoleCommandHandlerService(eventStore, repository)
            // When
            val event = service.handle(command)
            // Then
            assertThat(event::class).hasSameClassAs(RoleDeleteEvent::class)
            verifyOrder {
                eventStore.findAllByFilter(
                    EqFilter<String, String>(
                        EVENT_ROLE_ID_ID.fieldName,
                        command.roleId.id
                    )
                )
                eventStore.addEvent(event)
            }
            verify(exactly = 1) {
                repository.delete(any<EqFilter<String, String>>())
            }
        }

        "Scenario: handling RoleDeleteCommand for unknown role" {
            // Given
            val request = RoleRequest.build()
            val command = RoleCommandBuilder.buildDelete(request)
            val eventStore: EventStore<RoleEvent> = mockk<EventStore<RoleEvent>>()
            every {
                eventStore.findAllByFilter(
                    EqFilter<String, String>(
                        EVENT_ROLE_ID_ID.fieldName,
                        command.roleId.id
                    )
                )
            } returns listOf()
            every { eventStore.addEvent(any<RoleDeleteEvent>()) } just Runs
            val repository: Repository<Role> = mockk()
            every { repository.delete(any<EqFilter<String, String>>()) } just Runs
            val service = RoleCommandHandlerService(eventStore, repository)
            // When
            val exception: AssertionFailedException = catchThrowableOfType(
                { service.handle(command) },
                AssertionFailedException::class.java
            )
            // Then
            assertThat(exception).hasMessage(
                "There is no role defined for the given id [01FK96GENJKTN1BYZW6BRHFZFK]!"
            )
            assertThat(exception.code)
                .isEqualTo(MessageTrackerIdEnum.ROLE_NOT_DEFINED.messageTrackerId)
            verify(exactly = 1) {
                eventStore.findAllByFilter(
                    EqFilter<String, String>(
                        EVENT_ROLE_ID_ID.fieldName,
                        command.roleId.id
                    )
                )
            }
            verify(exactly = 0) {
                repository.update(any<EqFilter<String, String>>(), any<Role>())
                eventStore.addEvent(any<RoleDeleteEvent>())
            }
        }
    }
})
