package br.com.acmattos.hdc.common.context.port.persistence.mongodb

import io.kotest.core.spec.style.FreeSpec
import org.assertj.core.api.Assertions.assertThat

const val DATABASE_URL = "DATABASE_URL"
const val MONGO_URL = "mongodb://localhost:27017/hdc"
const val DATABASE_NAME = "hdc"

/**
 * @author ACMattos
 * @since 16/10/2021.
 */
class MdbConfigurationTest: FreeSpec({
    "Feature: ${MdbConfiguration::class.java} usage" - {
        "Scenario: simple ${MdbConfiguration::class.java} internal usage" - {
            lateinit var urlProperty: String
            lateinit var databaseUrl: String
            lateinit var databaseName: String
            lateinit var mdbConfiguration: MdbConfiguration
            "Given: a MongoDB URL property defined in application.properties: $DATABASE_URL" {
                urlProperty = DATABASE_URL
            }
            "And: a successful ${MdbConfiguration::class.java} instantiation" {
                mdbConfiguration = MdbConfiguration(urlProperty)
            }
            "When: #databaseUrl is executed" {
                databaseUrl = mdbConfiguration.databaseUrl()
            }
            "And: #databaseName is executed" {
                databaseName = mdbConfiguration.databaseName()
            }
            "Then: databaseUrl is equal to $MONGO_URL" {
                assertThat(databaseUrl).isEqualTo(MONGO_URL)
            }
            "And: databaseName is equal to $DATABASE_NAME" {
                assertThat(databaseName).isEqualTo(DATABASE_NAME)
            }
        }
    }
})
