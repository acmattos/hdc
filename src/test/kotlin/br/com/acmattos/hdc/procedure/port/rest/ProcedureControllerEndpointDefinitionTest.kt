package br.com.acmattos.hdc.procedure.port.rest

import br.com.acmattos.hdc.common.context.port.rest.EndpointDefinition
import br.com.acmattos.hdc.procedure.config.KoinComponentName.ENDPOINT_DEFINITION
import br.com.acmattos.hdc.procedure.config.ProcedureKoinComponent
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder
import io.kotest.core.spec.style.FreeSpec
import org.koin.core.Koin
import org.koin.core.qualifier.named
import org.koin.dsl.koinApplication

/**
 * @author ACMattos
 * @since 20/03/2022.
 */
class ProcedureControllerEndpointDefinitionTest: FreeSpec({
    "Feature: ProcedureControllerEndpointDefinition existence" - {
        "Scenario: endpoint definition exists" - {
            lateinit var koin: Koin
            lateinit var endpoint: EndpointDefinition
            "Given: a Koin Application properly instantiated" {
                koin = koinApplication {
                    modules(ProcedureKoinComponent.loadModule())
                }.koin
            }
            "And: a javalin server is configured" {
                ApiBuilder.setStaticJavalin(Javalin.create())
            }
            "When: a the endpoint definition is injected" {
                endpoint = koin.get(named(ENDPOINT_DEFINITION.value))
            }
            "Then: #routes can be called" {
                endpoint.routes()
            }
        }
    }
})
