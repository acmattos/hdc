package br.com.acmattos.hdc.person.domain.cqs

import br.com.acmattos.hdc.common.context.domain.cqs.AndFilter
import br.com.acmattos.hdc.common.context.domain.cqs.EmptyFilter
import br.com.acmattos.hdc.common.context.domain.cqs.EqFilter
import br.com.acmattos.hdc.common.context.domain.cqs.Filter
import br.com.acmattos.hdc.common.context.domain.cqs.OrFilter
import br.com.acmattos.hdc.common.context.domain.cqs.Query
import br.com.acmattos.hdc.common.context.domain.cqs.RegexFilter
import br.com.acmattos.hdc.common.context.domain.model.AuditLog
import br.com.acmattos.hdc.common.tool.page.Page
import br.com.acmattos.hdc.person.domain.model.PersonId
import br.com.acmattos.hdc.person.port.persistence.mongodb.DocumentIndexedField.CONTACTS_INFO
import br.com.acmattos.hdc.person.port.persistence.mongodb.DocumentIndexedField.CPF
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
        pageNumber: String?,
        pageSize: String?,
        auditLog: AuditLog
    ): this(
        Page.create(
            filter = filter(fullName, cpf, contact, dentalPlanName, personIds),
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
                val fnFilter = fullNameFilter(fullName)
                val cpfFilter = cpfFilter(cpf)
                val cFilter = contactFilter(contact)
                val dpnFilter = dentalPlanNameFilter(dentalPlanName)
                return if (fnFilter != null && cpfFilter != null && cFilter != null
                    && dpnFilter != null
                ) {
                    AndFilter(listOf(fnFilter, cpfFilter, cFilter, dpnFilter))
                } else if (fnFilter != null && cpfFilter != null && cFilter != null
                    && dpnFilter == null
                ) {
                    AndFilter(listOf(fnFilter, cpfFilter, cFilter))
                } else if (fnFilter != null && cpfFilter != null && cFilter == null
                    && dpnFilter != null
                ) {
                    AndFilter(listOf(fnFilter, cpfFilter, dpnFilter))
                } else if (fnFilter != null && cpfFilter == null && cFilter != null
                    && dpnFilter != null
                ) {
                    AndFilter(listOf(fnFilter, cFilter, dpnFilter))
                } else if (fnFilter == null && cpfFilter != null && cFilter != null
                    && dpnFilter != null
                ) {
                    AndFilter(listOf(cpfFilter, cFilter, dpnFilter))
                } else if (fnFilter != null && cpfFilter != null && cFilter == null
                    && dpnFilter == null
                ) {
                    AndFilter(listOf(fnFilter, cpfFilter))
                } else if (fnFilter != null && cpfFilter == null && cFilter != null
                    && dpnFilter == null
                ) {
                    AndFilter(listOf(fnFilter, cFilter))
                } else if (fnFilter == null && cpfFilter == null && cFilter != null
                    && dpnFilter != null
                ) {
                    AndFilter(listOf(cFilter, dpnFilter))
                } else if (fnFilter == null && cpfFilter != null && cFilter == null
                    && dpnFilter != null
                ) {
                    AndFilter(listOf(cpfFilter, dpnFilter))
                } else if (fnFilter == null && cpfFilter != null && cFilter != null
                    && dpnFilter == null
                ) {
                    AndFilter(listOf(cpfFilter, cFilter))
                } else if (fnFilter != null && cpfFilter == null && cFilter == null
                    && dpnFilter != null
                ) {
                    AndFilter(listOf(fnFilter, dpnFilter))
                } else if (fnFilter != null && cpfFilter == null && cFilter == null
                    && dpnFilter == null
                ) {
                    fnFilter
                } else if (fnFilter == null && cpfFilter != null && cFilter == null
                    && dpnFilter == null
                ) {
                    cpfFilter
                } else if (fnFilter == null && cpfFilter == null && cFilter != null
                    && dpnFilter == null
                ) {
                    cFilter
                } else if (fnFilter == null && cpfFilter == null && cFilter == null
                    && dpnFilter != null
                ) {
                    dpnFilter
                } else {
                    EmptyFilter()
                }
            } else {
                OrFilter<String>(
                    personIds
                        .split(",")
                        .map {
                            EqFilter(PERSON_ID.fieldName, it)
                        }
                )
            }
        }

        private fun fullNameFilter(fullName: String?): RegexFilter<String, String>? =
            if(!fullName.isNullOrEmpty()) {
                RegexFilter(FULL_NAME.fieldName, fullName)
            } else {
                null
            }

        private fun cpfFilter(cpf: String?): RegexFilter<String, String>? =
            if(!cpf.isNullOrEmpty()) {
                RegexFilter(CPF.fieldName, cpf)
            } else {
                null
            }

        private fun contactFilter(contact: String?): EqFilter<String, String>? =
            if(!contact.isNullOrEmpty()) {
                EqFilter(CONTACTS_INFO.fieldName, contact)
            } else {
                null
            }

        private fun dentalPlanNameFilter(dentalPlanName: String?): RegexFilter<String, String>? =
            if(!dentalPlanName.isNullOrEmpty()) {
                RegexFilter(DENTAL_PLAN_NAME.fieldName, dentalPlanName)
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
