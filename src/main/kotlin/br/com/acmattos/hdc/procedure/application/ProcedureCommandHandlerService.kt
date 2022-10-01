package br.com.acmattos.hdc.procedure.application

import br.com.acmattos.hdc.common.context.config.ContextLogEnum.SERVICE
import br.com.acmattos.hdc.common.context.domain.cqs.Command
import br.com.acmattos.hdc.common.context.domain.cqs.CommandHandler
import br.com.acmattos.hdc.common.context.domain.cqs.EventStore
import br.com.acmattos.hdc.common.context.domain.model.Repository
import br.com.acmattos.hdc.common.tool.assertion.Assertion
import br.com.acmattos.hdc.common.tool.loggable.Loggable
import br.com.acmattos.hdc.common.tool.page.EqFilter
import br.com.acmattos.hdc.procedure.config.MessageTrackerCodeEnum.PROCEDURE_ALREADY_DEFINED
import br.com.acmattos.hdc.procedure.config.MessageTrackerCodeEnum.PROCEDURE_NOT_DEFINED
import br.com.acmattos.hdc.procedure.config.ProcedureLogEnum.PROCEDURE
import br.com.acmattos.hdc.procedure.domain.cqs.CreateDentalProcedureCommand
import br.com.acmattos.hdc.procedure.domain.cqs.CreateDentalProcedureEvent
import br.com.acmattos.hdc.procedure.domain.cqs.DeleteDentalProcedureCommand
import br.com.acmattos.hdc.procedure.domain.cqs.DeleteDentalProcedureEvent
import br.com.acmattos.hdc.procedure.domain.cqs.ProcedureCommand
import br.com.acmattos.hdc.procedure.domain.cqs.ProcedureEvent
import br.com.acmattos.hdc.procedure.domain.cqs.UpdateDentalProcedureCommand
import br.com.acmattos.hdc.procedure.domain.cqs.UpdateDentalProcedureEvent
import br.com.acmattos.hdc.procedure.domain.model.Procedure
import br.com.acmattos.hdc.procedure.port.persistence.mongodb.DocumentIndexedField.EVENT_CODE
import br.com.acmattos.hdc.procedure.port.persistence.mongodb.DocumentIndexedField.EVENT_PROCEDURE_ID_ID
import br.com.acmattos.hdc.procedure.port.persistence.mongodb.DocumentIndexedField.PROCEDURE_ID
import org.bson.conversions.Bson

/**
 * @author ACMattos
 * @since 19/03/2022.
 */
class ProcedureCommandHandlerService(
    private val eventStore: EventStore<ProcedureEvent>,
    private val repository: Repository<Procedure>
): CommandHandler<ProcedureEvent> {
    override fun handle(command: Command): ProcedureEvent {
        logger.debug(
            "[{} {}] - Handling command {}...: -> {} <-",
            PROCEDURE.name,
            SERVICE.name,
            command.javaClass.simpleName,
            command.toString()
        )

        val event = when(command){
            is CreateDentalProcedureCommand -> handle(command)
            is UpdateDentalProcedureCommand -> handle(command)
            else -> handle(command as DeleteDentalProcedureCommand)
        }

        logger.info(
            "[{} {}] - Handling command {}...: -> Generated event {} !DONE!<-",
            PROCEDURE.name,
            SERVICE.name,
            command.javaClass.simpleName,
            event.javaClass.simpleName
        )
        return event
    }

    private fun handle(
        command: CreateDentalProcedureCommand
    ): ProcedureEvent {
        validateProcedureDoesNotExist(command)
        logger.trace(
            "[{} {}] - Procedure does not exist...",
            PROCEDURE.name,
            SERVICE.name
        )
        return command.let {
            val event = CreateDentalProcedureEvent(it).also { event ->
                logger.trace(
                    "[{} {}] - Event created: -> {} <-",
                    PROCEDURE.name,
                    SERVICE.name,
                    event.toString()
                )

                Procedure.apply(event).also { entity ->
                    logger.trace(
                        "[{} {}] - Entity created: -> {} <-",
                        PROCEDURE.name,
                        SERVICE.name,
                        entity.toString()
                    )

                    addEvent(event)

                    repository.save(entity)
                    logger.trace(
                        "[{} {}] - Entity saved: -> {} <-",
                        PROCEDURE.name,
                        SERVICE.name,
                        entity.javaClass.simpleName
                    )
                }
            }
            event
        }
    }

    private fun handle(
        command: UpdateDentalProcedureCommand
    ): ProcedureEvent {
        val procedureEvents = validateProcedureExists(command)
        logger.trace(
            "[{} {}] - Procedure exists...",
            PROCEDURE.name,
            SERVICE.name
        )

        return Procedure.apply(procedureEvents).let { oldEntity ->
            logger.trace(
                "[{} {}] - Procedure re-created: -> {} <-",
                PROCEDURE.name,
                SERVICE.name,
                oldEntity.toString()
            )

            UpdateDentalProcedureEvent(command).also { event ->
                logger.trace(
                    "[{} {}] - Event created: -> {} <-",
                    PROCEDURE.name,
                    SERVICE.name,
                    event.toString()
                )

                val entity = oldEntity.apply(event)
                logger.trace(
                    "[{} {}] - Entity updated by event: -> {} <-",
                    PROCEDURE.name,
                    SERVICE.name,
                    entity.toString()
                )

                addEvent(event)

                repository.update(
                    EqFilter<Bson, String>(
                        PROCEDURE_ID.fieldName,
                        entity.procedureId.id
                    ),
                    entity
                )
                logger.trace(
                    "[{} {}] - Entity updated: -> {} <-",
                    PROCEDURE.name,
                    SERVICE.name,
                    entity.javaClass.simpleName
                )
                event
            }
        }
    }

    private fun handle(
        command: DeleteDentalProcedureCommand
    ): ProcedureEvent {
        val procedureEvents = validateProcedureExists(command)
        logger.trace(
            "[{} {}] - Procedure exists...",
            PROCEDURE.name,
            SERVICE.name
        )

        return Procedure.apply(procedureEvents).let { entity ->
            logger.trace(
                "[{} {}] - Procedure re-created: -> {} <-",
                PROCEDURE.name,
                SERVICE.name,
                entity.toString()
            )

            DeleteDentalProcedureEvent(command).let { event ->
                logger.trace(
                    "[{} {}] - Event created: -> {} <-",
                    PROCEDURE.name,
                    SERVICE.name,
                    event.toString()
                )

                addEvent(event)

                repository.delete(
                    EqFilter<Bson, String>(
                        PROCEDURE_ID.fieldName,
                        entity.procedureId.id
                    )
                )
                logger.trace(
                    "[{} {}] - Entity deleted: -> {} <-",
                    PROCEDURE.name,
                    SERVICE.name,
                    entity.javaClass.simpleName
                )
                event
            }
        }
    }

    private fun addEvent(event: ProcedureEvent) {
        eventStore.addEvent(event)
        logger.trace(
            "[{} {}] - Event added: -> {} <-",
            PROCEDURE.name,
            SERVICE.name,
            event.javaClass.simpleName
        )
    }

    private fun validateProcedureDoesNotExist(command: CreateDentalProcedureCommand) {
        eventStore.findAllByFilter(
            getFilter(command)
        ).also {
            Assertion.assert(
                """
    There is a dental procedure already defined for the given code [${command.code}]!
                    """.trimIndent(),
                PROCEDURE.name,
                PROCEDURE_ALREADY_DEFINED
            ) {
                it.isEmpty()
            }
        }
    }

    private fun validateProcedureExists(command: ProcedureCommand) =
        eventStore.findAllByFilter(
            getFilter(command)
        ).let {
            Assertion.assert(
                """
    There is no dental procedure defined for the given id [${command.procedureId.id}]!
                    """.trimIndent(),
                PROCEDURE.name,
                PROCEDURE_NOT_DEFINED
            ) {
                it.isNotEmpty()
            }
            it
        }

    private fun getFilter(command: ProcedureCommand) = when(command) {
        is CreateDentalProcedureCommand -> EqFilter<String, Int>(
            EVENT_CODE. fieldName,
            command.code
        )
        is UpdateDentalProcedureCommand -> EqFilter<String, Int>(
            EVENT_CODE. fieldName,
            command.code
        )
        else -> EqFilter<String, String>(
            EVENT_PROCEDURE_ID_ID.fieldName,
            command.procedureId.id
        )
    }

    companion object: Loggable()
}
