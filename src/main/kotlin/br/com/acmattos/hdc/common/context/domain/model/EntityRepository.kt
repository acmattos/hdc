package br.com.acmattos.hdc.common.context.domain.model

import br.com.acmattos.hdc.common.context.config.ContextLogEnum.STORE
import br.com.acmattos.hdc.common.context.port.persistence.mongodb.MdbDocument
import br.com.acmattos.hdc.common.context.port.persistence.mongodb.MdbRepository
import br.com.acmattos.hdc.common.tool.loggable.Loggable
import br.com.acmattos.hdc.common.tool.page.Filter
import br.com.acmattos.hdc.common.tool.page.Page
import br.com.acmattos.hdc.common.tool.page.PageResult
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
            "[{}] Saving entity [{}] to repository...: -> !DONE! <-",
            STORE.name,
            entity.javaClass.name
        )
    }

    override fun update(filter: Filter<*>, entity: T) {
        mdbRepository.update(filter, converter(entity))
        logger.trace(
            "[{}] Updating entity [{}] to repository...: -> !DONE! <-",
            STORE.name,
            entity.javaClass.name
        )
    }

    override fun delete(filter: Filter<*>) {
        mdbRepository.delete(filter)
        logger.trace(
            "[{}] Deleting entity [{}] to repository...: -> !DONE! <-",
            STORE.name,
            filter.toString()
        )
    }

    override fun findOneByFilter(filter: Filter<*>) : Optional<T> =
        mdbRepository.findOneByFilter(filter).let { document ->
            return if(document.isPresent) {
                logger.trace(
                    "[{}] Finding one entity [{}] on the repository...: -> !DONE! <-",
                    STORE.name,
                    filter.toString()
                )
                Optional.of(document.get().toType() as T)
            } else {
                Optional.empty()
            }
        }

    override fun findAllByFilter(filter: Filter<*>) : List<T> =
        mdbRepository.findAllByFilter(filter)
            .map { it.toType() }
            .toList() as List<T>

    override fun findAll(): List<T> {// TODO Subject to change (APPLY a filter)
        return mdbRepository.findAll().map { it.toType() }.toList() as List<T>
    }

    override fun findAllByPage(page: Page) : PageResult<T> =
        mdbRepository.findAllByPage(page).let { result ->
            PageResult.create(
                result.results
                    .map { it.toType() }
                    .toList() as List<T>,
                result.page,
                result.total
            )
        }

    companion object: Loggable()
}
