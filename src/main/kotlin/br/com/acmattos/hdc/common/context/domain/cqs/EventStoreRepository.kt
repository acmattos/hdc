package br.com.acmattos.hdc.common.context.domain.cqs

import br.com.acmattos.hdc.common.context.domain.cqs.StoreEnum.STORE
import br.com.acmattos.hdc.common.context.port.persistence.mongodb.MdbCriteria
import br.com.acmattos.hdc.common.context.port.persistence.mongodb.MdbEventDocument
import br.com.acmattos.hdc.common.context.port.persistence.mongodb.MdbRepository
import br.com.acmattos.hdc.common.tool.loggable.Loggable

/**
 * @author ACMattos
 * @since 01/10/2021.
 */
open class EventStoreRepository<T: Event>(
    private val mdbRepository: MdbRepository<MdbEventDocument>
): EventStore<T> {
   override fun addEvent(event: T) {
        mdbRepository.save(MdbEventDocument(event))
        logger.trace(
            "[{}] Add event [{}] to store...: -> !DONE! <-",
            STORE.name,
            event.javaClass.name
        )
    }

    override fun findAllByField(fieldName: String, value: Any): List<T> =
        mdbRepository.findAllByField(fieldName, value).map { it.toType() } as List<T>

    override fun findAllByCriteria(crietria: MdbCriteria): List<T> =
        mdbRepository.findAllByCriteria(crietria).map { it.toType() } as List<T>

    companion object: Loggable()
}

/**
 * @author ACMattos
 * @since 12/10/2021.
 */
enum class StoreEnum {
    STORE;
}
