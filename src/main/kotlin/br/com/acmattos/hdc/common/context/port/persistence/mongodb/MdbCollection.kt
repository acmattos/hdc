package br.com.acmattos.hdc.common.context.port.persistence.mongodb

import com.mongodb.client.MongoCollection
import org.bson.conversions.Bson

/**
 * @author ACMattos
 * @since 18/08/2019.
 */
class MdbCollection<T: MdbDocument>(
    private val mdbDatabase: MdbDatabase,
    private val mdbCollectionConfig: MdbCollectionConfig<T>
) {
    fun getCollection(): MongoCollection<T> =
        mdbDatabase.getMongoDatabase().getCollection(
            mdbCollectionConfig.collectionName,
            mdbCollectionConfig.documentClass
        ).also { mongoCollection ->
            mdbCollectionConfig.indexes.forEach {
                mongoCollection.createIndex(it)
            }
        }
}

/**
 * @author ACMattos
 * @since 30/06/2020.
 */
data class MdbCollectionConfig<T: MdbDocument>(
    val collectionName: String,
    val documentClass: Class<T>,
    val indexes: MutableList<Bson> = mutableListOf()
) {
    fun addIndexes(vararg bsonDocuments: Bson): MdbCollectionConfig<T> {
        bsonDocuments.forEach {
            indexes.add(it)
        }
        return this
    }
}
