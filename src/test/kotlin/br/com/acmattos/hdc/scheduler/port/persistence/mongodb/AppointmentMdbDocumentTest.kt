//package br.com.acmattos.hdc.scheduler.port.persistence.mongodb
//
//import br.com.acmattos.hdc.scheduler.domain.cqs.CreateAppointmentForTheScheduleCommand
//import br.com.acmattos.hdc.scheduler.domain.cqs.CreateAppointmentForTheScheduleEvent
//import br.com.acmattos.hdc.scheduler.domain.cqs.CreateAppointmentsForTheScheduleCommand
//import br.com.acmattos.hdc.scheduler.domain.model.Appointment
//import br.com.acmattos.hdc.scheduler.domain.model.Schedule
//import br.com.acmattos.hdc.scheduler.port.rest.AppointmentRequestBuilder
//import br.com.acmattos.hdc.scheduler.port.rest.CreateAppointmentsForTheScheduleRequestBuilder
//import br.com.acmattos.hdc.scheduler.port.rest.ScheduleRequestBuilder
//import org.assertj.core.api.Assertions.assertThat
//import org.spekframework.spek2.Spek
//import org.spekframework.spek2.style.gherkin.Feature
//
///**
// * @author ACMattos
// * @since 27/11/2021.
// */
//object AppointmentMdbDocumentTest: FreeSpec({
//    "Feature: ${AppointmentMdbDocument::class.java} usage") {
//        "Scenario: ${AppointmentMdbDocument::class.java} population") {
//            lateinit var request: CreateAppointmentsForTheScheduleRequestBuilder
//            lateinit var command: CreateAppointmentsForTheScheduleCommand
//            lateinit var commands: List<CreateAppointmentForTheScheduleCommand>
//            lateinit var event: CreateAppointmentForTheScheduleEvent
//            lateinit var schedule: Schedule
//            lateinit var entity: Appointment
//            lateinit var document: AppointmentMdbDocument
//            "Given: a ${CreateAppointmentsForTheScheduleRequestBuilder::class.java} successfully instantiated" {
//                request = AppointmentRequestBuilder.buildCreateAppointmentsForTheScheduleRequestBuilder()
//            }
//            "And: a ${Schedule::class.java} successfully generated" {
//                schedule = ScheduleRequestBuilder.buildSchedule()
//            }
//            "And: a ${CreateAppointmentsForTheScheduleCommand::class.java} successfully generated" {
//                command = (request.toType() as CreateAppointmentsForTheScheduleCommand)
//            }
//            "And: a ${CreateAppointmentForTheScheduleCommand::class.java} list successfully instantiated" {
//                commands = schedule.createAppointmentCommands(command) as List<CreateAppointmentForTheScheduleCommand>
//            }
//            "And: a ${CreateAppointmentForTheScheduleEvent::class.java} successfully instantiated" {
//                event = CreateAppointmentForTheScheduleEvent(commands.first())
//            }
//            "And: a ${Appointment::class.java} successfully instantiated" {
//                entity = Appointment.apply(listOf(event)) as Appointment
//            }
//            "When: ${AppointmentMdbDocument::class.java} is instantiated" {
//                document = AppointmentMdbDocument(entity)
//            }
//            "Then: document#toType is equal to the entity" {
//                assertThat(document.toType().toString()).isEqualTo(entity.toString())
//            }
//        }
//    }
//})
