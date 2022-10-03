package br.com.acmattos.hdc.scheduler.domain.model

import br.com.acmattos.hdc.common.context.domain.model.AuditLog
import br.com.acmattos.hdc.common.tool.assertion.AssertionFailedException
import br.com.acmattos.hdc.scheduler.config.MessageTrackerIdEnum.ENTITY_APPOINTMENT_ENABLED_MUST_BE_TRUE
import br.com.acmattos.hdc.scheduler.config.MessageTrackerIdEnum.ENTITY_APPOINTMENT_STATUS_MUST_BE_FREED
import br.com.acmattos.hdc.scheduler.domain.cqs.CreateAppointmentForTheScheduleEvent
import br.com.acmattos.hdc.scheduler.domain.model.AppointmentStatus.FREED
import br.com.acmattos.hdc.scheduler.domain.model.AppointmentStatus.SCHEDULED
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import org.assertj.core.api.AbstractThrowableAssert
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThatCode
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

private const val MESSAGE_1 = "Status must be FREED!"
private const val MESSAGE_2 = "Enabled must be TRUE!"

/**
 * @author ACMattos
 * @since 29/11/2021.
 */
object AppointmentTest: Spek({
    Feature("${Appointment::class.java} usage") {
        Scenario("a valid appointment creation") {
            lateinit var event: CreateAppointmentForTheScheduleEvent
            lateinit var entity: Appointment
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            Given("a valid ${CreateAppointmentForTheScheduleEvent::class.java} instantiation is done") {
                event = CreateAppointmentForTheScheduleEvent(
                    AppointmentId("01FK96GENJKTN1BYZW6BRHFZFJ"),
                    ScheduleId("01FK96GENJKTN1BYZW6BRHFZFJ"),
                    "HDC",
                    LocalDate.of(2021, 11, 29),
                    LocalTime.of(14, 0,0),
                    30,
                    FREED,
                    true,
                    LocalDateTime.now(),
                    AuditLog("who", "what")
                )
            }
            When("a successful appointment instantiation is done") {
                assertion = assertThatCode {
                    entity = Appointment.apply(event)
                }
            }
            Then("no exception is raised") {
                assertion.doesNotThrowAnyException()
            }
            And("entity is not null") {
                Assertions.assertThat(entity).isNotNull()
            }
        }

        Scenario("an invalid appointment creation - invalid status") {
            lateinit var event: CreateAppointmentForTheScheduleEvent
            var entity: Appointment? = null
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            Given("an invalid ${CreateAppointmentForTheScheduleEvent::class.java} instantiation is done") {
                event = CreateAppointmentForTheScheduleEvent(
                    AppointmentId("01FK96GENJKTN1BYZW6BRHFZFJ"),
                    ScheduleId("01FK96GENJKTN1BYZW6BRHFZFJ"),
                    "HDC",
                    LocalDate.of(2021, 11, 29),
                    LocalTime.of(14, 0,0),
                    30,
                    SCHEDULED,
                    true,
                    LocalDateTime.now(),
                    AuditLog("who", "what")
                )
            }
            When("a unsuccessful appointment instantiation is done") {
                assertion = assertThatCode {
                    entity = Appointment.apply(event)
                }
            }
            Then("""instantiation throws exception""") {
                assertion.hasSameClassAs(AssertionFailedException(MESSAGE_1, ENTITY_APPOINTMENT_STATUS_MUST_BE_FREED.messageTrackerId))
            }
            And("""exception has messageTrackerId ${ENTITY_APPOINTMENT_STATUS_MUST_BE_FREED.messageTrackerId}""") {
                assertion.hasFieldOrPropertyWithValue("messageTrackerId", ENTITY_APPOINTMENT_STATUS_MUST_BE_FREED.messageTrackerId)
            }
            And("""exception has message $MESSAGE_1""") {
                assertion.hasMessage(MESSAGE_1)
            }
            And("""no entity was instantiated""") {
                Assertions.assertThat(entity).isNull()
            }
        }

        Scenario("an invalid appointment creation - false enabled") {
            lateinit var event: CreateAppointmentForTheScheduleEvent
            var entity: Appointment? = null
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            Given("an invalid ${CreateAppointmentForTheScheduleEvent::class.java} instantiation is done") {
                event = CreateAppointmentForTheScheduleEvent(
                    AppointmentId("01FK96GENJKTN1BYZW6BRHFZFJ"),
                    ScheduleId("01FK96GENJKTN1BYZW6BRHFZFJ"),
                    "HDC",
                    LocalDate.of(2021, 11, 29),
                    LocalTime.of(14, 0,0),
                    30,
                    FREED,
                    false,
                    LocalDateTime.now(),
                    AuditLog("who", "what")
                )
            }
            When("a unsuccessful appointment instantiation is done") {
                assertion = assertThatCode {
                    entity = Appointment.apply(event)
                }
            }
            Then("""instantiation throws exception""") {
                assertion.hasSameClassAs(AssertionFailedException(MESSAGE_2, ENTITY_APPOINTMENT_ENABLED_MUST_BE_TRUE.messageTrackerId))
            }
            And("""exception has messageTrackerId ${ENTITY_APPOINTMENT_ENABLED_MUST_BE_TRUE.messageTrackerId}""") {
                assertion.hasFieldOrPropertyWithValue("messageTrackerId", ENTITY_APPOINTMENT_ENABLED_MUST_BE_TRUE.messageTrackerId)
            }
            And("""exception has message $MESSAGE_2""") {
                assertion.hasMessage(MESSAGE_2)
            }
            And("""no entity was instantiated""") {
                Assertions.assertThat(entity).isNull()
            }
        }
    }
})
