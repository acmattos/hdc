package br.com.acmattos.hdc.common.context.domain.cqs

import br.com.acmattos.hdc.common.context.domain.cqs.EventStoreEnum.STORE
import br.com.acmattos.hdc.common.context.port.persistence.mongodb.MdbEventDocument
import br.com.acmattos.hdc.common.context.port.persistence.mongodb.MdbRepository
import br.com.acmattos.hdc.common.tool.loggable.Loggable

/**
 * @author ACMattos
 * @since 01/10/2021.
 */
class EventStoreRepository<T: Event>(
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
    companion object: Loggable()
}

/**
 * @author ACMattos
 * @since 12/10/2021.
 */
enum class EventStoreEnum {
    STORE;
}