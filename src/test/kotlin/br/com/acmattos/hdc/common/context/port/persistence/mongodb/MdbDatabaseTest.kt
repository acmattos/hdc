package br.com.acmattos.hdc.common.context.port.persistence.mongodb

import com.mongodb.client.MongoDatabase
import io.kotest.core.spec.style.FreeSpec
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.AbstractThrowableAssert
import org.assertj.core.api.Assertions.assertThatCode

/**
 * @author ACMattos
 * @since 16/10/2021.
 */
class MdbDatabaseTest: FreeSpec({
    "Feature: ${MdbDatabase::class.java} usage" - {
        "Scenario: simple ${MdbDatabase::class.java} usage" - {
            lateinit var mongoDatabase: MongoDatabase
            lateinit var mdbDatabase: MdbDatabase
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            "Given: a ${MdbDatabase::class.java} mockk" {
                mdbDatabase = mockk()
            }
            "And: a ${MongoDatabase::class.java} mockk" {
                mongoDatabase = mockk()
            }
            "And: a call to getMongoDatabase always return a ${MongoDatabase::class.java}" {
                every { mdbDatabase.getMongoDatabase() } returns mongoDatabase
            }
            "When: #getMongoDatabase is executed" {
                assertion = assertThatCode {
                    mdbDatabase.getMongoDatabase()
                }
            }
            "Then: no exception occurs" {
                assertion.doesNotThrowAnyException()
            }
        }
    }
})
