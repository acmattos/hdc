package br.com.acmattos.hdc.common.context.domain.model

import br.com.acmattos.hdc.common.context.domain.cqs.EventStoreEnum.STORE
import br.com.acmattos.hdc.common.context.port.persistence.mongodb.MdbDocument
import br.com.acmattos.hdc.common.context.port.persistence.mongodb.MdbRepository
import br.com.acmattos.hdc.common.tool.loggable.Loggable

/**
 * @author ACMattos
 * @since 01/10/2021.
 */
class EntityRepository<T: Entity>(
    private val mdbRepository: MdbRepository<MdbDocument>,
    private val converter: (T) -> MdbDocument,
): Repository<T> {
    override fun save(entity: T) {
        mdbRepository.save(converter(entity))
        logger.trace(
            "[{}] Save entity [{}] to repository...: -> !DONE! <-",
            STORE.name,
            entity.javaClass.name
        )
    }

    override fun findByField(fieldName: String, value: Any): T? {
        return mdbRepository.findByField(fieldName, value)?.toType() as T?
    }

    override fun findAllByField(fieldName: String, value: Any): List<T>? {
        return mdbRepository.findByField(fieldName, value)?.toType() as List<T>?
    }

    companion object: Loggable()
}
