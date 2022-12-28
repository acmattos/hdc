package br.com.acmattos.hdc.odontogram.port.rest

import br.com.acmattos.hdc.common.context.port.rest.EndpointDefinition
import br.com.acmattos.hdc.common.tool.loggable.Loggable
import io.javalin.apibuilder.ApiBuilder.get
import io.javalin.apibuilder.ApiBuilder.path

private const val ODONTOGRAMS = "odontograms"
private const val NEW = "new"
//private const val PROCEDURE_ID = ":procedure_id"

class OdontogramControllerEndpointDefinition(
//    private val command: OdontogramCommandController,
    private val query: OdontogramQueryController
): EndpointDefinition {
    override fun routes() {
        path(ODONTOGRAMS) {
//            post(command::createDentalProcedure)
//            put(command::updateDentalProcedure)
            path(NEW) {
                get(query::getABasicOdontogram)
            }
//            path(PROCEDURE_ID) {
//                ApiBuilder.get(query::findTheProcedure)
//                ApiBuilder.delete(command::deleteDentalProcedure)
//            }
        }
//        logger.info("Route loaded: -> POST   /$PROCEDURES <-")
//        logger.info("Route loaded: -> PUT    /$PROCEDURES <-")
        logger.info("Route loaded: -> GET    /$ODONTOGRAMS/$NEW <-")
//        logger.info("Route loaded: -> GET    /$PROCEDURES/$PROCEDURE_ID <-")
//        logger.info("Route loaded: -> DELETE /$PROCEDURES/$PROCEDURE_ID <-")
//        logger.info("All routes loaded for: -> ${OdontogramControllerEndpointDefinition::class.java.simpleName} <-")
    }

    companion object: Loggable()
}