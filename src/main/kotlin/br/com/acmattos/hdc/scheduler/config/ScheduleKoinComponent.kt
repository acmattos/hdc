package br.com.acmattos.hdc.scheduler.config

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
import br.com.acmattos.hdc.common.tool.http.Http
import br.com.acmattos.hdc.scheduler.application.ScheduleCommandApplicationService
import br.com.acmattos.hdc.scheduler.domain.cqs.ScheduleEvent
import br.com.acmattos.hdc.scheduler.domain.model.Schedule
import br.com.acmattos.hdc.scheduler.port.persistence.mongodb.ScheduleMdbDocument
import br.com.acmattos.hdc.scheduler.port.rest.ScheduleCommandController
import br.com.acmattos.hdc.scheduler.port.rest.ScheduleControllerEndpointDefinition
import br.com.acmattos.hdc.scheduler.port.service.DentistRestServiceBuilder
import com.mongodb.client.model.Indexes
import org.koin.core.component.KoinComponent
import org.koin.core.qualifier.named
import org.koin.dsl.module

const val ED = "ScheduleCommandControllerEndpointDefinition"
const val CH = "ScheduleCommandHandlerService"
const val EMC = "ScheduleEventMdbConfiguration"
const val EMU = "SCHEDULE_EVENT_MONGO_URL"
const val EMD = "ScheduleEventMdbDatabase"
const val ECC = "ScheduleEventMdbCollectionConfig"
const val PEN = "schedule_event"
const val EMO = "ScheduleEventMdbCollection"
const val EMR = "ScheduleEventMdbRepository"
const val PES = "ScheduleEventStore"

const val PMC = "ScheduleMdbConfiguration"
const val PMU = "SCHEDULE_MONGO_URL"
const val PMD = "ScheduleMdbDatabase"
const val PCC = "ScheduleMdbCollectionConfig"
const val PET = "schedule"
const val PMO = "ScheduleMdbCollection"
const val PMR = "ScheduleMdbRepository"
const val PRE = "ScheduleRepository"

const val DRS = "DentistRestService"
const val DHC = "DentistHttpClient"
const val DBU = "DENTIST_BASE_URL"

/**
 * @author ACMattos
 * @since 14/08/2019.
 */
object ScheduleKoinComponent: KoinComponent {
    fun loadModule() = module {
        // 1 - Endpoint Definition
        single<EndpointDefinition>(named(ED)) {
            ScheduleControllerEndpointDefinition(get())
        }
        // 2 - Controller Endpoint
        single {
            ScheduleCommandController(get(named(CH)))
        }
        // 3 - Command Handler
        single<CommandHandler<ScheduleEvent>>(named(CH)) {
            ScheduleCommandApplicationService(
                get(named(PES)),
                get(named(PRE)),
                get(named(DRS))
            )
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
                    Indexes.ascending("event.schedule_id.id"),
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
        single<EventStore<ScheduleEvent>>(named(PES)) {
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
            MdbCollectionConfig(PET, ScheduleMdbDocument::class.java)
                .addIndexes(
                    Indexes.ascending("Schedule_id"),
                )
        }
        // 13 - Entity Repository - MongoDB Collection
        single(named(PMO)) {
            MdbCollection<ScheduleMdbDocument>(get(named(PMD)), get(named(PCC)))
        }
        // 14 - Entity Repository - MongoDB Repository
        single(named(PMR)) {
            MdbRepository<ScheduleMdbDocument>(get(named(PMO)))
        }
        // 15 - Entity Repository - Entity Repository
        single<Repository<Schedule>>(named(PRE)) {
            EntityRepository(get(named(PMR))) { entity ->
                ScheduleMdbDocument(entity)
            }
        }
        // 16 - Dentist HTTP Client
        single(named(DHC)) {
            Http(DBU)
        }
        // 17 - Dentist Rest Service
        single(named(DRS)) {
            DentistRestServiceBuilder(get(named(DHC)))
                .build()
        }
    }
}
