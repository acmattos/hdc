//package br.com.acmattos.hdc.scheduler.domain.model
//
//import br.com.acmattos.hdc.common.tool.assertion.AssertionFailedException
//import br.com.acmattos.hdc.scheduler.config.MessageTrackerIdEnum.NO_PERIODS_DEFINED
//import br.com.acmattos.hdc.scheduler.config.MessageTrackerIdEnum.THERE_IS_A_COLLISION
//import br.com.acmattos.hdc.scheduler.domain.model.WeekDay.TUESDAY
//import org.assertj.core.api.AbstractThrowableAssert
//import org.assertj.core.api.Assertions
//import org.assertj.core.api.Assertions.assertThat
//import org.spekframework.spek2.Spek
//import org.spekframework.spek2.style.gherkin.Feature
//
//private const val MESSAGE_1 = "No periods were defined!"
//private const val MESSAGE_2 = "There is a collision for existing periods " +
//    "[[Period(weekDay=MONDAY, from=08:00, to=12:00, slot=60), Period(weekDay=MONDAY, " +
//    "from=13:00, to=17:00, slot=30), Period(weekDay=WEDNESDAY, from=09:00, to=12:00," +
//    " slot=20), Period(weekDay=FRIDAY, from=08:00, to=11:00, slot=15), " +
//    "Period(weekDay=FRIDAY, from=14:00, to=16:00, slot=10), Period(weekDay=TUESDAY, " +
//    "from=17:00, to=19:00, slot=60), Period(weekDay=TUESDAY, from=13:00, to=17:00, " +
//    "slot=15), Period(weekDay=TUESDAY, from=10:00, to=12:00, slot=30), " +
//    "Period(weekDay=TUESDAY, from=09:00, to=11:00, slot=15)]]"
//private const val MESSAGE_3 = "(to=[09:00] - from=[08:00]) % duration=[61] generates a reminder!"
//
///**
// * @author ACMattos
// * @since 30/10/2021.
// */
//object ScheduleTest: FreeSpec({
//    "Feature: ${Schedule::class.java} usage") {
//        "Scenario: a valid schedule creation") {
//            lateinit var entity: Schedule
//            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
//            " When: a successful schedule instantiation is done") {
//                assertion = Assertions.assertThatCode {
//                    entity = ScheduleBuilder.build()
//                }
//            }
//            " Then: no exception is raised") {
//                assertion.doesNotThrowAnyException()
//            }
//            "And: entity is not null") {
//                assertThat(entity).isNotNull()
//            }
//        }
//
//        "Scenario: no periods defined") {
//            lateinit var periods: List<Period>
//            var entity: Schedule? = null
//            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
//            "Given: a list of empty of periods") {
//                periods = listOf()
//            }
//            " When: an unsuccessful schedule instantiation is done") {
//                assertion = Assertions.assertThatCode {
//                    entity = ScheduleBuilder.buildWithPeriods(periods)
//                }
//            }
//            "Then: instantiation throws exception" {
//                assertion.hasSameClassAs(AssertionFailedException(MESSAGE_1, NO_PERIODS_DEFINED.messageTrackerId))
//            }
//            "And: exception has messageTrackerId ${NO_PERIODS_DEFINED.messageTrackerId}" {
//                assertion.hasFieldOrPropertyWithValue("code", NO_PERIODS_DEFINED.messageTrackerId)
//            }
//            "And: exception has message $MESSAGE_1" {
//                assertion.hasMessage(MESSAGE_1)
//            }
//            "And: no entity was instantiated" {
//                assertThat(entity).isNull()
//            }
//        }
//
//        "Scenario: period collision detected") {
//            lateinit var periods: List<Period>
//            var entity: Schedule? = null
//            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
//            "Given: a list of empty of periods") {
//                periods = PeriodBuilder.buildListWithThreeWeekDays() +
//                    PeriodBuilder.buildListWithOverlap(TUESDAY)
//            }
//            " When: an unsuccessful schedule instantiation is done") {
//                assertion = Assertions.assertThatCode {
//                    entity = ScheduleBuilder.buildWithPeriods(periods)
//                }
//            }
//            "Then: instantiation throws exception" {
//                assertion.hasSameClassAs(AssertionFailedException(MESSAGE_2, THERE_IS_A_COLLISION.messageTrackerId))
//            }
//            "And: exception has messageTrackerId ${THERE_IS_A_COLLISION.messageTrackerId}" {
//                assertion.hasFieldOrPropertyWithValue("code", THERE_IS_A_COLLISION.messageTrackerId)
//            }
//            "And: exception has message $MESSAGE_2" {
//                assertion.hasMessage(MESSAGE_2)
//            }
//            "And: no entity was instantiated" {
//                assertThat(entity).isNull()
//            }
//        }
//    }
//})
