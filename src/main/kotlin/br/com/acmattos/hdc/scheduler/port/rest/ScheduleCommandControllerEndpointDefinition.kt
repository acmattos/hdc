package br.com.acmattos.hdc.scheduler.port.rest

import br.com.acmattos.hdc.common.context.port.rest.EndpointDefinition
import io.javalin.apibuilder.ApiBuilder.path
import io.javalin.apibuilder.ApiBuilder.post

/**
* @author ACMattos
* @since 27/03/2021.
*/
class ScheduleCommandControllerEndpointDefinition(
    private val controller: ScheduleCommandController
): EndpointDefinition {
    override fun routes() {
        path("schedules") {
            post(controller::createAScheduleForTheDentist)
        }
    }
}
