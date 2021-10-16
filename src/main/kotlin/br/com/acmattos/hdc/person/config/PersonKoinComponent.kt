package br.com.acmattos.hdc.person.config

import br.com.acmattos.hdc.common.context.domain.cqs.CommandHandler
import br.com.acmattos.hdc.common.context.domain.cqs.EventStore
import br.com.acmattos.hdc.common.context.domain.cqs.EventStoreRepository
import br.com.acmattos.hdc.common.context.domain.model.EntityRepository
import br.com.acmattos.hdc.common.context.domain.model.Repository
import br.com.acmattos.hdc.common.context.port.persistence.mongodb.MdbCollection
import br.com.acmattos.hdc.common.context.port.persistence.mongodb.MdbCollectionConfig
import br.com.acmattos.hdc.common.context.port.persistence.mongodb.MdbConfiguration
import br.com.acmattos.hdc.common.context.port.persistence.mongodb.MdbDatabase
import br.com.acmattos.hdc.common.context.port.persistence.mongodb.MdbEventDocument
import br.com.acmattos.hdc.common.context.port.persistence.mongodb.MdbRepository
import br.com.acmattos.hdc.common.context.port.rest.EndpointDefinition
import br.com.acmattos.hdc.person.application.PersonCommandHandlerService
import br.com.acmattos.hdc.person.domain.cqs.PersonEvent
import br.com.acmattos.hdc.person.domain.model.Person
import br.com.acmattos.hdc.person.port.persistence.mongodb.PersonMdbDocument
import br.com.acmattos.hdc.person.port.rest.PersonCommandController
import br.com.acmattos.hdc.person.port.rest.PersonCommandControllerEndpointDefinition
import com.mongodb.client.model.Indexes
import org.koin.core.component.KoinComponent
import org.koin.core.qualifier.named
import org.koin.dsl.module

const val ED = "PersonCommandControllerEndpointDefinition"
const val CH = "PersonCommandHandlerService"
const val EMC = "PersonEventMdbConfiguration"
const val EMU = "PERSON_EVENT_MONGO_URL"
const val EMD = "PersonEventMdbDatabase"
const val ECC = "PersonEventMdbCollectionConfig"
const val PEN = "person_event"
const val EMO = "PersonEventMdbCollection"
const val EMR = "PersonEventMdbRepository"
const val PES = "PersonEventStore"

const val PMC = "PersonMdbConfiguration"
const val PMU = "PERSON_MONGO_URL"
const val PMD = "PersonMdbDatabase"
const val PCC = "PersonMdbCollectionConfig"
const val PET = "person"
const val PMO = "PersonMdbCollection"
const val PMR = "PersonMdbRepository"
const val PRE = "PersonRepository"

/**
 * @author ACMattos
 * @since 05/10/2021.
 */
object PersonKoinComponent: KoinComponent {
    fun loadModule() = module {
        // 1 - Endpoint Definition
        single<EndpointDefinition>(named(ED)) {
            PersonCommandControllerEndpointDefinition(get())
        }
        // 2 - Controller Endpoint
        single {
            PersonCommandController(get(named(CH)))
        }
        // 3 - Command Handler
        single<CommandHandler<PersonEvent>>(named(CH)) {
            PersonCommandHandlerService(get(named(PES)), get(named(PRE)))
        }
        // 4 - Event Store - MongoDB Configuration
        single(named(EMC)) {
            MdbConfiguration(EMU)
        }
        // 5 - Event Store - MongoDB Database
        single(named(EMD)) {
            MdbDatabase(get(named(EMC)))
        }
        // 6 - Event Store - MongoDB Collection Config
        single(named(ECC)) {
            MdbCollectionConfig(PEN, MdbEventDocument::class.java)
            .addIndexes(
                Indexes.ascending("event.person_id.id"),
            )
        }
        // 7 - Event Store - MongoDB Collection
        single(named(EMO)) {
            MdbCollection<MdbEventDocument>(get(named(EMD)), get(named(ECC)))
        }
        // 8 - Event Store - MongoDB Repository
        single(named(EMR)) {
            MdbRepository<MdbEventDocument>(get(named(EMO)))
        }
        // 9 - Event Store - Event Store Repository
        single<EventStore<PersonEvent>>(named(PES)) {
            EventStoreRepository(get(named(EMR)))
        }
        // 10 - Entity Repository - MongoDB Configuration 
        single(named(PMC)) {
            MdbConfiguration(PMU)
        }
        // 11 - Entity Repository - MongoDB Database 
        single(named(PMD)){
            MdbDatabase(get(named(PMC)))
        }
        // 12 - Entity Repository - MongoDB Collection Config
        single(named(PCC)) {
            MdbCollectionConfig(PET, PersonMdbDocument::class.java)
            .addIndexes(
                Indexes.ascending("person_id"),
            )
        }
        // 13 - Entity Repository - MongoDB Collection 
        single(named(PMO)) {
            MdbCollection<PersonMdbDocument>(get(named(PMD)), get(named(PCC)))
        }
        // 14 - Entity Repository - MongoDB Repository
        single(named(PMR)) {
            MdbRepository<PersonMdbDocument>(get(named(PMO)))
        }
        // 15 - Entity Repository - Entity Repository
        single<Repository<Person>>(named(PRE)) {
            EntityRepository(get(named(PMR))) { entity ->
                PersonMdbDocument(entity)
            }
        }
    }
}
