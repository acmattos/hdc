package br.com.acmattos.hdc.procedure.domain.cqs

import br.com.acmattos.hdc.common.context.domain.cqs.Event
import br.com.acmattos.hdc.common.context.domain.model.AuditLog
import br.com.acmattos.hdc.procedure.domain.model.ProcedureId
import com.fasterxml.jackson.annotation.JsonTypeName
import java.time.LocalDateTime

/**
 * @author ACMattos
 * @since 19/03/2022.
 */
open class ProcedureEvent(
    open val procedureId: ProcedureId,
    override val auditLog: AuditLog
): Event(auditLog)

/**
 * @author ACMattos
 * @since 19/03/2022.
 */
@JsonTypeName("CreateDentalProcedureEvent")
data class CreateDentalProcedureEvent(
    override val procedureId: ProcedureId,
    val code: Int,
    val description: String,
    val enabled: Boolean,
    val createdAt: LocalDateTime,
    override val auditLog: AuditLog
): ProcedureEvent(procedureId, auditLog) {
    constructor(
        command: CreateDentalProcedureCommand
    ): this(
        procedureId = command.procedureId,
        code = command.code!!,
        description = command.description,
        enabled = command.enabled,
        createdAt = command.createdAt,
        auditLog = command.auditLog
    )
}

/**
 * @author ACMattos
 * @since 25/03/2022.
 */
@JsonTypeName("UpdateDentalProcedureEvent")
data class UpdateDentalProcedureEvent(
    override val procedureId: ProcedureId,
    val code: Int,
    val description: String,
    val enabled: Boolean,
    val updatedAt: LocalDateTime,
    override val auditLog: AuditLog
): ProcedureEvent(procedureId, auditLog) {
    constructor(
        command: UpdateDentalProcedureCommand
    ): this(
        procedureId = command.procedureId,
        code = command.code!!,
        description = command.description,
        enabled = command.enabled,
        updatedAt = command.updatedAt,
        auditLog = command.auditLog
    )
}

/**
 * @author ACMattos
 * @since 25/03/2022.
 */
@JsonTypeName("DeleteDentalProcedureEvent")
data class DeleteDentalProcedureEvent(
    override val procedureId: ProcedureId,
    override val auditLog: AuditLog
): ProcedureEvent(procedureId, auditLog) {
    constructor(
        command: DeleteDentalProcedureCommand
    ): this(
        procedureId = command.procedureId,
        auditLog = command.auditLog
    )
}
