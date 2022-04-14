package br.com.acmattos.hdc.procedure.domain.cqs

import br.com.acmattos.hdc.common.context.domain.cqs.AndFilter
import br.com.acmattos.hdc.common.context.domain.cqs.EmptyFilter
import br.com.acmattos.hdc.common.context.domain.cqs.EqFilter
import br.com.acmattos.hdc.common.context.domain.cqs.Filter
import br.com.acmattos.hdc.common.context.domain.cqs.Query
import br.com.acmattos.hdc.common.context.domain.cqs.RegexFilter
import br.com.acmattos.hdc.common.context.domain.model.AuditLog
import br.com.acmattos.hdc.common.tool.page.Page
import br.com.acmattos.hdc.procedure.domain.model.ProcedureId
import br.com.acmattos.hdc.procedure.port.persistence.mongodb.DocumentIndexedField.PROCEDURE_ID

/**
 * @author ACMattos
 * @since 20/03/2021.
 */
open class ProcedureQuery(
    override val page: Page,
    override val auditLog: AuditLog
): Query(page, auditLog)

/**
 * @author ACMattos
 * @since 20/03/2022.
 */
data class FindAllProceduresQuery(
    override val page: Page,
    override val auditLog: AuditLog
): ProcedureQuery(Page.create(), auditLog) {
    constructor(
        code: String?,
        description: String?,
        pageNumber: String?,
        //pageSize: String?,
        auditLog: AuditLog
    ): this(
        Page.create(
            filter = filter(code, description),
            number = pageNumber,
            //size = pageSize
        ),
        auditLog
    )

    companion object {
        private fun filter(code: String?, description: String?): Filter<*> {
            val codeFilter = codeFilter(code)
            val descriptionFilter = descriptionFilter(description)
            return if(codeFilter != null && descriptionFilter != null) {
                AndFilter(listOf(codeFilter, descriptionFilter))
            } else if(codeFilter != null && descriptionFilter == null) {
                codeFilter
            } else if(codeFilter == null && descriptionFilter != null) {
                descriptionFilter
            } else {
                EmptyFilter()
            }
        }

        private fun codeFilter(code: String?): EqFilter<String, Int>? =
            if(!code.isNullOrEmpty()) {
                EqFilter("code", code.toInt())
            } else {
                null
            }

        private fun descriptionFilter(
            description: String?
        ): RegexFilter<String, String>? =
            if(!description.isNullOrEmpty()) {
                RegexFilter("description", description)
            } else {
                null
            }
    }
}


/**
 * @author ACMattos
 * @since 24/03/2022.
 */
data class FindTheProcedureQuery(
    override val page: Page,
    override val auditLog: AuditLog
): ProcedureQuery(page, auditLog) {
    constructor(
        id: ProcedureId,
        auditLog: AuditLog
    ): this(
        Page.create(filter = EqFilter<String, String>(
                PROCEDURE_ID.fieldName,
                id.id
            )
        ),
        auditLog
    )
}
