package br.com.acmattos.hdc.person.application

import br.com.acmattos.hdc.common.context.config.ContextLogEnum.SERVICE
import br.com.acmattos.hdc.common.context.domain.cqs.Command
import br.com.acmattos.hdc.common.context.domain.cqs.CommandHandler
import br.com.acmattos.hdc.common.context.domain.cqs.EventStore
import br.com.acmattos.hdc.common.context.domain.model.Repository
import br.com.acmattos.hdc.common.tool.assertion.Assertion
import br.com.acmattos.hdc.common.tool.loggable.Loggable
import br.com.acmattos.hdc.person.config.PersonLogEnum.PERSON
import br.com.acmattos.hdc.person.domain.cqs.CreateADentistCommand
import br.com.acmattos.hdc.person.domain.cqs.CreateADentistEvent
import br.com.acmattos.hdc.person.domain.cqs.PersonCommand
import br.com.acmattos.hdc.person.domain.cqs.PersonEvent
import br.com.acmattos.hdc.person.domain.model.Person

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
            is CreateADentistCommand -> handle(command)
            else -> handle(command as CreateADentistCommand)
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

    private fun handle(command: CreateADentistCommand): PersonEvent {
        validateDentistDoesNotExist(command)
        logger.trace(
            "[{} {}] - Dentist does not exist...",
            PERSON.name,
            SERVICE.name
        )

        val event = CreateADentistEvent(command).also { event ->
            logger.trace(
                "[{} {}] - Event created: -> {} <-",
                PERSON.name,
                SERVICE.name,
                event.toString()
            )

            eventStore.addEvent(event)
            logger.trace(
                "[{} {}] - Event added: -> {} <-",
                PERSON.name,
                SERVICE.name,
                event.javaClass.name
            )

            Person.apply(event).also { entity ->
                logger.trace(
                    "[{} {}] - Entity created: -> {} <-",
                    PERSON.name,
                    SERVICE.name,
                    entity.toString()
                )

                repository.save(entity)
                logger.trace(
                    "[{} {}] - Entity saved: -> {} <-",
                    PERSON.name,
                    SERVICE.name,
                    entity.javaClass.name
                )
            }
        }
        return event
    }

    private fun validateDentistDoesNotExist(command: PersonCommand) {
        repository.findByField(
            "full_name", command.fullName
        ).also {
            Assertion.assert(
                """
    There is a dentist already defined for the given full name: [${command.fullName}]!
                    """.trimIndent()
            ) {
                it == null
            }
        }
    }
    companion object: Loggable()
}