package br.com.acmattos.hdc.scheduler.port.rest

/**
 * @author ACMattos
 * @since 21/10/2021.
 */
object ScheduleRequestBuilder {
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
}
