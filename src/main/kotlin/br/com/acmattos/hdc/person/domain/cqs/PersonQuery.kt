package br.com.acmattos.hdc.person.domain.cqs

import br.com.acmattos.hdc.common.context.domain.cqs.Query
import br.com.acmattos.hdc.common.context.domain.model.AuditLog
import br.com.acmattos.hdc.common.tool.page.AndFilterBuilder
import br.com.acmattos.hdc.common.tool.page.EqFilter
import br.com.acmattos.hdc.common.tool.page.EqFilterBuilder
import br.com.acmattos.hdc.common.tool.page.EqNullFilter
import br.com.acmattos.hdc.common.tool.page.Filter
import br.com.acmattos.hdc.common.tool.page.OrFilterBuilder
import br.com.acmattos.hdc.common.tool.page.Page
import br.com.acmattos.hdc.common.tool.page.RegexFilter
import br.com.acmattos.hdc.common.tool.page.RegexFilterBuilder
import br.com.acmattos.hdc.common.tool.page.SingleSortBuilder
import br.com.acmattos.hdc.person.domain.model.PersonId
import br.com.acmattos.hdc.person.port.persistence.mongodb.DocumentIndexedField.CONTACTS_INFO
import br.com.acmattos.hdc.person.port.persistence.mongodb.DocumentIndexedField.CPF
import br.com.acmattos.hdc.person.port.persistence.mongodb.DocumentIndexedField.DENTAL_PLAN
import br.com.acmattos.hdc.person.port.persistence.mongodb.DocumentIndexedField.DENTAL_PLAN_NAME
import br.com.acmattos.hdc.person.port.persistence.mongodb.DocumentIndexedField.FULL_NAME
import br.com.acmattos.hdc.person.port.persistence.mongodb.DocumentIndexedField.PERSON_ID

/**
 * @author ACMattos
 * @since 01/11/2021.
 */
open class PersonQuery(
    override val page: Page,
    override val auditLog: AuditLog
): Query(page, auditLog)

private const val NO_DENTAL_PLAN = "particular"

/**
 * @author ACMattos
 * @since 04/02/2022.
 */
data class FindAllPersonsQuery(
    override val page: Page,
    override val auditLog: AuditLog
): PersonQuery(page, auditLog) {
    constructor(
        fullName: String?,
        cpf: String?,
        contact: String?,
        dentalPlanName: String?,
        personIds: String?,
        fullNameOrder: String?,
        pageNumber: String?,
        pageSize: String?,
        auditLog: AuditLog
    ): this(
        Page.create(
            filter = filter(fullName, cpf, contact, dentalPlanName, personIds),
            sort = SingleSortBuilder.build(FULL_NAME.fieldName, fullNameOrder),
            number = pageNumber,
            size = pageSize
        ),
        auditLog
    )

    companion object {
        private fun filter(
            fullName: String?,
            cpf: String?,
            contact: String?,
            dentalPlanName: String?,
            personIds: String?,
        ): Filter<*> {
            return if(personIds == null) {
                return AndFilterBuilder.build(
                    RegexFilterBuilder.build(FULL_NAME.fieldName, fullName),
                    RegexFilterBuilder.build(CPF.fieldName, cpf),
                    EqFilterBuilder.build(CONTACTS_INFO.fieldName, contact),
                    dentalPlanNameFilter(dentalPlanName)
                )
            } else {
                OrFilterBuilder.build(PERSON_ID.fieldName, personIds)
            }
        }

        private fun dentalPlanNameFilter(dentalPlanName: String?): Filter<String>? =
            if(!dentalPlanName.isNullOrEmpty()) {
                if(!dentalPlanName.contains(NO_DENTAL_PLAN, ignoreCase = true)) {
                    RegexFilter(DENTAL_PLAN_NAME.fieldName, dentalPlanName)
                } else {
                    EqNullFilter(DENTAL_PLAN.fieldName)
                }
            } else {
                null
            }
    }
}

/**
 * @author ACMattos
 * @since 02/11/2021.
 */
data class FindTheDentistQuery(
    override val page: Page,
    override val auditLog: AuditLog
): PersonQuery(page, auditLog) {
    constructor(
        id: PersonId,
        auditLog: AuditLog
    ): this(
        Page.create(filter =
            EqFilter<String, String>(
                PERSON_ID.fieldName,
                id.id
            )
        ),
        auditLog
    )
}

/**
 * @author ACMattos
 * @since 10/03/2022.
 */
data class FindAllContactTypesQuery(
    override val auditLog: AuditLog
): PersonQuery(Page.create(), auditLog)

/**
 * @author ACMattos
 * @since 04/05/2022.
 */
data class FindAllGendersQuery(
    override val auditLog: AuditLog
): PersonQuery(Page.create(), auditLog)

/**
 * @author ACMattos
 * @since 04/05/2022.
 */
data class FindAllMaritalStatusesQuery(
    override val auditLog: AuditLog
): PersonQuery(Page.create(), auditLog)

/**
 * @author ACMattos
 * @since 23/02/2022.
 */
data class FindAllPersonTypesQuery(
    override val auditLog: AuditLog
): PersonQuery(Page.create(), auditLog)

/**
 * @author ACMattos
 * @since 10/03/2022.
 */
data class FindAllStatesQuery(
    override val auditLog: AuditLog
): PersonQuery(Page.create(), auditLog)

/**
 * @author ACMattos
 * @since 04/05/2022.
 */
data class FindAllStatusesQuery(
    override val auditLog: AuditLog
): PersonQuery(Page.create(), auditLog)
