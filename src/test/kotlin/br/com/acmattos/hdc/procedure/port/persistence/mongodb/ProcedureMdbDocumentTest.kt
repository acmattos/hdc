package br.com.acmattos.hdc.procedure.port.persistence.mongodb

import br.com.acmattos.hdc.procedure.domain.cqs.CreateDentalProcedureCommand
import br.com.acmattos.hdc.procedure.domain.cqs.CreateDentalProcedureEvent
import br.com.acmattos.hdc.procedure.domain.model.Procedure
import br.com.acmattos.hdc.procedure.port.rest.CreateDentalProcedureRequest
import br.com.acmattos.hdc.procedure.port.rest.ProcedureRequestBuilder
import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

/**
 * @author ACMattos
 * @since 20/03/2022.
 */
object ProcedureMdbDocumentTest: Spek({
    Feature("${ProcedureMdbDocument::class.java.simpleName} usage") {
        Scenario("${ProcedureMdbDocument::class.java.simpleName} population") {
            lateinit var request: CreateDentalProcedureRequest
            lateinit var command: CreateDentalProcedureCommand
            lateinit var event: CreateDentalProcedureEvent
            lateinit var entity: Procedure
            lateinit var document: ProcedureMdbDocument
            Given("""a ${CreateDentalProcedureRequest::class.java.simpleName} successfully instantiated""") {
                request = ProcedureRequestBuilder.buildCreateDentalProcedureRequest()
            }
            And("""a ${CreateDentalProcedureCommand::class.java.simpleName} successfully generated""") {
                command = (request.toType() as CreateDentalProcedureCommand)
            }
            And("""a ${CreateDentalProcedureEvent::class.java.simpleName} successfully instantiated""") {
                event = CreateDentalProcedureEvent(command)
            }
            And("""a ${Procedure::class.java.simpleName} successfully instantiated""") {
                entity = Procedure.apply(listOf(event))
            }
            When("""${ProcedureMdbDocument::class.java.simpleName} is instantiated""") {
                document = ProcedureMdbDocument(entity)
            }
            Then("""document#toType is equal to the entity""") {
                assertThat(document.toType().toString()).isEqualTo(entity.toString())
            }
        }
    }
})
