package br.com.acmattos.hdc.person.application

import br.com.acmattos.hdc.common.context.config.ContextLogEnum.SERVICE
import br.com.acmattos.hdc.common.context.domain.cqs.Command
import br.com.acmattos.hdc.common.context.domain.cqs.CommandHandler
import br.com.acmattos.hdc.common.context.domain.cqs.EventStore
import br.com.acmattos.hdc.common.context.domain.model.Repository
import br.com.acmattos.hdc.common.tool.assertion.Assertion
import br.com.acmattos.hdc.common.tool.loggable.Loggable
import br.com.acmattos.hdc.common.tool.page.EqFilter
import br.com.acmattos.hdc.person.config.MessageTrackerIdEnum.PERSON_ALREADY_EXISTS
import br.com.acmattos.hdc.person.config.MessageTrackerIdEnum.PERSON_NOT_DEFINED
import br.com.acmattos.hdc.person.config.PersonLogEnum.PERSON
import br.com.acmattos.hdc.person.domain.cqs.CreateDentistCommand
import br.com.acmattos.hdc.person.domain.cqs.CreateDentistEvent
import br.com.acmattos.hdc.person.domain.cqs.CreatePatientCommand
import br.com.acmattos.hdc.person.domain.cqs.CreatePatientEvent
import br.com.acmattos.hdc.person.domain.cqs.PersonCommand
import br.com.acmattos.hdc.person.domain.cqs.PersonEvent
import br.com.acmattos.hdc.person.domain.cqs.UpdatePatientCommand
import br.com.acmattos.hdc.person.domain.cqs.UpdatePatientEvent
import br.com.acmattos.hdc.person.domain.model.Person
import br.com.acmattos.hdc.person.port.persistence.mongodb.DocumentIndexedField.EVENT_PERSON_ID_ID
import br.com.acmattos.hdc.person.port.persistence.mongodb.DocumentIndexedField.FULL_NAME
import br.com.acmattos.hdc.person.port.persistence.mongodb.DocumentIndexedField.PERSON_ID
import org.bson.conversions.Bson

/**
 * @author ACMattos
 * @since 30/06/2019.
 */
class PersonCommandHandlerService(
    private val eventStore: EventStore<PersonEvent>,
    private val repository: Repository<Person>,
): CommandHandler<PersonEvent> {
    override fun handle(command: Command): PersonEvent {
        logger.debug(
            "[{} {}] - Handling command {}...: -> {} <-",
            PERSON.name,
            SERVICE.name,
            command.javaClass.name,
            command.toString()
        )

        val event = when(command){
            is CreateDentistCommand -> handle(command)
            is CreatePatientCommand -> handle(command)
            else -> handle(command as UpdatePatientCommand)
        }

        logger.info(
            "[{} {}] - Handling command {}...: -> Generated event {} !DONE!<-",
            PERSON.name,
            SERVICE.name,
            command.javaClass.name,
            event.javaClass.name
        )
        return event
    }

    private fun handle(command: CreateDentistCommand): PersonEvent {
        validatePersonDoesNotExist(command)
        logger.trace(
            "[{} {}] - Dentist does not exist...",
            PERSON.name,
            SERVICE.name
        )

        return CreateDentistEvent(command).also { event ->
            logger.trace(
                "[{} {}] - Event created: -> {} <-",
                PERSON.name,
                SERVICE.name,
                event.toString()
            )

            Person.apply(event).also { entity ->
                logger.trace(
                    "[{} {}] - Entity created: -> {} <-",
                    PERSON.name,
                    SERVICE.name,
                    entity.toString()
                )

                eventStore.addEvent(event)
                logger.trace(
                    "[{} {}] - Event added: -> {} <-",
                    PERSON.name,
                    SERVICE.name,
                    event.javaClass.name
                )

                repository.save(entity)
                logger.trace(
                    "[{} {}] - Entity saved: -> {} <-",
                    PERSON.name,
                    SERVICE.name,
                    entity.javaClass.name
                )
            }
            event
        }
    }

    private fun handle(command: CreatePatientCommand): PersonEvent {
        validatePersonDoesNotExist(command)
        logger.trace(
            "[{} {}] - Patient does not exist...",
            PERSON.name,
            SERVICE.name
        )

        return CreatePatientEvent(command).also { event ->
            logger.trace(
                "[{} {}] - Event created: -> {} <-",
                PERSON.name,
                SERVICE.name,
                event.toString()
            )

            Person.apply(event).also { entity ->
                logger.trace(
                    "[{} {}] - Entity created: -> {} <-",
                    PERSON.name,
                    SERVICE.name,
                    entity.toString()
                )

                eventStore.addEvent(event)
                logger.trace(
                    "[{} {}] - Event added: -> {} <-",
                    PERSON.name,
                    SERVICE.name,
                    event.javaClass.name
                )

                repository.save(entity)
                logger.trace(
                    "[{} {}] - Entity saved: -> {} <-",
                    PERSON.name,
                    SERVICE.name,
                    entity.javaClass.name
                )
            }
            event
        }
    }

    private fun handle(command: UpdatePatientCommand): PersonEvent {
        val patientEvents = validatePersonExists(command)
        logger.trace(
            "[{} {}] - Patient exists...",
            PERSON.name,
            SERVICE.name
        )

        return Person.apply(patientEvents).let { oldEntity ->
            logger.trace(
                "[{} {}] - Patient re-created: -> {} <-",
                PERSON.name,
                SERVICE.name,
                oldEntity.toString()
            )

            UpdatePatientEvent(command).also { event ->
                logger.trace(
                    "[{} {}] - Event created: -> {} <-",
                    PERSON.name,
                    SERVICE.name,
                    event.toString()
                )

                val entity = oldEntity.apply(event)
                logger.trace(
                    "[{} {}] - Entity updated by event: -> {} <-",
                    PERSON.name,
                    SERVICE.name,
                    entity.toString()
                )

                addEvent(event)

                repository.update(
                    EqFilter<Bson, String>(
                        PERSON_ID.fieldName,
                        entity.personId.id
                    ),
                    entity
                )
                logger.trace(
                    "[{} {}] - Entity updated: -> {} <-",
                    PERSON.name,
                    SERVICE.name,
                    entity.javaClass.simpleName
                )
                event
            }
        }
    }

//    private fun handle(
//        command: DeleteDentalProcedureCommand
//    ): ProcedureEvent {
//        val procedureEvents = validateProcedureExists(command)
//        logger.trace(
//            "[{} {}] - Procedure exists...",
//            PROCEDURE.name,
//            SERVICE.name
//        )
//
//        return Procedure.apply(procedureEvents).let { entity ->
//            logger.trace(
//                "[{} {}] - Procedure re-created: -> {} <-",
//                PROCEDURE.name,
//                SERVICE.name,
//                entity.toString()
//            )
//
//            DeleteDentalProcedureEvent(command).let { event ->
//                logger.trace(
//                    "[{} {}] - Event created: -> {} <-",
//                    PROCEDURE.name,
//                    SERVICE.name,
//                    event.toString()
//                )
//
//                addEvent(event)
//
//                repository.delete(
//                    EqFilter<Bson, String>(
//                        PROCEDURE_ID.fieldName,
//                        entity.procedureId.id
//                    )
//                )
//                logger.trace(
//                    "[{} {}] - Entity deleted: -> {} <-",
//                    PROCEDURE.name,
//                    SERVICE.name,
//                    entity.javaClass.simpleName
//                )
//                event
//            }
//        }
//    }

    private fun addEvent(event: PersonEvent) {
        eventStore.addEvent(event)
        logger.trace(
            "[{} {}] - Event added: -> {} <-",
            PERSON.name,
            SERVICE.name,
            event.javaClass.simpleName
        )
    }

    private fun validatePersonDoesNotExist(command: PersonCommand) {
        repository.findOneByFilter(
            EqFilter<String, String>(
                FULL_NAME.fieldName,
                command.fullName
            )
        ).also {
            Assertion.assert(
                """
    There is a person already defined for the given full name: [${command.fullName}]!
                    """.trimIndent(),
                PERSON.name,
                PERSON_ALREADY_EXISTS
            ) {
                !it.isPresent
            }
        }
    }

    private fun validatePersonExists(command: PersonCommand) =
        eventStore.findAllByFilter(
            //getFilter(command)
            EqFilter<String, String>(
                EVENT_PERSON_ID_ID.fieldName,
                command.personId.id
            )
        ).let {
            Assertion.assert(
                """
    There is no person defined for the given id [${command.personId.id}]!
                    """.trimIndent(),
                PERSON.name,
                PERSON_NOT_DEFINED
            ) {
                it.isNotEmpty()
            }
            it
        }

//    private fun getFilter(command: PersonCommand) = when(command) {
//        is CreateAPersonCommand -> EqFilter<String, String>(
//            EVENT_PROCEDURE_ID_ID.fieldName,
//            command.personId.id
//        )
//        is UpdatePatientCommand -> EqFilter<String, String>(
//            EVENT_PROCEDURE_ID_ID.fieldName,
//            command.personId.id
//        )
////        else -> EqFilter<String, String>(
////            EVENT_PATIENT_ID_ID.fieldName,
////            command.procedureId.id
////        )
//    }

    companion object: Loggable()
}
