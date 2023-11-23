package br.com.acmattos.hdc.procedure.port.rest

import br.com.acmattos.hdc.common.context.port.rest.EndpointDefinition
import br.com.acmattos.hdc.common.tool.loggable.Loggable
import io.javalin.apibuilder.ApiBuilder.delete
import io.javalin.apibuilder.ApiBuilder.get
import io.javalin.apibuilder.ApiBuilder.path
import io.javalin.apibuilder.ApiBuilder.post
import io.javalin.apibuilder.ApiBuilder.put

private const val CONTEXT = "procedures"
private const val ID = ":procedure_id"
private const val IDS = ":procedure_ids"

/**
 * @author ACMattos
 * @since 20/03/2022.
 */
class ProcedureControllerEndpointDefinition(
    private val command: ProcedureCommandController,
    private val query: ProcedureQueryController
): EndpointDefinition {
    override fun routes() {
        path(CONTEXT) {
            post(command::create)
            logger.info(routePostMessage("/$CONTEXT"))
            put(command::update)
            logger.info(routePutMessage("/$CONTEXT"))
            get(query::findAllProcedures)
            logger.info(routeGetMessage("/$CONTEXT"))
            path(ID) {
                get(query::findTheProcedure)
                logger.info(routeGetMessage("/$CONTEXT/$ID"))
            }
            path(IDS) {
                delete(command::delete)
                logger.info(routeDeleteMessage("/$CONTEXT/$IDS"))
            }
        }
        logger.info("All routes loaded for: -> ${ProcedureControllerEndpointDefinition::class.java.simpleName} <-")
    }

    companion object: Loggable()
}
