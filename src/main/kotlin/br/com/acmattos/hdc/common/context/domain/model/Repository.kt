package br.com.acmattos.hdc.common.context.domain.model

import br.com.acmattos.hdc.common.context.port.persistence.mongodb.MdbCriteria
import java.util.Optional

/**
 * @author ACMattos
 * @since 25/07/2019.
 */
interface Repository<T> {
    fun save(t: T)
    fun findByField(fieldName: String, value: Any): Optional<T>
    fun findAllByField(fieldName: String, value: Any): List<T>
    fun findAllByCriteria(criteria: MdbCriteria): List<T>
}
