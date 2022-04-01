package br.com.acmattos.hdc.scheduler.application

import br.com.acmattos.hdc.common.context.config.ContextLogEnum.SERVICE
import br.com.acmattos.hdc.common.context.domain.cqs.AndFilter
import br.com.acmattos.hdc.common.context.domain.cqs.Command
import br.com.acmattos.hdc.common.context.domain.cqs.CommandHandler
import br.com.acmattos.hdc.common.context.domain.cqs.EqFilter
import br.com.acmattos.hdc.common.context.domain.cqs.EventStore
import br.com.acmattos.hdc.common.context.domain.model.Repository
import br.com.acmattos.hdc.common.tool.assertion.Assertion
import br.com.acmattos.hdc.common.tool.loggable.Loggable
import br.com.acmattos.hdc.scheduler.config.MessageTrackerCodeEnum.OVERLAPPING_APPOINTMENT
import br.com.acmattos.hdc.scheduler.config.MessageTrackerCodeEnum.SCHEDULE_NOT_DEFINED
import br.com.acmattos.hdc.scheduler.config.ScheduleLogEnum.APPOINTMENT
import br.com.acmattos.hdc.scheduler.domain.cqs.AppointmentCommand
import br.com.acmattos.hdc.scheduler.domain.cqs.AppointmentEvent
import br.com.acmattos.hdc.scheduler.domain.cqs.CreateAppointmentForTheScheduleCommand
import br.com.acmattos.hdc.scheduler.domain.cqs.CreateAppointmentForTheScheduleEvent
import br.com.acmattos.hdc.scheduler.domain.cqs.CreateAppointmentsForTheScheduleCommand
import br.com.acmattos.hdc.scheduler.domain.cqs.CreateAppointmentsForTheScheduleEvent
import br.com.acmattos.hdc.scheduler.domain.cqs.ScheduleEvent
import br.com.acmattos.hdc.scheduler.domain.model.Appointment
import br.com.acmattos.hdc.scheduler.domain.model.Schedule
import java.time.LocalTime

/**
 * @author ACMattos
 * @since 03/08/2021.
 */
class AppointmentCommandHandlerService(// TODO Test
    private val eventStore: EventStore<AppointmentEvent>,
    private val repository: Repository<Appointment>,
    private val scheduleEventStore: EventStore<ScheduleEvent>,
): CommandHandler<AppointmentEvent> {
    override fun handle(command: Command): AppointmentEvent {
        logger.debug(
            "[{} {}] - Handling command {}...: -> {} <-",
            APPOINTMENT.name,
            SERVICE.name,
            command.javaClass.name,
            command.toString()
        )

        val event = when(command){
            is CreateAppointmentsForTheScheduleCommand -> handle(command)
            else -> handle(command as CreateAppointmentForTheScheduleCommand)
        }

        logger.info(
            "[{} {}] - Handling command {}...: -> Generated event {} !DONE!<-",
            APPOINTMENT.name,
            SERVICE.name,
            command.javaClass.name,
            event.javaClass.name
        )
        return event
    }

    private fun handle(
        command: CreateAppointmentsForTheScheduleCommand
    ): AppointmentEvent {
        val scheduleEvents = validateScheduleExists(command)
        logger.trace(
            "[{} {}] - Schedule exists...",
            APPOINTMENT.name,
            SERVICE.name
        )


        return Schedule.apply(scheduleEvents).let { entity ->
            logger.trace(
                "[{} {}] - Schedule re-created: -> {} <-",
                APPOINTMENT.name,
                SERVICE.name,
                entity.toString()
            )
            val events = entity.createAppointmentCommands(command).map {
                // TODO validateHasNoConflictingAppointments(command)
                handle(it as CreateAppointmentForTheScheduleCommand) as CreateAppointmentForTheScheduleEvent
            }.toList()
            // TODO Think about no appointments created scenario
            CreateAppointmentsForTheScheduleEvent(command, events).also { event ->
                logger.trace(
                    "[{} {}] - Event created: -> {} <-",
                    APPOINTMENT.name,
                    SERVICE.name,
                    event.toString()
                )
                event
            }
        }
    }

    private fun handle(
        command: CreateAppointmentForTheScheduleCommand
    ): AppointmentEvent =
        CreateAppointmentForTheScheduleEvent(command).also { event ->
            logger.trace(
                "[{} {}] - Event created: -> {} <-",
                APPOINTMENT.name,
                SERVICE.name,
                event.toString()
            )

            // TODO
            validateHasNoConflictingAppointment(command)

            Appointment.apply(event).let { entity ->
                logger.trace(
                    "[{} {}] - Entity created: -> {} <-",
                    APPOINTMENT.name,
                    SERVICE.name,
                    entity.toString()
                )

                eventStore.addEvent(event)
                logger.trace(
                    "[{} {}] - Event added: -> {} <-",
                    APPOINTMENT.name,
                    SERVICE.name,
                    event.javaClass.name
                )

                repository.save(entity)
                logger.trace(
                    "[{} {}] - Entity saved: -> {} <-",
                    APPOINTMENT.name,
                    SERVICE.name,
                    entity.javaClass.name
                )
                event
            }
        }

    private fun validateScheduleExists(command: AppointmentCommand) =
        scheduleEventStore.findAllByFilter(
            EqFilter<String, String>(
                "event.schedule_id.id",// TODO TRACK THIS FIELD
                command.scheduleId.id
            )
        ).also { events ->
            Assertion.assert(
                """
    There is no schedule defined for the given scheduleId [${command.scheduleId}]!
                    """.trimIndent(),
                APPOINTMENT.name,
                SCHEDULE_NOT_DEFINED.code
            ) {
                events.isNotEmpty()
            }
            events
        }

    private fun validateHasNoConflictingAppointment(
        command: CreateAppointmentForTheScheduleCommand
    ) {
        eventStore.findAllByFilter(
            AndFilter(
                listOf(
                    EqFilter("event.schedule_id.id", command.scheduleId.id), // TODO TRACK THIS FIELD
                    EqFilter("event.date", command.date), // TODO TRACK THIS FIELD
                    EqFilter<String, LocalTime>("event.time", command.time) // TODO TRACK THIS FIELD
                )
            )
            // TODO VERIFY AND THEN REMOVE!
//            OverlappingAppointmentCriteria(
//                command.scheduleId,
//                command.date,
//                command.time
//            )
        ).also { events ->
            Assertion.assert(
                """
There is an appointment already defined for the given scheduleId, date and time 
[${command.scheduleId}, ${command.date}, ${command.time}]!
                    """.trimIndent(),
                APPOINTMENT.name,
                OVERLAPPING_APPOINTMENT.code
            ) {
                events.isEmpty()
            }
            events
        }
    }
    //fun validateHasNoConflictingAppointments(command) {
    //    repositoryAssertion.assert() {}
    // }

    //    // Transactional
//    private fun handle(
//        command: SetAnAppointmentForAPatientCommand
//    ): AppointmentEvent {
//        //scheduleRepository.getTransaction().handle {
//        val allEvents = validateAppointmentExists(command).let { previousEvents ->
//            val events = mutableListOf<AppointmentEvent>()
//            previousEvents.forEach { event ->
//                events.add(event)
//            }
//            events.add(SetAnAppointmentForAPatientEvent(command))
//            events
//        }
//        logger.trace("Appointment exists...")
//
//        val appointment = Appointment.apply(allEvents)
//        logger.trace("Appointment recreated: -> {} <-", appointment.toString())
//
//        messageBroker.publish(allEvents.last()) // Updating entity and Persisting event
//        logger.trace("Event published: -> {} <-", allEvents.last().javaClass.name)
//
//        command.thenApply { allEvents.last() }
//        logger.trace("Event applied to command...")
//
//        return allEvents.last()// TODO new event with total created!!!
//        //}
//    }
//
//    private fun validateAppointmentExists(command: SetAnAppointmentForAPatientCommand) =
//        eventStore.findAll(
//            command.appointmentId
//        ).also {
//            Assertion.assert(
//                """
//There are no appointments defined for the given appointmentId [${command.appointmentId}]!
//                """.trimIndent()
//            ) {
//                it.isEmpty()
//            }
//        }
    companion object: Loggable()
}
