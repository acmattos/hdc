package br.com.acmattos.hdc.person.port.rest

import br.com.acmattos.hdc.common.context.port.rest.EndpointDefinition
import br.com.acmattos.hdc.common.tool.loggable.Loggable
import io.javalin.apibuilder.ApiBuilder.get
import io.javalin.apibuilder.ApiBuilder.path
import io.javalin.apibuilder.ApiBuilder.post
import io.javalin.apibuilder.ApiBuilder.put

private const val PERSONS = "persons"
private const val CONTACT_TYPES = "contact_types"
private const val GENDERS = "genders"
private const val MARITAL_STATUSES = "marital_statuses"
private const val PERSON_TYPES = "person_types"
private const val STATES = "states"
private const val STATUSES = "statuses"
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
            //post(command::createADentist)
            post(command::createPatient)
            put(command::updatePatient)
            get(query::findAllPersons)
            path(CONTACT_TYPES){
                get(query::findAllContactTypes)
            }
            path(GENDERS){
                get(query::findAllGenders)
            }
            path(MARITAL_STATUSES){
                get(query::findAllMaritalStatuses)
            }
            path(PERSON_TYPES){
                get(query::findAllPersonTypes)
            }
            path(STATES){
                get(query::findAllStates)
            }
            path(STATUSES){
                get(query::findAllStatuses)
            }
            path(DENTIST_ID) {
                get(query::findTheDentist)
            }
        }
        logger.info("Route loaded: -> POST /$PERSONS <-")
        logger.info("Route loaded: -> PUT /$PERSONS <-")
        logger.info("Route loaded: -> GET  /$PERSONS <-")
        logger.info("Route loaded: -> GET  /$PERSONS/$CONTACT_TYPES <-")
        logger.info("Route loaded: -> GET  /$PERSONS/$GENDERS <-")
        logger.info("Route loaded: -> GET  /$PERSONS/$MARITAL_STATUSES <-")
        logger.info("Route loaded: -> GET  /$PERSONS/$PERSON_TYPES <-")
        logger.info("Route loaded: -> GET  /$PERSONS/$STATES <-")
        logger.info("Route loaded: -> GET  /$PERSONS/$STATUSES <-")
        logger.info("Route loaded: -> GET  /$PERSONS/$DENTIST_ID <-")
        logger.info("All routes loaded for: -> ${PersonControllerEndpointDefinition::class.java} <-")
    }

    companion object: Loggable()
}
