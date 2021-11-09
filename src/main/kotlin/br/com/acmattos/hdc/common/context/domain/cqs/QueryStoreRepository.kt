package br.com.acmattos.hdc.common.context.domain.cqs

import br.com.acmattos.hdc.common.context.domain.cqs.StoreEnum.STORE
import br.com.acmattos.hdc.common.context.port.persistence.mongodb.MdbQueryDocument
import br.com.acmattos.hdc.common.context.port.persistence.mongodb.MdbRepository
import br.com.acmattos.hdc.common.tool.loggable.Loggable

/**
 * @author ACMattos
 * @since 02/11/2021.
 */
class QueryStoreRepository<T: Query>(
    private val mdbRepository: MdbRepository<MdbQueryDocument>
): QueryStore<T> {
    override fun addQuery(query: T) {
        mdbRepository.save(MdbQueryDocument(query))
        logger.trace(
            "[{}] Add query [{}] to store...: -> !DONE! <-",
            STORE.name,
            query.javaClass.name
        )
    }

    companion object: Loggable()
}
