package br.com.acmattos.hdc.common.context.domain.model

import br.com.acmattos.hdc.common.context.port.persistence.mongodb.MdbDocument
import br.com.acmattos.hdc.common.context.port.persistence.mongodb.MdbRepository
import br.com.acmattos.hdc.common.tool.loggable.LogEventsAppender
import ch.qos.logback.classic.Level
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

const val MESSAGE =
    "[STORE] save entity to repository br.com.acmattos.hdc.common.context.domain.model.TestEntity...: -> !DONE! <-"
/**
 * @author ACMattos
 * @since 14/10/2021.
 */
object EntityRepositoryTest: Spek({
    Feature("${EntityRepository::class.java} usage") {
        Scenario("saving entity") {
            lateinit var repository: EntityRepository<Entity>
            lateinit var mdbRepository: MdbRepository<MdbDocument>
            lateinit var converter: (Entity) -> MdbDocument
            lateinit var appender: LogEventsAppender
            Given("""a prepared ${LogEventsAppender::class.java}""") {
                appender = LogEventsAppender(EntityRepository::class.java)
            }
            And("""a mdbRepository mock created""") {
                mdbRepository = mockk(relaxed = true)
            }
            And("""a converter to MdbDocument defined""") {
                converter = { _ -> TestMdbDocument() }
            }
            And("""a successful ${EntityRepository::class.java} instantiation""") {
                repository = EntityRepository(mdbRepository, converter)
            }
            When("""#save is executed""") {
                repository.save(TestEntity())
            }
            Then("""the message is $MESSAGE""") {
                assertThat(appender.getMessage(0)).isEqualTo(MESSAGE)
            }
            And("the level is ${Level.TRACE}") {
                assertThat(appender.getLoggingEvent(0).level).isEqualTo(Level.TRACE)
            }
        }

        Scenario("finding entity by field") {
            lateinit var repository: EntityRepository<Entity>
            lateinit var mdbRepository: MdbRepository<MdbDocument>
            lateinit var converter: (Entity) -> MdbDocument
            var entity: Entity? = null
            Given("""a mdbRepository mock created""") {
                mdbRepository = mockk(relaxed = true)
            }
            And("""the mock configured to return a ${MdbDocument::class.java}""") {
                every { mdbRepository.findByField(any(), any()) } returns TestMdbDocument()
            }
            And("""a converter to ${MdbDocument::class.java} defined""") {
                converter = { _ -> TestMdbDocument() }
            }
            And("""a successful ${EntityRepository::class.java} instantiation""") {
                repository = EntityRepository(mdbRepository, converter)
            }
            When("""#findByField is executed""") {
                entity = repository.findByField("fie", "")
            }
            Then("""the message is """) {
                assertThat(entity).isNotNull()
            }
        }
    }
})

class TestEntity(): Entity {}
class TestMdbDocument: MdbDocument() {
    override fun toType(): Any = TestEntity()
}