package br.com.acmattos.hdc.common.tool.server.prometheus

import br.com.acmattos.hdc.common.tool.HttpClient
import com.github.kittinunf.fuel.core.Response
import io.javalin.plugin.openapi.annotations.ContentType
import org.assertj.core.api.Assertions.assertThat
import org.eclipse.jetty.server.handler.StatisticsHandler
import org.eclipse.jetty.util.thread.QueuedThreadPool
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

/**
 * @author ACMattos
 * @since 09/06/2020.
 */
object PrometheusServerTest : Spek({
    Feature("${PrometheusServer::class.java} usage") {
        val port = 7777
        lateinit var statisticsHandler: StatisticsHandler
        lateinit var queuedThreadPool: QueuedThreadPool
        lateinit var prometheusServer: PrometheusServer

        afterEachScenario { prometheusServer.stop() }

        Scenario("${PrometheusServer::class.java} successfully started usage") {
            lateinit var response: Response
            Given("""a statistics handler""") {
                statisticsHandler = StatisticsHandler()
            }
            And("""a queued thread pool""") {
                queuedThreadPool = QueuedThreadPool(300,50,60000)
            }
            And("""a prometheus server instantiated and started""") {
                prometheusServer = PrometheusServer(
                    statisticsHandler = statisticsHandler,
                    queuedThreadPool = queuedThreadPool
                ).start(port)
            }
            When("""a HTTP GET connection can be done to prometheus""") {
                response = HttpClient.port(port).get("")
                StatisticsHandlerCollector.finalize()
                QueuedThreadPoolCollector.finalize()
            }
            Then("""response status is 200 OK""") {
                assertThat(response.statusCode).isEqualTo(200)
            }
            And("""the response body contains # TYPE jetty_queued_thread_pool_threads gauge"""){
                assertThat(response.body().asString(ContentType.JSON))
                    .contains("# TYPE jetty_queued_thread_pool_threads gauge")
            }
        }

        Scenario("${PrometheusServer::class.java} successfully stopped usage") {
            lateinit var response: Response
            Given("""a statistics handler""") {
                statisticsHandler = StatisticsHandler()
            }
            And("""a queued thread pool""") {
                queuedThreadPool = QueuedThreadPool(300,50,60000)
            }
            And("""a prometheus server instantiated" and stopped""") {
                prometheusServer = PrometheusServer(
                    statisticsHandler = statisticsHandler,
                    queuedThreadPool = queuedThreadPool
                ).start(port).stop()
            }
            When("""an HTTP connection can be done to prometheus""") {
                response = HttpClient.port(port).get("")
            }
            Then("""response status is -1 (Fuel Status)""") {
                assertThat(response.statusCode).isEqualTo(-1)
            }
            And(""" the response body is empty"""){
                assertThat(response.body().isEmpty()).isTrue()
            }
        }
    }
})