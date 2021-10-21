package br.com.acmattos.hdc.common.context.port.persistence.mongodb

import br.com.acmattos.hdc.common.context.config.ContextLogEnum.REPOSITORY
import br.com.acmattos.hdc.common.context.domain.model.Repository
import br.com.acmattos.hdc.common.tool.exception.ExceptionCatcher.catch
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters

/**
 * @author ACMattos
 * @since 17/08/2019.
 */
open class MdbRepository<T: MdbDocument>(
    private val mongoDBCollection: MdbCollection<T>
): Repository<T> {
    override fun save(document: T) {
        catch(
            "[{}] - Save document into the repository: -> {} <-",
            REPOSITORY.name,
            document.toString()
        ) {
            getCollection().insertOne(document)
        }
    }

    override fun findByField(fieldName: String, value: Any): T? =
        catch(
            "[{}] - Finding document by field in the repository: -> {}={} <-",
            REPOSITORY.name,
            fieldName,
            value.toString()
        ) {
            return@catch getCollection().find(Filters.eq(fieldName, value))
                .firstOrNull()
        }

    override fun findAllByField(fieldName: String, value: Any): List<T>? =
        catch(
            "[{}] - Finding all documents by field in the repository: -> {}={} <-",
            REPOSITORY.name,
            fieldName,
            value.toString()
        ) {
            return@catch getCollection().find(Filters.eq(fieldName, value))
                .map { it }.toList()
        }

    private fun getCollection(): MongoCollection<T> =
        mongoDBCollection.getCollection()
}
