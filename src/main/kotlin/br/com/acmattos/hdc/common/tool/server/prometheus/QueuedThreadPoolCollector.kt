package br.com.acmattos.hdc.common.tool.server.prometheus

import io.prometheus.client.Collector
import io.prometheus.client.CollectorRegistry
import org.eclipse.jetty.util.thread.QueuedThreadPool
import java.util.Collections.emptyList

/**
 * @author ACMattos
 * @since 09/06/2020.
 */
class QueuedThreadPoolCollector private constructor(
    private val queuedThreadPool: QueuedThreadPool
) : Collector() {
    override fun collect(): List<MetricFamilySamples> {
        return listOf(
            buildGauge(
                "jetty_queued_thread_pool_threads",
                "Number of total threads",
                queuedThreadPool.threads.toDouble()
            ),
            buildGauge(
                "jetty_queued_thread_pool_utilization",
                "Percentage of threads in use",
                queuedThreadPool.threads.toDouble() / queuedThreadPool.maxThreads
            ),
            buildGauge(
                "jetty_queued_thread_pool_threads_idle",
                "Number of idle threads",
                queuedThreadPool.idleThreads.toDouble()
            ),
            buildGauge(
                "jetty_queued_thread_pool_jobs",
                "Number of total jobs",
                queuedThreadPool.queueSize.toDouble()
            )
        )
    }

    companion object {
        fun initialize(
            queuedThreadPool: QueuedThreadPool,
            enableDefaultRegistry: Boolean = true
        ): QueuedThreadPoolCollector {
            val queuedThreadPoolCollector = QueuedThreadPoolCollector(
                queuedThreadPool
            )
            if(enableDefaultRegistry) {
                queuedThreadPoolCollector.register<QueuedThreadPoolCollector>()
            } else {
                queuedThreadPoolCollector.register<QueuedThreadPoolCollector>(CollectorRegistry())
            }
            return queuedThreadPoolCollector
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
    }
}