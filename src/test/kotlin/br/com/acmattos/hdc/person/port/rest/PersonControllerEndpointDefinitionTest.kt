package br.com.acmattos.hdc.person.port.rest

import br.com.acmattos.hdc.common.context.port.rest.EndpointDefinition
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder
import io.kotest.core.spec.style.FreeSpec
import io.mockk.mockk

/**
 * @author ACMattos
 * @since 05/10/2021.
 */
class PersonControllerEndpointDefinitionTest: FreeSpec({
    "Feature: PersonControllerEndpointDefinition existence" - {
        "Scenario: endpoint definition exists" - {
            lateinit var ccontroller: PersonCommandController
            lateinit var qcontroller: PersonQueryController
            lateinit var endpoint: EndpointDefinition
            "Given: a mocked command controller" {
                ccontroller = mockk()
            }
            "And: a mocked query controller" {
                qcontroller = mockk()
            }
            "And: a javalin server is configured" {
                ApiBuilder.setStaticJavalin(Javalin.create())
            }
            "When: a ${PersonControllerEndpointDefinition::class.java} is successfully instantiated" {
                endpoint = PersonControllerEndpointDefinition(ccontroller, qcontroller)
            }
            "Then: #routes can be called" {
                endpoint.routes()
            }
        }
    }
})
