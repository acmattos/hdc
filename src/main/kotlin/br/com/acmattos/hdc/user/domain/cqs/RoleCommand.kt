package br.com.acmattos.hdc.user.domain.cqs

import br.com.acmattos.hdc.common.context.domain.cqs.AuditableCommand
import br.com.acmattos.hdc.common.context.domain.cqs.CreateCommand
import br.com.acmattos.hdc.common.context.domain.cqs.DeleteCommand
import br.com.acmattos.hdc.common.context.domain.cqs.UpdateCommand
import br.com.acmattos.hdc.common.context.domain.cqs.UpsertCommand
import br.com.acmattos.hdc.common.context.domain.model.AuditLog
import br.com.acmattos.hdc.user.domain.model.RoleId
import java.time.LocalDateTime

/**
 * @author ACMattos
 * @since 04/12/2023.
 */
open class RoleCommand(
    open val roleId: RoleId,
    override val auditLog: AuditLog
): AuditableCommand(auditLog)

/**
 * @author ACMattos
 * @since 04/12/2023.
 */
data class RoleCreateCommand(
    override val roleId: RoleId,
    val code: String,
    val description: String,
    val enabled: Boolean,
    val createdAt: LocalDateTime,
    override val auditLog: AuditLog,
): CreateCommand, RoleCommand(roleId, auditLog) {
    constructor(
        code: String,
        description: String,
        enabled: Boolean,
        auditLog: AuditLog
    ): this(
        RoleId(),
        code,
        description,
        enabled,
        LocalDateTime.now(),
        auditLog,
    )
}

/**
 * @author ACMattos
 * @since 04/12/2023.
 */
data class RoleUpsertCommand(
    override val roleId: RoleId,
    val code: String,
    val description: String,
    val enabled: Boolean,
    val updatedAt: LocalDateTime,
    val deletedAt: LocalDateTime?,
    override val auditLog: AuditLog,
    val events: List<RoleEvent>,
): UpsertCommand, RoleCommand(roleId, auditLog) {
    constructor(
        command: RoleCreateCommand,
        events: List<RoleEvent>,
    ): this(
        events.last().roleId,
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
 * @since 04/12/2023.
 */
data class RoleUpdateCommand(
    override val roleId: RoleId,
    val code: String?,
    val description: String?,
    val enabled: Boolean?,
    val updatedAt: LocalDateTime,
    override val auditLog: AuditLog,
): UpdateCommand, RoleCommand(roleId, auditLog) {
    constructor(
        roleId: String,
        code: String?,
        description: String?,
        enabled: Boolean?,
        auditLog: AuditLog,
    ): this(
        RoleId(roleId),
        code,
        description,
        enabled,
        LocalDateTime.now(),
        auditLog,
    )
}

/**
 * @author ACMattos
 * @since 04/12/2023.
 */
data class RoleDeleteCommand(
    override val roleId: RoleId,
    val updatedAt: LocalDateTime?,
    val deletedAt: LocalDateTime,
    override val auditLog: AuditLog,
): DeleteCommand, RoleCommand(roleId, auditLog) {
    constructor(
        roleId: String,
        auditLog: AuditLog,
    ): this(
        RoleId(roleId),
        null,
        LocalDateTime.now(),
        auditLog
    )
}
