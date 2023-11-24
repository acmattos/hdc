//package br.com.acmattos.hdc.odontogram.config
//
//import br.com.acmattos.hdc.common.context.domain.cqs.CommandHandler
//import br.com.acmattos.hdc.common.context.domain.cqs.EventStore
//import br.com.acmattos.hdc.common.context.domain.cqs.EventStoreRepository
//import br.com.acmattos.hdc.common.context.domain.cqs.QueryHandler
//import br.com.acmattos.hdc.common.context.domain.cqs.QueryStore
//import br.com.acmattos.hdc.common.context.domain.cqs.QueryStoreRepository
//import br.com.acmattos.hdc.common.context.domain.model.EntityRepository
//import br.com.acmattos.hdc.common.context.domain.model.Repository
//import br.com.acmattos.hdc.common.context.port.persistence.mongodb.MdbCollection
//import br.com.acmattos.hdc.common.context.port.persistence.mongodb.MdbCollectionConfig
//import br.com.acmattos.hdc.common.context.port.persistence.mongodb.MdbConfiguration
//import br.com.acmattos.hdc.common.context.port.persistence.mongodb.MdbDatabase
//import br.com.acmattos.hdc.common.context.port.persistence.mongodb.MdbEventDocument
//import br.com.acmattos.hdc.common.context.port.persistence.mongodb.MdbFilterTranslator
//import br.com.acmattos.hdc.common.context.port.persistence.mongodb.MdbQueryDocument
//import br.com.acmattos.hdc.common.context.port.persistence.mongodb.MdbRepository
//import br.com.acmattos.hdc.common.context.port.persistence.mongodb.MdbSortTranslator
//import br.com.acmattos.hdc.common.context.port.rest.EndpointDefinition
//import br.com.acmattos.hdc.odontogram.application.OdontogramCommandHandlerService
//import br.com.acmattos.hdc.odontogram.application.OdontogramQueryHandlerService
//import br.com.acmattos.hdc.odontogram.config.KoinComponentName.COMMAND_HANDLER
//import br.com.acmattos.hdc.odontogram.config.KoinComponentName.ENDPOINT_DEFINITION
//import br.com.acmattos.hdc.odontogram.config.KoinComponentName.ENTITY_MDB_COLLECT
//import br.com.acmattos.hdc.odontogram.config.KoinComponentName.ENTITY_MDB_COLLECT_CONF
//import br.com.acmattos.hdc.odontogram.config.KoinComponentName.ENTITY_MDB_CONF
//import br.com.acmattos.hdc.odontogram.config.KoinComponentName.ENTITY_MDB_DB
//import br.com.acmattos.hdc.odontogram.config.KoinComponentName.ENTITY_MDB_NAME
//import br.com.acmattos.hdc.odontogram.config.KoinComponentName.ENTITY_MDB_REPO
//import br.com.acmattos.hdc.odontogram.config.KoinComponentName.ENTITY_MDB_URL
//import br.com.acmattos.hdc.odontogram.config.KoinComponentName.ENTITY_REPO
//import br.com.acmattos.hdc.odontogram.config.KoinComponentName.EVENT_MDB_COLLECT
//import br.com.acmattos.hdc.odontogram.config.KoinComponentName.EVENT_MDB_COLLECT_CONF
//import br.com.acmattos.hdc.odontogram.config.KoinComponentName.EVENT_MDB_CONF
//import br.com.acmattos.hdc.odontogram.config.KoinComponentName.EVENT_MDB_DB
//import br.com.acmattos.hdc.odontogram.config.KoinComponentName.EVENT_MDB_NAME
//import br.com.acmattos.hdc.odontogram.config.KoinComponentName.EVENT_MDB_REPO
//import br.com.acmattos.hdc.odontogram.config.KoinComponentName.EVENT_MDB_URL
//import br.com.acmattos.hdc.odontogram.config.KoinComponentName.EVENT_STORE
//import br.com.acmattos.hdc.odontogram.config.KoinComponentName.QUERY_HANDLER
//import br.com.acmattos.hdc.odontogram.config.KoinComponentName.QUERY_MDB_COLLECT
//import br.com.acmattos.hdc.odontogram.config.KoinComponentName.QUERY_MDB_COLLECT_CONF
//import br.com.acmattos.hdc.odontogram.config.KoinComponentName.QUERY_MDB_CONF
//import br.com.acmattos.hdc.odontogram.config.KoinComponentName.QUERY_MDB_DB
//import br.com.acmattos.hdc.odontogram.config.KoinComponentName.QUERY_MDB_NAME
//import br.com.acmattos.hdc.odontogram.config.KoinComponentName.QUERY_MDB_REPO
//import br.com.acmattos.hdc.odontogram.config.KoinComponentName.QUERY_MDB_URL
//import br.com.acmattos.hdc.odontogram.config.KoinComponentName.QUERY_STORE
//import br.com.acmattos.hdc.odontogram.domain.cqs.OdontogramEvent
//import br.com.acmattos.hdc.odontogram.domain.cqs.OdontogramQuery
//import br.com.acmattos.hdc.odontogram.domain.model.Odontogram
//import br.com.acmattos.hdc.odontogram.port.persistence.mongodb.DocumentIndexedField.EVENT_ODONTOGRAM_ID_ID
//import br.com.acmattos.hdc.odontogram.port.persistence.mongodb.DocumentIndexedField.ODONTOGRAM_ID
//import br.com.acmattos.hdc.odontogram.port.persistence.mongodb.OdontogramMdbDocument
//import br.com.acmattos.hdc.odontogram.port.rest.OdontogramCommandController
//import br.com.acmattos.hdc.odontogram.port.rest.OdontogramControllerEndpointDefinition
//import br.com.acmattos.hdc.odontogram.port.rest.OdontogramQueryController
//import com.mongodb.client.model.Indexes
//import org.koin.core.component.KoinComponent
//import org.koin.core.qualifier.named
//import org.koin.dsl.module
//
///**
// * @author ACMattos
// * @since 27/12/2022.
// */
//object OdontogramKoinComponent: KoinComponent {
//    fun loadModule() = module {
//        // 1 - Endpoint Definition
//        single<EndpointDefinition>(named(ENDPOINT_DEFINITION.value)) {
//            OdontogramControllerEndpointDefinition(get(), get())
//        }
//        // 2 - Controller Endpoint
//        single {
//            OdontogramCommandController(get(named(COMMAND_HANDLER.value)))
//        }
//        // 3 - Command Handler
//        single<CommandHandler<OdontogramEvent>>(named(COMMAND_HANDLER.value)) {
//            OdontogramCommandHandlerService(
//                get(named(EVENT_STORE.value)),
//                get(named(ENTITY_REPO.value))
//            )
//        }
//        // 4 - Event Store - MongoDB Configuration
//        single(named(EVENT_MDB_CONF.value)) {
//            MdbConfiguration(EVENT_MDB_URL.value)
//        }
//        // 5 - Event Store - MongoDB Database
//        single(named(EVENT_MDB_DB.value)) {
//            MdbDatabase(get(named(EVENT_MDB_CONF.value)))
//        }
//        // 6 - Event Store - MongoDB Collection Config
//        single(named(EVENT_MDB_COLLECT_CONF.value)) {
//            MdbCollectionConfig(
//                EVENT_MDB_NAME.value,
//                MdbEventDocument::class.java
//            )
//            .addIndexes(
//                Indexes.ascending(EVENT_ODONTOGRAM_ID_ID.fieldName),
////                Indexes.ascending(EVENT_CODE.fieldName),
//            )
//        }
//        // 7 - Event Store - MongoDB Collection
//        single(named(EVENT_MDB_COLLECT.value)) {
//            MdbCollection<MdbEventDocument>(
//                get(named(EVENT_MDB_DB.value)),
//                get(named(EVENT_MDB_COLLECT_CONF.value))
//            )
//        }
//        // 8 - Event Store - MongoDB Repository
//        single(named(EVENT_MDB_REPO.value)) {
//            MdbRepository<MdbEventDocument>(
//                get(named(EVENT_MDB_COLLECT.value)),
//                MdbFilterTranslator(),
//                MdbSortTranslator(),
//            )
//        }
//        // 9 - Event Store - Event Store Repository
//        single<EventStore<OdontogramEvent>>(named(EVENT_STORE.value)) {
//            EventStoreRepository(get(named(EVENT_MDB_REPO.value)))
//        }
//        // 10 - Entity Repository - MongoDB Configuration
//        single(named(ENTITY_MDB_CONF.value)) {
//            MdbConfiguration(ENTITY_MDB_URL.value)
//        }
//        // 11 - Entity Repository - MongoDB Database
//        single(named(ENTITY_MDB_DB.value)){
//            MdbDatabase(get(named(ENTITY_MDB_CONF.value)))
//        }
//        // 12 - Entity Repository - MongoDB Collection Config
//        single(named(ENTITY_MDB_COLLECT_CONF.value)) {
//            MdbCollectionConfig(
//                ENTITY_MDB_NAME.value,
//                OdontogramMdbDocument::class.java
//            )
//            .addIndexes(
//                Indexes.ascending(ODONTOGRAM_ID.fieldName),
//            )
////                .addCompoundIndexes(
////                    Pair<Bson, IndexOptions>(
////                        Document.parse("{ '${CODE.fieldName}': 1 }"),
////                        IndexOptions().unique(true)
////                    )
////                )
//        }
//        // 13 - Entity Repository - MongoDB Collection
//        single(named(ENTITY_MDB_COLLECT.value)) {
//            MdbCollection<OdontogramMdbDocument>(
//                get(named(ENTITY_MDB_DB.value)),
//                get(named(ENTITY_MDB_COLLECT_CONF.value))
//            )
//        }
//        // 14 - Entity Repository - MongoDB Repository
//        single(named(ENTITY_MDB_REPO.value)) {
//            MdbRepository<OdontogramMdbDocument>(
//                get(named(ENTITY_MDB_COLLECT.value)),
//                MdbFilterTranslator(),
//                MdbSortTranslator(),
//            )
//        }
//        // 15 - Entity Repository - Entity Repository
//        single<Repository<Odontogram>>(named(ENTITY_REPO.value)) {
//            EntityRepository(get(named(ENTITY_MDB_REPO.value))) { entity ->
//                OdontogramMdbDocument(entity)
//            }
//        }
//        // 16 - Query Controller Endpoint
//        single {
//            OdontogramQueryController(get(named(QUERY_HANDLER.value)))
//        }
//        // 17 - Query Handler
//        single<QueryHandler<Odontogram>>(named(QUERY_HANDLER.value)) {
//            OdontogramQueryHandlerService(
//                get(named(QUERY_STORE.value)),
////                get(named(ENTITY_REPO.value))
//            )
//        }
//        // 18 - Query Store - MongoDB Configuration
//        single(named(QUERY_MDB_CONF.value)) {
//            MdbConfiguration(QUERY_MDB_URL.value)
//        }
//        // 19 - Query Store - MongoDB Database
//        single(named(QUERY_MDB_DB.value)) {
//            MdbDatabase(get(named(QUERY_MDB_CONF.value)))
//        }
//        // 20 - Query Store - MongoDB Collection Config
//        single(named(QUERY_MDB_COLLECT_CONF.value)) {
//            MdbCollectionConfig(
//                QUERY_MDB_NAME.value,
//                MdbQueryDocument::class.java
//            )
////            .addIndexes(
////                Indexes.ascending(QUERY_ID_ID.fieldName),
////                Indexes.ascending(QUERY_AUDIT_LOG_WHO.fieldName),
////            )
//        }
//        // 21 - Query Store - MongoDB Collection
//        single(named(QUERY_MDB_COLLECT.value)) {
//            MdbCollection<MdbEventDocument>(
//                get(named(QUERY_MDB_DB.value)),
//                get(named(QUERY_MDB_COLLECT_CONF.value))
//            )
//        }
//        // 22 - Query Store - MongoDB Repository
//        single(named(QUERY_MDB_REPO.value)) {
//            MdbRepository<MdbQueryDocument>(
//                get(named(QUERY_MDB_COLLECT.value)),
//                MdbFilterTranslator(),
//                MdbSortTranslator(),
//            )
//        }
//        // 23 - Query Store - Event Store Repository
//        single<QueryStore<OdontogramQuery>>(named(QUERY_STORE.value)) {
//            QueryStoreRepository(get(named(QUERY_MDB_REPO.value)))
//        }
//    }
//}
//
///**
// * @author ACMattos
// * @since 26/03/2022.
// */
//enum class KoinComponentName(val value: String) {
//    ENDPOINT_DEFINITION("OdontogramControllerEndpointDefinition"),
//    COMMAND_HANDLER("OdontogramCommandHandlerService"),
//
//    EVENT_MDB_CONF("OdontogramEventMdbConfiguration"),
//    EVENT_MDB_URL("ODONTOGRAM_EVENT_MONGO_URL"),
//    EVENT_MDB_DB("OdontogramEventMdbDatabase"),
//    EVENT_MDB_COLLECT_CONF("OdontogramEventMdbCollectionConfig"),
//    EVENT_MDB_NAME("odontogram_event"),
//    EVENT_MDB_COLLECT("OdontogramEventMdbCollection"),
//    EVENT_MDB_REPO("OdontogramEventMdbRepository"),
//    EVENT_STORE("OdontogramEventStore"),
//
//    ENTITY_MDB_CONF("OdontogramMdbConfiguration"),
//    ENTITY_MDB_URL("ODONTOGRAM_MONGO_URL"),
//    ENTITY_MDB_DB("OdontogramMdbDatabase"),
//    ENTITY_MDB_COLLECT_CONF("OdontogramMdbCollectionConfig"),
//    ENTITY_MDB_NAME("odontogram"),
//    ENTITY_MDB_COLLECT("OdontogramMdbCollection"),
//    ENTITY_MDB_REPO("OdontogramMdbRepository"),
//    ENTITY_REPO("OdontogramRepository"),
//
//    QUERY_HANDLER("OdontogramQueryHandlerService"),
//    QUERY_MDB_CONF("OdontogramQueryMdbConfiguration"),
//    QUERY_MDB_URL("ODONTOGRAM_QUERY_MONGO_URL"),
//    QUERY_MDB_DB("OdontogramQueryMdbDatabase"),
//    QUERY_MDB_COLLECT_CONF("OdontogramQueryMdbCollectionConfig"),
//    QUERY_MDB_NAME("odontogram_query"),
//    QUERY_MDB_COLLECT("OdontogramQueryMdbCollection"),
//    QUERY_MDB_REPO("OdontogramQueryMdbRepository"),
//    QUERY_STORE("OdontogramQueryStore"),
//}