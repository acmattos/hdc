package br.com.acmattos.hdc.odontogram.domain.cqs

import br.com.acmattos.hdc.common.context.domain.cqs.AuditableCommand
import br.com.acmattos.hdc.common.context.domain.cqs.CreateCommand
import br.com.acmattos.hdc.common.context.domain.cqs.DeleteCommand
import br.com.acmattos.hdc.common.context.domain.cqs.UpdateCommand
import br.com.acmattos.hdc.common.context.domain.cqs.UpsertCommand
import br.com.acmattos.hdc.common.context.domain.model.AuditLog
import br.com.acmattos.hdc.odontogram.domain.model.OdontogramId
import br.com.acmattos.hdc.odontogram.domain.model.Tooth
import java.time.LocalDateTime

/**
 * @author ACMattos
 * @since 29/08/2022.
 */
open class OdontogramCommand(
    open val odontogramId: OdontogramId,
    override val auditLog: AuditLog
): AuditableCommand(auditLog)

/**
 * @author ACMattos
 * @since 29/08/2022.
 */
data class OdontogramCreateCommand(
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
    val createdAt: LocalDateTime,
    override val auditLog: AuditLog,
): CreateCommand, OdontogramCommand(odontogramId, auditLog) {
    constructor(
        upperLeft: List<Tooth>,
        upperRight: List<Tooth>,
        lowerLeft: List<Tooth>,
        lowerRight: List<Tooth>,
        upperLeftChild: List<Tooth>,
        upperRightChild: List<Tooth>,
        lowerLeftChild: List<Tooth>,
        lowerRightChild: List<Tooth>,
        auditLog: AuditLog
    ): this(
        OdontogramId(),
        upperLeft,
        upperRight,
        lowerLeft,
        lowerRight,
        upperLeftChild,
        upperRightChild,
        lowerLeftChild,
        lowerRightChild,
        true,
        LocalDateTime.now(),
        auditLog,
    )
}

/**
 * @author ACMattos
 * @since 13/02/2023.
 */
data class OdontogramUpsertCommand(
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
    val updatedAt: LocalDateTime,
    val deletedAt: LocalDateTime?,
    override val auditLog: AuditLog,
    val events: List<OdontogramEvent>,
): UpsertCommand, OdontogramCommand(odontogramId, auditLog) {
    constructor(
        command: OdontogramCreateCommand,
        events: List<OdontogramEvent>,
    ): this(
        events.last().odontogramId,
        command.upperLeft,
        command.upperRight,
        command.lowerLeft,
        command.lowerRight,
        command.upperLeftChild,
        command.upperRightChild,
        command.lowerLeftChild,
        command.lowerRightChild,
        command.enabled,
        LocalDateTime.now(),
        null,
        command.auditLog,
        events,
    )
}

/**
 * @author ACMattos
 * @since 03/01/2023.
 */
data class OdontogramUpdateCommand(
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
    val updatedAt: LocalDateTime,
    override val auditLog: AuditLog,
): UpdateCommand, OdontogramCommand(odontogramId, auditLog) {
    constructor(
        odontogramId: String,
        upperLeft: List<Tooth>,
        upperRight: List<Tooth>,
        lowerLeft: List<Tooth>,
        lowerRight: List<Tooth>,
        upperLeftChild: List<Tooth>,
        upperRightChild: List<Tooth>,
        lowerLeftChild: List<Tooth>,
        lowerRightChild: List<Tooth>,
        enabled: Boolean,
        auditLog: AuditLog,
    ): this(
        OdontogramId(odontogramId),
        upperLeft,
        upperRight,
        lowerLeft,
        lowerRight,
        upperLeftChild,
        upperRightChild,
        lowerLeftChild,
        lowerRightChild,
        enabled,
        LocalDateTime.now(),
        auditLog,
    )
}

/**
 * @author ACMattos
 * @since 03/01/2023.
 */
data class OdontogramDeleteCommand(
    override val odontogramId: OdontogramId,
    val updatedAt: LocalDateTime?,
    val deletedAt: LocalDateTime,
    override val auditLog: AuditLog,
): DeleteCommand, OdontogramCommand(odontogramId, auditLog) {
    constructor(
        odontogramId: String,
        auditLog: AuditLog,
    ): this(
        OdontogramId(odontogramId),
        null,
        LocalDateTime.now(),
        auditLog
    )
}
