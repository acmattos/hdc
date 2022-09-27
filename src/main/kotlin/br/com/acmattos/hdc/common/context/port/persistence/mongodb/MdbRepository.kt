package br.com.acmattos.hdc.common.context.port.persistence.mongodb

import br.com.acmattos.hdc.common.context.config.ContextLogEnum.REPOSITORY
import br.com.acmattos.hdc.common.context.config.MessageTrackerCodeEnum.FIND_ALL_BY_FILTER_FAILED
import br.com.acmattos.hdc.common.context.config.MessageTrackerCodeEnum.FIND_ALL_BY_PAGE
import br.com.acmattos.hdc.common.context.config.MessageTrackerCodeEnum.FIND_ALL_FAILED
import br.com.acmattos.hdc.common.context.config.MessageTrackerCodeEnum.FIND_ONE_BY_FILTER_FAILED
import br.com.acmattos.hdc.common.context.config.MessageTrackerCodeEnum.SAVE_FAILED
import br.com.acmattos.hdc.common.context.config.MessageTrackerCodeEnum.UPDATE_FAILED
import br.com.acmattos.hdc.common.context.domain.model.Repository
import br.com.acmattos.hdc.common.tool.exception.ExceptionCatcher.catch
import br.com.acmattos.hdc.common.tool.page.Filter
import br.com.acmattos.hdc.common.tool.page.FilterTranslator
import br.com.acmattos.hdc.common.tool.page.Page
import br.com.acmattos.hdc.common.tool.page.PageResult
import br.com.acmattos.hdc.common.tool.page.Sort
import br.com.acmattos.hdc.common.tool.page.SortTranslator
import com.mongodb.client.MongoCollection
import java.util.Optional
import org.bson.conversions.Bson

/**
 * @author ACMattos
 * @since 17/08/2019.
 */
open class MdbRepository<T: MdbDocument>(
    private val mongoDBCollection: MdbCollection<T>,
    private val filterTranslator: FilterTranslator<Bson>,
    private val sortTranslator: SortTranslator<Bson>,
): Repository<T> {

    override fun save(document: T) {
        catch(
            "[{}] - Saving document into the repository: -> {} <-",
            SAVE_FAILED.code,
            REPOSITORY.name,
            document.toString()
        ) {
            getCollection().insertOne(document)
        }
    }

    override fun update(filter: Filter<*>, document: T) {
        catch(
            "[{}] - Updating document into the repository: -> {} <-",
            UPDATE_FAILED.code,
            REPOSITORY.name,
            document.toString()
        ) {
            getCollection().replaceOne(
                filterTranslator.createTranslation(filter as Filter<Bson>),
                document
            )
        }
    }

    override fun delete(filter: Filter<*>) {
        catch(
            "[{}] - Deleting document into the repository: -> {} <-",
            UPDATE_FAILED.code,
            REPOSITORY.name,
            filter.toString()
        ) {
            getCollection().deleteOne(
                filterTranslator.createTranslation(filter as Filter<Bson>)
            )
        }
    }

    override fun findOneByFilter(filter: Filter<*>) : Optional<T> =
        catch(
            "[{}] - Finding document by filter in the repository: -> {}={} <-",
            FIND_ONE_BY_FILTER_FAILED.code,
            REPOSITORY.name,
            filter.javaClass.simpleName,
            filter.toString()
        ) {
            return@catch Optional.ofNullable(getCollection().find(
                filterTranslator.createTranslation(filter as Filter<Bson>)
            )
            .firstOrNull())
        }

    override fun findAllByFilter(filter: Filter<*>) : List<T> =
        catch(
            "[{}] - Finding all document by filter the repository: -> {}={} <-",
            FIND_ALL_BY_FILTER_FAILED.code,
            REPOSITORY.name,
            filter.javaClass.simpleName,
            filter.toString()
        ) {
            return@catch getCollection().find(
                filterTranslator.createTranslation(filter as Filter<Bson>)
            )
            .map { it }.toList()
        }

    override fun findAll(): List<T> =// TODO Subject to change (APPLY a filter)
        catch(
            "[{}] - Finding all documents in the repository: -> {}={} <-",
            FIND_ALL_FAILED.code,
            REPOSITORY.name,
            REPOSITORY.name // TODO Verify this log
        ) {
            return@catch getCollection().find()
                .map { it }.toList()
        }

    override fun findAllByPage(page: Page): PageResult<T> =
        catch(
            "[{}] - Finding documents by page in the repository: -> {}={} <-",
            FIND_ALL_BY_PAGE.code,
            REPOSITORY.name,
            page.javaClass.simpleName,
            page.toString()
        ) {
            val filter: Bson = filterTranslator.createTranslation(
                page.filter as Filter<Bson>
            )
            val sort: Bson = sortTranslator.createTranslation(
                page.sort as Sort<Bson>
            )
            val total = getCollection().countDocuments(filter)
            val results = getCollection()
                .find(filter)
                .sort(sort)
                .limit(page.size)
                .skip(page.page)
                .map { it }.toList()
            return@catch PageResult.create(results, page, total)
        }

    private fun getCollection(): MongoCollection<T> =
        mongoDBCollection.getCollection()
}
