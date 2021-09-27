package br.com.acmattos.hdc.common.plugin

import io.javalin.Javalin
import io.javalin.plugin.openapi.OpenApiPlugin
import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

private const val SWAGGER_DOCS = "/swagger-docs"

/**
 * @author ACMattos
 * @since 26/09/2021.
 */
object OpenApiPluginConfigurationTest: Spek({
    Feature("${OpenApiPluginConfiguration::class.java} usage") {
        Scenario("plugin configuration is done successfully") {
            lateinit var configuration: OpenApiPluginConfiguration
            lateinit var plugin: OpenApiPlugin
            lateinit var javalin: Javalin
            Given("""a ${OpenApiPluginConfiguration::class.java}""") {
                configuration = OpenApiPluginConfiguration
            }
            When("""#getConfiguredPlugin is executed""") {
                plugin =  configuration.getConfiguredPlugin()
            }
            And("""the plugin is initialized with a Javalin instance""") {
                javalin = Javalin.create()
                plugin.init(javalin)
            }
            Then("""swagger docs path is equal to $SWAGGER_DOCS""") {
                assertThat(
                    plugin.openApiHandler.options.path
                ).isEqualTo(SWAGGER_DOCS)
            }
            And("reDoc is configured") {
                assertThat(
                    plugin.openApiHandler.options.reDoc
                ).isNotNull()
            }
            And("swagger is configured") {
                assertThat(
                    plugin.openApiHandler.options.swagger
                ).isNotNull()
                javalin.stop()
           }
        }
    }
})
