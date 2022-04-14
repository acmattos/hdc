package br.com.acmattos.hdc.common.context.domain.cqs

import java.util.Optional

/**
 * @author ACMattos
 * @since 02/11/2021.
 */
open class QueryResult<T>(
    open val results: List<T>
): Message {
    companion object {
        fun <T> build(entities: List<T>): QueryResult<T> =
           QueryResult(entities)

        fun <T> build(optionalEntity: Optional<T>): QueryResult<T> =
            optionalEntity
                .map{ entity -> QueryResult(listOf(entity)) }
                .orElseGet { QueryResult(listOf()) }
    }
}
