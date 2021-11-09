package br.com.acmattos.hdc.person.domain.cqs

import br.com.acmattos.hdc.common.context.domain.cqs.Query
import br.com.acmattos.hdc.common.context.domain.model.AuditLog
import br.com.acmattos.hdc.common.context.domain.model.Id

/**
 * @author ACMattos
 * @since 01/11/2021.
 */
open class PersonQuery(
    override val id: Id,
    override val auditLog: AuditLog
): Query(id, auditLog)

/**
 * @author ACMattos
 * @since 02/11/2021.
 */
data class FindTheDentistQuery(
    override val id: Id,
    override val auditLog: AuditLog
): PersonQuery(id, auditLog)
