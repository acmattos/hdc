package br.com.acmattos.hdc.common.context.domain.cqs

import br.com.acmattos.hdc.common.context.domain.model.AuditLog
import br.com.acmattos.hdc.common.context.domain.model.ValueObject

/**
 * @author ACMattos
 * @since 26/06/2020.
 */
open class Command(open val auditLog: AuditLog): Message, ValueObject

/**
 * @author ACMattos
 * @since 07/08/2021.
 */
interface CommandHandler<T> {
    fun handle(command: Command): T
}
