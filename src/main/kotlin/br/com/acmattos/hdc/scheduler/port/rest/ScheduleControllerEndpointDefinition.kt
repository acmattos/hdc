package br.com.acmattos.hdc.scheduler.port.rest

import br.com.acmattos.hdc.common.context.port.rest.EndpointDefinition
import br.com.acmattos.hdc.common.tool.loggable.Loggable
import io.javalin.apibuilder.ApiBuilder.path
import io.javalin.apibuilder.ApiBuilder.post

private const val SCHEDULES = "schedules"

/**
* @author ACMattos
* @since 27/03/2021.
*/
class ScheduleControllerEndpointDefinition(
    private val controller: ScheduleCommandController
): EndpointDefinition {
    override fun routes() {
        path(SCHEDULES) {
            post(controller::createAScheduleForTheDentist)
        }
        logger.info("Route loaded: -> POST /$SCHEDULES <-")
        logger.info("All routes loaded for: -> ${ScheduleControllerEndpointDefinition::class.java.simpleName} <-")
    }

    companion object: Loggable()
}
