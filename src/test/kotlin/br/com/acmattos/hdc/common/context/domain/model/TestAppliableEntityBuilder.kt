package br.com.acmattos.hdc.common.context.domain.model

import br.com.acmattos.hdc.common.context.domain.cqs.*
import java.time.LocalDateTime

/**
 * @author ACMattos
 * @since 17/01/2023.
 */
object TestAppliableEntityBuilder {
    fun buildCreated(event: TestEvent) = TestAppliableEntity.apply(event)

    fun buildUpdated(event: TestEvent) = TestAppliableEntity.apply(
        listOf(TestAppliableEntityEventBuilder.buildCreateEvent(), event)
    )

    fun buildDeleted(event: TestEvent) = TestAppliableEntity.apply(
        listOf(
            TestAppliableEntityEventBuilder.buildCreateEvent(),
            TestAppliableEntityEventBuilder.buildUpdateEvent(),
            event
        )
    )

    fun buildUpserted(event: TestEvent) = TestAppliableEntity.apply(
        listOf(
            TestAppliableEntityEventBuilder.buildCreateEvent(),
            TestAppliableEntityEventBuilder.buildUpdateEvent(),
            TestAppliableEntityEventBuilder.buildDeleteEvent(),
            event
        )
    )
}



/**
 * @author ACMattos
 * @since 17/01/2023.
 */
object DateTimetBuilder {
    fun createdAt(): LocalDateTime = LocalDateTime.of(2023, 1, 16, 0, 0, 0)
    fun updatedAt(day: Int = 17): LocalDateTime = LocalDateTime.of(2023, 1, day, 0, 0, 0)
    fun deletedAt(day: Int = 18): LocalDateTime = LocalDateTime.of(2023, 1, day, 0, 0, 0)
}
/**
 * @author ACMattos
 * @since 17/01/2023.
 */
object TestAppliableEntityEventBuilder {
    fun buildCreateEvent() = TestCreateEvent()
    fun buildInvalidCreateEvent(
        updatedAt: LocalDateTime? = DateTimetBuilder.updatedAt(),
        deletedAt: LocalDateTime? = DateTimetBuilder.deletedAt(),
    ) = TestCreateEvent(updatedAt = updatedAt, deletedAt = deletedAt)

    fun buildUpsertEvent() = TestUpsertEvent()
    fun buildInvalidUpsertEvent(
        deletedAt: LocalDateTime? = DateTimetBuilder.deletedAt(),
    ) = TestUpsertEvent(deletedAt = deletedAt)

    fun buildUpdateEvent() = TestUpdateEvent()
    fun buildInvalidUpdateEvent(
        deletedAt: LocalDateTime? = DateTimetBuilder.deletedAt(),
    ) = TestUpdateEvent(deletedAt = deletedAt)

    fun buildDeleteEvent() = TestDeleteEvent()
    fun buildInvalidDeleteEvent(
        updatedAt: LocalDateTime? = DateTimetBuilder.updatedAt(),
    ) = TestDeleteEvent(updatedAt = updatedAt)
}

open class TestEvent(
    override val auditLog: AuditLog = AuditLog("who", "what"),
): EntityEvent(auditLog)

data class TestCreateEvent(
    override val createdAt: LocalDateTime = DateTimetBuilder.createdAt(),
    val updatedAt: LocalDateTime? = null,
    val deletedAt: LocalDateTime? = null,
): CreateEvent, TestEvent()

data class TestUpsertEvent(
    override val updatedAt: LocalDateTime = DateTimetBuilder.updatedAt(),
    override val deletedAt: LocalDateTime? = null,
): UpsertEvent, TestEvent()

data class TestUpdateEvent(
    override val updatedAt: LocalDateTime = DateTimetBuilder.updatedAt(),
    val deletedAt: LocalDateTime? = null,
): UpdateEvent, TestEvent()

data class TestDeleteEvent(
    override val updatedAt: LocalDateTime? = null,
    override val deletedAt: LocalDateTime = DateTimetBuilder.deletedAt(),
): DeleteEvent, TestEvent()
