package br.com.acmattos.hdc.person.port.rest

import br.com.acmattos.hdc.common.context.port.rest.EndpointDefinition
import io.javalin.apibuilder.ApiBuilder.path
import io.javalin.apibuilder.ApiBuilder.post

/**
 * @author ACMattos
 * @since 05/10/2021.
 */
class PersonCommandControllerEndpointDefinition(
    private val controller: PersonCommandController
): EndpointDefinition {
    override fun routes() {
        path("persons") {
            post(controller::createADentist)
        }
    }
}
