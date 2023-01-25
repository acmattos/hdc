package br.com.acmattos.hdc.procedure.domain.cqs

import br.com.acmattos.hdc.common.context.domain.cqs.AuditableCommand
import br.com.acmattos.hdc.common.context.domain.cqs.CreateCommand
import br.com.acmattos.hdc.common.context.domain.cqs.DeleteCommand
import br.com.acmattos.hdc.common.context.domain.cqs.UpdateCommand
import br.com.acmattos.hdc.common.context.domain.cqs.UpsertCommand
import br.com.acmattos.hdc.common.context.domain.model.AuditLog
import br.com.acmattos.hdc.procedure.domain.model.ProcedureId
import java.time.LocalDateTime

/**
 * @author ACMattos
 * @since 19/03/2022.
 */
open class ProcedureCommand(
    open val procedureId: ProcedureId,
    override val auditLog: AuditLog,
): AuditableCommand(auditLog)

/**
 * @author ACMattos
 * @since 19/03/2022.
 */
data class ProcedureCreateCommand private constructor(
    override val procedureId: ProcedureId,
    val code: Int,
    val description: String,
    val enabled: Boolean,
    val createdAt: LocalDateTime,
    override val auditLog: AuditLog,
): CreateCommand, ProcedureCommand(procedureId, auditLog) {
    constructor(
        code: Int,
        description: String,
        auditLog: AuditLog,
    ): this(
        procedureId = ProcedureId(),
        code = code,
        description = description,
        enabled = true,
        createdAt = LocalDateTime.now(),
        auditLog =auditLog,
    )
}

/**
 * @author ACMattos
 * @since 10/01/2023.
 */
data class ProcedureUpsertCommand private constructor(
    override val procedureId: ProcedureId,
    val code: Int,
    val description: String,
    val enabled: Boolean,
    val updatedAt: LocalDateTime,
    val deletedAt: LocalDateTime?,
    override val auditLog: AuditLog,
    val events: List<ProcedureEvent>,
): UpsertCommand, ProcedureCommand(procedureId, auditLog) {
    constructor(
        command: ProcedureCreateCommand,
        events: List<ProcedureEvent>,
    ): this(
        procedureId = events.last().procedureId,
        code = command.code,
        description = command.description,
        enabled = command.enabled,
        updatedAt = LocalDateTime.now(),
        deletedAt = null,
        auditLog = command.auditLog,
        events = events,
    )
}

/**
 * @author ACMattos
 * @since 24/03/2022.
 */
data class ProcedureUpdateCommand private constructor(
    override val procedureId: ProcedureId,
    val code: Int,
    val description: String,
    val enabled: Boolean,
    val updatedAt: LocalDateTime,
    override val auditLog: AuditLog,
): UpdateCommand, ProcedureCommand(procedureId, auditLog)  {
    constructor(
        procedureId: String,
        code: Int,
        description: String,
        enabled: Boolean,
        auditLog: AuditLog,
    ): this(
        procedureId = ProcedureId(procedureId),
        code = code,
        description = description,
        enabled = enabled,
        updatedAt = LocalDateTime.now(),
        auditLog = auditLog,
    )
}

/**
 * @author ACMattos
 * @since 26/03/2022.
 */
data class ProcedureDeleteCommand private constructor(
    override val procedureId: ProcedureId,
    val code: Int,
    val updatedAt: LocalDateTime?,
    val deletedAt: LocalDateTime,
    override val auditLog: AuditLog,
): DeleteCommand, ProcedureCommand(procedureId, auditLog)  {
    constructor(
        procedureId: String,
        code: String,
        auditLog: AuditLog,
    ): this(
        procedureId = ProcedureId(procedureId),
        code = code.toInt(),
        updatedAt = null,
        deletedAt = LocalDateTime.now(),
        auditLog = auditLog,
    )
}
