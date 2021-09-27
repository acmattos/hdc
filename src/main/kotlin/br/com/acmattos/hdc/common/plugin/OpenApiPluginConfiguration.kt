package br.com.acmattos.hdc.common.plugin

import br.com.acmattos.hdc.common.tool.server.javalin.Response
import br.com.acmattos.hdc.common.tool.server.mapper.JacksonObjectMapperFactory
import io.javalin.plugin.openapi.OpenApiOptions
import io.javalin.plugin.openapi.OpenApiPlugin
import io.javalin.plugin.openapi.jackson.JacksonModelConverterFactory
import io.javalin.plugin.openapi.jackson.JacksonToJsonMapper
import io.javalin.plugin.openapi.ui.ReDocOptions
import io.javalin.plugin.openapi.ui.SwaggerOptions
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License

/**
 * @author ACMattos
 * @since 23/09/2021.
 */
object OpenApiPluginConfiguration {
    fun getConfiguredPlugin() = OpenApiPlugin(
        OpenApiOptions(
            Info().apply {
                title("HDC API")
                description("Helena Dental Care API allows schedule control")
                termsOfService("Terms")
                contact(
                    Contact()
                        .name("A")
                        . url("")
                        .email("acmattos@gmail.com")
                )
                license(
                    License()
                        .name("ACMattos License Restricted")
                        .url("Restricted to ACMattos - acmattos@gmail.com use ONLY")
                )
                version("0.0.1-SNAPSHOT")
            }
        ).apply {
            path("/swagger-docs") // Activate the open api endpoint json
            defaultDocumentation { doc ->
                doc.json("500", Response::class.java)
            }// Lambda that will be applied to every documentation
            activateAnnotationScanningFor("br.com.acmattos.hdc") // Activate annotation scanning (Required for annotation api with static java methods)
            .toJsonMapper(JacksonToJsonMapper(JacksonObjectMapperFactory.build())) // Custom json mapper
            .modelConverterFactory(JacksonModelConverterFactory(JacksonObjectMapperFactory.build())) // Custom OpenApi model converter
            swagger(SwaggerOptions("/swagger")) // Activate the swagger ui
            reDoc(ReDocOptions("/redoc")) // Active the ReDoc UI
        }
    )
}