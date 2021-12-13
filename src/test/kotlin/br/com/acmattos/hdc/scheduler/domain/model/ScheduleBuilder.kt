package br.com.acmattos.hdc.scheduler.domain.model

import br.com.acmattos.hdc.common.context.domain.model.AuditLog
import br.com.acmattos.hdc.scheduler.domain.cqs.CreateAScheduleForTheDentistCommand
import br.com.acmattos.hdc.scheduler.domain.cqs.CreateAScheduleForTheDentistEvent
import java.time.LocalDateTime

/**
 * @author ACMattos
 * @since 30/10/2021.
 */
object ScheduleBuilder {
    fun buildSuccessfull() = Schedule(
        ScheduleId("01FK96GENJKTN1BYZW6BRHFZFJ"),
        DentistBuilder.build(),
        PeriodBuilder.buildListWithThreeWeekDays(),
        true,
        LocalDateTime.of(2021, 10, 30, 15, 16, 0),
        LocalDateTime.of(2021, 10, 30, 15, 17, 0)
    )

    fun buildWithPeriods(periods: List<Period>) = Schedule.apply(
        CreateAScheduleForTheDentistEvent(
            CreateAScheduleForTheDentistCommand(
                ScheduleId("01FK96GENJKTN1BYZW6BRHFZFJ"),
                DentistId("01FK96HFJQKTN1BYZW6BRHFZFJ"),
                DentistBuilder.build(),
                periods,
                true,
                LocalDateTime.of(2021, 10, 30, 15, 16, 0),
                AuditLog("who", "what")
            )
        )
    )
}
