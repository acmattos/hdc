package br.com.acmattos.hdc.common.context.domain.cqs

import br.com.acmattos.hdc.common.context.domain.model.AuditLog
import br.com.acmattos.hdc.common.context.port.persistence.mongodb.MdbEventDocument
import br.com.acmattos.hdc.common.context.port.persistence.mongodb.MdbRepository
import br.com.acmattos.hdc.common.tool.loggable.LogEventsAppender
import ch.qos.logback.classic.Level
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

private const val MESSAGE =
    "[STORE] Add event [br.com.acmattos.hdc.common.context.domain.cqs.TestEvent] to store...: -> !DONE! <-"
/**
 * @author ACMattos
 * @since 15/10/2021.
 */
object EventStoreRepositoryTest: Spek({
    Feature("${EventStoreRepository::class.java} usage") {
        Scenario("adding event") {
            lateinit var repository: EventStoreRepository<TestEvent>
            lateinit var mdbRepository: MdbRepository<MdbEventDocument>
            lateinit var appender: LogEventsAppender
            Given("""a prepared ${LogEventsAppender::class.java}""") {
                appender = LogEventsAppender(EventStoreRepository::class.java)
            }
            And("""a mdbRepository mock created""") {
                mdbRepository = mockk(relaxed = true)
            }
            And("""a successful ${EventStoreRepository::class.java} instantiation""") {
                repository = EventStoreRepository(mdbRepository)
            }
            When("""#addEvent is executed""") {
                repository.addEvent(TestEvent(EventId(), AuditLog("who", "what")))
            }
            Then("""the message is $MESSAGE""") {
                assertThat(appender.getMessage(0)).isEqualTo(MESSAGE)
            }
            And("the level is ${Level.TRACE}") {
                assertThat(appender.getLoggingEvent(0).level).isEqualTo(Level.TRACE)
            }
        }
    }
})

class TestEvent(
    override val eventId: EventId,
    override val auditLog: AuditLog
): EntityEvent(eventId, auditLog)
