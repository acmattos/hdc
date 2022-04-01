package br.com.acmattos.hdc.person.domain.cqs

import br.com.acmattos.hdc.common.context.domain.cqs.EmptyFilter
import br.com.acmattos.hdc.common.context.domain.cqs.EqFilter
import br.com.acmattos.hdc.common.context.domain.cqs.Filter
import br.com.acmattos.hdc.common.context.domain.cqs.Query
import br.com.acmattos.hdc.common.context.domain.model.AuditLog
import br.com.acmattos.hdc.person.domain.model.PersonId

/**
 * @author ACMattos
 * @since 01/11/2021.
 */
open class PersonQuery(
    override val filter: Filter<*>,
    override val auditLog: AuditLog
): Query(filter, auditLog)

/**
 * @author ACMattos
 * @since 04/02/2022.
 */
data class FindAllPersonsQuery(
    override val auditLog: AuditLog
): PersonQuery(EmptyFilter(), auditLog)

/**
 * @author ACMattos
 * @since 02/11/2021.
 */
data class FindTheDentistQuery(
    override val filter: Filter<*>,
    override val auditLog: AuditLog
): PersonQuery(filter, auditLog) {
    constructor(
        id: PersonId,
        auditLog: AuditLog
    ): this(
        EqFilter<String, String>(
            "person_id", // TODO TRACK this
            id.id
        ),
        auditLog
    )
}

/**
 * @author ACMattos
 * @since 23/02/2022.
 */
data class FindAllPersonTypesQuery(
    override val auditLog: AuditLog
): PersonQuery(EmptyFilter(), auditLog)

/**
 * @author ACMattos
 * @since 10/03/2022.
 */
data class FindAllStatesQuery(
    override val auditLog: AuditLog
): PersonQuery(EmptyFilter(), auditLog)

/**
 * @author ACMattos
 * @since 10/03/2022.
 */
data class FindAllContactTypesQuery(
    override val auditLog: AuditLog
): PersonQuery(EmptyFilter(), auditLog)
