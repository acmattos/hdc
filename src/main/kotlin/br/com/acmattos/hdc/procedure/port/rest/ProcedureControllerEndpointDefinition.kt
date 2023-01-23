package br.com.acmattos.hdc.procedure.port.rest

import br.com.acmattos.hdc.common.context.port.rest.EndpointDefinition
import br.com.acmattos.hdc.common.tool.loggable.Loggable
import io.javalin.apibuilder.ApiBuilder.delete
import io.javalin.apibuilder.ApiBuilder.get
import io.javalin.apibuilder.ApiBuilder.path
import io.javalin.apibuilder.ApiBuilder.post
import io.javalin.apibuilder.ApiBuilder.put

private const val PROCEDURES = "procedures"
private const val PROCEDURE_ID = ":procedure_id"
private const val PROCEDURE_IDS = ":procedure_ids"

/**
 * @author ACMattos
 * @since 20/03/2022.
 */
class ProcedureControllerEndpointDefinition(
    private val command: ProcedureCommandController,
    private val query: ProcedureQueryController
): EndpointDefinition {
    override fun routes() {
        path(PROCEDURES) {
            post(command::create)
            put(command::update)
            get(query::findAllProcedures)
            path(PROCEDURE_ID) {
                get(query::findTheProcedure)
            }
            path(PROCEDURE_IDS) {
                delete(command::delete)
            }
        }
        logger.info("Route loaded: -> POST   /$PROCEDURES <-")
        logger.info("Route loaded: -> PUT    /$PROCEDURES <-")
        logger.info("Route loaded: -> GET    /$PROCEDURES <-")
        logger.info("Route loaded: -> GET    /$PROCEDURES/$PROCEDURE_ID <-")
        logger.info("Route loaded: -> DELETE /$PROCEDURES/$PROCEDURE_IDS <-")
        logger.info("All routes loaded for: -> ${ProcedureControllerEndpointDefinition::class.java.simpleName} <-")
    }

    companion object: Loggable()
}
