package br.com.acmattos.hdc.common.context.application

import br.com.acmattos.hdc.common.context.config.ContextLogEnum.SERVICE
import br.com.acmattos.hdc.common.context.domain.cqs.Command
import br.com.acmattos.hdc.common.context.domain.cqs.CommandHandler
import br.com.acmattos.hdc.common.context.domain.cqs.CreateCommand
import br.com.acmattos.hdc.common.context.domain.cqs.DeleteCommand
import br.com.acmattos.hdc.common.context.domain.cqs.EntityEvent
import br.com.acmattos.hdc.common.context.domain.cqs.EventStore
import br.com.acmattos.hdc.common.context.domain.cqs.UpdateCommand
import br.com.acmattos.hdc.common.context.domain.cqs.UpsertCommand
import br.com.acmattos.hdc.common.context.domain.model.AppliableEntity
import br.com.acmattos.hdc.common.context.domain.model.Repository
import br.com.acmattos.hdc.common.tool.loggable.Loggable
import br.com.acmattos.hdc.common.tool.oneof.OneOfThree
import br.com.acmattos.hdc.common.tool.oneof.OneOfTwo
import br.com.acmattos.hdc.common.tool.page.Filter

/**
 * @author ACMattos
 * @since 07/01/2023.
 */
abstract class CommandHandlerService<E: EntityEvent, T: AppliableEntity>(
    private val eventStore: EventStore<E>,
    private val repository: Repository<T>,
    protected val contextName: String,
): CommandHandler<E> {
    override fun handle(command: Command): E {
        logger.debug(
            "[{} {}] - Handling command {}...: -> {} <-",
            contextName,
            SERVICE.name,
            command.javaClass.simpleName,
            command.toString()
        )
        return oneOfThree(command)
            .theThird(::handleOneOfTwo)
            .orTheSecond(::handle)
            .orTheFirst(::handle).let { event ->
                logger.info(
                    "[{} {}] - Handling command {}...: -> Generated event {} !DONE!<-",
                    contextName,
                    SERVICE.name,
                    command.javaClass.simpleName,
                    event.javaClass.simpleName
                )
                event
            }
    }

    private fun oneOfThree(command: Command) = when(command){
        is CreateCommand -> OneOfThree
            .first<CreateCommand, UpdateCommand, DeleteCommand>(command)
        is UpdateCommand -> OneOfThree.second(command)
        else -> OneOfThree.third(command as DeleteCommand)
    }

    private fun handleOneOfTwo(command: CreateCommand): E =
        validateEntityDoesNotExist(command)
            .theSecond(::handle)
            .orTheFirst(::handle)

    protected abstract fun handle(command: CreateCommand): E
    protected abstract fun handle(command: UpsertCommand): E
    protected abstract fun handle(command: UpdateCommand): E
    protected abstract fun handle(command: DeleteCommand): E

    protected abstract fun validateEntityDoesNotExist(
        command: CreateCommand
    ): OneOfTwo<UpsertCommand, CreateCommand>

    protected abstract fun validateEntityExists(
        command: Command
    ): List<EntityEvent>

    protected abstract fun oneOfTwo(
        command: CreateCommand,
        events: List<EntityEvent>
    ):OneOfTwo<UpsertCommand, CreateCommand>

    protected fun logEntity(oldEntity: T) {
        logger.trace(
            "[{} {}] - Entity re-created: -> {} <-",
            contextName,
            SERVICE.name,
            oldEntity.toString()
        )
    }

    fun create(event: E, entity: T) {
        logger.trace(
            "[{} {}] - Entity created: -> {} <-",
            contextName,
            SERVICE.name,
            entity.toString()
        )
        addEventToStore(event)
        saveEntityInRepository(entity)
    }

    fun upsert(event: E, entity: T) =
        applyEventToEntity(event, entity).let {
            addEventToStore(event)
            saveEntityInRepository(entity)
            event
        }

    fun update(event: E, entity: T, filter: Filter<*>) =
        applyEventToEntity(event, entity).let { updated ->
            addEventToStore(event)
            updateEntityInRepository(filter, updated)
            event
        }

    fun delete(event: E, entity: T, filter: Filter<*>) =
        applyEventToEntity(event, entity).let { deleted ->
            addEventToStore(event)
            deleteEntityInRepository(filter, deleted)
            event
        }

    private fun addEventToStore(event: E) {
        eventStore.addEvent(event)
        logger.trace(
            "[{} {}] - Event added: -> {} <-",
            contextName,
            SERVICE.name,
            event.javaClass.simpleName
        )
    }

    private fun saveEntityInRepository(entity: T) {
        repository.save(entity)
        logger.trace(
            "[{} {}] - Entity saved: -> {} <-",
            contextName,
            SERVICE.name,
            entity.javaClass.simpleName
        )
    }

    private fun applyEventToEntity(event: E, old: T): T {
        logger.trace(
            "[{} {}] - Event created: -> {} <-",
            contextName,
            SERVICE.name,
            event.toString()
        )
        return old.apply(event).let { updated ->
            logger.trace(
                "[{} {}] - Entity updated by event: -> {} <-",
                contextName,
                SERVICE.name,
                updated.toString()
            )
            updated as T
        }
    }

    private fun updateEntityInRepository(filter: Filter<*>, entity: T) {
        repository.update(filter, entity)
        logger.trace(
            "[{} {}] - Entity updated: -> {} <-",
            contextName,
            SERVICE.name,
            entity.javaClass.simpleName
        )
    }

    private fun deleteEntityInRepository(filter: Filter<*>, entity: T) {
        repository.delete(filter)
        logger.trace(
            "[{} {}] - Entity deleted: -> {} <-",
            contextName,
            SERVICE.name,
            entity.javaClass.simpleName
        )
    }

    companion object: Loggable()
}
