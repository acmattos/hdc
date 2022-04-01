package br.com.acmattos.hdc.common.context.domain.cqs

import br.com.acmattos.hdc.common.context.domain.model.AuditLog
import br.com.acmattos.hdc.common.context.domain.model.Entity
import br.com.acmattos.hdc.common.context.domain.model.Id

/**
 * @author ACMattos
 * @since 14/08/2021.
 */
open class Query(
    open val queryId: QueryId,
    open val filter: Filter<*>,
    open val auditLog: AuditLog
): Entity  {
    constructor(
        id: Filter<*>,
        auditLog: AuditLog
    ): this(
        QueryId(),
        id,
        auditLog
    )
}

/**
 * @author ACMattos
 * @since 03/11/2021.
 */
class QueryId: Id {
    constructor(id: String): super(id)
    constructor(): super()
}

/**
 * @author ACMattos
 * @since 14/08/2021.
 */
interface QueryHandler<T: Entity> {
    fun handle(query: Query): QueryResult<T>
}
