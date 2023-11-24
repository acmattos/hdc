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
): EndpointDefinition() {
    override fun routes() {
        path(CONTEXT) {
            post(command::create)
            logPostRoute("/$CONTEXT")

            put(command::update)
            logPutRoute("/$CONTEXT")

            get(query::findAllProcedures)
            logGetRoute("/$CONTEXT")

            path(ID) {
                get(query::findTheProcedure)
                logGetRoute("/$CONTEXT/$ID")
            }

            path(IDS) {
                delete(command::delete)
                logDeleteRoute("/$CONTEXT/$IDS")
            }
        }
        logAllRoutesLoaded()
    }

    override fun name() = "${ProcedureControllerEndpointDefinition::class.java}"

    companion object: Loggable()
}
