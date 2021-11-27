package br.com.acmattos.hdc.common.context.domain.cqs

import br.com.acmattos.hdc.common.context.port.persistence.mongodb.MdbCriteria

/**
 * @author ACMattos
 * @since 30/06/2020.
 */
interface EventStore<T: Event> {
    fun addEvent(event: T)
    fun findAllByField(fieldName: String, value: Any): List<T>
    fun findAllByCriteria(criteria: MdbCriteria): List<T>
}
