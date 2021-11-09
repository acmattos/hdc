package br.com.acmattos.hdc.scheduler.domain.model

import br.com.acmattos.hdc.common.context.domain.DomainLogEnum.ENTITY
import br.com.acmattos.hdc.common.context.domain.model.Entity
import br.com.acmattos.hdc.common.context.domain.model.Id
import br.com.acmattos.hdc.common.tool.assertion.Assertion
import br.com.acmattos.hdc.common.tool.loggable.Loggable
import br.com.acmattos.hdc.scheduler.config.ErrorTrackerCodeEnum.NO_PERIODS_DEFINED
import br.com.acmattos.hdc.scheduler.config.ErrorTrackerCodeEnum.THERE_IS_A_COLLISION
import br.com.acmattos.hdc.scheduler.domain.cqs.CreateAScheduleForTheDentistEvent
import br.com.acmattos.hdc.scheduler.domain.cqs.ScheduleEvent
import java.time.LocalDateTime

/**
 * @author ACMattos
 * @since 10/07/2019.
 */
data class Schedule(
    private var scheduleIdData: ScheduleId? = null,
    private var dentistData: Dentist? = null,
    private var periodsData: List<Period>? = null,
    private var enabledData: Boolean = true,
    private var createdAtData: LocalDateTime = LocalDateTime.now(),
    private var updatedAtData: LocalDateTime? = null
): Entity {
    val scheduleId get() = scheduleIdData!!
    val dentist get() = dentistData!!
    val periods get() = periodsData!!
    val enabled get() = enabledData
    val createdAt get() = createdAtData
    val updatedAt get() = updatedAtData

    fun apply(events: List<ScheduleEvent>): Schedule {
        for (event in events) {
            apply(event)
        }
        return this
    }

    fun apply(event: ScheduleEvent): Schedule {
        when(event) {
            is CreateAScheduleForTheDentistEvent -> apply(event)
            else -> apply(event as CreateAScheduleForTheDentistEvent)
        }
        return this
    }
    
    private fun apply(event: CreateAScheduleForTheDentistEvent) {
        scheduleIdData = event.scheduleId
        dentistData = event.dentist
        periodsData = event.periods
        enabledData = event.enabled
        createdAtData = event.createdAt
        assertHasNoPeriodsIssues()
    }

    private fun assertHasNoPeriodsIssues() {
        Assertion.assert(
            "No periods were defined!",
            NO_PERIODS_DEFINED.code
        ) {
            periodsData!!.isNotEmpty()
        }
        Assertion.assert(
            "There is a collision for existing periods [$periodsData]",
            THERE_IS_A_COLLISION.code
        ) {
            !hasSomePeriodCollision()
        }
    }

    private fun hasSomePeriodCollision(): Boolean {
        val periodsMap: Map<WeekDay, List<Period>> = convertListOfPeriodsInMap()
        for (weekday in periodsMap.keys) {
            if(hasOverlapFor(periodsMap[weekday]!!)) {
                return true
            }
        }
        return false
    }

    private fun convertListOfPeriodsInMap(): Map<WeekDay, List<Period>> {
        val sortedPeriods = periodsData!!.sortedWith(
            compareBy(
                { it.weekDay }, { it.from }
            )
        )
        logger.trace("[{}] Periods sorted...", ENTITY.name)

        var basePeriod = sortedPeriods.first()
        val mapOfPeriods = mutableMapOf(
            basePeriod.weekDay to mutableListOf<Period>()
        )
        logger.trace(
            "[{}] Map of periods created...: -> {} <-",
            ENTITY.name,
            mapOfPeriods.toString()
        )

        for(period in sortedPeriods) {
            if(period.weekDay === basePeriod.weekDay) {
                val list = mapOfPeriods[period.weekDay]
                list!!.add(period)
            } else {
                basePeriod = period
                mapOfPeriods[basePeriod.weekDay] =
                    mutableListOf(basePeriod)
            }
        }
        logger.trace(
            "[{}] Map of periods updated...: -> {} <-",
            ENTITY.name,
            mapOfPeriods.toString()
        )
        return mapOfPeriods
    }

    private fun hasOverlapFor(periods: List<Period>): Boolean {
        for (i in 1 until periods.size) {
            if (periods[i - 1].to > periods[i].from) {
                return true
            }
        }
        return false
    }

    companion object: Loggable() {
        fun apply(events: List<ScheduleEvent>): Schedule = Schedule().apply(events)
        fun apply(event: ScheduleEvent): Schedule = Schedule().apply(event)
    }
}

/**
 * @author ACMattos
 * @since 15/09/2019.
 */
class ScheduleId: Id {
    constructor(id: String): super(id)
    constructor(): super()
}
