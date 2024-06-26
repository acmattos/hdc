package br.com.acmattos.hdc.procedure.port.persistence.mongodb

import br.com.acmattos.hdc.procedure.domain.cqs.ProcedureCreateCommand
import br.com.acmattos.hdc.procedure.domain.cqs.ProcedureCreateEvent
import br.com.acmattos.hdc.procedure.domain.model.Procedure
import br.com.acmattos.hdc.procedure.domain.model.RequestBuilder
import br.com.acmattos.hdc.procedure.port.rest.ProcedureCreateRequest
import io.kotest.core.spec.style.FreeSpec
import org.assertj.core.api.Assertions.assertThat

/**
 * @author ACMattos
 * @since 20/03/2022.
 */
class ProcedureMdbDocumentTest: FreeSpec({
    "Feature:ProcedureMdbDocument usage" - {
        "Scenario: ProcedureMdbDocument population" - {
            lateinit var request: ProcedureCreateRequest
            lateinit var command: ProcedureCreateCommand
            lateinit var event: ProcedureCreateEvent
            lateinit var entity: Procedure
            lateinit var document: ProcedureMdbDocument
            "Given: a ${ProcedureCreateRequest::class.java.simpleName} successfully instantiated" {
                request = RequestBuilder.buildCreateRequest()
            }
            "And: a ${ProcedureCreateCommand::class.java.simpleName} successfully generated" {
                command = (request.toType() as ProcedureCreateCommand)
            }
            "And: a ${ProcedureCreateEvent::class.java.simpleName} successfully instantiated" {
                event = ProcedureCreateEvent(command)
            }
            "And: a ${Procedure::class.java.simpleName} successfully instantiated" {
                entity = Procedure.apply(listOf(event))
            }
            "When: ${ProcedureMdbDocument::class.java.simpleName} is instantiated" {
                document = ProcedureMdbDocument(entity)
            }
            "Then: document#toType is equal to the entity" {
                assertThat(document.toType().toString()).isEqualTo(entity.toString())
            }
        }
    }
})
