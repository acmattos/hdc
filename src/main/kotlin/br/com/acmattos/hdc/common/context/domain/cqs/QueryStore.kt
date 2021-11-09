package br.com.acmattos.hdc.common.context.domain.cqs

/**
 * @author ACMattos
 * @since 30/06/2020.
 */
interface QueryStore<T: Query> {
    fun addQuery(query: T)
}
