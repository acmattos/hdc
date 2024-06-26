package br.com.acmattos.hdc.common.context.domain.model

import br.com.acmattos.hdc.common.context.port.persistence.mongodb.MdbDocument
import br.com.acmattos.hdc.common.context.port.persistence.mongodb.MdbRepository
import br.com.acmattos.hdc.common.tool.loggable.LogEventsAppender
import br.com.acmattos.hdc.common.tool.page.EqFilter
import ch.qos.logback.classic.Level
import io.kotest.core.spec.style.FreeSpec
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import java.util.*

private const val MESSAGE =
    "[STORE] Saving entity [br.com.acmattos.hdc.common.context.domain.model.TestEntity] to repository...: -> !DONE! <-"
/**
 * @author ACMattos
 * @since 14/10/2021.
 */
class EntityRepositoryTest: FreeSpec({
    "Feature: ${EntityRepository::class.java} usage" - {
        "Scenario: saving entity" - {
            lateinit var repository: EntityRepository<Entity>
            lateinit var mdbRepository: MdbRepository<MdbDocument>
            lateinit var converter: (Entity) -> MdbDocument
            lateinit var appender: LogEventsAppender
            "Given: a prepared ${LogEventsAppender::class.java}" {
                appender = LogEventsAppender(EntityRepository::class.java)
            }
            "And: a ${MdbRepository::class.java} mock created" {
                mdbRepository = mockk(relaxed = true)
            }
            "And: a converter to ${TestMdbDocument::class.java} defined" {
                converter = { _ -> TestMdbDocument("id") }
            }
            "And: a successful ${EntityRepository::class.java} instantiation" {
                repository = EntityRepository(mdbRepository, converter)
            }
            "When: #save is executed" {
                repository.save(TestEntity("id"))
            }
            "Then: the message is $MESSAGE" {
                assertThat(appender.containsMessage(MESSAGE)).isTrue();
            }
            "And: the level is ${Level.TRACE}" {
                assertThat(appender.containsMessage(MESSAGE)).isTrue()
            }
        }

        "Scenario: finding entity by field" - {
            lateinit var repository: EntityRepository<Entity>
            lateinit var mdbRepository: MdbRepository<MdbDocument>
            lateinit var converter: (Entity) -> MdbDocument
            lateinit var document: Optional<MdbDocument>
            var entity: Optional<Entity>? = null
            "Given: a mdbRepository mock created" {
                mdbRepository = mockk(relaxed = true)
            }
            "And: a ${MdbDocument::class.java} mock created" {
                document = mockk(relaxed = true)
            }
            "And: a ${Entity::class.java} mock created" {
                entity = mockk(relaxed = true)
            }
            "And: the mock configured to return a ${MdbDocument::class.java}" {
                every { mdbRepository.findOneByFilter(any()) } returns document
            }
            "And: the mock configured to return a ${Entity::class.java}" {
                every { document.get().toType() } returns entity!!
            }
            "And: a converter to ${MdbDocument::class.java} defined" {
                converter = { _ -> TestMdbDocument("id") }
            }
            "And: a successful ${EntityRepository::class.java} instantiation" {
                repository = EntityRepository(mdbRepository, converter)
            }
            "When: #findByField is executed" {
                entity = repository.findOneByFilter(
                    EqFilter<String, String>("fieldName", "value")
                )
            }
            "Then: the entity is found" {
                assertThat(entity).isNotNull()
            }
        }
    }
})

data class TestEntity(val id: String): Entity
data class TestMdbDocument(val id: String): MdbDocument() {
    override fun toType(): Any = TestEntity(id)
}
