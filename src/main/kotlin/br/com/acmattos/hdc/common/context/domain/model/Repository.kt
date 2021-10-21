package br.com.acmattos.hdc.common.context.domain.model

/**
 * @author ACMattos
 * @since 25/07/2019.
 */
interface Repository<T> {
    fun save(entity: T)
    fun findByField(fieldName: String, value: Any): T?
    fun findAllByField(fieldName: String, value: Any): List<T>?
}
