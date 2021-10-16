package br.com.acmattos.hdc.scheduler.port.rest

import br.com.acmattos.hdc.common.context.port.rest.EndpointDefinition
import io.javalin.apibuilder.ApiBuilder.get
import io.javalin.apibuilder.ApiBuilder.path

/**
* @author ACMattos
* @since 27/03/2021.
*/
class ScheduleCommandControllerEndpointDefinition(
    private val controller: ScheduleCommandController
): EndpointDefinition {
    override fun routes() {
        path("schedules") {
            get(controller::createTheScheduleForTheDentist)
        }
    }
}