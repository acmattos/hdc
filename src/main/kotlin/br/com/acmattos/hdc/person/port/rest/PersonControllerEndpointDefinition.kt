package br.com.acmattos.hdc.person.port.rest

import br.com.acmattos.hdc.common.context.port.rest.EndpointDefinition
import br.com.acmattos.hdc.common.tool.loggable.Loggable
import io.javalin.apibuilder.ApiBuilder.get
import io.javalin.apibuilder.ApiBuilder.path
import io.javalin.apibuilder.ApiBuilder.post

private const val PERSONS = "persons"
private const val DENTIST_ID = ":dentist_id"
/**
 * @author ACMattos
 * @since 05/10/2021.
 */
class PersonControllerEndpointDefinition(
    private val command: PersonCommandController,
    private val query: PersonQueryController
): EndpointDefinition {
    override fun routes() {
        path(PERSONS) {
            post(command::createADentist)
            get(query::findAllPersons)
            path(DENTIST_ID) {
                get(query::findTheDentist)
            }
        }
        logger.info("Route loaded: -> POST /$PERSONS <-")
        logger.info("Route loaded: -> GET /$PERSONS <-")
        logger.info("Route loaded: -> GET /$PERSONS/$DENTIST_ID <-")
        logger.info("All routes loaded for: -> ${PersonControllerEndpointDefinition::class.java} <-")
    }

    companion object: Loggable()
}
