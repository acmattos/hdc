package br.com.acmattos.hdc.scheduler.port.rest

import br.com.acmattos.hdc.common.tool.server.javalin.ContextBuilder
import io.mockk.every

/**
 * @author ACMattos
 * @since 27/11/2021.
 */
object AppointmentBuilder {
    fun buildCreateAppointmentsForTheScheduleRequestBuilder(): CreateAppointmentsForTheScheduleRequestBuilder {
        val context = ContextBuilder.mockContext("schedule_id")
        every { ContextBuilder.req.inputStream.readBytes() } returns """
            {
               "from":"2021-11-07",
               "to":"2021-11-12"
            }
        """.trimIndent().encodeToByteArray()

        return CreateAppointmentsForTheScheduleRequestBuilder(
            context
        )
    }
}
