package br.com.acmattos.hdc.scheduler.port.rest

import br.com.acmattos.hdc.common.tool.loggable.Loggable
import io.javalin.http.Context
import org.eclipse.jetty.http.HttpStatus

private const val SCHEDULE_ENDPOINT = "SCHEDULE_ENDPOINT"

/**
* @author ACMattos
* @since 26/06/2019.
*/
class ScheduleCommandController() {
    fun createScheduleForADentist(context: Context) {
        logger.debug("[{}] - Creating a schedule for a dentist...", SCHEDULE_ENDPOINT)
        context
            .status(HttpStatus.OK_200)
            .json("OK!")
        logger.info("[{}] - Schedule created for a dentist successfully!", SCHEDULE_ENDPOINT)
    }

    companion object: Loggable()
}
