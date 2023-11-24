package br.com.acmattos.hdc.scheduler.port.rest

import br.com.acmattos.hdc.common.context.port.rest.EndpointDefinition
import br.com.acmattos.hdc.common.tool.loggable.Loggable
import io.javalin.apibuilder.ApiBuilder.path
import io.javalin.apibuilder.ApiBuilder.post

private const val SCHEDULES = "schedules"
private const val SCHEDULE_ID = ":schedule_id/appointments"

/**
 * @author ACMattos
 * @since 06/08/2021.
 */
class AppointmentControllerEndpointDefinition (
    private val controller: AppointmentCommandController
): EndpointDefinition() {
    override fun routes() {
        path(SCHEDULES) {
            path(SCHEDULE_ID) {
                post(controller::createAppointmentsForTheSchedule)
                logPostRoute("/$SCHEDULES/$SCHEDULE_ID")
            }
        }
        logAllRoutesLoaded()
    }

    override fun name() = "${AppointmentControllerEndpointDefinition::class.java}"

    companion object: Loggable()
}
