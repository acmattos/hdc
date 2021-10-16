package br.com.acmattos.hdc.person.application

import br.com.acmattos.hdc.common.context.domain.cqs.EventStore
import br.com.acmattos.hdc.common.context.domain.model.Repository
import br.com.acmattos.hdc.common.tool.assertion.AssertionFailedException
import br.com.acmattos.hdc.person.domain.cqs.CreateADentistCommand
import br.com.acmattos.hdc.person.domain.cqs.CreateADentistEvent
import br.com.acmattos.hdc.person.domain.cqs.PersonEvent
import br.com.acmattos.hdc.person.domain.model.Person
import br.com.acmattos.hdc.person.port.rest.CreateADentistRequest
import br.com.acmattos.hdc.person.port.rest.PersonRequestBuilder.buildCreateADentistRequest
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import org.assertj.core.api.AbstractThrowableAssert
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatCode
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

const val EXCEPTION_MESSAGE = "There is a dentist already defined for the given full name: [fullName]!"

/**
 * @author ACMattos
 * @since 16/10/2021.
 */
object PersonCommandHandlerServiceTest: Spek({
    Feature("${PersonCommandHandlerService::class.java} usage") {
        Scenario("handling ${CreateADentistCommand::class.java} successfully") {
            lateinit var command: CreateADentistCommand
            lateinit var eventStore: EventStore<PersonEvent>
            lateinit var repository: Repository<Person>
            lateinit var service: PersonCommandHandlerService
            lateinit var event: CreateADentistEvent
            Given("""a ${EventStore::class.java} mock""") {
                eventStore = mockk()
            }
            And("""eventStore#addEvent just runs""") {
                every { eventStore.addEvent(any<CreateADentistEvent>()) } just Runs
            }
            And("""a ${Repository::class.java} mock""") {
                repository = mockk()
            }
            And("""repository#findByField returns null""") {
                every { repository.findByField("full_name", "fullName") } returns null
            }
            And("""repository#save just runs""") {
                every { repository.save(any()) } just Runs
            }
            And("""a ${CreateADentistCommand::class.java} generated from ${CreateADentistRequest::class.java}""") {
                command = buildCreateADentistRequest().toType() as CreateADentistCommand
            }
            And("""a ${PersonCommandHandlerService::class.java} successfully instantiated""") {
                service = PersonCommandHandlerService(eventStore, repository)
            }
            When("""#handle is executed""") {
                event = service.handle(command) as CreateADentistEvent
            }
            Then("""${CreateADentistEvent::class.java} is not null""") {
                assertThat(event).isNotNull()
            }
            And("""the repository is accessed in the right order""") {
                verifyOrder {
                    repository.findByField("full_name", "fullName")
                    repository.save(any())
                }
            }
            And("""the event store is accessed as well""") {
                verify {
                    eventStore.addEvent(any<CreateADentistEvent>())
                }
            }
        }

        Scenario("handling ${CreateADentistCommand::class.java} for a already registered dentist") {
            lateinit var command: CreateADentistCommand
            lateinit var eventStore: EventStore<PersonEvent>
            lateinit var repository: Repository<Person>
            lateinit var service: PersonCommandHandlerService
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            Given("""a ${EventStore::class.java} mock""") {
                eventStore = mockk()
            }
            And("""eventStore#addEvent just runs""") {
                every { eventStore.addEvent(any<CreateADentistEvent>()) } just Runs
            }
            And("""a ${Repository::class.java} mock""") {
                repository = mockk()
            }
            And("""repository#findByField returns null""") {
                every {
                    repository.findByField("full_name", "fullName")
                } returns Person.apply(
                    CreateADentistEvent(buildCreateADentistRequest().toType() as CreateADentistCommand)
                )
            }
            And("""repository#save just runs""") {
                every { repository.save(any()) } just Runs
            }
            And("""a ${CreateADentistCommand::class.java} generated from ${CreateADentistRequest::class.java}""") {
                command = buildCreateADentistRequest().toType() as CreateADentistCommand
            }
            And("""a ${PersonCommandHandlerService::class.java} successfully instantiated""") {
                service = PersonCommandHandlerService(eventStore, repository)
            }
            When("""#handle is executed""") {
                assertion = assertThatCode {
                    service.handle(command) as CreateADentistEvent
                }
            }
            Then("""${AssertionFailedException::class.java} is raised with message""") {
                assertion.hasSameClassAs(AssertionFailedException(EXCEPTION_MESSAGE))
            }
            And("""the message is $EXCEPTION_MESSAGE""") {
                assertion.hasMessage(EXCEPTION_MESSAGE)
            }
            And("""the repository#findByField is accessed""") {
                verify(exactly = 1) {
                    repository.findByField("full_name", "fullName")
                }
            }
            And("""the repository#save is not accessed""") {
                verify(exactly = 0) {
                    repository.save(any())
                }
            }
            And("""the event store is not accessed """) {
                verify(exactly = 0){
                    eventStore.addEvent(any<CreateADentistEvent>())
                }
            }
        }
    }
})
