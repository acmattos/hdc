package br.com.acmattos.hdc.procedure.domain.cqs

import br.com.acmattos.hdc.common.context.domain.cqs.Command
import br.com.acmattos.hdc.common.context.domain.model.AuditLog
import br.com.acmattos.hdc.procedure.domain.model.ProcedureId
import java.time.LocalDateTime

/**
 * @author ACMattos
 * @since 19/03/2022.
 */
open class ProcedureCommand(
    open val procedureId: ProcedureId,
    override val auditLog: AuditLog
): Command(auditLog)

/**
 * @author ACMattos
 * @since 19/03/2022.
 */
data class CreateDentalProcedureCommand(
    override val procedureId: ProcedureId,
    val code: Int,
    val description: String,
    val enabled: Boolean,
    val createdAt: LocalDateTime,
    override val auditLog: AuditLog
): ProcedureCommand(procedureId, auditLog) {
    constructor(
        code: Int,
        description: String,
        auditLog: AuditLog
    ): this(
        ProcedureId(),
        code,
        description,
        true,
        LocalDateTime.now(),
        auditLog
    )
}

/**
 * @author ACMattos
 * @since 24/03/2022.
 */
data class UpdateDentalProcedureCommand(
    override val procedureId: ProcedureId,
    val code: Int,
    val description: String,
    val enabled: Boolean,
    val updatedAt: LocalDateTime,
    override val auditLog: AuditLog
): ProcedureCommand(procedureId, auditLog) {
    constructor(
        procedureId: String,
        code: Int,
        description: String,
        enabled: Boolean,
        auditLog: AuditLog
    ): this(
        ProcedureId(procedureId),
        code,
        description,
        enabled,
        LocalDateTime.now(),
        auditLog
    )
}

/**
 * @author ACMattos
 * @since 26/03/2022.
 */
data class DeleteDentalProcedureCommand(
    override val procedureId: ProcedureId,
    override val auditLog: AuditLog
): ProcedureCommand(procedureId, auditLog) {
    constructor(
        procedureId: String,
        auditLog: AuditLog
    ): this(
        ProcedureId(procedureId),
        auditLog
    )
}
