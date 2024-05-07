package br.com.acmattos.hdc.common.context.domain.model

import br.com.acmattos.hdc.common.tool.assertion.Assertion
import br.com.acmattos.hdc.common.tool.loggable.LogEventsAppender
import io.kotest.core.spec.style.FreeSpec
import org.assertj.core.api.AbstractThrowableAssert
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatCode

private const val UPDATED_AT_NEEDS_TO_BE_NULL =
    "[ENTITY UPDATED_AT_NEEDS_TO_BE_NULL ASSERTION <UPDATED_AT_NEEDS_TO_BE_NULL> FAILURE]: -> 'Invalid 'updatedAt' date: needs to be null!' <-"
private const val DELETED_AT_NEEDS_TO_BE_NULL =
    "[ENTITY DELETED_AT_NEEDS_TO_BE_NULL ASSERTION <DELETED_AT_NEEDS_TO_BE_NULL> FAILURE]: -> 'Invalid 'deletedAt' date: needs to be null!' <-"

/**
 * @author ACMattos
 * @since 17/01/2023.
 */
class AppliableEntityTest: FreeSpec({
    "Feature: ${AppliableEntity::class.java.simpleName} usage" - {
        "Scenario: creating valid entity" - {
            lateinit var event: TestEvent
            lateinit var appender: LogEventsAppender
            lateinit var entity: AppliableEntity
            "Given: a valid event: ${TestCreateEvent::class.java.simpleName}" {
                event = TestAppliableEntityEventBuilder.buildCreateEvent()
            }
            "And: a prepared ${LogEventsAppender::class.java.simpleName}" {
                appender = LogEventsAppender(Assertion::class.java)
            }
            "When: #apply the event to the entity is executed" {
                entity = TestAppliableEntityBuilder.buildCreated(event)
            }
            "Then: .createdAtData is ${DateTimetBuilder.createdAt()}" {
                assertThat(entity.createdAtData).isEqualTo(DateTimetBuilder.createdAt())
            }
            "And: .updatedAtData is null" {
                assertThat(entity.updatedAtData).isNull()
            }
            "And: .deletedAtData is null" {
                assertThat(entity.deletedAtData).isNull()
            }
        }

        "Scenario: creating invalid entity: .updatedAtData is not null" - {
            lateinit var event: TestEvent
            lateinit var appender: LogEventsAppender
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            "Given: an invalid event: ${TestCreateEvent::class.java.simpleName}" {
                event = TestAppliableEntityEventBuilder.buildInvalidCreateEvent(deletedAt = null)
            }
            "And: a prepared ${LogEventsAppender::class.java.simpleName}" {
                appender = LogEventsAppender(Assertion::class.java)
            }
            "When: #apply the event to the entity is executed" {
                assertion = assertThatCode { TestAppliableEntityBuilder.buildCreated(event) }
            }
            "Then: the message is UPDATED_AT_NEEDS_TO_BE_NULL" {
                assertThat(appender.containsMessage(UPDATED_AT_NEEDS_TO_BE_NULL)).isTrue()
            }
            "And: the assertion message is the expected" {
                assertion.hasMessage("Invalid 'updatedAt' date: needs to be null!")
            }
        }

        "Scenario: creating invalid entity: .deletedAtData is not null" - {
            lateinit var event: TestEvent
            lateinit var appender: LogEventsAppender
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            "Given: an invalid event: ${TestCreateEvent::class.java.simpleName}" {
                event = TestAppliableEntityEventBuilder.buildInvalidCreateEvent(updatedAt = null)
            }
            "And: a prepared ${LogEventsAppender::class.java.simpleName}" {
                appender = LogEventsAppender(Assertion::class.java)
            }
            "When: #apply the event to the entity is executed" {
                assertion = assertThatCode { TestAppliableEntityBuilder.buildCreated(event) }
            }
            "Then: the message is $DELETED_AT_NEEDS_TO_BE_NULL" {
                assertThat(appender.containsMessage(DELETED_AT_NEEDS_TO_BE_NULL)).isTrue()
            }
            "And: the assertion message is the expected" {
                assertion.hasMessage("Invalid 'deletedAt' date: needs to be null!")
            }
        }

//        "Scenario: updating valid entity" - {
//            lateinit var event: TestEvent
//            lateinit var appender: LogEventsAppender
//            lateinit var entity: AppliableEntity
//            "Given: a valid event: ${TestUpdateEvent::class.java.simpleName}" {
//                event = TestAppliableEntityEventBuilder.buildUpdateEvent()
//            }
//            "And: a prepared ${LogEventsAppender::class.java.simpleName}" {
//                appender = LogEventsAppender(Assertion::class.java)
//            }
//            "When: #apply the event to the entity is executed" {
//                entity = TestAppliableEntityBuilder.buildUpdated(event)
//            }
//            "Then: .createdAtData is ${DateTimetBuilder.createdAt()}" {
//                assertThat(entity.createdAtData).isEqualTo(DateTimetBuilder.createdAt())
//            }
//            "And: .updatedAtData is not null" {
//                assertThat(entity.updatedAtData).isEqualTo(DateTimetBuilder.updatedAt())
//            }
//            "And: .deletedAtData is null" {
//                assertThat(entity.deletedAtData).isNull()
//            }
//        }

//        "Scenario: updating invalid entity: .deletedAtData is not null" - {
//            lateinit var event: TestEvent
//            lateinit var appender: LogEventsAppender
//            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
//            "Given: an invalid event: ${TestUpdateEvent::class.java.simpleName}" {
//                event = TestAppliableEntityEventBuilder.buildInvalidUpdateEvent()
//            }
//            "And: a prepared ${LogEventsAppender::class.java.simpleName}" {
//                appender = LogEventsAppender(Assertion::class.java)
//            }
//            "When: #apply the event to the entity is executed" {
//                assertion = assertThatCode { TestAppliableEntityBuilder.buildUpdated(event) }
//            }
//            "Then: the message is $DELETED_AT_NEEDS_TO_BE_NULL" {
//                assertThat(appender.containsMessage(DELETED_AT_NEEDS_TO_BE_NULL)).isTrue()
//            }
//            "And: the assertion message is the expected" {
//                assertion.hasMessage("Invalid 'deletedAt' date: needs to be null!")
//            }
//        }

        "Scenario: deleting valid entity" - {
            lateinit var event: TestEvent
            lateinit var appender: LogEventsAppender
            lateinit var entity: AppliableEntity
            "Given: a valid event: ${TestDeleteEvent::class.java.simpleName}" {
                event = TestAppliableEntityEventBuilder.buildDeleteEvent()
            }
            "And: a prepared ${LogEventsAppender::class.java.simpleName}" {
                appender = LogEventsAppender(Assertion::class.java)
            }
            "When: #apply the event to the entity is executed" {
                entity = TestAppliableEntityBuilder.buildDeleted(event)
            }
            "Then: .createdAtData is ${DateTimetBuilder.createdAt()}" {
                assertThat(entity.createdAtData).isEqualTo(DateTimetBuilder.createdAt())
            }
            "And: .updatedAtData is null" {
                assertThat(entity.updatedAtData).isNull()
            }
            "And: .deletedAtData is not null" {
                assertThat(entity.deletedAtData).isEqualTo(DateTimetBuilder.deletedAt())
            }
        }

//        "Scenario: deleting invalid entity: .updatedAtData is not null" - {
//            lateinit var event: TestEvent
//            lateinit var appender: LogEventsAppender
//            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
//            "Given: an invalid event: ${TestDeleteEvent::class.java.simpleName}" {
//                event = TestAppliableEntityEventBuilder.buildInvalidDeleteEvent()
//            }
//            "And: a prepared ${LogEventsAppender::class.java.simpleName}" {
//                appender = LogEventsAppender(Assertion::class.java)
//            }
//            "When: #apply the event to the entity is executed" {
//                assertion = assertThatCode { TestAppliableEntityBuilder.buildDeleted(event) }
//            }
//            "Then: the message is $UPDATED_AT_NEEDS_TO_BE_NULL" {
//                assertThat(appender.containsMessage(UPDATED_AT_NEEDS_TO_BE_NULL)).isTrue()
//            }
//            "And: the assertion message is the expected" {
//                assertion.hasMessage("Invalid 'updatedAt' date: needs to be null!")
//            }
//        }

        "Scenario: upserting valid entity" - {
            lateinit var event: TestEvent
            lateinit var appender: LogEventsAppender
            lateinit var entity: AppliableEntity
            "Given: a valid event: ${TestUpsertEvent::class.java.simpleName}" {
                event = TestAppliableEntityEventBuilder.buildUpsertEvent()
            }
            "And: a prepared ${LogEventsAppender::class.java.simpleName}" {
                appender = LogEventsAppender(Assertion::class.java)
            }
            "When: #apply the event to the entity is executed" {
                entity = TestAppliableEntityBuilder.buildUpserted(event)
            }
            "Then: .createdAtData is ${DateTimetBuilder.createdAt()}" {
                assertThat(entity.createdAtData).isEqualTo(DateTimetBuilder.createdAt())
            }
            "And: .updatedAtData is not null" {
                assertThat(entity.updatedAtData).isEqualTo(DateTimetBuilder.updatedAt())
            }
            "And: .deletedAtData is null" {
                assertThat(entity.deletedAtData).isNull()
            }
        }

//        "Scenario: upserting invalid entity: .updatedAtData is not null" - {
//            lateinit var event: TestEvent
//            lateinit var appender: LogEventsAppender
//            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
//            "Given: an invalid event: ${TestUpsertEvent::class.java.simpleName}" {
//                event = TestAppliableEntityEventBuilder.buildInvalidUpsertEvent()
//            }
//            "And: a prepared ${LogEventsAppender::class.java.simpleName}" {
//                appender = LogEventsAppender(Assertion::class.java)
//            }
//            "When: #apply the event to the entity is executed" {
//                assertion = assertThatCode { TestAppliableEntityBuilder.buildUpserted(event) }
//            }
//            "Then: the message is $DELETED_AT_NEEDS_TO_BE_NULL" {
//                assertThat(appender.containsMessage(DELETED_AT_NEEDS_TO_BE_NULL)).isTrue()
//            }
//            "And: the assertion message is the expected" {
//                assertion.hasMessage("Invalid 'deletedAt' date: needs to be null!")
//            }
//        }
    }
})

