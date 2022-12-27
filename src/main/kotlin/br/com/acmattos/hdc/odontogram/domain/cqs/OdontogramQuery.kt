package br.com.acmattos.hdc.odontogram.domain.cqs

import br.com.acmattos.hdc.common.context.domain.cqs.Query
import br.com.acmattos.hdc.common.context.domain.model.AuditLog
import br.com.acmattos.hdc.common.tool.page.Page

/**
 * @author ACMattos
 * @since 18/10/2022.
 */
open class OdontogramQuery(
    override val page: Page,
    override val auditLog: AuditLog
): Query(page, auditLog)

/**
 * @author ACMattos
 * @since 18/10/2022.
 */
data class GetAnOdontogramQuery(
    override val page: Page,
    override val auditLog: AuditLog
): OdontogramQuery(page, auditLog) {
    constructor(
        auditLog: AuditLog
    ): this(
        Page.create(),
        auditLog
    )
}
