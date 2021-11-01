package br.com.acmattos.hdc.scheduler.application

import br.com.acmattos.hdc.common.context.config.ContextLogEnum.SERVICE
import br.com.acmattos.hdc.common.context.domain.cqs.Command
import br.com.acmattos.hdc.common.context.domain.cqs.CommandHandler
import br.com.acmattos.hdc.common.context.domain.cqs.EventStore
import br.com.acmattos.hdc.common.context.domain.model.Repository
import br.com.acmattos.hdc.common.tool.assertion.Assertion
import br.com.acmattos.hdc.common.tool.loggable.Loggable
import br.com.acmattos.hdc.scheduler.config.ErrorTrackerCodeEnum.SCHEDULE_ALREADY_DEFINED
import br.com.acmattos.hdc.scheduler.config.ScheduleLogEnum.SCHEDULE
import br.com.acmattos.hdc.scheduler.domain.cqs.CreateAScheduleForTheDentistCommand
import br.com.acmattos.hdc.scheduler.domain.cqs.CreateAScheduleForTheDentistEvent
import br.com.acmattos.hdc.scheduler.domain.cqs.ScheduleEvent
import br.com.acmattos.hdc.scheduler.domain.model.Schedule


/**
 * @author ACMattos
 * @since 28/09/2019.
 */
class ScheduleCommandApplicationService(// TODO ScheduleCommandHandlerService
    private val eventStore: EventStore<ScheduleEvent>,
    private val repository: Repository<Schedule>,
): CommandHandler<ScheduleEvent> {
    override fun handle(command: Command): ScheduleEvent {
        logger.debug(
            "[{} {}] - Handling command {}...: -> {} <-",
            SCHEDULE.name,
            SERVICE.name,
            command.javaClass.name,
            command.toString()
        )

        val event = when(command){
            is CreateAScheduleForTheDentistCommand -> handle(command)
            else -> handle(command as CreateAScheduleForTheDentistCommand)
        }

        logger.info(
            "[{} {}] - Handling command {}...: -> Generated event {} !DONE!<-",
            SCHEDULE.name,
            SERVICE.name,
            command.javaClass.name,
            event.javaClass.name
        )
        return event
    }

    private fun handle(command: CreateAScheduleForTheDentistCommand): ScheduleEvent {
        validateScheduleDoesNotExist(command)
        logger.trace(
            "[{} {}] - Schedule does not exist...",
            SCHEDULE.name,
            SERVICE.name
        )

        val event = CreateAScheduleForTheDentistEvent(command).also { event ->
            logger.trace(
                "[{} {}] - Event created: -> {} <-",
                SCHEDULE.name,
                SERVICE.name,
                event.toString()
            )

            eventStore.addEvent(event)
            logger.trace(
                "[{} {}] - Event added: -> {} <-",
                SCHEDULE.name,
                SERVICE.name,
                event.javaClass.name
            )

            Schedule.apply(event).also { entity ->
                logger.trace(
                    "[{} {}] - Entity created: -> {} <-",
                    SCHEDULE.name,
                    SERVICE.name,
                    entity.toString()
                )

                repository.save(entity)
                logger.trace(
                    "[{} {}] - Entity saved: -> {} <-",
                    SCHEDULE.name,
                    SERVICE.name,
                    entity.javaClass.name
                )
            }
        }
        return event
    }

    private fun validateScheduleDoesNotExist(command: CreateAScheduleForTheDentistCommand) {
        eventStore.findAllByField(
            "event.schedule_dentist_id.id",
            command.scheduleDentistId.id
        ).also {
            Assertion.assert(
                """
    There is a schedule already defined for the given scheduleDentistId [${command.scheduleDentistId}]!
                    """.trimIndent(),
                SCHEDULE_ALREADY_DEFINED.code
            ) {
                it == null || it.isEmpty()
            }
        }
    }

    companion object: Loggable()
}
