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
data class ProcedureCreateCommand(
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
        ProcedureId(),
        code,
        description,
        true,
        LocalDateTime.now(),
        auditLog,
    )
}

/**
 * @author ACMattos
 * @since 10/01/2023.
 */
data class ProcedureUpsertCommand(
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
        events.last().procedureId,
        command.code,
        command.description,
        command.enabled,
        LocalDateTime.now(),
        null,
        command.auditLog,
        events,
    )
}

/**
 * @author ACMattos
 * @since 24/03/2022.
 */
data class ProcedureUpdateCommand(
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
        ProcedureId(procedureId),
        code,
        description,
        enabled,
        LocalDateTime.now(),
        auditLog,
    )
}

/**
 * @author ACMattos
 * @since 26/03/2022.
 */
data class ProcedureDeleteCommand(
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
        ProcedureId(procedureId),
        code.toInt(),
        updatedAt = null,
        deletedAt = LocalDateTime.now(),
        auditLog = auditLog,
    )
}
