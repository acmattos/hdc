package br.com.acmattos.hdc.user.application

import br.com.acmattos.hdc.common.context.application.CommandHandlerService
import br.com.acmattos.hdc.common.context.config.ContextLogEnum.SERVICE
import br.com.acmattos.hdc.common.context.domain.cqs.Command
import br.com.acmattos.hdc.common.context.domain.cqs.CreateCommand
import br.com.acmattos.hdc.common.context.domain.cqs.DeleteCommand
import br.com.acmattos.hdc.common.context.domain.cqs.DeleteEvent
import br.com.acmattos.hdc.common.context.domain.cqs.EntityEvent
import br.com.acmattos.hdc.common.context.domain.cqs.EventStore
import br.com.acmattos.hdc.common.context.domain.cqs.UpdateCommand
import br.com.acmattos.hdc.common.context.domain.cqs.UpsertCommand
import br.com.acmattos.hdc.common.context.domain.model.Repository
import br.com.acmattos.hdc.common.tool.assertion.Assertion
import br.com.acmattos.hdc.common.tool.loggable.Loggable
import br.com.acmattos.hdc.common.tool.oneof.OneOfTwo
import br.com.acmattos.hdc.common.tool.page.EqFilter
import br.com.acmattos.hdc.user.config.MessageTrackerIdEnum.ROLE_ALREADY_DEFINED
import br.com.acmattos.hdc.user.config.MessageTrackerIdEnum.ROLE_NOT_DEFINED
import br.com.acmattos.hdc.user.config.UserLogEnum.ROLE
import br.com.acmattos.hdc.user.domain.cqs.RoleCommand
import br.com.acmattos.hdc.user.domain.cqs.RoleCreateCommand
import br.com.acmattos.hdc.user.domain.cqs.RoleCreateEvent
import br.com.acmattos.hdc.user.domain.cqs.RoleDeleteCommand
import br.com.acmattos.hdc.user.domain.cqs.RoleDeleteEvent
import br.com.acmattos.hdc.user.domain.cqs.RoleEvent
import br.com.acmattos.hdc.user.domain.cqs.RoleUpdateCommand
import br.com.acmattos.hdc.user.domain.cqs.RoleUpdateEvent
import br.com.acmattos.hdc.user.domain.cqs.RoleUpsertCommand
import br.com.acmattos.hdc.user.domain.cqs.RoleUpsertEvent
import br.com.acmattos.hdc.user.domain.model.Role
import br.com.acmattos.hdc.user.port.persistence.mongodb.DocumentIndexedField.EVENT_CODE
import br.com.acmattos.hdc.user.port.persistence.mongodb.DocumentIndexedField.EVENT_ROLE_ID_ID
import br.com.acmattos.hdc.user.port.persistence.mongodb.DocumentIndexedField.ROLE_ID

/**
 * @author ACMattos
 * @since 04/12/2023.
 */
class RoleCommandHandlerService (
    private val eventStore: EventStore<RoleEvent>,
    repository: Repository<Role>
): CommandHandlerService<RoleEvent, Role>(eventStore, repository, ROLE.name) {

   override fun handle(command: CreateCommand): RoleEvent =
       RoleCreateEvent(
           command as RoleCreateCommand
       ).also { event ->
            logger.trace(
                "[{} {}] - Event created: -> {} <-",
                contextName,
                SERVICE.name,
                event.toString()
            )
            create(event, Role.apply(event))
        }

    override fun handle(command: UpsertCommand): RoleEvent =
        Role.apply(
            (command as RoleUpsertCommand).events
        ).let { oldEntity ->
            logEntity(oldEntity)
            upsert(
                RoleUpsertEvent(command),
                oldEntity
            )
        }

    override fun handle(command: UpdateCommand): RoleEvent =
        Role.apply(
             validateEntityExists(command)
         ).let { oldEntity ->
            logEntity(oldEntity)
            update(
                RoleUpdateEvent(command as RoleUpdateCommand),
                oldEntity,
                entityEqFilter(oldEntity.roleId.id)
            )
        }

    override fun handle(command: DeleteCommand): RoleEvent =
        Role.apply(
            validateEntityExists(command)
        ).let { oldEntity ->
            logEntity(oldEntity)
            delete(
                RoleDeleteEvent(command as RoleDeleteCommand/*, oldEntity.code*/),
                oldEntity,
                entityEqFilter(oldEntity.roleId.id)
            )
        }

    override fun validateEntityDoesNotExist(
        command: CreateCommand
    ): OneOfTwo<UpsertCommand, CreateCommand> =
        eventStore.findAllByFilter(
            eventEqFilter(command as RoleCreateCommand)
        ).let { events ->
            Assertion.assert(
                """
        There is a role already defined for the given code [${command.code}]!
                        """.trimIndent(),
                contextName,
                ROLE_ALREADY_DEFINED
            ) {
                events.isEmpty() || events.last() is DeleteEvent
            }
            oneOfTwo(command, events)
        }

    override fun validateEntityExists(command: Command): List<RoleEvent> =
        eventStore.findAllByFilter(
            eventEqFilter(command as RoleCommand)
        ).let { events ->
            Assertion.assert(
                """
    There is no role defined for the given id [${command.roleId.id}]!
                    """.trimIndent(),
                contextName,
                ROLE_NOT_DEFINED
            ) {
                events.isNotEmpty() && events.last() !is DeleteEvent
            }
            events
         }

    override fun oneOfTwo(command: CreateCommand, events: List<EntityEvent>) =
        if(events.isNotEmpty() && events.last() is DeleteEvent) {
            OneOfTwo
                .first<UpsertCommand, CreateCommand>(
                    RoleUpsertCommand(
                        command as RoleCreateCommand,
                        events as List<RoleEvent>
                )
            )
        } else {
            OneOfTwo.second(command)
        }

    private fun entityEqFilter(id: String) =
        eqFilter(ROLE_ID.fieldName, id)

    private fun eventEqFilter(command: Command) = when (command) {
        is RoleCreateCommand -> eqFilter(
            EVENT_CODE.fieldName,
            command.code
        )
        else ->  eqFilter(
            EVENT_ROLE_ID_ID.fieldName,
            (command as RoleCommand).roleId.id)
    }

    private fun eqFilter(fieldName: String, id: String) =
        EqFilter<String, String>(fieldName, id)

    companion object: Loggable()
}
