package br.com.acmattos.hdc.common.context.domain.model

import br.com.acmattos.hdc.common.context.domain.cqs.Filter
import br.com.acmattos.hdc.common.context.port.persistence.mongodb.MdbCriteria
import java.util.Optional

/**
 * @author ACMattos
 * @since 25/07/2019.
 */
interface Repository<T> {
    fun save(t: T)
    fun update(filter: Filter<*>, t: T)
    fun delete(filter: Filter<*>)
    fun findOneByFilter(filter: Filter<*>): Optional<T>
    fun findAllByFilter(filter: Filter<*>): List<T>
    fun findAll(): List<T>// TODO Subject to change (APPLY a filter)
    @Deprecated("DO NOT USE IT!")
    fun findByField(fieldName: String, value: Any): Optional<T>
    @Deprecated("DO NOT USE IT!")
    fun findAllByField(fieldName: String, value: Any): List<T>
    @Deprecated("DO NOT USE IT!")
    fun findAllByCriteria(criteria: MdbCriteria): List<T>
}
