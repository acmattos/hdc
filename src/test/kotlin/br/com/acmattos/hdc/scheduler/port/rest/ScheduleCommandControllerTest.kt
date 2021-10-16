package br.com.acmattos.hdc.scheduler.port.rest

import br.com.acmattos.hdc.common.tool.loggable.LogEventsAppender
import ch.qos.logback.classic.Level
import io.javalin.http.Context
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

private const val DEBUG_MESSAGE = "[SCHEDULE ENDPOINT] - Creating the schedule for the dentist..."
private const val INFO_MESSAGE = "[SCHEDULE ENDPOINT] - Creating the schedule for the dentist... -> !DONE! <-"

/**
 * @author ACMattos
 * @since 29/09/2021.
 */
object ScheduleCommandControllerTest: Spek({
    Feature("${ScheduleCommandController::class.java} existence") {
        Scenario("trace message generation") {
            lateinit var controller: ScheduleCommandController
            lateinit var context: Context
            lateinit var appender: LogEventsAppender
            Given("""a prepared ${LogEventsAppender::class.java}""") {
                appender = LogEventsAppender(ScheduleCommandController::class.java)
            }
            And("""a successful ${ScheduleCommandController::class.java} instantiation""") {
                controller = ScheduleCommandController()
            }
            And("""a successful ${ScheduleCommandController::class.java} instantiation""") {
                context = mockk(relaxed = true)
                every { context.status(any()) }.returns(context)
            }
            When("""#createScheduleForADentist is executed""") {
                controller.createTheScheduleForTheDentist(context)
            }
            Then("""the message is $DEBUG_MESSAGE""") {
                assertThat(appender.getMessage(0)).isEqualTo(DEBUG_MESSAGE)
            }
            And("the level is ${Level.DEBUG}") {
                assertThat(appender.getLoggingEvent(0).level).isEqualTo(Level.DEBUG)
            }
            Then("""the message is $INFO_MESSAGE""") {
                assertThat(appender.getMessage(1)).isEqualTo(INFO_MESSAGE)
            }
            And("the level is ${Level.INFO}") {
                assertThat(appender.getLoggingEvent(1).level).isEqualTo(Level.INFO)
            }
        }
    }
})
