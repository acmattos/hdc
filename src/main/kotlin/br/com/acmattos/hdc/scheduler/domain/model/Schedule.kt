package br.com.acmattos.hdc.scheduler.domain.model

import br.com.acmattos.hdc.common.context.domain.DomainLogEnum.ENTITY
import br.com.acmattos.hdc.common.context.domain.model.AuditLog
import br.com.acmattos.hdc.common.context.domain.model.Entity
import br.com.acmattos.hdc.common.context.domain.model.Id
import br.com.acmattos.hdc.common.tool.assertion.Assertion
import br.com.acmattos.hdc.common.tool.loggable.Loggable
import br.com.acmattos.hdc.scheduler.config.MessageTrackerIdEnum.FROM_LESSER_OR_EQUAL_TO_TO
import br.com.acmattos.hdc.scheduler.config.MessageTrackerIdEnum.NO_PERIODS_DEFINED
import br.com.acmattos.hdc.scheduler.config.MessageTrackerIdEnum.THERE_IS_A_COLLISION
import br.com.acmattos.hdc.scheduler.config.ScheduleLogEnum.APPOINTMENT
import br.com.acmattos.hdc.scheduler.config.ScheduleLogEnum.SCHEDULE
import br.com.acmattos.hdc.scheduler.domain.cqs.AppointmentCommand
import br.com.acmattos.hdc.scheduler.domain.cqs.CreateAScheduleForTheDentistEvent
import br.com.acmattos.hdc.scheduler.domain.cqs.CreateAppointmentForTheScheduleCommand
import br.com.acmattos.hdc.scheduler.domain.cqs.CreateAppointmentsForTheScheduleCommand
import br.com.acmattos.hdc.scheduler.domain.cqs.ScheduleEvent
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

/**
 * @author ACMattos
 * @since 10/07/2019.
 */
data class Schedule(// TODO Test
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
            SCHEDULE.name,
            NO_PERIODS_DEFINED
        ) {
            periodsData!!.isNotEmpty()
        }
        Assertion.assert(
            "There is a collision for existing periods [$periodsData]",
            SCHEDULE.name,
            THERE_IS_A_COLLISION
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
        logger.trace(
            "[{} {}] - Converting list of periods in a map...",
            SCHEDULE.name,
            ENTITY.name,
        )
        val sortedPeriods = periodsData!!.sortedWith(
            compareBy(
                { it.weekDay }, { it.from }
            )
        )
        logger.trace(
            "[{} {}] Periods sorted...",
            SCHEDULE.name,
            ENTITY.name
        )

        var basePeriod = sortedPeriods.first()
        val mapOfPeriods = mutableMapOf(
            basePeriod.weekDay to mutableListOf<Period>()
        )
        logger.trace(
            "[{}] Map of periods created...: -> {} <-",
            SCHEDULE.name,
            ENTITY.name,
            mapOfPeriods.toString()
        )

        for(period in sortedPeriods) {
            if(period.weekDay === basePeriod.weekDay) {
                mapOfPeriods[period.weekDay]!!.add(period)
            } else {
                basePeriod = period
                mapOfPeriods[basePeriod.weekDay] =
                    mutableListOf(basePeriod)
            }
        }
        logger.trace(
            "[{} {}]Converting list of periods in a map: -> {} <-",
            SCHEDULE.name,
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

    fun createAppointmentCommands(
        command: CreateAppointmentsForTheScheduleCommand
    ): List<AppointmentCommand> {
        logger.trace(
            "[{} {}] - Creating appointment commands...",
            SCHEDULE.name,
            ENTITY.name,
        )
        val commands: MutableList<AppointmentCommand> = mutableListOf()
        val dates: MutableList<LocalDate> = createPeriodOfDates(command)
        val periods: Map<WeekDay, List<Period>> = convertListOfPeriodsInMap()
        for(date in dates) {
            val weekday = WeekDay.convert(date.dayOfWeek.name)
            val weekdayPeriods: List<Period>? = periods[weekday]
            if (weekdayPeriods != null) {
                for (period in weekdayPeriods) {
                    commands.addAll(
                        buildCreateAppointmentForTheScheduleCommandList(
                            date,
                            period,
                            command.auditLog
                        )
                    )
                }
            }
        }
        logger.debug(
            "[{} {}] - Creating appointment commands: -> size: {} <-",
            SCHEDULE.name,
            ENTITY.name,
            commands.size.toString()
        )
        return commands
    }

    private fun createPeriodOfDates(
        command: CreateAppointmentsForTheScheduleCommand
    ): MutableList<LocalDate> {
        logger.trace(
            "[{} {}] - Creating period of dates...",
            SCHEDULE.name,
            ENTITY.name,
        )
        assertFromLesserOrEqualToTo(command)// TODO TEST or just validate on SCHEDULE??????
        val dates = mutableListOf(command.from)
        var date = command.from.plusDays(1)
        while(date.isBefore(command.to) || date.isEqual(command.to)) {
            dates.add(date)
            date = date.plusDays(1)
        }
        logger.debug(
            "[{} {}] - Creating period of dates: -> [{} - {}] <-",
            SCHEDULE.name,
            ENTITY.name,
            dates.first().toString(),
            dates.last().toString(),
        )
        return dates
    }

    private fun assertFromLesserOrEqualToTo(
        command: CreateAppointmentsForTheScheduleCommand
    ) {
        val days = java.time.Period.between(command.from, command.to).days
        Assertion.assert(
            "from=[$command.from] must be lesser or equal to to=[$command.to]!",
            APPOINTMENT.name,
            FROM_LESSER_OR_EQUAL_TO_TO
        ) { days >= 0 }
    }

    private fun buildCreateAppointmentForTheScheduleCommandList(
        date: LocalDate,
        period: Period,
        auditLog: AuditLog
    ): List<CreateAppointmentForTheScheduleCommand> {
        logger.trace(
            "[{} {}] - Building list of ${CreateAppointmentForTheScheduleCommand::class.java}...",
            SCHEDULE.name,
            ENTITY.name,
        )
        val commands = mutableListOf<CreateAppointmentForTheScheduleCommand>()
        val slots = createAppointmentSlotsForPeriod(period)
        for(slot in slots) {
            commands.add(
                CreateAppointmentForTheScheduleCommand(
                    scheduleId = this.scheduleId,
                    acronym = this.dentist.getAcronym(),
                    date = date,
                    time = slot,
                    duration = period.slot,
                    auditLog = auditLog
                )
            )
        }
        logger.debug(
            "[{} {}] - Building list of ${CreateAppointmentForTheScheduleCommand::class.java}: -> size: {} <-",
            SCHEDULE.name,
            ENTITY.name,
            commands.size.toString()
        )
        return commands
    }

    private fun createAppointmentSlotsForPeriod(
       period: Period
    ):MutableList<LocalTime> {
        logger.trace(
            "[{} {}] - Creating appointment slots for period...",
            SCHEDULE.name,
            ENTITY.name,
        )
        val slots = mutableListOf(period.from)
        var slot = period.from.plusMinutes(period.slot)
        while(slot.isBefore(period.to)) {
            slots.add(slot)
            slot = slot.plusMinutes(period.slot)
        }
        logger.debug(
            "[{} {}] - Creating  appointment slots for period: -> [{} - {}] <-",
            SCHEDULE.name,
            ENTITY.name,
            slots.first().toString(),
            slots.last().toString(),
        )
        return slots
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
