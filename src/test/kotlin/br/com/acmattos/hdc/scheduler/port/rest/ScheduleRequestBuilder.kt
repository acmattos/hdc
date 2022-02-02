package br.com.acmattos.hdc.scheduler.port.rest

import br.com.acmattos.hdc.scheduler.domain.cqs.CreateAScheduleForTheDentistCommand
import br.com.acmattos.hdc.scheduler.domain.cqs.CreateAScheduleForTheDentistEvent
import br.com.acmattos.hdc.scheduler.domain.model.DentistBuilder
import br.com.acmattos.hdc.scheduler.domain.model.Schedule

/**
 * @author ACMattos
 * @since 21/10/2021.
 */
object ScheduleRequestBuilder { // TODO  Test
    fun buildCreateAScheduleForTheDentistRequest() = CreateAScheduleForTheDentistRequest(
        "12345678901234567890123456",
        listOf(
            PeriodRequest(
                "MONDAY",
                "09:00",
                "18:00",
            30
            )
        ),
    )

    fun buildSchedule() = Schedule.apply(
        CreateAScheduleForTheDentistEvent(
            (buildCreateAScheduleForTheDentistRequest()
                .toType() as CreateAScheduleForTheDentistCommand)
            .copy(dentist = DentistBuilder.build())
        )
    )
}
