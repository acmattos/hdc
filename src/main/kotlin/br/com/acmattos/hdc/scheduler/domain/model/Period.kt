package br.com.acmattos.hdc.scheduler.domain.model

import br.com.acmattos.hdc.common.context.domain.model.ValueObject
import br.com.acmattos.hdc.common.tool.assertion.Assertion
import br.com.acmattos.hdc.common.tool.enum.assertThatTerm
import br.com.acmattos.hdc.scheduler.config.MessageTrackerIdEnum.DURATION_CREATES_PERFECT_SLOTS
import br.com.acmattos.hdc.scheduler.config.MessageTrackerIdEnum.FROM_LESS_THAN_TO
import br.com.acmattos.hdc.scheduler.config.MessageTrackerIdEnum.VALID_SLOT
import br.com.acmattos.hdc.scheduler.config.MessageTrackerIdEnum.WEEK_DAY_CONVERT_FAILED
import br.com.acmattos.hdc.scheduler.config.ScheduleLogEnum.SCHEDULE
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

/**
 * @author ACMattos
 * @since 11/07/2019.
 */
data class Period(
    val weekDay: WeekDay,
    val from: LocalTime,
    val to: LocalTime,
    val slot: Long
): ValueObject {
    init {
        assertValid()
    }

    private fun assertValid() {
        val minutes = Duration.between(this.from, this.to).toMinutes()
        assertFromLessThanTo(minutes)
        assertValidSlot()
        assertDurationCreatesPerfectSlots(minutes)
    }

    private fun assertFromLessThanTo(minutes: Long) {
        Assertion.assert(
            "from=[$from] must be smaller than to=[$to]!",
            SCHEDULE.name,
            FROM_LESS_THAN_TO
        ) { minutes > 0 }
    }

    private fun assertValidSlot() {
        Assertion.assert(
            "slot=[$slot] must be bigger than zero!",
            SCHEDULE.name,
            VALID_SLOT
        ) { slot > 0 }
    }

    private fun assertDurationCreatesPerfectSlots(minutes: Long) {
        val division = minutes % this.slot
        Assertion.assert(
            "(to=[$to] - from=[$from]) % duration=[$slot] generates a reminder!",
            SCHEDULE.name,
            DURATION_CREATES_PERFECT_SLOTS
        ) { division == 0L }
    }
}

/**
 * @author ACMattos
 * @since 11/07/2019.
 */
enum class WeekDay {
    SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY;

    companion object {
        fun convert(term: String): WeekDay = assertThatTerm(
            term,
            "[$term] does not correspond to a valid WeekDay!",
            SCHEDULE.name,
            WEEK_DAY_CONVERT_FAILED
        )
    }
}

/**
 * @author ACMattos
 * @since 11/07/2019.
 */
fun String.toWeekDay(): WeekDay {
    return WeekDay.convert(this)
}

/**
 * Format: YYYY-MM-DD
 * @author ACMattos
 * @since 11/07/2019.
 */
fun String.toLocalDate(): LocalDate {
    return LocalDate.parse(this, DateTimeFormatter.ISO_LOCAL_DATE)
}

/**
 * @author ACMattos
 * @since 11/07/2019.
 */
fun String.toLocalTime(): LocalTime {
    return LocalTime.parse(this, DateTimeFormatter.ISO_LOCAL_TIME)
}
