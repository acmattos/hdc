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
import br.com.acmattos.hdc.common.context.port.persistence.mongodb.MdbFilterTranslator
import br.com.acmattos.hdc.common.context.port.persistence.mongodb.MdbRepository
import br.com.acmattos.hdc.common.context.port.rest.EndpointDefinition
import br.com.acmattos.hdc.scheduler.application.AppointmentCommandHandlerService
import br.com.acmattos.hdc.scheduler.domain.cqs.AppointmentEvent
import br.com.acmattos.hdc.scheduler.domain.model.Appointment
import br.com.acmattos.hdc.scheduler.port.persistence.mongodb.AppointmentMdbDocument
import br.com.acmattos.hdc.scheduler.port.rest.AppointmentCommandController
import br.com.acmattos.hdc.scheduler.port.rest.AppointmentControllerEndpointDefinition
import com.mongodb.client.model.Indexes
import org.koin.core.component.KoinComponent
import org.koin.core.qualifier.named
import org.koin.dsl.module

const val AED = "AppointmentControllerEndpointDefinition"
const val ACH = "AppointmentCommandHandlerService"
const val AEMC = "AppointmentEventMdbConfiguration"
const val AEMU = "APPOINTMENT_EVENT_MONGO_URL"
const val AEMD = "AppointmentEventMdbDatabase"
const val AECC = "AppointmentEventMdbCollectionConfig"
const val APEN = "appointment_event"
const val AEMO = "AppointmentEventMdbCollection"
const val AEMR = "AppointmentEventMdbRepository"
const val APES = "AppointmentEventStore"

const val APMC = "AppointmentMdbConfiguration"
const val APMU = "APPOINTMENT_MONGO_URL"
const val APMD = "AppointmentMdbDatabase"
const val APCC = "AppointmentMdbCollectionConfig"
const val APET = "appointment"
const val APMO = "AppointmentMdbCollection"
const val APMR = "AppointmentMdbRepository"
const val APRE = "AppointmentRepository"

/**
 * @author ACMattos
 * @since 03/11/2020.
 */
object AppointmentKoinComponent: KoinComponent {
    fun loadModule() = module {
        // 1 - Endpoint Definition
        single<EndpointDefinition>(named(AED)) {
            AppointmentControllerEndpointDefinition(get())
        }
        // 2 - Controller Endpoint
        single {
            AppointmentCommandController(get(named(ACH)))
        }
        // 3 - Command Handler
        single<CommandHandler<AppointmentEvent>>(named(ACH)) {
            AppointmentCommandHandlerService(
                get(named(APES)),
                get(named(APRE)),
                get(named(PES))
            )
        }
        // 4 - Event Store - MongoDB Configuration
        single(named(AEMC)) {
            MdbConfiguration(AEMU)
        }
        // 5 - Event Store - MongoDB Database
        single(named(AEMD)) {
            MdbDatabase(get(named(AEMC)))
        }
        // 6 - Event Store - MongoDB Collection Config
        single(named(AECC)) {
            MdbCollectionConfig(APEN, MdbEventDocument::class.java)
                .addIndexes(
                    Indexes.ascending("event.appointment_id.id"),
                )
        }
        // 7 - Event Store - MongoDB Collection
        single(named(AEMO)) {
            MdbCollection<MdbEventDocument>(get(named(AEMD)), get(named(AECC)))
        }
        // 8 - Event Store - MongoDB Repository
        single(named(AEMR)) {
            MdbRepository<MdbEventDocument>(
                get(named(AEMO)),
                MdbFilterTranslator()
            )
        }
        // 9 - Event Store - Event Store Repository
        single<EventStore<AppointmentEvent>>(named(APES)) {
            EventStoreRepository(get(named(AEMR)))
        }
        // 10 - Entity Repository - MongoDB Configuration
        single(named(APMC)) {
            MdbConfiguration(APMU)
        }
        // 11 - Entity Repository - MongoDB Database
        single(named(APMD)) {
            MdbDatabase(get(named(APMC)))
        }
        // 12 - Entity Repository - MongoDB Collection Config
        single(named(APCC)) {
            MdbCollectionConfig(APET, AppointmentMdbDocument::class.java)
                .addIndexes(
                    Indexes.ascending("appointment_id"),
                )
        }
        // 13 - Entity Repository - MongoDB Collection
        single(named(APMO)) {
            MdbCollection<AppointmentMdbDocument>(get(named(APMD)), get(named(APCC)))
        }
        // 14 - Entity Repository - MongoDB Repository
        single(named(APMR)) {
            MdbRepository<AppointmentMdbDocument>(
                get(named(APMO)),
                MdbFilterTranslator()
            )
        }
        // 15 - Entity Repository - Entity Repository
        single<Repository<Appointment>>(named(APRE)) {
            EntityRepository(get(named(APMR))) { entity ->
                AppointmentMdbDocument(entity)
            }
        }
    }
}
