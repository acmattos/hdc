package br.com.acmattos.hdc.common.tool.server.prometheus

import io.mockk.every
import io.mockk.mockk
import io.prometheus.client.Collector
import org.assertj.core.api.Assertions.assertThat
import org.eclipse.jetty.util.thread.QueuedThreadPool
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

/**
 * @author ACMattos
 * @since 09/06/2020.
 */
object QueuedThreadPoolCollectorTest : Spek({
    Feature("${QueuedThreadPoolCollector::class.java} usage") {
        beforeEachScenario { QueuedThreadPoolCollector.finalize() }
        Scenario("queued thread pool collector successfully initialized") {
            lateinit var pool: QueuedThreadPool
            lateinit var collector: QueuedThreadPoolCollector
            lateinit var samples: List<Collector.MetricFamilySamples>

            Given("""a queued thread pool""") {
                pool = mockk<QueuedThreadPool>(relaxed = true)
            }
            And("""a queued thread pool collector for a set of metrics""") {
                collector = QueuedThreadPoolCollector.initialize(
                    queuedThreadPool = pool,
                    enableDefaultRegistry = false
                )
            }
            When("""an collect operation is done""") {
                samples = collector.collect()
            }
            Then("""the queued thread pool has 10 max threads""") {
                every { pool.maxThreads } returns 10
            }
            And(""" the list of ${Collector.MetricFamilySamples::class.java} has size 4"""){
                assertThat(samples).hasSize(4)
            }
            And(""" the type GAUGE has size 4"""){
                val samplesByType = samples.groupBy { it.type }
                assertThat(samplesByType[Collector.Type.GAUGE]).hasSize(4)
            }
        }
    }
})
