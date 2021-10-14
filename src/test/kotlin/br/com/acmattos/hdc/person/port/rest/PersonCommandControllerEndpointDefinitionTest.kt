package br.com.acmattos.hdc.person.port.rest

import br.com.acmattos.hdc.common.context.port.rest.EndpointDefinition
import br.com.acmattos.hdc.person.config.ED
import br.com.acmattos.hdc.person.config.PersonKoinComponent
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder
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
            lateinit var koin: Koin
            lateinit var endpoint: EndpointDefinition
            Given("""a Koin Application properly instantiated""") {
                koin = koinApplication {
                    modules(PersonKoinComponent.loadModule())
                }.koin
            }
            And("""a javalin server is configured""") {
                ApiBuilder.setStaticJavalin(Javalin.create())
            }
            When("""a the endpoint definition is injected""") {
                endpoint = koin.get<EndpointDefinition>(named(ED))
            }
            Then("""#routes can be called""") {
                endpoint.routes()
            }
        }
    }
})
