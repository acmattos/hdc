package br.com.acmattos.hdc.odontogram.port.persistence.mongodb

import br.com.acmattos.hdc.odontogram.domain.cqs.OdontogramCreateCommand
import br.com.acmattos.hdc.odontogram.domain.cqs.OdontogramCreateEvent
import br.com.acmattos.hdc.odontogram.domain.model.Odontogram
import br.com.acmattos.hdc.odontogram.domain.model.RequestBuilder
import br.com.acmattos.hdc.odontogram.port.rest.OdontogramCreateRequest
import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

/**
 * @author ACMattos
 * @since 19/02/2032.
 */
object OdontogramMdbDocumentTest: Spek({
    Feature("${OdontogramMdbDocument::class.java.simpleName} usage") {
        Scenario("${OdontogramMdbDocument::class.java.simpleName} population") {
            lateinit var request: OdontogramCreateRequest
            lateinit var command: OdontogramCreateCommand
            lateinit var event: OdontogramCreateEvent
            lateinit var entity: Odontogram
            lateinit var document: OdontogramMdbDocument
            Given("""a ${OdontogramCreateRequest::class.java.simpleName} successfully instantiated""") {
                request = RequestBuilder.buildCreateRequest()
            }
            And("""a ${OdontogramCreateCommand::class.java.simpleName} successfully generated""") {
                command = (request.toType() as OdontogramCreateCommand)
            }
            And("""a ${OdontogramCreateEvent::class.java.simpleName} successfully instantiated""") {
                event = OdontogramCreateEvent(command)
            }
            And("""a ${Odontogram::class.java.simpleName} successfully instantiated""") {
                entity = Odontogram.apply(listOf(event))
            }
            When("""${OdontogramMdbDocument::class.java.simpleName} is instantiated""") {
                document = OdontogramMdbDocument(entity)
            }
            Then("""document#toType is equal to the entity""") {
                assertThat(document.toType().toString()).isEqualTo(entity.toString())
            }
        }
    }
})
