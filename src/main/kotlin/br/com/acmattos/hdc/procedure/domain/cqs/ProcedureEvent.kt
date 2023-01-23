package br.com.acmattos.hdc.procedure.domain.cqs

import br.com.acmattos.hdc.common.context.domain.cqs.CreateEvent
import br.com.acmattos.hdc.common.context.domain.cqs.DeleteEvent
import br.com.acmattos.hdc.common.context.domain.cqs.EntityEvent
import br.com.acmattos.hdc.common.context.domain.cqs.UpdateEvent
import br.com.acmattos.hdc.common.context.domain.cqs.UpsertEvent
import br.com.acmattos.hdc.common.context.domain.model.AuditLog
import br.com.acmattos.hdc.procedure.domain.model.ProcedureId
import java.time.LocalDateTime

/**
 * @author ACMattos
 * @since 19/03/2022.
 */
open class ProcedureEvent(
    open val procedureId: ProcedureId,
    override val auditLog: AuditLog
): EntityEvent(auditLog)

/**
 * @author ACMattos
 * @since 19/03/2022.
 */
data class ProcedureCreateEvent(
    override val procedureId: ProcedureId,
    val code: Int,
    val description: String,
    val enabled: Boolean,
    override val createdAt: LocalDateTime,
    override val auditLog: AuditLog,
): CreateEvent, ProcedureEvent(procedureId, auditLog) {
    constructor(
        command: ProcedureCreateCommand
    ): this(
        procedureId = command.procedureId,
        code = command.code,
        description = command.description,
        enabled = command.enabled,
        createdAt = command.createdAt,
        auditLog = command.auditLog,
    )
}

/**
 * @author ACMattos
 * @since 25/03/2022.
 */
data class ProcedureUpsertEvent(
    override val procedureId: ProcedureId,
    val code: Int,
    val description: String,
    val enabled: Boolean,
    override val updatedAt: LocalDateTime,
    override val deletedAt: LocalDateTime?,
    override val auditLog: AuditLog,
): UpsertEvent, ProcedureEvent(procedureId, auditLog) {
    constructor(
        command: ProcedureUpsertCommand
    ): this(
        procedureId = command.procedureId,
        code = command.code,
        description = command.description,
        enabled = command.enabled,
        updatedAt = command.updatedAt,
        deletedAt = command.deletedAt,
        auditLog = command.auditLog,
    )
}

/**
 * @author ACMattos
 * @since 25/03/2022.
 */
data class ProcedureUpdateEvent(
    override val procedureId: ProcedureId,
    val code: Int,
    val description: String,
    val enabled: Boolean,
    override val updatedAt: LocalDateTime,
    override val auditLog: AuditLog,
): UpdateEvent, ProcedureEvent(procedureId, auditLog) {
    constructor(
        command: ProcedureUpdateCommand
    ): this(
        procedureId = command.procedureId,
        code = command.code,
        description = command.description,
        enabled = command.enabled,
        updatedAt = command.updatedAt,
        auditLog = command.auditLog,
    )
}

/**
 * @author ACMattos
 * @since 25/03/2022.
 */
data class ProcedureDeleteEvent(
    override val procedureId: ProcedureId,
    val code: Int,
    override val deletedAt: LocalDateTime,
    override val auditLog: AuditLog,
): DeleteEvent, ProcedureEvent(procedureId, auditLog) {
    constructor(
        command: ProcedureDeleteCommand
    ): this(
        procedureId = command.procedureId,
        code = command.code,
        deletedAt = command.deletedAt,
        auditLog = command.auditLog,
    )
}
