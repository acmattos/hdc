package br.com.acmattos.hdc.person.port.rest

import br.com.acmattos.hdc.common.context.port.rest.EndpointDefinition
import io.javalin.apibuilder.ApiBuilder.get
import io.javalin.apibuilder.ApiBuilder.path
import io.javalin.apibuilder.ApiBuilder.post

/**
 * @author ACMattos
 * @since 05/10/2021.
 */
class PersonControllerEndpointDefinition(
    private val command: PersonCommandController,
    private val query: PersonQueryController
): EndpointDefinition {
    override fun routes() {
        path("persons") {
            post(command::createADentist)
            path(":dentist_id") {
                get(query::findTheDentist)
            }
       }
    }
}
