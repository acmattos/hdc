package br.com.acmattos.hdc.odontogram.port.rest

import br.com.acmattos.hdc.common.context.port.rest.EndpointDefinition
import br.com.acmattos.hdc.common.tool.loggable.Loggable
import io.javalin.apibuilder.ApiBuilder.delete
import io.javalin.apibuilder.ApiBuilder.get
import io.javalin.apibuilder.ApiBuilder.path
import io.javalin.apibuilder.ApiBuilder.post
import io.javalin.apibuilder.ApiBuilder.put

private const val ODONTOGRAMS = "odontograms"
private const val NEW = "new"
private const val ODONTOGRAM_ID = ":odontogram_id"

class OdontogramControllerEndpointDefinition(
    private val command: OdontogramCommandController,
    private val query: OdontogramQueryController
): EndpointDefinition() {
    override fun routes() {
        path(ODONTOGRAMS) {
            post(command::create)
            logPostRoute("Route loaded: -> POST   /$ODONTOGRAMS <-")

            put(command::update)
            logPutRoute("Route loaded: -> PUT    /$ODONTOGRAMS <-")

            path(ODONTOGRAM_ID) {
                get(query::get)
                logGetRoute("Route loaded: -> GET    /$ODONTOGRAMS/$ODONTOGRAM_ID <-")

                delete(command::delete)
                logDeleteRoute("Route loaded: -> DELETE /$ODONTOGRAMS/ODONTOGRAM_ID <-")
            }
        }
        logAllRoutesLoaded()
    }

    override fun name() = "${OdontogramControllerEndpointDefinition::class.java}"

    companion object: Loggable()
}
