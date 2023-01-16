package br.com.acmattos.hdc.common.context.domain.cqs

import br.com.acmattos.hdc.common.context.domain.model.AuditLog
import br.com.acmattos.hdc.common.context.domain.model.ValueObject

/**
 * @author ACMattos
 * @since 12/01/2023.
 */
interface Command: Message, ValueObject

/**
 * @author ACMattos
 * @since 09/01/2023.
 */
interface CreateCommand: Command

/**
 * @author ACMattos
 * @since 09/01/2023.
 */
interface UpdateCommand: Command

/**
 * @author ACMattos
 * @since 10/01/2023.
 */
interface UpsertCommand: Command

/**
 * @author ACMattos
 * @since 09/01/2023.
 */
interface DeleteCommand: Command

/**
 * @author ACMattos
 * @since 26/06/2020.
 */
open class AuditableCommand(open val auditLog: AuditLog): Command

/**
 * @author ACMattos
 * @since 07/08/2021.
 */
interface CommandHandler<T> {
    fun handle(command: Command): T
}
