package br.com.acmattos.hdc.procedure.config

import br.com.acmattos.hdc.common.context.domain.cqs.CommandHandler
import br.com.acmattos.hdc.common.context.domain.cqs.EventStore
import br.com.acmattos.hdc.common.context.domain.cqs.EventStoreRepository
import br.com.acmattos.hdc.common.context.domain.cqs.QueryHandler
import br.com.acmattos.hdc.common.context.domain.cqs.QueryStore
import br.com.acmattos.hdc.common.context.domain.cqs.QueryStoreRepository
import br.com.acmattos.hdc.common.context.domain.model.EntityRepository
import br.com.acmattos.hdc.common.context.domain.model.Repository
import br.com.acmattos.hdc.common.context.port.persistence.mongodb.MdbCollection
import br.com.acmattos.hdc.common.context.port.persistence.mongodb.MdbCollectionConfig
import br.com.acmattos.hdc.common.context.port.persistence.mongodb.MdbConfiguration
import br.com.acmattos.hdc.common.context.port.persistence.mongodb.MdbDatabase
import br.com.acmattos.hdc.common.context.port.persistence.mongodb.MdbEventDocument
import br.com.acmattos.hdc.common.context.port.persistence.mongodb.MdbFilterTranslator
import br.com.acmattos.hdc.common.context.port.persistence.mongodb.MdbQueryDocument
import br.com.acmattos.hdc.common.context.port.persistence.mongodb.MdbRepository
import br.com.acmattos.hdc.common.context.port.persistence.mongodb.MdbSortTranslator
import br.com.acmattos.hdc.common.context.port.rest.EndpointDefinition
import br.com.acmattos.hdc.procedure.application.ProcedureCommandHandlerService
import br.com.acmattos.hdc.procedure.application.ProcedureQueryHandlerService
import br.com.acmattos.hdc.procedure.config.KoinComponentName.COMMAND_HANDLER
import br.com.acmattos.hdc.procedure.config.KoinComponentName.ENDPOINT_DEFINITION
import br.com.acmattos.hdc.procedure.config.KoinComponentName.ENTITY_MDB_COLLECT
import br.com.acmattos.hdc.procedure.config.KoinComponentName.ENTITY_MDB_COLLECT_CONF
import br.com.acmattos.hdc.procedure.config.KoinComponentName.ENTITY_MDB_CONF
import br.com.acmattos.hdc.procedure.config.KoinComponentName.ENTITY_MDB_DB
import br.com.acmattos.hdc.procedure.config.KoinComponentName.ENTITY_MDB_NAME
import br.com.acmattos.hdc.procedure.config.KoinComponentName.ENTITY_MDB_REPO
import br.com.acmattos.hdc.procedure.config.KoinComponentName.ENTITY_MDB_URL
import br.com.acmattos.hdc.procedure.config.KoinComponentName.ENTITY_REPO
import br.com.acmattos.hdc.procedure.config.KoinComponentName.EVENT_MDB_COLLECT
import br.com.acmattos.hdc.procedure.config.KoinComponentName.EVENT_MDB_COLLECT_CONF
import br.com.acmattos.hdc.procedure.config.KoinComponentName.EVENT_MDB_CONF
import br.com.acmattos.hdc.procedure.config.KoinComponentName.EVENT_MDB_DB
import br.com.acmattos.hdc.procedure.config.KoinComponentName.EVENT_MDB_NAME
import br.com.acmattos.hdc.procedure.config.KoinComponentName.EVENT_MDB_REPO
import br.com.acmattos.hdc.procedure.config.KoinComponentName.EVENT_MDB_URL
import br.com.acmattos.hdc.procedure.config.KoinComponentName.EVENT_STORE
import br.com.acmattos.hdc.procedure.config.KoinComponentName.QUERY_HANDLER
import br.com.acmattos.hdc.procedure.config.KoinComponentName.QUERY_MDB_COLLECT
import br.com.acmattos.hdc.procedure.config.KoinComponentName.QUERY_MDB_COLLECT_CONF
import br.com.acmattos.hdc.procedure.config.KoinComponentName.QUERY_MDB_CONF
import br.com.acmattos.hdc.procedure.config.KoinComponentName.QUERY_MDB_DB
import br.com.acmattos.hdc.procedure.config.KoinComponentName.QUERY_MDB_NAME
import br.com.acmattos.hdc.procedure.config.KoinComponentName.QUERY_MDB_REPO
import br.com.acmattos.hdc.procedure.config.KoinComponentName.QUERY_MDB_URL
import br.com.acmattos.hdc.procedure.config.KoinComponentName.QUERY_STORE
import br.com.acmattos.hdc.procedure.domain.cqs.ProcedureEvent
import br.com.acmattos.hdc.procedure.domain.cqs.ProcedureQuery
import br.com.acmattos.hdc.procedure.domain.model.Procedure
import br.com.acmattos.hdc.procedure.port.persistence.mongodb.DocumentIndexedField.CODE
import br.com.acmattos.hdc.procedure.port.persistence.mongodb.DocumentIndexedField.EVENT_CODE
import br.com.acmattos.hdc.procedure.port.persistence.mongodb.DocumentIndexedField.EVENT_PROCEDURE_ID_ID
import br.com.acmattos.hdc.procedure.port.persistence.mongodb.DocumentIndexedField.PROCEDURE_ID
import br.com.acmattos.hdc.procedure.port.persistence.mongodb.DocumentIndexedField.QUERY_AUDIT_LOG_WHO
import br.com.acmattos.hdc.procedure.port.persistence.mongodb.DocumentIndexedField.QUERY_ID_ID
import br.com.acmattos.hdc.procedure.port.persistence.mongodb.ProcedureMdbDocument
import br.com.acmattos.hdc.procedure.port.rest.ProcedureCommandController
import br.com.acmattos.hdc.procedure.port.rest.ProcedureControllerEndpointDefinition
import br.com.acmattos.hdc.procedure.port.rest.ProcedureQueryController
import com.mongodb.client.model.IndexOptions
import com.mongodb.client.model.Indexes
import org.bson.Document
import org.bson.conversions.Bson
import org.koin.core.component.KoinComponent
import org.koin.core.qualifier.named
import org.koin.dsl.module

/**
 * @author ACMattos
 * @since 20/03/2022.
 */
object ProcedureKoinComponent: KoinComponent {
    fun loadModule() = module {
        // 1 - Endpoint Definition
        single<EndpointDefinition>(named(ENDPOINT_DEFINITION.value)) {
            ProcedureControllerEndpointDefinition(get(), get())
        }
        // 2 - Controller Endpoint
        single {
            ProcedureCommandController(get(named(COMMAND_HANDLER.value)))
        }
        // 3 - Command Handler
        single<CommandHandler<ProcedureEvent>>(named(COMMAND_HANDLER.value)) {
            ProcedureCommandHandlerService(
                get(named(EVENT_STORE.value)),
                get(named(ENTITY_REPO.value))
            )
        }
        // 4 - Event Store - MongoDB Configuration
        single(named(EVENT_MDB_CONF.value)) {
            MdbConfiguration(EVENT_MDB_URL.value)
        }
        // 5 - Event Store - MongoDB Database
        single(named(EVENT_MDB_DB.value)) {
            MdbDatabase(get(named(EVENT_MDB_CONF.value)))
        }
        // 6 - Event Store - MongoDB Collection Config
        single(named(EVENT_MDB_COLLECT_CONF.value)) {
            MdbCollectionConfig(
                EVENT_MDB_NAME.value,
                MdbEventDocument::class.java
            )
            .addIndexes(
                Indexes.ascending(EVENT_PROCEDURE_ID_ID.fieldName),
                Indexes.ascending(EVENT_CODE.fieldName),
            )
        }
        // 7 - Event Store - MongoDB Collection
        single(named(EVENT_MDB_COLLECT.value)) {
            MdbCollection<MdbEventDocument>(
                get(named(EVENT_MDB_DB.value)),
                get(named(EVENT_MDB_COLLECT_CONF.value))
            )
        }
        // 8 - Event Store - MongoDB Repository
        single(named(EVENT_MDB_REPO.value)) {
            MdbRepository<MdbEventDocument>(
                get(named(EVENT_MDB_COLLECT.value)),
                MdbFilterTranslator(),
                MdbSortTranslator(),
            )
        }
        // 9 - Event Store - Event Store Repository
        single<EventStore<ProcedureEvent>>(named(EVENT_STORE.value)) {
            EventStoreRepository(get(named(EVENT_MDB_REPO.value)))
        }
        // 10 - Entity Repository - MongoDB Configuration
        single(named(ENTITY_MDB_CONF.value)) {
            MdbConfiguration(ENTITY_MDB_URL.value)
        }
        // 11 - Entity Repository - MongoDB Database
        single(named(ENTITY_MDB_DB.value)){
            MdbDatabase(get(named(ENTITY_MDB_CONF.value)))
        }
        // 12 - Entity Repository - MongoDB Collection Config
        single(named(ENTITY_MDB_COLLECT_CONF.value)) {
            MdbCollectionConfig(
                ENTITY_MDB_NAME.value,
                ProcedureMdbDocument::class.java
            )
            .addIndexes(
                Indexes.ascending(PROCEDURE_ID.fieldName),
            )
            .addCompoundIndexes(
                Pair<Bson, IndexOptions>(
                    Document.parse("{ '${CODE.fieldName}': 1 }"),
                    IndexOptions().unique(true)
                )
            )
        }
        // 13 - Entity Repository - MongoDB Collection
        single(named(ENTITY_MDB_COLLECT.value)) {
            MdbCollection<ProcedureMdbDocument>(
                get(named(ENTITY_MDB_DB.value)),
                get(named(ENTITY_MDB_COLLECT_CONF.value))
            )
        }
        // 14 - Entity Repository - MongoDB Repository
        single(named(ENTITY_MDB_REPO.value)) {
            MdbRepository<ProcedureMdbDocument>(
                get(named(ENTITY_MDB_COLLECT.value)),
                MdbFilterTranslator(),
                MdbSortTranslator(),
            )
        }
        // 15 - Entity Repository - Entity Repository
        single<Repository<Procedure>>(named(ENTITY_REPO.value)) {
            EntityRepository(get(named(ENTITY_MDB_REPO.value))) { entity ->
                ProcedureMdbDocument(entity)
            }
        }
        // 16 - Query Controller Endpoint
        single {
            ProcedureQueryController(get(named(QUERY_HANDLER.value)))
        }
        // 17 - Query Handler
        single<QueryHandler<Procedure>>(named(QUERY_HANDLER.value)) {
            ProcedureQueryHandlerService(
                get(named(QUERY_STORE.value)),
                get(named(ENTITY_REPO.value))
            )
        }
        // 18 - Query Store - MongoDB Configuration
        single(named(QUERY_MDB_CONF.value)) {
            MdbConfiguration(QUERY_MDB_URL.value)
        }
        // 19 - Query Store - MongoDB Database
        single(named(QUERY_MDB_DB.value)) {
            MdbDatabase(get(named(QUERY_MDB_CONF.value)))
        }
        // 20 - Query Store - MongoDB Collection Config
        single(named(QUERY_MDB_COLLECT_CONF.value)) {
            MdbCollectionConfig(
                QUERY_MDB_NAME.value,
                MdbQueryDocument::class.java
            )
            .addIndexes(
                Indexes.ascending(QUERY_ID_ID.fieldName),
                Indexes.ascending(QUERY_AUDIT_LOG_WHO.fieldName),
            )
        }
        // 21 - Query Store - MongoDB Collection
        single(named(QUERY_MDB_COLLECT.value)) {
            MdbCollection<MdbEventDocument>(
                get(named(QUERY_MDB_DB.value)),
                get(named(QUERY_MDB_COLLECT_CONF.value))
            )
        }
        // 22 - Query Store - MongoDB Repository
        single(named(QUERY_MDB_REPO.value)) {
            MdbRepository<MdbQueryDocument>(
                get(named(QUERY_MDB_COLLECT.value)),
                MdbFilterTranslator(),
                MdbSortTranslator(),
            )
        }
        // 23 - Query Store - Event Store Repository
        single<QueryStore<ProcedureQuery>>(named(QUERY_STORE.value)) {
            QueryStoreRepository(get(named(QUERY_MDB_REPO.value)))
        }
    }
}

/**
 * @author ACMattos
 * @since 26/03/2022.
 */
enum class KoinComponentName(val value: String) {
    ENDPOINT_DEFINITION("ProcedureControllerEndpointDefinition"),
    COMMAND_HANDLER("ProcedureCommandHandlerService"),

    EVENT_MDB_CONF("ProcedureEventMdbConfiguration"),
    EVENT_MDB_URL("PROCEDURE_EVENT_MONGO_URL"),
    EVENT_MDB_DB("ProcedureEventMdbDatabase"),
    EVENT_MDB_COLLECT_CONF("ProcedureEventMdbCollectionConfig"),
    EVENT_MDB_NAME("procedure_event"),
    EVENT_MDB_COLLECT("ProcedureEventMdbCollection"),
    EVENT_MDB_REPO("ProcedureEventMdbRepository"),
    EVENT_STORE("ProcedureEventStore"),

    ENTITY_MDB_CONF("ProcedureMdbConfiguration"),
    ENTITY_MDB_URL("PROCEDURE_MONGO_URL"),
    ENTITY_MDB_DB("ProcedureMdbDatabase"),
    ENTITY_MDB_COLLECT_CONF("ProcedureMdbCollectionConfig"),
    ENTITY_MDB_NAME("procedure"),
    ENTITY_MDB_COLLECT("ProcedureMdbCollection"),
    ENTITY_MDB_REPO("ProcedureMdbRepository"),
    ENTITY_REPO("ProcedureRepository"),

    QUERY_HANDLER("ProcedureQueryHandlerService"),
    QUERY_MDB_CONF("ProcedureQueryMdbConfiguration"),
    QUERY_MDB_URL("PROCEDURE_QUERY_MONGO_URL"),
    QUERY_MDB_DB("ProcedureQueryMdbDatabase"),
    QUERY_MDB_COLLECT_CONF("ProcedureQueryMdbCollectionConfig"),
    QUERY_MDB_NAME("procedure_query"),
    QUERY_MDB_COLLECT("ProcedureQueryMdbCollection"),
    QUERY_MDB_REPO("ProcedureQueryMdbRepository"),
    QUERY_STORE("ProcedureQueryStore"),
}
