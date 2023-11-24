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
): EndpointDefinition() {
    override fun routes() {
        path(PERSONS) {
            post(command::createPatient)
            logPostRoute("/$PERSONS")

            put(command::updatePatient)
            logPutRoute("/$PERSONS")

            get(query::findAllPersons)
            logGetRoute("/$PERSONS")

            path(CONTACT_TYPES){
                get(query::findAllContactTypes)
                logGetRoute("/$PERSONS/$CONTACT_TYPES")
            }
            path(GENDERS){
                get(query::findAllGenders)
                logGetRoute("/$PERSONS/$GENDERS")
            }
            path(MARITAL_STATUSES){
                get(query::findAllMaritalStatuses)
                logGetRoute("/$PERSONS/$MARITAL_STATUSES")
            }
            path(PERSON_TYPES){
                get(query::findAllPersonTypes)
                logGetRoute("/$PERSONS/$PERSON_TYPES")
            }
            path(STATES){
                get(query::findAllStates)
                logGetRoute("/$PERSONS/$STATES")
            }
            path(STATUSES){
                get(query::findAllStatuses)
                logGetRoute("/$PERSONS/$STATUSES")
            }
            path(DENTIST_ID) {
                get(query::findTheDentist)
                logGetRoute("/$PERSONS/$DENTIST_ID")
            }
        }
        logAllRoutesLoaded()
        logger.info("All routes loaded for: -> ${PersonControllerEndpointDefinition::class.java} <-")
    }

    override fun name() = "${PersonControllerEndpointDefinition::class.java}"

    companion object: Loggable()
}
