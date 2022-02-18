package br.com.acmattos.hdc.common.context.port.persistence.mongodb

import br.com.acmattos.hdc.common.context.config.ContextLogEnum.REPOSITORY
import br.com.acmattos.hdc.common.context.config.ErrorTrackerCodeEnum.FIND_ALL_BY_CRITERIA_FAILED
import br.com.acmattos.hdc.common.context.config.ErrorTrackerCodeEnum.FIND_ALL_BY_FIELD_FAILED
import br.com.acmattos.hdc.common.context.config.ErrorTrackerCodeEnum.FIND_ALL_FAILED
import br.com.acmattos.hdc.common.context.config.ErrorTrackerCodeEnum.FIND_BY_FIELD_FAILED
import br.com.acmattos.hdc.common.context.config.ErrorTrackerCodeEnum.SAVE_FAILED
import br.com.acmattos.hdc.common.context.domain.model.Repository
import br.com.acmattos.hdc.common.tool.exception.ExceptionCatcher.catch
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters
import java.util.Optional

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
            SAVE_FAILED.code,
            REPOSITORY.name,
            document.toString()
        ) {
            getCollection().insertOne(document)
        }
    }

    override fun findByField(fieldName: String, value: Any): Optional<T> =
        catch(
            "[{}] - Finding document by field in the repository: -> {}={} <-",
            FIND_BY_FIELD_FAILED.code,
            REPOSITORY.name,
            fieldName,
            value.toString()
        ) {
            val document = getCollection().find(Filters.eq(fieldName, value))
                .firstOrNull()
            val optionalDocument = if(document != null) {
                Optional.of(document)
            } else {
                Optional.empty()
            }
            return@catch optionalDocument
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

    override fun findAllByField(fieldName: String, value: Any): List<T> =
        catch(
            "[{}] - Finding all documents by field in the repository: -> {}={} <-",
            FIND_ALL_BY_FIELD_FAILED.code,
            REPOSITORY.name,
            fieldName,
            value.toString()
        ) {
            return@catch getCollection().find(Filters.eq(fieldName, value))
                .map { it }.toList()
        }

    override fun findAllByCriteria(criteria: MdbCriteria): List<T> {
        return catch(
            "[{}] - Finding all documents by criteria in the repository: -> {} <-",
            FIND_ALL_BY_CRITERIA_FAILED.code,
            REPOSITORY.name,
            criteria.toString()
        ) {
            return@catch getCollection().find(criteria.filters)
                .map { it }.toList()
        }
    }


    private fun getCollection(): MongoCollection<T> =
        mongoDBCollection.getCollection()
}
