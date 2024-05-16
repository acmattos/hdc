package br.com.acmattos.hdc.user.domain.cqs

import br.com.acmattos.hdc.common.context.domain.cqs.CreateEvent
import br.com.acmattos.hdc.common.context.domain.cqs.DeleteEvent
import br.com.acmattos.hdc.common.context.domain.cqs.EntityEvent
import br.com.acmattos.hdc.common.context.domain.cqs.UpdateEvent
import br.com.acmattos.hdc.common.context.domain.cqs.UpsertEvent
import br.com.acmattos.hdc.common.context.domain.model.AuditLog
import br.com.acmattos.hdc.user.domain.model.RoleId
import java.time.LocalDateTime

/**
 * @author ACMattos
 * @since 04/12/2023.
 */
open class RoleEvent(
    open val roleId: RoleId,
    override val auditLog: AuditLog
): EntityEvent(auditLog)

/**
 * @author ACMattos
 * @since 04/12/2023.
 */
data class RoleCreateEvent(
    override val roleId: RoleId,
    val code: String,
    val description: String,
    val enabled: Boolean,
    override val createdAt: LocalDateTime,
    override val auditLog: AuditLog
): CreateEvent, RoleEvent(roleId, auditLog) {
    constructor(
        command: RoleCreateCommand
    ): this(
        command.roleId,
        command.code,
        command.description,
        command.enabled,
        command.createdAt,
        command.auditLog,
    )
}

/**
 * @author ACMattos
 * @since 04/12/2023.
 */
data class RoleUpsertEvent(
    override val roleId: RoleId,
    val code: String,
    val description: String,
    val enabled: Boolean,
    override val updatedAt: LocalDateTime,
    override val deletedAt: LocalDateTime?,
    override val auditLog: AuditLog,
): UpsertEvent, RoleEvent(roleId, auditLog) {
    constructor(
        command: RoleUpsertCommand
    ): this(
        command.roleId,
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
 * @since 04/12/2023.
 */
data class RoleUpdateEvent(
    override val roleId: RoleId,
    val code: String?,
    val description: String?,
    val enabled: Boolean?,
    override val updatedAt: LocalDateTime,
    override val auditLog: AuditLog
): UpdateEvent, RoleEvent(roleId, auditLog) {
    constructor(
        command: RoleUpdateCommand
    ): this(
        command.roleId,
        command.code,
        command.description,
        command.enabled,
        command.updatedAt,
        command.auditLog,
    )
}

/**
 * @author ACMattos
 * @since 04/12/2023.
 */
data class RoleDeleteEvent(
    override val roleId: RoleId,
    override val updatedAt: LocalDateTime?,
    override val deletedAt: LocalDateTime,
    override val auditLog: AuditLog,
): DeleteEvent, RoleEvent(roleId, auditLog) {
    constructor(
        command: RoleDeleteCommand
    ): this(
        command.roleId,
        command.updatedAt,
        command.deletedAt,
        command.auditLog
    )
}
