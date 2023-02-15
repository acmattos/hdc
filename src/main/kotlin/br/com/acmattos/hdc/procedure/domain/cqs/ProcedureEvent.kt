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
        command.procedureId,
        command.code,
        description = command.description,
        command.enabled,
        command.createdAt,
        command.auditLog,
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
        command.procedureId,
        command.code,
        command.description,
        command.enabled,
        command.updatedAt,
        command.deletedAt,
        command.auditLog,
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
        command.procedureId,
        command.code,
        command.description,
        command.enabled,
        command.updatedAt,
        command.auditLog,
    )
}

/**
 * @author ACMattos
 * @since 25/03/2022.
 */
data class ProcedureDeleteEvent(
    override val procedureId: ProcedureId,
    val code: Int,
    override val updatedAt: LocalDateTime?,
    override val deletedAt: LocalDateTime,
    override val auditLog: AuditLog,
): DeleteEvent, ProcedureEvent(procedureId, auditLog) {
    constructor(
        command: ProcedureDeleteCommand
    ): this(
        command.procedureId,
        command.code,
        command.updatedAt,
        command.deletedAt,
        command.auditLog,
    )
}
