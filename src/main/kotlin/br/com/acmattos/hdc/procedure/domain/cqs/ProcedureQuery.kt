package br.com.acmattos.hdc.procedure.domain.cqs

import br.com.acmattos.hdc.common.context.domain.cqs.EmptyFilter
import br.com.acmattos.hdc.common.context.domain.cqs.EqFilter
import br.com.acmattos.hdc.common.context.domain.cqs.Filter
import br.com.acmattos.hdc.common.context.domain.cqs.Query
import br.com.acmattos.hdc.common.context.domain.model.AuditLog
import br.com.acmattos.hdc.procedure.domain.model.ProcedureId
import br.com.acmattos.hdc.procedure.port.persistence.mongodb.DocumentIndexedField.PROCEDURE_ID

/**
 * @author ACMattos
 * @since 20/03/2021.
 */
open class ProcedureQuery(
    override val filter: Filter<*>,
    override val auditLog: AuditLog
): Query(filter, auditLog)

/**
 * @author ACMattos
 * @since 20/03/2022.
 */
data class FindAllProceduresQuery(
    override val auditLog: AuditLog
): ProcedureQuery(EmptyFilter(), auditLog)

/**
 * @author ACMattos
 * @since 24/03/2022.
 */
data class FindTheProcedureQuery(
    override val filter: Filter<*>,
    override val auditLog: AuditLog
): ProcedureQuery(filter, auditLog) {
    constructor(
        id: ProcedureId,
        auditLog: AuditLog
    ): this(
        EqFilter<String, String>(
            PROCEDURE_ID.fieldName,
            id.id
        ),
        auditLog
    )
}
