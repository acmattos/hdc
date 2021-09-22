package br.com.acmattos.hdc.common.tool.server.prometheus

import io.prometheus.client.CollectorRegistry
import io.prometheus.client.exporter.HTTPServer
import org.eclipse.jetty.server.handler.StatisticsHandler
import org.eclipse.jetty.util.thread.QueuedThreadPool

/**
 * @author ACMattos
 * @since 09/06/2020.
 */
class PrometheusServer(
    private val statisticsHandler: StatisticsHandler,
    private val queuedThreadPool: QueuedThreadPool
){
    private lateinit var httpServer: HTTPServer

    fun start(port: Int): PrometheusServer {
        StatisticsHandlerCollector.initialize(statisticsHandler)
        QueuedThreadPoolCollector.initialize(queuedThreadPool)
        httpServer = HTTPServer(port)
        return this
    }

    fun stop() : PrometheusServer {
        StatisticsHandlerCollector.finalize()
        QueuedThreadPoolCollector.finalize()
        httpServer.close()
        CollectorRegistry.defaultRegistry.clear()
        return this
    }
}