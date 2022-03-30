package br.com.acmattos.hdc.person.domain.cqs

import br.com.acmattos.hdc.common.context.domain.cqs.Query
import br.com.acmattos.hdc.common.context.domain.model.AuditLog
import br.com.acmattos.hdc.common.context.domain.model.Id

/**
 * @author ACMattos
 * @since 01/11/2021.
 */
open class PersonQuery(
    override val id: Id, // TODO Change it to FILTER
    override val auditLog: AuditLog
): Query(id, auditLog)

/**
 * @author ACMattos
 * @since 04/02/2022.
 */
data class FindAllPersonsQuery(
    override val auditLog: AuditLog
): PersonQuery(Id(), auditLog)

/**
 * @author ACMattos
 * @since 02/11/2021.
 */
data class FindTheDentistQuery(
    override val id: Id,
    override val auditLog: AuditLog
): PersonQuery(id, auditLog)

/**
 * @author ACMattos
 * @since 23/02/2022.
 */
data class FindAllPersonTypesQuery(
    override val auditLog: AuditLog
): PersonQuery(Id(), auditLog)

/**
 * @author ACMattos
 * @since 10/03/2022.
 */
data class FindAllStatesQuery(
    override val auditLog: AuditLog
): PersonQuery(Id(), auditLog)

/**
 * @author ACMattos
 * @since 10/03/2022.
 */
data class FindAllContactTypesQuery(
    override val auditLog: AuditLog
): PersonQuery(Id(), auditLog)
