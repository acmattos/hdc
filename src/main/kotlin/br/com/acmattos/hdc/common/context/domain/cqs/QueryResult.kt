package br.com.acmattos.hdc.common.context.domain.cqs

import br.com.acmattos.hdc.common.context.domain.model.Entity
import java.util.Optional

/**
 * @author ACMattos
 * @since 02/11/2021.
 */
data class QueryResult<T: Entity>(
    val results: List<T>
): Message {
    companion object {
        fun <T: Entity> build(entities: List<T>): QueryResult<T> =// TODO test
           QueryResult(entities)

        fun <T: Entity> build(optionalEntity: Optional<T>): QueryResult<T> = // TODO test
            if(optionalEntity.isPresent) {
                QueryResult(listOf(optionalEntity.get()))
            } else {
                QueryResult(listOf())
            }
    }
}
