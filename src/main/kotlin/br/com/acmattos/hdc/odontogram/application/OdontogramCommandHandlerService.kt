package br.com.acmattos.hdc.odontogram.application

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
import br.com.acmattos.hdc.odontogram.config.MessageTrackerIdEnum.ODONTOGRAM_ALREADY_DEFINED
import br.com.acmattos.hdc.odontogram.config.MessageTrackerIdEnum.ODONTOGRAM_NOT_DEFINED
import br.com.acmattos.hdc.odontogram.config.OdontogramLogEnum.ODONTOGRAM
import br.com.acmattos.hdc.odontogram.domain.cqs.OdontogramCommand
import br.com.acmattos.hdc.odontogram.domain.cqs.OdontogramCreateCommand
import br.com.acmattos.hdc.odontogram.domain.cqs.OdontogramCreateEvent
import br.com.acmattos.hdc.odontogram.domain.cqs.OdontogramDeleteCommand
import br.com.acmattos.hdc.odontogram.domain.cqs.OdontogramDeleteEvent
import br.com.acmattos.hdc.odontogram.domain.cqs.OdontogramEvent
import br.com.acmattos.hdc.odontogram.domain.cqs.OdontogramUpdateCommand
import br.com.acmattos.hdc.odontogram.domain.cqs.OdontogramUpdateEvent
import br.com.acmattos.hdc.odontogram.domain.cqs.OdontogramUpsertCommand
import br.com.acmattos.hdc.odontogram.domain.cqs.OdontogramUpsertEvent
import br.com.acmattos.hdc.odontogram.domain.model.Odontogram
import br.com.acmattos.hdc.odontogram.port.persistence.mongodb.DocumentIndexedField.EVENT_ODONTOGRAM_ID_ID
import br.com.acmattos.hdc.odontogram.port.persistence.mongodb.DocumentIndexedField.ODONTOGRAM_ID

/**
 * @author ACMattos
 * @since 18/10/2022.
 */
class OdontogramCommandHandlerService (
    private val eventStore: EventStore<OdontogramEvent>,
    val repository: Repository<Odontogram>
): CommandHandlerService<OdontogramEvent, Odontogram>(eventStore, repository, ODONTOGRAM.name) {

   override fun handle(command: CreateCommand): OdontogramEvent =
       OdontogramCreateEvent(
           command as OdontogramCreateCommand
       ).also { event ->
            logger.trace(
                "[{} {}] - Event created: -> {} <-",
                contextName,
                SERVICE.name,
                event.toString()
            )
            create(event, Odontogram.apply(event))
        }

    override fun handle(command: UpsertCommand): OdontogramEvent =
        Odontogram.apply(
            (command as OdontogramUpsertCommand).events
        ).let { oldEntity ->
            logEntity(oldEntity)
            upsert(OdontogramUpsertEvent(command), oldEntity)
        }

    override fun handle(command: UpdateCommand): OdontogramEvent =
        Odontogram.apply(
             validateEntityExists(command)
         ).let { oldEntity ->
            logEntity(oldEntity)
            update(
                OdontogramUpdateEvent(command as OdontogramUpdateCommand),
                oldEntity,
                entityEqFilter(oldEntity.odontogramId.id)
            )
        }

    override fun handle(command: DeleteCommand): OdontogramEvent =
        Odontogram.apply(
            validateEntityExists(command)
        ).let { oldEntity ->
            logEntity(oldEntity)
            delete(
                OdontogramDeleteEvent(command as OdontogramDeleteCommand),
                oldEntity,
                entityEqFilter(oldEntity.odontogramId.id)
            )
        }

    override fun validateEntityDoesNotExist(
        command: CreateCommand
    ): OneOfTwo<UpsertCommand, CreateCommand> =
        eventStore.findAllByFilter(
            eventEqFilter(
                (command as OdontogramCreateCommand).odontogramId.id
            )
        ).let { events ->
            Assertion.assert(
                """
        There is an odontogram already defined for the given id [${command.odontogramId.id}]!
                        """.trimIndent(),
                contextName,
                ODONTOGRAM_ALREADY_DEFINED
            ) {
                events.isEmpty() || events.last() is DeleteEvent
            }
                oneOfTwo(command, events)
        }

    override fun validateEntityExists(command: Command): List<OdontogramEvent> =
        eventStore.findAllByFilter(
            eventEqFilter(
                (command as OdontogramCommand).odontogramId.id
            )
        ).let { events ->
            Assertion.assert(
                """
    There is no odontogram defined for the given id [${command.odontogramId.id}]!
                    """.trimIndent(),
                contextName,
                ODONTOGRAM_NOT_DEFINED
            ) {
                events.isNotEmpty() && events.last() !is DeleteEvent
            }
            events
        }

    override fun oneOfTwo(command: CreateCommand, events: List<EntityEvent>) =
        if(events.isNotEmpty() && events.last() is DeleteEvent) {
            OneOfTwo
                .first<UpsertCommand, CreateCommand>(
                    OdontogramUpsertCommand(
                        command as OdontogramCreateCommand,
                        events as List<OdontogramEvent>
                )
            )
        } else {
            OneOfTwo.second(command)
    }

    private fun entityEqFilter(id: String) =
        eqFilter(ODONTOGRAM_ID.fieldName, id)

    private fun eventEqFilter(id: String) =
        eqFilter(EVENT_ODONTOGRAM_ID_ID.fieldName, id)

    private fun eqFilter(fieldName: String, id: String) =
        EqFilter<String, String>(fieldName, id)

    companion object: Loggable()
}
