package br.com.acmattos.hdc.common.context.domain.model

import br.com.acmattos.hdc.common.context.domain.cqs.StoreEnum.STORE
import br.com.acmattos.hdc.common.context.port.persistence.mongodb.MdbDocument
import br.com.acmattos.hdc.common.context.port.persistence.mongodb.MdbRepository
import br.com.acmattos.hdc.common.tool.loggable.Loggable
import java.util.Optional

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

    override fun findByField(fieldName: String, value: Any): Optional<T> {
        val document = mdbRepository.findByField(fieldName, value)
        return if(document.isPresent) {
            Optional.of(document.get().toType() as T)
        } else {
            Optional.empty()
        }
    }

    override fun findAllByField(fieldName: String, value: Any): List<T> {
        return mdbRepository.findAllByField(fieldName, value).map { it.toType() }.toList() as List<T>
    }

    companion object: Loggable()
}
