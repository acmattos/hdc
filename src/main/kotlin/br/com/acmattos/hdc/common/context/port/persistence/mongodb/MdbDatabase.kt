package br.com.acmattos.hdc.common.context.port.persistence.mongodb

import br.com.acmattos.hdc.common.tool.config.PropHandler.getProperty
import br.com.acmattos.hdc.common.tool.server.mapper.JacksonObjectMapperFactory
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoDatabase
import org.litote.kmongo.KMongo
import org.litote.kmongo.util.KMongoConfiguration

/**
 * @author ACMattos
 * @since 26/07/2019.
 */
class MdbDatabase(
    configuration: MdbConfiguration
) {
    private val mongoClient: MongoClient
    private val mongoDatabase: MongoDatabase

    init{
        KMongoConfiguration.bsonMapper.propertyNamingStrategy =
            JacksonObjectMapperFactory.build().propertyNamingStrategy
        KMongoConfiguration.bsonMapperCopy.propertyNamingStrategy =
            JacksonObjectMapperFactory.build().propertyNamingStrategy
        KMongoConfiguration.extendedJsonMapper.propertyNamingStrategy =
            JacksonObjectMapperFactory.build().propertyNamingStrategy

        mongoClient = KMongo.createClient(configuration.databaseUrl())
        mongoDatabase = mongoClient.getDatabase(configuration.databaseName())
    }

    fun getMongoDatabase(): MongoDatabase = mongoDatabase
}

/**
 * @author ACMattos
 * @since 14/08/2019.
 */
data class MdbConfiguration(
    private val urlProperty: String
) {
    fun databaseUrl() = getProperty<String>(urlProperty)

    fun databaseName() = databaseNameFromUrl()

    private fun databaseNameFromUrl() =
        databaseUrl().split("/")[3].substringBefore("?")
}
