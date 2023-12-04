package br.com.acmattos.hdc.user.domain.cqs

import br.com.acmattos.hdc.common.context.domain.cqs.Query
import br.com.acmattos.hdc.common.context.domain.model.AuditLog
import br.com.acmattos.hdc.common.tool.page.AndFilterBuilder
import br.com.acmattos.hdc.common.tool.page.EqFilter
import br.com.acmattos.hdc.common.tool.page.Page
import br.com.acmattos.hdc.common.tool.page.RegexFilterBuilder
import br.com.acmattos.hdc.common.tool.page.SingleSortBuilder
import br.com.acmattos.hdc.user.domain.model.UserId
import br.com.acmattos.hdc.user.port.persistence.mongodb.DocumentIndexedField.EMAIL
import br.com.acmattos.hdc.user.port.persistence.mongodb.DocumentIndexedField.NAME
import br.com.acmattos.hdc.user.port.persistence.mongodb.DocumentIndexedField.USERNAME
import br.com.acmattos.hdc.user.port.persistence.mongodb.DocumentIndexedField.USER_ID

/**
 * @author ACMattos
 * @since 23/11/2023.
 */
open class UserQuery(
    override val page: Page,
    override val auditLog: AuditLog
): Query(page, auditLog)

/**
 * @author ACMattos
 * @since 23/11/2023.
 */
data class FindAllUsersQuery(
    override val page: Page,
    override val auditLog: AuditLog
): UserQuery(Page.create(), auditLog) {
    constructor(
        name: String?,
        username: String?,
        email: String?,
        nameOrder: String?,
        usernameOrder: String?,
        emailOrder: String?,
        pageNumber: String?,
        pageSize: String?,
        auditLog: AuditLog
    ): this(
        Page.create(
            filter = AndFilterBuilder.build(
                RegexFilterBuilder.build(NAME.fieldName, name),
                RegexFilterBuilder.build(USERNAME.fieldName, username),
                RegexFilterBuilder.build(EMAIL.fieldName, email)
            ),
            sort = SingleSortBuilder.build(NAME.fieldName, nameOrder),
            number = pageNumber,
            size = pageSize
        ),
        auditLog
    )
}

/**
 * @author ACMattos
 * @since 24/03/2022.
 */
data class FindTheUserQuery(
    override val page: Page,
    override val auditLog: AuditLog
): UserQuery(page, auditLog) {
    constructor(
        id: UserId,
        auditLog: AuditLog
    ): this(
        Page.create(
            filter = EqFilter<String, String>(
                USER_ID.fieldName,
                id.id
            )
        ),
        auditLog
    )
}
