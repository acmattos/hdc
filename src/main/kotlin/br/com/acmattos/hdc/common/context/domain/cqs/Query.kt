package br.com.acmattos.hdc.common.context.domain.cqs

import br.com.acmattos.hdc.common.context.domain.model.AuditLog
import br.com.acmattos.hdc.common.context.domain.model.Entity
import br.com.acmattos.hdc.common.context.domain.model.Id
import br.com.acmattos.hdc.common.tool.page.Page

/**
 * @author ACMattos
 * @since 14/08/2021.
 */
open class Query(
    open val queryId: QueryId,
    open val page: Page,
    open val auditLog: AuditLog
): Entity  {
    constructor(
        page: Page,
        auditLog: AuditLog
    ): this(
        QueryId(),
        page,
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
