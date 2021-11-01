package br.com.acmattos.hdc.scheduler.domain.model

import br.com.acmattos.hdc.common.tool.assertion.AssertionFailedException
import br.com.acmattos.hdc.scheduler.config.ErrorTrackerCodeEnum.FROM_BIGGER_THAN_TO
import br.com.acmattos.hdc.scheduler.config.ErrorTrackerCodeEnum.OPERATION_GENERATES_REMINDER
import br.com.acmattos.hdc.scheduler.config.ErrorTrackerCodeEnum.SLOT_BELLOW_ONE
import br.com.acmattos.hdc.scheduler.domain.model.WeekDay.TUESDAY
import java.time.LocalTime
import org.assertj.core.api.AbstractThrowableAssert
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

private const val MESSAGE_1 = "from=[09:00] must be smaller than to=[08:00]!"
private const val MESSAGE_2 = "slot=[0] must be bigger than zero!"
private const val MESSAGE_3 = "(to=[09:00] - from=[08:00]) % duration=[61] generates a reminder!"

/**
 * @author ACMattos
 * @since 31/10/2019.
 */
object PeriodTest: Spek({
    Feature("${Period::class.java} usage") {
        Scenario("${Period::class.java} instantiation with no exception") {
            lateinit var weekDay: WeekDay
            lateinit var from: LocalTime
            lateinit var to: LocalTime
            var slot = 0
            var period: Period? = null
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            Given("""a valid ${WeekDay::class.java}""") {
                weekDay = TUESDAY
            }
            And("""a valid ${LocalTime::class.java} for from""") {
                from = LocalTime.of(8, 0)
            }
            And("""a valid ${LocalTime::class.java} for to""") {
                to = LocalTime.of(9, 0)
            }
            And("""a valid ${Integer::class.java} for slot""") {
                slot = 30
            }
            When("""${Period::class.java} instantiation is executed""") {
                assertion = Assertions.assertThatCode {
                    period = Period(weekDay, from, to, slot)
                }
            }
            Then("""instantiation throws no exception""") {
                assertion.doesNotThrowAnyException()
            }
            And("""period is not null""") {
                assertThat(period).isNotNull()
            }
        }

        Scenario("${Period::class.java}#assertFromLessThanTo throws exception") {
            lateinit var weekDay: WeekDay
            lateinit var from: LocalTime
            lateinit var to: LocalTime
            var slot = 0
            var period: Period? = null
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            Given("""a valid ${WeekDay::class.java}""") {
                weekDay = TUESDAY
            }
            And("""a invalid ${LocalTime::class.java} for from""") {
                from = LocalTime.of(9, 0)
            }
            And("""a invalid ${LocalTime::class.java} for to""") {
                to = LocalTime.of(8, 0)
            }
            And("""a valid ${Integer::class.java} for slot""") {
                slot = 30
            }
            When("""${Period::class.java} instantiation is executed""") {
                assertion = Assertions.assertThatCode {
                    period = Period(weekDay, from, to, slot)
                }
            }
            Then("""instantiation throws exception""") {
                assertion.hasSameClassAs(AssertionFailedException(MESSAGE_1, FROM_BIGGER_THAN_TO.code))
            }
            And("""exception has code ${FROM_BIGGER_THAN_TO.code}""") {
                assertion.hasFieldOrPropertyWithValue("code", FROM_BIGGER_THAN_TO.code)
            }
            And("""exception has message $MESSAGE_1""") {
                assertion.hasMessage(MESSAGE_1)
            }
            And("""no enum was retrieved""") {
                assertThat(period).isNull()
            }
        }

        Scenario("${Period::class.java}#assertValidSlot throws exception") {
            lateinit var weekDay: WeekDay
            lateinit var from: LocalTime
            lateinit var to: LocalTime
            var slot = 0
            var period: Period? = null
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            Given("""a valid ${WeekDay::class.java}""") {
                weekDay = TUESDAY
            }
            And("""a valid ${LocalTime::class.java} for from""") {
                from = LocalTime.of(8, 0)
            }
            And("""a valid ${LocalTime::class.java} for to""") {
                to = LocalTime.of(9, 0)
            }
            And("""a invalid ${Integer::class.java} for slot""") {
                slot = 0
            }
            When("""${Period::class.java} instantiation is executed""") {
                assertion = Assertions.assertThatCode {
                    period = Period(weekDay, from, to, slot)
                }
            }
            Then("""instantiation throws exception""") {
                assertion.hasSameClassAs(AssertionFailedException(MESSAGE_2, SLOT_BELLOW_ONE.code))
            }
            And("""exception has code ${SLOT_BELLOW_ONE.code}""") {
                assertion.hasFieldOrPropertyWithValue("code", SLOT_BELLOW_ONE.code)
            }
            And("""exception has message $MESSAGE_2""") {
                assertion.hasMessage(MESSAGE_2)
            }
            And("""no enum was retrieved""") {
                assertThat(period).isNull()
            }
        }

        Scenario("${Period::class.java}#assertDurationCreatesPerfectSlots throws exception") {
            lateinit var weekDay: WeekDay
            lateinit var from: LocalTime
            lateinit var to: LocalTime
            var slot = 0
            var period: Period? = null
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            Given("""a valid ${WeekDay::class.java}""") {
                weekDay = TUESDAY
            }
            And("""a valid ${LocalTime::class.java} for from""") {
                from = LocalTime.of(8, 0)
            }
            And("""a valid ${LocalTime::class.java} for to""") {
                to = LocalTime.of(9, 0)
            }
            And("""a invalid ${Integer::class.java} for slot""") {
                slot = 61
            }
            When("""${Period::class.java} instantiation is executed""") {
                assertion = Assertions.assertThatCode {
                    period = Period(weekDay, from, to, slot)
                }
            }
            Then("""instantiation throws exception""") {
                assertion.hasSameClassAs(AssertionFailedException(MESSAGE_3, OPERATION_GENERATES_REMINDER.code))
            }
            And("""exception has code ${OPERATION_GENERATES_REMINDER.code}""") {
                assertion.hasFieldOrPropertyWithValue("code", OPERATION_GENERATES_REMINDER.code)
            }
            And("""exception has message $MESSAGE_3""") {
                assertion.hasMessage(MESSAGE_3)
            }
            And("""no enum was retrieved""") {
                assertThat(period).isNull()
            }
        }
    }
})
