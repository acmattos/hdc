package br.com.acmattos.hdc.common.tool.server.javalin

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.javalin.core.JavalinConfig
import java.util.function.Consumer

/**
 * @author ACMattos
 * @since 23/06/2019.
 */
object JavalinServerBuilder {
    private var javalinConfig: Consumer<JavalinConfig> = Consumer {}
    private lateinit var routes: () -> Unit
    private var objectMapper: ObjectMapper = jacksonObjectMapper()
    private var port: Int = 7000

    fun config(javalinConfig: Consumer<JavalinConfig>): JavalinServerBuilder {
        JavalinServerBuilder.javalinConfig = javalinConfig
        return this
    }

    fun routes(routes: () -> Unit): JavalinServerBuilder {
        JavalinServerBuilder.routes = routes
        return this
    }

    fun objectMapper(
        objectMapper: ObjectMapper
    ): JavalinServerBuilder {
        JavalinServerBuilder.objectMapper = objectMapper
        return this
    }

    fun port(port: Int): JavalinServerBuilder {
        JavalinServerBuilder.port = port
        return this
    }

    fun build(): JavalinServer {
        return JavalinServer(
            javalinConfig = javalinConfig,
            routes = routes,
            objectMapper = objectMapper,
            port = port
        ).start()
    }
}