//package br.com.acmattos.hdc.scheduler.port.persistence.mongodb
//
//import br.com.acmattos.hdc.scheduler.domain.cqs.CreateAScheduleForTheDentistCommand
//import br.com.acmattos.hdc.scheduler.domain.cqs.CreateAScheduleForTheDentistEvent
//import br.com.acmattos.hdc.scheduler.domain.model.DentistBuilder
//import br.com.acmattos.hdc.scheduler.domain.model.Schedule
//import br.com.acmattos.hdc.scheduler.port.rest.CreateAScheduleForTheDentistRequest
//import br.com.acmattos.hdc.scheduler.port.rest.ScheduleRequestBuilder
//import org.assertj.core.api.Assertions.assertThat
//import org.spekframework.spek2.Spek
//import org.spekframework.spek2.style.gherkin.Feature
//
///**
// * @author ACMattos
// * @since 21/10/2021.
// */
//object ScheduleMdbDocumentTest: FreeSpec({
//    "Feature: ${ScheduleMdbDocument::class.java.simpleName} usage") {
//        "Scenario: ${ScheduleMdbDocument::class.java.simpleName} population") {
//            lateinit var request: CreateAScheduleForTheDentistRequest
//            lateinit var command: CreateAScheduleForTheDentistCommand
//            lateinit var event: CreateAScheduleForTheDentistEvent
//            lateinit var entity: Schedule
//            lateinit var document: ScheduleMdbDocument
//            "Given: a ${CreateAScheduleForTheDentistRequest::class.java.simpleName} successfully instantiated" {
//                request = ScheduleRequestBuilder.buildCreateAScheduleForTheDentistRequest()
//            }
//            "And: a ${CreateAScheduleForTheDentistCommand::class.java.simpleName} successfully generated" {
//                command = (request.toType() as CreateAScheduleForTheDentistCommand).copy(dentist = DentistBuilder.build())
//            }
//            "And: a ${CreateAScheduleForTheDentistEvent::class.java.simpleName} successfully instantiated" {
//                event = CreateAScheduleForTheDentistEvent(command)
//            }
//            "And: a ${Schedule::class.java} successfully instantiated" {
//                entity = Schedule.apply(listOf(event))
//            }
//            "When: ${ScheduleMdbDocument::class.java.simpleName} is instantiated" {
//                document = ScheduleMdbDocument(entity)
//            }
//            "Then: document#toType is equal to the entity" {
//                assertThat(document.toType().toString()).isEqualTo(entity.toString())
//            }
//        }
//    }
//})
