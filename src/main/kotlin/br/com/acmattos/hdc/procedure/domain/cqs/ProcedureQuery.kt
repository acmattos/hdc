package br.com.acmattos.hdc.procedure.domain.cqs

import br.com.acmattos.hdc.common.context.domain.cqs.Query
import br.com.acmattos.hdc.common.context.domain.model.AuditLog
import br.com.acmattos.hdc.common.context.domain.model.Id

/**
 * @author ACMattos
 * @since 20/03/2021.
 */
open class ProcedureQuery(
    override val id: Id, // TODO Change it to FILTER
    override val auditLog: AuditLog
): Query(id, auditLog)

/**
 * @author ACMattos
 * @since 20/03/2022.
 */
data class FindAllProceduresQuery(
    override val auditLog: AuditLog
): ProcedureQuery(Id(), auditLog)

/**
 * @author ACMattos
 * @since 24/03/2022.
 */
data class FindTheProcedureQuery(
    override val id: Id,
    override val auditLog: AuditLog
): ProcedureQuery(id, auditLog)
