package br.com.acmattos.hdc.common.context.port.persistence.mongodb

import br.com.acmattos.hdc.common.context.config.ErrorTrackerCodeEnum.FIND_BY_FIELD_FAILED
import br.com.acmattos.hdc.common.context.config.ErrorTrackerCodeEnum.SAVE_FAILED
import br.com.acmattos.hdc.common.context.domain.model.TestMdbDocument
import br.com.acmattos.hdc.common.tool.exception.ExceptionCatcher
import br.com.acmattos.hdc.common.tool.exception.InternalServerErrorException
import br.com.acmattos.hdc.common.tool.loggable.LogEventsAppender
import ch.qos.logback.classic.Level
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.AbstractThrowableAssert
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatCode
import org.bson.conversions.Bson
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

const val EXCEPTION_MESSAGE = "No message was provided for this exception!"
const val MESSAGE_SAVE = "[REPOSITORY] - Save document into the repository: -> TestMdbDocument(id=id) <-"
const val LOGGER_SAVE_EXCEPTION_MESSAGE = "No message was provided for this exception! > [CATCHER] $MESSAGE_SAVE"
const val MESSAGE_FIND = "[REPOSITORY] - Finding document by field in the repository: -> field=id <-"
const val LOGGER_FIND_EXCEPTION_MESSAGE = "No message was provided for this exception! > [CATCHER] $MESSAGE_FIND"

/**
 * @author ACMattos
 * @since 14/10/2021.
 */
object MdbRepositoryTest: Spek({
    Feature("${MdbRepository::class.java} usage") {
        Scenario("saving entity successfully") {
            lateinit var repository: MdbRepository<MdbDocument>
            lateinit var mongoDBCollection: MdbCollection<MdbDocument>
            lateinit var appender: LogEventsAppender
            Given("""a prepared ${LogEventsAppender::class.java}""") {
                appender = LogEventsAppender(ExceptionCatcher::class.java)
            }
            And("""a ${MdbCollection::class.java} mock created""") {
                mongoDBCollection = mockk(relaxed = true)
            }
            And("""a successful ${MdbRepository::class.java} instantiation""") {
                repository = MdbRepository(mongoDBCollection)
            }
            When("""#save is executed""") {
                repository.save(TestMdbDocument("id"))
            }
            Then("""the message is $MESSAGE_SAVE""") {
                assertThat(appender.containsMessage(MESSAGE_SAVE)).isTrue()
            }
            And("the level is ${Level.TRACE}") {
                assertThat(appender.getLoggingEvent(0).level).isEqualTo(Level.TRACE)
            }
        }

        Scenario("saving entity throws ${InternalServerErrorException::class.java}") {
            lateinit var repository: MdbRepository<MdbDocument>
            lateinit var mdbCollection: MdbCollection<MdbDocument>
            lateinit var appender: LogEventsAppender
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            Given("""a prepared ${LogEventsAppender::class.java}""") {
                appender = LogEventsAppender(ExceptionCatcher::class.java)
            }
            And("""a ${MdbCollection::class.java} mock created""") {
                mdbCollection = mockk(relaxed = true)
            }
            And("""the mock configured to throw an ${Exception::class.java}""") {
                every { mdbCollection.getCollection().insertOne(any()) } throws Exception()
            }
            And("""a successful ${MdbRepository::class.java} instantiation""") {
                repository = MdbRepository(mdbCollection)
            }
            When("""#save is executed""") {
                assertion = assertThatCode {
                    repository.save(TestMdbDocument("id"))
                }
            }
            Then("""${InternalServerErrorException::class.java} is raised""") {
                assertion.hasSameClassAs(InternalServerErrorException(
                    EXCEPTION_MESSAGE,
                    SAVE_FAILED.code,
                    Exception(EXCEPTION_MESSAGE)
                ))
            }
            And("""message is $EXCEPTION_MESSAGE""") {
                assertion.hasMessage(EXCEPTION_MESSAGE)
            }
            And("""code is ${SAVE_FAILED.code}""") {
                assertion.hasFieldOrPropertyWithValue("code", SAVE_FAILED.code)
            }
            And("""the message is $MESSAGE_SAVE""") {
                assertThat(appender.containsMessage(MESSAGE_SAVE)).isTrue()
            }
            And("the level is ${Level.TRACE}") {
                assertThat(appender.getMessageLevel(MESSAGE_SAVE)).isEqualTo(Level.TRACE)
            }
            Then("""the message is $LOGGER_SAVE_EXCEPTION_MESSAGE""") {
                assertThat(appender.containsMessage(LOGGER_SAVE_EXCEPTION_MESSAGE)).isTrue()
            }
            And("the level is ${Level.ERROR}") {
                assertThat(appender.getMessageLevel(LOGGER_SAVE_EXCEPTION_MESSAGE)).isEqualTo(Level.ERROR)
            }
        }

        Scenario("finding entity by field") {
            lateinit var repository: MdbRepository<MdbDocument>
            lateinit var mdbCollection: MdbCollection<MdbDocument>
            lateinit var appender: LogEventsAppender
            var entity: MdbDocument? = null
            Given("""a prepared ${LogEventsAppender::class.java}""") {
                appender = LogEventsAppender(ExceptionCatcher::class.java)
            }
            And("""a ${MdbCollection::class.java} mock created""") {
                mdbCollection = mockk(relaxed = true)
            }
            And("""a ${MdbDocument::class.java} mock created""") {
                entity = mockk(relaxed = true)
            }
            And("""the mock configured return a ${MdbDocument::class.java}""") {
                every { mdbCollection.getCollection().find(any<Bson>()).firstOrNull() } returns entity
            }
            And("""a successful ${MdbRepository::class.java} instantiation""") {
                repository = MdbRepository(mdbCollection)
            }
            When("""#findByField is executed""") {
                entity = repository.findByField("field", "id").get()
            }
            Then("""the message is """) {
                assertThat(entity).isNotNull()
            }
            And("""the message is $MESSAGE_FIND""") {
                assertThat(appender.containsMessage(MESSAGE_FIND)).isTrue()
            }
            And("the level is ${Level.TRACE}") {
                assertThat(appender.getMessageLevel(MESSAGE_FIND)).isEqualTo(Level.TRACE)
            }
        }

        Scenario("finding entity by field throws ${InternalServerErrorException::class.java}") {
            lateinit var repository: MdbRepository<MdbDocument>
            lateinit var mdbCollection: MdbCollection<MdbDocument>
            lateinit var appender: LogEventsAppender
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            Given("""a prepared ${LogEventsAppender::class.java}""") {
                appender = LogEventsAppender(ExceptionCatcher::class.java)
            }
            And("""a ${MdbCollection::class.java} mock created""") {
                mdbCollection = mockk(relaxed = true)
            }
            And("""the mock configured to throw an ${Exception::class.java}""") {
                every { mdbCollection.getCollection().find(any<Bson>()) } throws Exception()
            }
            And("""a successful ${MdbRepository::class.java} instantiation""") {
                repository = MdbRepository(mdbCollection)
            }
            When("""#findByField is executed""") {
                assertion = assertThatCode {
                    repository.findByField("field", "id")
                }
            }
            Then("""${InternalServerErrorException::class.java} is raised""") {
                assertion.hasSameClassAs(InternalServerErrorException(
                    EXCEPTION_MESSAGE,
                    FIND_BY_FIELD_FAILED.code,
                    Exception(EXCEPTION_MESSAGE)
                ))
            }
            And("""message is $EXCEPTION_MESSAGE""") {
                assertion.hasMessage(EXCEPTION_MESSAGE)
            }
            And("""code is ${FIND_BY_FIELD_FAILED.code}""") {
                assertion.hasFieldOrPropertyWithValue("code", FIND_BY_FIELD_FAILED.code)
            }
            And("""the message is $MESSAGE_FIND""") {
                assertThat(appender.containsMessage(MESSAGE_FIND)).isTrue()
            }
            And("the level is ${Level.TRACE}") {
                assertThat(appender.getLoggingEvent(0).level).isEqualTo(Level.TRACE)
            }
            Then("""the message is $LOGGER_FIND_EXCEPTION_MESSAGE""") {
                assertThat(appender.containsMessage(LOGGER_FIND_EXCEPTION_MESSAGE)).isTrue()
            }
            And("the level is ${Level.ERROR}") {
                assertThat(appender.getMessageLevel(LOGGER_FIND_EXCEPTION_MESSAGE)).isEqualTo(Level.ERROR)
            }
        }
    }
})
