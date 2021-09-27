package br.com.acmattos.hdc.common.tool.server.javalin

import br.com.acmattos.hdc.common.tool.assertion.AssertionFailedException
import br.com.acmattos.hdc.common.tool.config.PropHandler.getProperty
import br.com.acmattos.hdc.common.tool.loggable.Loggable
import br.com.acmattos.hdc.common.tool.server.prometheus.PrometheusServer
import com.fasterxml.jackson.databind.ObjectMapper
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.get
import io.javalin.apibuilder.ApiBuilder.path
import io.javalin.core.JavalinConfig
import io.javalin.http.Context
import io.javalin.http.NotFoundResponse
import io.javalin.plugin.json.JavalinJackson
import io.javalin.plugin.openapi.annotations.OpenApi
import java.util.function.Consumer
import org.eclipse.jetty.http.HttpStatus
import org.eclipse.jetty.server.handler.StatisticsHandler
import org.eclipse.jetty.util.thread.QueuedThreadPool

private const val START_TIME = "startTime"
private const val JAVALIN = "JAVALIN"
private const val ERROR = "ERROR"
private const val PATH_HEALTH_CHECK = "/health-check"

/**
 * @author ACMattos
 * @since 09/06/2020.
 */
class JavalinServer(
    private val javalinConfig: Consumer<JavalinConfig>?,
    private val routes: (() -> Unit),
    private val objectMapper: ObjectMapper,
    private val port: Int
){
    private var javalin: Javalin? = null
    private var prometheusServer: PrometheusServer? = null

    fun start(): JavalinServer {
        JavalinJackson.configure(objectMapper)
        javalin = if(javalinConfig == null) {
            Javalin.create()
        } else {
            Javalin.create(javalinConfig)
        }

        setupPrometheusServer()

        with(javalin!!) {
            routes {
                before { context ->
                    context.req.characterEncoding = Charsets.UTF_8.name()
                    context.res.characterEncoding = Charsets.UTF_8.name()
                    context.attribute(START_TIME, System.currentTimeMillis())
                }

                after { context ->
                    context.res.setHeader("Server", "HDCServer")
                    val duration = System.currentTimeMillis() -
                        context.attribute<Long>(START_TIME)!!
                    logger.info(
                        "[{}]: -> {} <-",
                        JAVALIN,
                        (createContextMessage(context) +
                            Pair("durationInMillis", duration)).toString()
                    )
                }
                path(PATH_HEALTH_CHECK) {
                    get(::healthCheck)
                }
                routes()
            }

            error(404) { context ->
                handleNotFoundException(context, NotFoundResponse())
            }

            exception(AssertionFailedException::class.java) { exception, context ->
                handleBadRequestException(context, exception)
            }
        }
        .logJavalinBanner()
        .start(port)
        return this
    }

    fun stop() {
        prometheusServer?.stop()
        javalin!!.stop()
    }

    @OpenApi(
        description = "Health Check Status",
        tags = ["healthCheck"]
    )
    private fun healthCheck(context: Context) =
        context.json(mapOf("version" to "1.0.0-SNAPSHOT"))

    private fun setupPrometheusServer() {
        if (isPrometheusServerEnabled()) {
            val statisticsHandler = StatisticsHandler()
            val queuedThreadPool = QueuedThreadPool(
                getProperty("JAVALIN_SERVER_MAX_THREADS", 300),
                getProperty("JAVALIN_SERVER_MIN_THREADS", 50),
                getProperty(
                    "JAVALIN_SERVER_THREAD_IDLE_TIMEOUT_IN_MILLIS",
                    60000
                )
            )
            prometheusServer = PrometheusServer(
                statisticsHandler = statisticsHandler,
                queuedThreadPool = queuedThreadPool
            ).start(
                getProperty(
                    "PROMETHEUS_SERVER_PORT",
                    this.port + 1
                )
            )
        }
    }

    private fun isPrometheusServerEnabled() =
        getProperty("PROMETHEUS_SERVER_ENABLED", false)

    private fun handleNotFoundException(context: Context, exception: Exception) {
        handleException(context, HttpStatus.NOT_FOUND_404, exception)
    }

    private fun handleBadRequestException(context: Context, exception: Exception) {
        handleException(context, HttpStatus.BAD_REQUEST_400, exception)
    }

    private fun handleException(context: Context, status:Int, exception: Exception) {
        val errorResponse = createErrorResponse(
            status,
            exception
        )
        setupErrorResponse(context, errorResponse, exception)
    }

    private fun createErrorResponse(status: Int, exception: Throwable) =
        Response.create(status, null, exception)

    private fun setupErrorResponse(
        context: Context,
        errorResponse: Response,
        exception: Exception
    ) {
        context.status(errorResponse.status)
        context.json(errorResponse)
        logger.warn(
            "[{} {}] - EUID:[{}] -> {} <-",
            exception,
            JAVALIN,
            ERROR,
            errorResponse.uid,
            createContextMessage(context).toString()
        )
    }

    private fun createContextMessage(context:Context) = mutableMapOf(
        "ip" to context.ip(),
        "method" to context.method(),
        "uri" to context.path(),
        "status" to context.status()
    )

    companion object: Loggable()
}

fun Javalin.logJavalinBanner(): Javalin {
    if (this.config.showJavalinBanner) {
        Javalin.log?.info(
        "\n" + """
          |        __  __ ____   ______  _____                                   
          |       / / / // __ \ / ____/ / ___/ ___   _____ _   __ ___   _____
          |      / /_/ // / / // /      \__ \ / _ \ / ___/| | / // _ \ / ___/
          |     / __  // /_/ // /___   ___/ //  __// /    | |/ //  __// /    
          |    /_/ /_//_____/ \____/  /____/ \___//_/     |___/ \___//_/
          |         
          |    Javalin Based Server - Version 0.0.1
          |""".trimMargin()
        )
        this.config.showJavalinBanner = false
    }
    return this
}