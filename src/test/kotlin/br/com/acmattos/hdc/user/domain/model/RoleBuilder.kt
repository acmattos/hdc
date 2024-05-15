package br.com.acmattos.hdc.user.domain.model

import br.com.acmattos.hdc.common.context.domain.model.AuditLog
import br.com.acmattos.hdc.user.domain.cqs.*
import java.time.LocalDateTime


/**
 * @author ACMattos
 * @since 14/05/2024.
 */
data class RoleRequest(
    val roleId: RoleId = RoleId("01FK96GENJKTN1BYZW6BRHFZFK"),
    val code: String = "CODE_12",
    val description: String = "DESCRIPTION",
    val enabled: Boolean = true,
    val createdAt: LocalDateTime = LocalDateTime.of(2024, 5, 14, 16, 0, 0),
    val updatedAt: LocalDateTime = LocalDateTime.of(2024, 5, 15, 17, 0, 0),
    val deletedAt: LocalDateTime = LocalDateTime.of(2024, 5, 16, 18, 0, 0),
    val auditLog: AuditLog = AuditLog( "who", "what", LocalDateTime.of(2024, 5, 14, 16, 0, 0))
) {
    companion object {
        fun build() = RoleRequest()

        fun buildWithInvalidCode(invalid: String) =
            RoleRequest(code = invalid)

        fun buildWithInvalidDescription(invalid: String) =
            RoleRequest(description = invalid)
    }
}

/**
 * @author ACMattos
 * @since 14/05/2024.
 */
object RoleBuilder{
    fun buildCreate(data: RoleRequest = RoleRequest.build()) =
        Role.apply(
            buildCreateEvent(data),
            true
        )

    fun buildUpsert(data: RoleRequest = RoleRequest.build()) =
        Role.apply(
            listOf(
                buildCreateEvent(),
                buildDeleteEvent(),
                buildUpsertEvent(data)
            ),true
        )

    fun buildUpdate(data: RoleRequest = RoleRequest.build()) =
        Role.apply(
            listOf(
                buildCreateEvent(),
                buildUpdateEvent(data)
            ), true
        )

    fun buildDelete(data: RoleRequest = RoleRequest.build()) =
        Role.apply(
            listOf(
                buildCreateEvent(),
                buildDeleteEvent(data)
            ), true
        )

    private fun buildCreateEvent(data: RoleRequest = RoleRequest.build()) =
        RoleEventBuilder.buildCreate(
            RoleCommandBuilder.buildCreate(
                data
            )
        )

    private fun buildUpsertEvent(data: RoleRequest = RoleRequest.build()) =
        RoleEventBuilder.buildUpsert(
            RoleCommandBuilder.buildUpsert(
                data
            )
        )

    private fun buildUpdateEvent(data: RoleRequest = RoleRequest.build()) =
        RoleEventBuilder.buildUpdate(
            RoleCommandBuilder.buildUpdate(
                data
            )
        )

    private fun buildDeleteEvent(data: RoleRequest = RoleRequest.build()) =
        RoleEventBuilder.buildDelete(
            RoleCommandBuilder.buildDelete(
                data
            )
        )
}

/**
 * @author ACMattos
 * @since 14/05/2024.
 */
private object RoleEventBuilder {
    fun buildCreate(command: RoleCreateCommand) = RoleCreateEvent(
        command
    )

    fun buildUpsert(command: RoleUpsertCommand) = RoleUpsertEvent(
        command
    )

    fun buildUpdate(command: RoleUpdateCommand) = RoleUpdateEvent(
        command
    )

    fun buildDelete(command: RoleDeleteCommand) = RoleDeleteEvent(
        command
    )
}

/**
 * @author ACMattos
 * @since 14/05/2024.
 */
private object RoleCommandBuilder {
    fun buildCreate(data: RoleRequest) = RoleCreateCommand(
        roleId = data.roleId,
        code = data.code,
        description = data.description,
        enabled = data.enabled,
        createdAt = data.createdAt,
        auditLog = data.auditLog,
    )

    fun buildUpsert(data: RoleRequest) = RoleUpsertCommand(
        roleId = data.roleId,
        code = data.code,
        description = data.description,
        enabled = data.enabled,
        updatedAt = data.updatedAt,
        deletedAt = null,
        auditLog = data.auditLog,
        events = listOf()
    )

    fun buildUpdate(data: RoleRequest) = RoleUpdateCommand(
        roleId = data.roleId,
        code = data.code,
        description = data.description,
        enabled = data.enabled,
        updatedAt = data.updatedAt,
        auditLog = data.auditLog,
    )

    fun buildDelete(data: RoleRequest) = RoleDeleteCommand(
        roleId = data.roleId,
        updatedAt = null,
        deletedAt = data.deletedAt,
        auditLog = data.auditLog,
    )
}
