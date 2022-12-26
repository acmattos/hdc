package br.com.acmattos.hdc.odontogram.config

import br.com.acmattos.hdc.common.context.config.ContextLogEnum.SERVICE
import br.com.acmattos.hdc.common.context.domain.cqs.Query
import br.com.acmattos.hdc.common.context.domain.cqs.QueryHandler
import br.com.acmattos.hdc.common.context.domain.cqs.QueryResult
import br.com.acmattos.hdc.common.context.domain.cqs.QueryStore
import br.com.acmattos.hdc.common.tool.loggable.Loggable
import br.com.acmattos.hdc.odontogram.config.OdontogramLogEnum.ODONTOGRAM
import br.com.acmattos.hdc.odontogram.domain.cqs.GetAnOdontogramQuery
import br.com.acmattos.hdc.odontogram.domain.cqs.OdontogramQuery
import br.com.acmattos.hdc.odontogram.domain.model.Odontogram

/**
 * @author ACMattos
 * @since 18/10/2022.
 */
class OdontogramQueryHandlerService(
    private val queryStore: QueryStore<OdontogramQuery>,
): QueryHandler<Odontogram> {
    override fun handle(query: Query): QueryResult<Odontogram> {
        logger.debug(
            "[{} {}] - Handling query {}...: -> {} <-",
            ODONTOGRAM.name,
            SERVICE.name,
            query.javaClass.name,
            query.toString()
        )

        val result = when(query){
            is GetAnOdontogramQuery -> handle(query)
            else -> handle(query as GetAnOdontogramQuery)
        }

        logger.info(
            "[{} {}] - Handling query {}...: -> Generated result {} !DONE!<-",
            ODONTOGRAM.name,
            SERVICE.name,
            query.javaClass.name,
            result.javaClass.name
        )
        return result
    }

    private fun handle(query: GetAnOdontogramQuery): QueryResult<Odontogram> {
        queryStore.addQuery(query)
        logger.trace(
            "[{} {}] - Query added: -> {} <-",
            ODONTOGRAM.name,
            SERVICE.name,
            query.javaClass.name
        )
        val entity = Odontogram.create()

        logger.trace(
            "[{} {}] - Entity created: -> {} <-",
            ODONTOGRAM.name,
            SERVICE.name,
            entity.toString()
        )
        return QueryResult.build(listOf(entity))
    }

    companion object: Loggable()
}
