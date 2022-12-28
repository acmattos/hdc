package br.com.acmattos.hdc.odontogram.port.rest

import br.com.acmattos.hdc.common.context.port.rest.EndpointDefinition
import br.com.acmattos.hdc.odontogram.config.KoinComponentName.ENDPOINT_DEFINITION
import br.com.acmattos.hdc.odontogram.config.OdontogramKoinComponent
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.setStaticJavalin
import org.koin.core.Koin
import org.koin.core.qualifier.named
import org.koin.dsl.koinApplication
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

/**
 * @author ACMattos
 * @since 27/12/2022.
 */
object OdontogramControllerEndpointDefinitionTest: Spek({
    Feature("${OdontogramControllerEndpointDefinition::class.java.simpleName} existence") {
        Scenario("endpoint definition exists") {
            lateinit var koin: Koin
            lateinit var endpoint: EndpointDefinition
            Given("""a Koin Application properly instantiated""") {
                koin = koinApplication {
                    modules(OdontogramKoinComponent.loadModule())
                }.koin
            }
            And("""a javalin server is configured""") {
                setStaticJavalin(Javalin.create())
            }
            When("""a the endpoint definition is injected""") {
                endpoint = koin.get(named(ENDPOINT_DEFINITION.value))
            }
            Then("""#routes can be called""") {
                endpoint.routes()
            }
        }
    }
})
