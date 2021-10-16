package br.com.acmattos.hdc.person.port.persistence.mongodb

import br.com.acmattos.hdc.person.domain.cqs.CreateADentistCommand
import br.com.acmattos.hdc.person.domain.cqs.CreateADentistEvent
import br.com.acmattos.hdc.person.domain.model.Person
import br.com.acmattos.hdc.person.port.rest.CreateADentistRequest
import br.com.acmattos.hdc.person.port.rest.PersonRequestBuilder
import br.com.acmattos.hdc.person.port.rest.STATUS
import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

/**
 * @author ACMattos
 * @since 16/10/2021.
 */
object PersonMdbDocumentTest: Spek({
    Feature("${PersonMdbDocument::class.java} usage") {
        Scenario("${PersonMdbDocument::class.java} population") {
            lateinit var request: CreateADentistRequest
            lateinit var command: CreateADentistCommand
            lateinit var event: CreateADentistEvent
            lateinit var person: Person
            lateinit var document: PersonMdbDocument
            Given("""a ${CreateADentistRequest::class.java} successfully instantiated""") {
                request = PersonRequestBuilder.buildCreateADentistRequest()
            }
            And("""a ${CreateADentistCommand::class.java} successfully generated""") {
                command = request.toType() as CreateADentistCommand
            }
            And("""a ${CreateADentistEvent::class.java} successfully instantiated""") {
                event = CreateADentistEvent(command)
            }
            And("""a ${Person::class.java} successfully instantiated""") {
                person = Person.apply(listOf(event))
            }
            When("""${PersonMdbDocument::class.java} is instantiated""") {
                document = PersonMdbDocument(person)
            }
            Then("""status is $STATUS""") {
                assertThat(document.toType().toString()).isEqualTo(person.toString())
            }
        }
    }
})
