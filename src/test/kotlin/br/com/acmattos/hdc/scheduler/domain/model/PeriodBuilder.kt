package br.com.acmattos.hdc.scheduler.domain.model

import java.time.LocalTime

/**
 * @author ACMattos
 * @since 31/10/2019.
 */
object PeriodBuilder {
    fun buildListWithoutOverlap(weekDay: WeekDay = WeekDay.MONDAY) =
        listOf(
            Period(weekDay, LocalTime.parse("17:00"), LocalTime.parse("19:00"), 60),
            Period(weekDay, LocalTime.parse("13:00"), LocalTime.parse("17:00"), 15),
            Period(weekDay, LocalTime.parse("11:00"), LocalTime.parse("12:00"), 30),
            Period(weekDay, LocalTime.parse("09:00"), LocalTime.parse("11:00"), 15)
        )

    fun buildListWithOverlap(weekDay: WeekDay = WeekDay.MONDAY) =
        listOf(
            Period(weekDay, LocalTime.parse("17:00"), LocalTime.parse("19:00"), 60),
            Period(weekDay, LocalTime.parse("13:00"), LocalTime.parse("17:00"), 15),
            Period(weekDay, LocalTime.parse("10:00"), LocalTime.parse("12:00"), 30),
            Period(weekDay, LocalTime.parse("09:00"), LocalTime.parse("11:00"), 15)
        )

    fun buildListWithThreeWeekDays() =
        listOf<Period>(
            Period(WeekDay.MONDAY, LocalTime.parse("08:00"), LocalTime.parse("12:00"), 60),
            Period(WeekDay.MONDAY, LocalTime.parse("13:00"), LocalTime.parse("17:00"), 30),
            Period(WeekDay.WEDNESDAY, LocalTime.parse("09:00"), LocalTime.parse("12:00"), 20),
            Period(WeekDay.FRIDAY, LocalTime.parse("08:00"), LocalTime.parse("11:00"), 15),
            Period(WeekDay.FRIDAY, LocalTime.parse("14:00"), LocalTime.parse("16:00"), 10)
        )
}
