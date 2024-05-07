package br.com.acmattos.hdc.common.context.domain.cqs

import br.com.acmattos.hdc.common.context.domain.model.AuditLog
import br.com.acmattos.hdc.common.context.port.persistence.mongodb.MdbQueryDocument
import br.com.acmattos.hdc.common.context.port.persistence.mongodb.MdbRepository
import br.com.acmattos.hdc.common.tool.loggable.LogEventsAppender
import br.com.acmattos.hdc.common.tool.page.EqFilter
import br.com.acmattos.hdc.common.tool.page.Page
import ch.qos.logback.classic.Level
import io.kotest.core.spec.style.FreeSpec
import io.mockk.mockk
import org.assertj.core.api.Assertions

private const val MESSAGE =
    "[STORE] Add query [br.com.acmattos.hdc.common.context.domain.cqs.TestQuery] to store...: -> !DONE! <-"
/**
 * @author ACMattos
 * @since 06/11/2021.
 */
class QueryStoreRepositoryTest: FreeSpec({
    "Feature: ${QueryStoreRepository::class.java} usage" - {
        "Scenario: adding event" - {
            lateinit var repository: QueryStoreRepository<TestQuery>
            lateinit var mdbRepository: MdbRepository<MdbQueryDocument>
            lateinit var appender: LogEventsAppender
            "Given: a prepared ${LogEventsAppender::class.java}" {
                appender = LogEventsAppender(QueryStoreRepository::class.java)
            }
            "And: a mdbRepository mock created" {
                mdbRepository = mockk(relaxed = true)
            }
            "And: a successful ${QueryStoreRepository::class.java} instantiation" {
                repository = QueryStoreRepository(mdbRepository)
            }
            "When: #addQuery is executed" {
                repository.addQuery(TestQuery(QueryId(), AuditLog("who", "what")))
            }
            "Then: the message is $MESSAGE" {
                Assertions.assertThat(appender.containsMessage(MESSAGE)).isTrue()
            }
            "MESSAGE: the level is ${Level.TRACE}" {
                Assertions.assertThat(appender.getMessageLevel(MESSAGE))
                    .isEqualTo(Level.TRACE)
            }
        }
    }
})

/**
 * @author ACMattos
 * @since 06/11/2021.
 */
class TestQuery(
    override val page: Page,
    override val auditLog: AuditLog
): Query(page, auditLog) {
    constructor(
        id: QueryId,
        auditLog: AuditLog
    ): this(
        Page.create(filter =
            EqFilter<String, String>(
                "query_id",
                id.id
            )
        ),
        auditLog
    )
}