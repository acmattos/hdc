package br.com.acmattos.hdc.common.context.domain.model

import br.com.acmattos.hdc.common.context.config.ContextLogEnum.ENTITY
import br.com.acmattos.hdc.common.context.config.MessageTrackerIdEnum.DELETED_AT_NEEDS_TO_BE_NOT_NULL
import br.com.acmattos.hdc.common.context.config.MessageTrackerIdEnum.DELETED_AT_NEEDS_TO_BE_NULL
import br.com.acmattos.hdc.common.context.config.MessageTrackerIdEnum.UPDATED_AT_NEEDS_TO_BE_NOT_NULL
import br.com.acmattos.hdc.common.context.config.MessageTrackerIdEnum.UPDATED_AT_NEEDS_TO_BE_NULL
import br.com.acmattos.hdc.common.context.domain.cqs.CreateEvent
import br.com.acmattos.hdc.common.context.domain.cqs.DeleteEvent
import br.com.acmattos.hdc.common.context.domain.cqs.EntityEvent
import br.com.acmattos.hdc.common.context.domain.cqs.Message
import br.com.acmattos.hdc.common.context.domain.cqs.UpdateEvent
import br.com.acmattos.hdc.common.context.domain.cqs.UpsertEvent
import br.com.acmattos.hdc.common.tool.assertion.Assertion
import br.com.acmattos.hdc.common.tool.loggable.Loggable
import java.time.LocalDateTime

/**
 * @author ACMattos
 * @since 27/07/2019.
 */
interface Entity: Message

/**
 * @author ACMattos
 * @since 04/01/2023.
 */
interface AppliableEntity: Entity {
    var createdAtData: LocalDateTime
    var updatedAtData: LocalDateTime?
    var deletedAtData: LocalDateTime?

    fun apply(events: List<EntityEvent>): AppliableEntity {
        for (event in events) {
            apply(event)
            logger.trace(
                "[{} {}] - Event [{}] applied to Entity: -> {} <-",
                this.javaClass.simpleName.toUpperCase(),
                ENTITY.name,
                event.javaClass.simpleName,
                this.toString()
            )
        }
        return this
    }

    fun apply(event: EntityEvent): AppliableEntity {
        when(event) {
            is CreateEvent -> apply(event as CreateEvent)
            is UpdateEvent -> apply(event as UpdateEvent)
            is UpsertEvent -> apply(event as UpsertEvent)
            else -> apply(event as DeleteEvent)
        }
        return this
    }

    fun apply(event: CreateEvent) {
        createdAtData = event.createdAt
        assertUpdatedAtIsNull()
        assertDeletedAtIsNull()
    }

    fun apply(event: UpsertEvent) {
        updatedAtData = event.updatedAt
        deletedAtData = event.deletedAt
        assertUpdatedAtIsNotNull()
        assertDeletedAtIsNull()
    }

    fun apply(event: UpdateEvent) {
        updatedAtData = event.updatedAt
        assertUpdatedAtIsNotNull()
        assertDeletedAtIsNull()
    }

    fun apply(event: DeleteEvent) {
        deletedAtData = event.deletedAt
        assertUpdatedAtIsNull()
        assertDeletedAtIsNotNull()
    }

    private fun assertUpdatedAtIsNull() {
        Assertion.assert(
            "Invalid 'updatedAt' date: needs to be null!",
            "${ENTITY.name} $UPDATED_AT_NEEDS_TO_BE_NULL",
            UPDATED_AT_NEEDS_TO_BE_NULL
        ) {
            updatedAtData == null
        }
    }

    fun assertUpdatedAtIsNotNull() {
        Assertion.assert(
            "Invalid 'updatedAt' date: needs to be not null!",
            "${ENTITY.name} $UPDATED_AT_NEEDS_TO_BE_NOT_NULL",
            UPDATED_AT_NEEDS_TO_BE_NOT_NULL
        ) {
            updatedAtData != null
        }
    }

    fun assertDeletedAtIsNull() {
        Assertion.assert(
            "Invalid 'deletedAt' date: needs to be null!",
            "${ENTITY.name} $DELETED_AT_NEEDS_TO_BE_NULL",
            DELETED_AT_NEEDS_TO_BE_NULL
        ) {
            deletedAtData == null
        }
    }

    fun assertDeletedAtIsNotNull()  {
        Assertion.assert(
            "Invalid 'deletedAt' date: needs to be not null!",
            "${ENTITY.name} $DELETED_AT_NEEDS_TO_BE_NOT_NULL",
            DELETED_AT_NEEDS_TO_BE_NOT_NULL
        ) {
            deletedAtData != null
        }
    }

    companion object: Loggable()
}

/**
 * @author ACMattos
 * @since 28/06/2020.
 */
interface ValueObject
