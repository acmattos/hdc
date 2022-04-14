package br.com.acmattos.hdc.person.application

import br.com.acmattos.hdc.common.context.config.ContextLogEnum.SERVICE
import br.com.acmattos.hdc.common.context.domain.cqs.Query
import br.com.acmattos.hdc.common.context.domain.cqs.QueryHandler
import br.com.acmattos.hdc.common.context.domain.cqs.QueryResult
import br.com.acmattos.hdc.common.context.domain.cqs.QueryStore
import br.com.acmattos.hdc.common.context.domain.model.Repository
import br.com.acmattos.hdc.common.tool.loggable.Loggable
import br.com.acmattos.hdc.person.config.PersonLogEnum.PERSON
import br.com.acmattos.hdc.person.domain.cqs.FindAllPersonTypesQuery
import br.com.acmattos.hdc.person.domain.cqs.FindAllPersonsQuery
import br.com.acmattos.hdc.person.domain.cqs.FindTheDentistQuery
import br.com.acmattos.hdc.person.domain.cqs.PersonQuery
import br.com.acmattos.hdc.person.domain.model.Person

/**
 * @author ACMattos
 * @since 02/11/2021.
 */
class PersonQueryHandlerService(
    private val queryStore: QueryStore<PersonQuery>,
    private val repository: Repository<Person>
): QueryHandler<Person> {
    override fun handle(query: Query): QueryResult<Person> {
        logger.debug(
            "[{} {}] - Handling query {}...: -> {} <-",
            PERSON.name,
            SERVICE.name,
            query.javaClass.name,
            query.toString()
        )

        val result = when(query){
            is FindAllPersonsQuery -> handle(query)
            is FindTheDentistQuery -> handle(query)
            else -> handle(query as FindAllPersonTypesQuery)
        }

        logger.info(
            "[{} {}] - Handling query {}...: -> Generated result {} !DONE!<-",
            PERSON.name,
            SERVICE.name,
            query.javaClass.name,
            result.javaClass.name
        )
        return result
    }

    private fun handle(query: FindAllPersonsQuery): QueryResult<Person> {// TODO Test
        queryStore.addQuery(query)
        logger.trace(
            "[{} {}] - Query added: -> {} <-",
            PERSON.name,
            SERVICE.name,
            query.javaClass.name
        )
        val pageResult = repository.findAllByPage(query.page)
        logger.trace(
            "[{} {}] - Entity found?: -> {} <-",
            PERSON.name,
            SERVICE.name,
            pageResult.toString() // TODO change the approach to log
        )
        return pageResult
    }

    private fun handle(query: FindTheDentistQuery): QueryResult<Person> {
        queryStore.addQuery(query)
        logger.trace(
            "[{} {}] - Query added: -> {} <-",
            PERSON.name,
            SERVICE.name,
            query.javaClass.name
        )
        val optionalEntity = repository.findOneByFilter(
            query.page.filter
//            EqFilter<String, String>(
//                "person_id", // TODO MAP FIELD
//                .Anyid
//            )
        )
        logger.trace(
            "[{} {}] - Entity found?: -> {} <-",
            PERSON.name,
            SERVICE.name,
            optionalEntity.toString()
        )
        return QueryResult.build(optionalEntity)
    }

//    private fun handle(query: FindAllPersonTypesQuery): QueryResult<PersonType> {// TODO Test
//        queryStore.addQuery(query)
//        logger.trace(
//            "[{} {}] - Query added: -> {} <-",
//            PERSON.name,
//            SERVICE.name,
//            query.javaClass.name
//        )
//        val entities = PersonType.values().toList()
//        logger.trace(
//            "[{} {}] - Entity found?: -> {} <-",
//            PERSON.name,
//            SERVICE.name,
//            entities.toString() // TODO change the approach to log
//        )
//        return QueryResult.build(entities)
//    }

    companion object: Loggable()
}
