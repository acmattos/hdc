package br.com.acmattos.hdc.common.tool.server.prometheus

import io.prometheus.client.Collector
import io.prometheus.client.CollectorRegistry
import org.eclipse.jetty.server.handler.StatisticsHandler
import java.util.Collections.emptyList

/**
 * @author ACMattos
 * @since 09/06/2020.
 */
class StatisticsHandlerCollector private constructor(
    private val statisticsHandler: StatisticsHandler
) : Collector() {
    override fun collect(): List<MetricFamilySamples> {
        return listOf(
            buildCounter(
                "jetty_requests_total",
                "Number of requests",
                statisticsHandler.requests.toDouble()
            ),
            buildGauge(
                "jetty_requests_active",
                "Number of requests currently active",
                statisticsHandler.requestsActive.toDouble()
            ),
            buildGauge(
                "jetty_requests_active_max",
                "Maximum number of requests that have been active at once",
                statisticsHandler.requestsActiveMax.toDouble()
            ),
            buildGauge(
                "jetty_request_time_max_seconds",
                "Maximum time spent handling requests",
                statisticsHandler.requestTimeMax / 1000.0
            ),
            buildCounter(
                "jetty_request_time_seconds_total",
                "Total time spent in all request handling",
                statisticsHandler.requestTimeTotal / 1000.0
            ),
            buildCounter(
                "jetty_dispatched_total",
                "Number of dispatches",
                statisticsHandler.dispatched.toDouble()
            ),
            buildGauge(
                "jetty_dispatched_active",
                "Number of dispatches currently active",
                statisticsHandler.dispatchedActive.toDouble()
            ),
            buildGauge(
                "jetty_dispatched_active_max",
                "Maximum number of active dispatches being handled",
                statisticsHandler.dispatchedActiveMax.toDouble()
            ),
            buildGauge(
                "jetty_dispatched_time_max",
                "Maximum time spent in dispatch handling",
                statisticsHandler.dispatchedTimeMax.toDouble()
            ),
            buildCounter(
                "jetty_dispatched_time_seconds_total",
                "Total time spent in dispatch handling",
                statisticsHandler.dispatchedTimeTotal / 1000.0
            ),
            buildCounter(
                "jetty_async_requests_total",
                "Total number of async requests",
                statisticsHandler.asyncRequests.toDouble()
            ),
            buildGauge(
                "jetty_async_requests_waiting",
                "Currently waiting async requests",
                statisticsHandler.asyncRequestsWaiting.toDouble()
            ),
            buildGauge(
                "jetty_async_requests_waiting_max",
                "Maximum number of waiting async requests",
                statisticsHandler.asyncRequestsWaitingMax.toDouble()
            ),
            buildCounter(
                "jetty_async_dispatches_total",
                "Number of requested that have been asynchronously dispatched",
                statisticsHandler.asyncDispatches.toDouble()
            ),
            buildCounter(
                "jetty_expires_total",
                "Number of async requests requests that have expired",
                statisticsHandler.expires.toDouble()
            ),
            buildStatusCounter(),
            buildGauge(
                "jetty_stats_seconds",
                "Time in seconds stats have been collected for",
                statisticsHandler.statsOnMs / 1000.0
            ),
            buildCounter(
                "jetty_responses_bytes_total",
                "Total number of bytes across all responses",
                statisticsHandler.responsesBytesTotal.toDouble()
            )
        )
    }

    private fun buildStatusCounter(): MetricFamilySamples {
        val name = "jetty_responses_total"
        return MetricFamilySamples(
            name,
            Type.COUNTER,
            "Number of requests with response status",
            listOf(
                buildStatusSample(
                    name,
                    "1xx",
                    statisticsHandler.responses1xx.toDouble()
                ),
                buildStatusSample(
                    name,
                    "2xx",
                    statisticsHandler.responses2xx.toDouble()
                ),
                buildStatusSample(
                    name,
                    "3xx",
                    statisticsHandler.responses3xx.toDouble()
                ),
                buildStatusSample(
                    name,
                    "4xx",
                    statisticsHandler.responses4xx.toDouble()
                ),
                buildStatusSample(
                    name,
                    "5xx",
                    statisticsHandler.responses5xx.toDouble()
                )
            )
        )
    }

    companion object {
        fun initialize(
            statisticsHandler: StatisticsHandler,
            enableDefaultRegistry: Boolean = true
        ): StatisticsHandlerCollector {
            val statisticsHandlerCollector = StatisticsHandlerCollector(
                statisticsHandler
            )
            if(enableDefaultRegistry) {
                statisticsHandlerCollector.register<StatisticsHandlerCollector>()
            } else {
                statisticsHandlerCollector.register<StatisticsHandlerCollector>(CollectorRegistry())
            }
            return statisticsHandlerCollector
        }

        fun finalize() {
            CollectorRegistry.defaultRegistry.clear()
        }

        private fun buildGauge(name: String, help: String, value: Double): MetricFamilySamples {
            return MetricFamilySamples(
                name,
                Type.GAUGE,
                help,
                listOf(
                    MetricFamilySamples.Sample(
                        name,
                        emptyList(),
                        emptyList(),
                        value
                    )
                )
            )
        }

        private fun buildCounter(name: String, help: String, value: Double): MetricFamilySamples {
            return MetricFamilySamples(
                name,
                Type.COUNTER,
                help,
                listOf(
                    MetricFamilySamples.Sample(
                        name,
                        emptyList(),
                        emptyList(),
                        value
                    )
                )
            )
        }

        private fun buildStatusSample(
            name: String,
            status: String,
            value: Double
        ): MetricFamilySamples.Sample {
            return MetricFamilySamples.Sample(
                name, listOf("code"), listOf(status),
                value
            )
        }
    }
}