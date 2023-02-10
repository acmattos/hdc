package br.com.acmattos.hdc.procedure.application

import br.com.acmattos.hdc.common.context.application.CommandHandlerService
import br.com.acmattos.hdc.common.context.config.ContextLogEnum.SERVICE
import br.com.acmattos.hdc.common.context.domain.cqs.Command
import br.com.acmattos.hdc.common.context.domain.cqs.CreateCommand
import br.com.acmattos.hdc.common.context.domain.cqs.DeleteCommand
import br.com.acmattos.hdc.common.context.domain.cqs.DeleteEvent
import br.com.acmattos.hdc.common.context.domain.cqs.EntityEvent
import br.com.acmattos.hdc.common.context.domain.cqs.EventStore
import br.com.acmattos.hdc.common.context.domain.cqs.Message
import br.com.acmattos.hdc.common.context.domain.cqs.UpdateCommand
import br.com.acmattos.hdc.common.context.domain.cqs.UpsertCommand
import br.com.acmattos.hdc.common.context.domain.model.Repository
import br.com.acmattos.hdc.common.tool.assertion.Assertion
import br.com.acmattos.hdc.common.tool.loggable.Loggable
import br.com.acmattos.hdc.common.tool.oneof.OneOfTwo
import br.com.acmattos.hdc.common.tool.page.EqFilter
import br.com.acmattos.hdc.procedure.config.MessageTrackerIdEnum.PROCEDURE_ALREADY_DEFINED
import br.com.acmattos.hdc.procedure.config.MessageTrackerIdEnum.PROCEDURE_NOT_DEFINED
import br.com.acmattos.hdc.procedure.config.ProcedureLogEnum.PROCEDURE
import br.com.acmattos.hdc.procedure.domain.cqs.ProcedureCommand
import br.com.acmattos.hdc.procedure.domain.cqs.ProcedureCreateCommand
import br.com.acmattos.hdc.procedure.domain.cqs.ProcedureCreateEvent
import br.com.acmattos.hdc.procedure.domain.cqs.ProcedureDeleteCommand
import br.com.acmattos.hdc.procedure.domain.cqs.ProcedureDeleteEvent
import br.com.acmattos.hdc.procedure.domain.cqs.ProcedureEvent
import br.com.acmattos.hdc.procedure.domain.cqs.ProcedureUpdateCommand
import br.com.acmattos.hdc.procedure.domain.cqs.ProcedureUpdateEvent
import br.com.acmattos.hdc.procedure.domain.cqs.ProcedureUpsertCommand
import br.com.acmattos.hdc.procedure.domain.cqs.ProcedureUpsertEvent
import br.com.acmattos.hdc.procedure.domain.model.Procedure
import br.com.acmattos.hdc.procedure.port.persistence.mongodb.DocumentIndexedField.EVENT_CODE
import br.com.acmattos.hdc.procedure.port.persistence.mongodb.DocumentIndexedField.EVENT_PROCEDURE_ID_ID
import br.com.acmattos.hdc.procedure.port.persistence.mongodb.DocumentIndexedField.PROCEDURE_ID

/**
 * @author ACMattos
 * @since 19/03/2022.
 */
class ProcedureCommandHandlerService(
    private val eventStore: EventStore<ProcedureEvent>,
    repository: Repository<Procedure>
): CommandHandlerService<ProcedureEvent,Procedure>(eventStore, repository, PROCEDURE.name) {

    override fun handle(command: CreateCommand): ProcedureEvent =
        ProcedureCreateEvent(
            command as ProcedureCreateCommand
        ).also { event ->
            logger.trace(
                "[{} {}] - Event created: -> {} <-",
                contextName,
                SERVICE.name,
                event.toString()
            )
            create(event, Procedure.apply(event))
        }

    override fun handle(command: UpsertCommand): ProcedureEvent =
        Procedure.apply(
            (command as ProcedureUpsertCommand).events
        ).let { oldEntity ->
            logEntity(oldEntity)
            upsert(ProcedureUpsertEvent(command), oldEntity)
        }

    override fun handle(command: UpdateCommand): ProcedureEvent =
        Procedure.apply(
            validateEntityExists(command)
        ).let { oldEntity ->
            logEntity(oldEntity)
            update(
                ProcedureUpdateEvent(command as ProcedureUpdateCommand),
                oldEntity,
                entityIdEqFilter(oldEntity.procedureId.id)
            )
        }

    override fun handle(command: DeleteCommand): ProcedureEvent =
        Procedure.apply(
            validateEntityExists(command)
        ).let { oldEntity ->
            logEntity(oldEntity)
            delete(
                ProcedureDeleteEvent(command as ProcedureDeleteCommand),
                oldEntity,
                entityIdEqFilter(oldEntity.procedureId.id)
            )
        }

    private fun logEntity(oldEntity: Procedure) {
        logger.trace(
            "[{} {}] - Entity re-created: -> {} <-",
            PROCEDURE.name,
            SERVICE.name,
            oldEntity.toString()
        )
    }

    override fun validateEntityDoesNotExist(
        command: CreateCommand
    ): OneOfTwo<UpsertCommand, CreateCommand> =
        eventStore.findAllByFilter(
            codeEqFilter((command as ProcedureCreateCommand).code)
        ).let { events ->
            Assertion.assert(
                """
    There is a procedure already defined for the given code [${command.code}]!
                    """.trimIndent(),
                contextName,
                PROCEDURE_ALREADY_DEFINED
            ) {
                events.isEmpty() || events.last() is DeleteEvent
            }
            oneOfTwo(command, events)
        }

    override fun validateEntityExists(command: Command): List<ProcedureEvent> =
        eventStore.findAllByFilter(
            eqFilter(command)
        ).let { events ->
            Assertion.assert(
                """
    There is no procedure defined for the given id [${(command as ProcedureCommand).procedureId.id}]!
                    """.trimIndent(),
                contextName,
                PROCEDURE_NOT_DEFINED
            ) {
                events.isNotEmpty() && events.last() !is DeleteEvent
            }
            events
        }

    override fun oneOfTwo(command: CreateCommand, events: List<EntityEvent>) =
        if(events.isNotEmpty() && events.last() is DeleteEvent) {
            OneOfTwo
                .first<UpsertCommand, CreateCommand>(
                    ProcedureUpsertCommand(
                        command as ProcedureCreateCommand,
                        events as List<ProcedureEvent>
                    )
                )
        } else {
            OneOfTwo.second(command)
        }

    private fun eqFilter(message: Message) = when(message) {
        is ProcedureUpdateCommand -> codeEqFilter(message.code)
        else  -> idEqFilter((message as ProcedureDeleteCommand).procedureId.id)
    }

    private fun entityIdEqFilter(id: String) =
        eqFilter(PROCEDURE_ID.fieldName, id)

    private fun idEqFilter(id: String) =
        eqFilter(EVENT_PROCEDURE_ID_ID.fieldName, id)

    private fun codeEqFilter(code: Int) =
        eqFilter(EVENT_CODE.fieldName, code)

    private fun eqFilter(fieldName: String, id: String) =
        EqFilter<String, String>(fieldName, id)

    private fun eqFilter(fieldName: String, code: Int) =
        EqFilter<String, Int>(fieldName, code)

    companion object: Loggable()
}
