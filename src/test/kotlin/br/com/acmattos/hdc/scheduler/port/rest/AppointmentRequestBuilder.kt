//package br.com.acmattos.hdc.scheduler.port.rest
//
//import br.com.acmattos.hdc.common.tool.server.javalin.ContextBuilder
//
///**
// * @author ACMattos
// * @since 27/11/2021.
// */
//object AppointmentRequestBuilder {
//    fun buildCreateAppointmentsForTheScheduleRequestBuilder(): CreateAppointmentsForTheScheduleRequestBuilder {
//        return CreateAppointmentsForTheScheduleRequestBuilder(
//            getContext()
//        )
//    }
//
//    fun getContext() = ContextBuilder().mockContext(
//        matchedPath = "schedule_id",
//        jsonBody = """
//            {
//               "from":"2021-11-07",
//               "to":"2021-11-12"
//            }
//            """.trimIndent()
//    )
//}
