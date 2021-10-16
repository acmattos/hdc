package br.com.acmattos.hdc.person.port.rest

import br.com.acmattos.hdc.common.context.port.rest.EndpointDefinition
import br.com.acmattos.hdc.person.config.ED
import br.com.acmattos.hdc.person.config.PersonKoinComponent
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder
import io.mockk.mockk
import org.koin.core.Koin
import org.koin.core.qualifier.named
import org.koin.dsl.koinApplication
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

/**
 * @author ACMattos
 * @since 05/10/2021.
 */
object PersonCommandControllerEndpointDefinitionTest: Spek({
    Feature("${PersonCommandControllerEndpointDefinition::class.java} existence") {
        Scenario("endpoint definition exists") {
            lateinit var controller: PersonCommandController
            lateinit var endpoint: EndpointDefinition
            Given("""a mocked controller""") {
                controller = mockk()
            }
            And("""a javalin server is configured""") {
                ApiBuilder.setStaticJavalin(Javalin.create())
            }
            When("""a ${PersonCommandControllerEndpointDefinition::class.java} is successfully instantiated""") {
                endpoint = PersonCommandControllerEndpointDefinition(controller)
            }
            Then("""#routes can be called""") {
                endpoint.routes()
            }
        }
    }
})
