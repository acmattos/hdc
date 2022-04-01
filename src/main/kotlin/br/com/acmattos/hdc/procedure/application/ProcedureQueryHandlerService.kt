package br.com.acmattos.hdc.procedure.application

import br.com.acmattos.hdc.common.context.config.ContextLogEnum.SERVICE
import br.com.acmattos.hdc.common.context.domain.cqs.Query
import br.com.acmattos.hdc.common.context.domain.cqs.QueryHandler
import br.com.acmattos.hdc.common.context.domain.cqs.QueryResult
import br.com.acmattos.hdc.common.context.domain.cqs.QueryStore
import br.com.acmattos.hdc.common.context.domain.model.Repository
import br.com.acmattos.hdc.common.tool.loggable.Loggable
import br.com.acmattos.hdc.procedure.config.ProcedureLogEnum.PROCEDURE
import br.com.acmattos.hdc.procedure.domain.cqs.FindAllProceduresQuery
import br.com.acmattos.hdc.procedure.domain.cqs.FindTheProcedureQuery
import br.com.acmattos.hdc.procedure.domain.cqs.ProcedureQuery
import br.com.acmattos.hdc.procedure.domain.model.Procedure

/**
 * @author ACMattos
 * @since 20/03/2023.
 */
class ProcedureQueryHandlerService(
    private val queryStore: QueryStore<ProcedureQuery>,
    private val repository: Repository<Procedure>
): QueryHandler<Procedure> {
    override fun handle(query: Query): QueryResult<Procedure> {
        logger.debug(
            "[{} {}] - Handling query {}...: -> {} <-",
            PROCEDURE.name,
            SERVICE.name,
            query.javaClass.name,
            query.toString()
        )

        val result = when(query){
            is FindAllProceduresQuery -> handle(query)
            else -> handle(query as FindTheProcedureQuery)
        }

        logger.info(
            "[{} {}] - Handling query {}...: -> Generated result {} !DONE!<-",
            PROCEDURE.name,
            SERVICE.name,
            query.javaClass.name,
            result.javaClass.name
        )
        return result
    }

    private fun handle(query: FindAllProceduresQuery): QueryResult<Procedure> {
        queryStore.addQuery(query)
        logger.trace(
            "[{} {}] - Query added: -> {} <-",
            PROCEDURE.name,
            SERVICE.name,
            query.javaClass.name
        )
        val entities = repository.findAll()
        logger.trace(
            "[{} {}] - Entity found?: -> {} <-",
            PROCEDURE.name,
            SERVICE.name,
            entities.toString() // TODO change the approach to log
        )
        return QueryResult.build(entities)
    }

    private fun handle(query: FindTheProcedureQuery): QueryResult<Procedure> {
        queryStore.addQuery(query)
        logger.trace(
            "[{} {}] - Query added: -> {} <-",
            PROCEDURE.name,
            SERVICE.name,
            query.javaClass.name
        )
        val optionalEntity = repository.findOneByFilter(query.filter)

        logger.trace(
            "[{} {}] - Entity found?: -> {} <-",
            PROCEDURE.name,
            SERVICE.name,
            optionalEntity.toString()
        )
        return QueryResult.build(optionalEntity)
    }

    companion object: Loggable()
}
