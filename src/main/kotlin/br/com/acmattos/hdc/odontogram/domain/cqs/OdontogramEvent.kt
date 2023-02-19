package br.com.acmattos.hdc.odontogram.domain.cqs

import br.com.acmattos.hdc.common.context.domain.cqs.CreateEvent
import br.com.acmattos.hdc.common.context.domain.cqs.DeleteEvent
import br.com.acmattos.hdc.common.context.domain.cqs.EntityEvent
import br.com.acmattos.hdc.common.context.domain.cqs.UpdateEvent
import br.com.acmattos.hdc.common.context.domain.cqs.UpsertEvent
import br.com.acmattos.hdc.common.context.domain.model.AuditLog
import br.com.acmattos.hdc.odontogram.domain.model.OdontogramId
import br.com.acmattos.hdc.odontogram.domain.model.Tooth
import java.time.LocalDateTime

/**
 * @author ACMattos
 * @since 29/08/2022.
 */
open class OdontogramEvent(
    open val odontogramId: OdontogramId,
    override val auditLog: AuditLog
): EntityEvent(auditLog)

/**
 * @author ACMattos
 * @since 29/08/2022.
 */
data class OdontogramCreateEvent(
    override val odontogramId: OdontogramId,
    val upperLeft: List<Tooth>,
    val upperRight: List<Tooth>,
    val lowerLeft: List<Tooth>,
    val lowerRight: List<Tooth>,
    val upperLeftChild: List<Tooth>,
    val upperRightChild: List<Tooth>,
    val lowerLeftChild: List<Tooth>,
    val lowerRightChild: List<Tooth>,
    val enabled: Boolean,
    override val createdAt: LocalDateTime,
    override val auditLog: AuditLog
): CreateEvent, OdontogramEvent(odontogramId, auditLog) {
    constructor(
        command: OdontogramCreateCommand
    ): this(
        command.odontogramId,
        command.upperLeft,
        command.upperRight,
        command.lowerLeft,
        command.lowerRight,
        command.upperLeftChild,
        command.upperRightChild,
        command.lowerLeftChild,
        command.lowerRightChild,
        command.enabled,
        command.createdAt,
        command.auditLog,
    )
}

/**
 * @author ACMattos
 * @since 13/02/2023.
 */
data class OdontogramUpsertEvent(
    override val odontogramId: OdontogramId,
    val upperLeft: List<Tooth>,
    val upperRight: List<Tooth>,
    val lowerLeft: List<Tooth>,
    val lowerRight: List<Tooth>,
    val upperLeftChild: List<Tooth>,
    val upperRightChild: List<Tooth>,
    val lowerLeftChild: List<Tooth>,
    val lowerRightChild: List<Tooth>,
    val enabled: Boolean,
    override val updatedAt: LocalDateTime,
    override val deletedAt: LocalDateTime?,
    override val auditLog: AuditLog,
): UpsertEvent, OdontogramEvent(odontogramId, auditLog) {
    constructor(
        command: OdontogramUpsertCommand
    ): this(
        command.odontogramId,
        command.upperLeft,
        command.upperRight,
        command.lowerLeft,
        command.lowerRight,
        command.upperLeftChild,
        command.upperRightChild,
        command.lowerLeftChild,
        command.lowerRightChild,
        command.enabled,
        command.updatedAt,
        command.deletedAt,
        auditLog = command.auditLog,
    )
}

/**
 * @author ACMattos
 * @since 03/01/2023.
 */
data class OdontogramUpdateEvent(
    override val odontogramId: OdontogramId,
    val upperLeft: List<Tooth>,
    val upperRight: List<Tooth>,
    val lowerLeft: List<Tooth>,
    val lowerRight: List<Tooth>,
    val upperLeftChild: List<Tooth>,
    val upperRightChild: List<Tooth>,
    val lowerLeftChild: List<Tooth>,
    val lowerRightChild: List<Tooth>,
    val enabled: Boolean,
    override val updatedAt: LocalDateTime,
    override val auditLog: AuditLog
): UpdateEvent, OdontogramEvent(odontogramId, auditLog) {
    constructor(
        command: OdontogramUpdateCommand
    ): this(
        command.odontogramId,
        command.upperLeft,
        command.upperRight,
        command.lowerLeft,
        command.lowerRight,
        command.upperLeftChild,
        command.upperRightChild,
        command.lowerLeftChild,
        command.lowerRightChild,
        command.enabled,
        command.updatedAt,
        command.auditLog,
    )
}

/**
 * @author ACMattos
 * @since 03/01/2023.
 */
data class OdontogramDeleteEvent(
    override val odontogramId: OdontogramId,
    override val updatedAt: LocalDateTime?,
    override val deletedAt: LocalDateTime,
    override val auditLog: AuditLog,
): DeleteEvent, OdontogramEvent(odontogramId, auditLog) {
    constructor(
        command: OdontogramDeleteCommand
    ): this(
        odontogramId = command.odontogramId,
        command.updatedAt,
        command.deletedAt,
        auditLog = command.auditLog
    )
}
