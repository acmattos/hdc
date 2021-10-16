package br.com.acmattos.hdc.common.context.port.persistence.mongodb

import br.com.acmattos.hdc.common.context.domain.model.TestMdbDocument
import io.mockk.mockk
import org.assertj.core.api.AbstractThrowableAssert
import org.assertj.core.api.Assertions.assertThatCode
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

/**
 * @author ACMattos
 * @since 16/10/2021.
 */
object MdbCollectionTest: Spek({
    Feature("${MdbCollection::class.java} usage") {
        Scenario("ex.message was found") {
            lateinit var mdbDatabase: MdbDatabase
            lateinit var mdbCollectionConfig: MdbCollectionConfig<TestMdbDocument>
            lateinit var mdbCollection: MdbCollection<TestMdbDocument>
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            Given("""a ${MdbDatabase::class.java} mock""") {
                mdbDatabase = mockk(relaxed = true)
            }
            And("""a ${MdbCollectionConfig::class.java} instance""") {
                mdbCollectionConfig = MdbCollectionConfig(
                    "collectionName",
                    TestMdbDocument::class.java,
                )
            }
            And("""a successful ${MdbCollection::class.java} instantiation""") {
                mdbCollection = MdbCollection(mdbDatabase, mdbCollectionConfig)
            }
            When("""#catch is executed""") {
                assertion = assertThatCode {
                    mdbCollection.getCollection()
                }
            }
            Then("""the call does not throw any exception""") {
                assertion.doesNotThrowAnyException()
            }
        }
    }
})
