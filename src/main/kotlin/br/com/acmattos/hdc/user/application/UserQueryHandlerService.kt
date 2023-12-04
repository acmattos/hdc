package br.com.acmattos.hdc.user.application

import br.com.acmattos.hdc.common.context.config.ContextLogEnum.SERVICE
import br.com.acmattos.hdc.common.context.domain.cqs.Query
import br.com.acmattos.hdc.common.context.domain.cqs.QueryHandler
import br.com.acmattos.hdc.common.context.domain.cqs.QueryResult
import br.com.acmattos.hdc.common.context.domain.cqs.QueryStore
import br.com.acmattos.hdc.common.context.domain.model.Repository
import br.com.acmattos.hdc.common.tool.loggable.Loggable
import br.com.acmattos.hdc.user.config.UserLogEnum.USER
import br.com.acmattos.hdc.user.domain.cqs.FindAllUsersQuery
import br.com.acmattos.hdc.user.domain.cqs.FindTheUserQuery
import br.com.acmattos.hdc.user.domain.cqs.UserQuery
import br.com.acmattos.hdc.user.domain.model.User

/**
 * @author ACMattos
 * @since 24/11/2023.
 */
class UserQueryHandlerService(
    private val queryStore: QueryStore<UserQuery>,
    private val repository: Repository<User>
): QueryHandler<User> {
    override fun handle(query: Query): QueryResult<User> {
        logger.debug(
            "[{} {}] - Handling query {}...: -> {} <-",
            USER.name,
            SERVICE.name,
            query.javaClass.name,
            query.toString()
        )

        val result = when(query){
            is FindAllUsersQuery -> handle(query)
            else -> handle(query as FindTheUserQuery)
        }

        logger.info(
            "[{} {}] - Handling query {}...: -> Generated result {} !DONE!<-",
            USER.name,
            SERVICE.name,
            query.javaClass.name,
            result.javaClass.name
        )
        return result
    }

    private fun handle(query: FindAllUsersQuery): QueryResult<User> {// TODO Test
        queryStore.addQuery(query)
        logger.trace(
            "[{} {}] - Query added: -> {} <-",
            USER.name,
            SERVICE.name,
            query.javaClass.name
        )
        val pageResult = repository.findAllByPage(query.page)
        logger.trace(
            "[{} {}] - Entity found?: -> {} <-",
            USER.name,
            SERVICE.name,
            pageResult.toString() // TODO change the approach to log
        )
        return pageResult
    }

    private fun handle(query: FindTheUserQuery): QueryResult<User> {
        queryStore.addQuery(query)
        logger.trace(
            "[{} {}] - Query added: -> {} <-",
            USER.name,
            SERVICE.name,
            query.javaClass.name
        )
        val optionalEntity = repository.findOneByFilter(query.page.filter)
        logger.trace(
            "[{} {}] - Entity found?: -> {} <-",
            USER.name,
            SERVICE.name,
            optionalEntity.toString()
        )
        return QueryResult.build(optionalEntity)
    }

    companion object: Loggable()
}
