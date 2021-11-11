package br.com.acmattos.hdc

import br.com.acmattos.hdc.common.context.port.rest.EndpointDefinition
import br.com.acmattos.hdc.common.plugin.OpenApiPluginConfiguration
import br.com.acmattos.hdc.common.tool.config.PropHandler.getProperty
import br.com.acmattos.hdc.common.tool.server.javalin.JavalinServer
import br.com.acmattos.hdc.common.tool.server.javalin.JavalinServerBuilder
import br.com.acmattos.hdc.common.tool.server.mapper.JacksonObjectMapperFactory
import br.com.acmattos.hdc.person.config.PersonKoinComponent
import br.com.acmattos.hdc.scheduler.config.AppointmentKoinConfig
import br.com.acmattos.hdc.scheduler.config.ScheduleKoinComponent
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.handler.StatisticsHandler
import org.eclipse.jetty.util.thread.QueuedThreadPool
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named

/**
 * @author ACMattos
 * @since 07/06/2020.
 */
class HdcApplication {
    init {
        startKoin {
            printLogger()

            koin.loadModules(listOf(AppointmentKoinConfig.loadModule()))
            koin.loadModules(listOf(PersonKoinComponent.loadModule()))
            koin.loadModules(listOf(ScheduleKoinComponent.loadModule()))
        }
    }

    fun init(): JavalinServer {
        return startHttpServer()
    }

    companion object: KoinComponent {
        private val appointmentEndpointDefinition: EndpointDefinition
            by inject(named("AppointmentControllerEndpointDefinition"))
        private val personCommandEndpointDefinition: EndpointDefinition
            by inject(named("PersonCommandControllerEndpointDefinition"))
        private val scheduleCommandEndpointDefinition: EndpointDefinition
            by inject(named("ScheduleCommandControllerEndpointDefinition"))

        private fun startHttpServer (): JavalinServer {
            return JavalinServerBuilder
                .config { config ->
                    config.defaultContentType = "application/text"
                    config.showJavalinBanner = true
                    config.enableCorsForAllOrigins()

                    config.enableDevLogging()
                    config.enableWebjars()

                    config.server {
                        val statisticsHandler = StatisticsHandler()
                        val queuedThreadPool = QueuedThreadPool(
                            getProperty("JAVALIN_SERVER_MAX_THREADS", 300),
                            getProperty("JAVALIN_SERVER_MIN_THREADS", 50),
                            getProperty(
                                "JAVALIN_SERVER_THREAD_IDLE_TIMEOUT_IN_MILLIS",
                                60000
                            )
                        )
                        val server = Server(queuedThreadPool)
                        server.handler = statisticsHandler
                        server
                    }
                    config.registerPlugin(
                        OpenApiPluginConfiguration.getConfiguredPlugin()
                    )
                }
                .routes {
                    appointmentEndpointDefinition.routes()
                    personCommandEndpointDefinition.routes()
                    scheduleCommandEndpointDefinition.routes()
                }
                .objectMapper(JacksonObjectMapperFactory.build())
                .port( getProperty("JAVALIN_SERVER_PORT", 7000))
                .build()
        }
    }
}

fun main(){
    HdcApplication().init()
    println("Check out ReDoc docs at http://localhost:7000/redoc")
    println("Check out Swagger UI docs at http://localhost:7000/swagger")
    println(
        de.huxhorn.sulky.ulid.ULID(
            java.security.SecureRandom(HdcApplication::javaClass.name.toByteArray())
        ).nextULID()
    )
    println(
        de.huxhorn.sulky.ulid.ULID(
            java.security.SecureRandom(HdcApplication::javaClass.name.toByteArray())
        ).nextULID()
    )
}
