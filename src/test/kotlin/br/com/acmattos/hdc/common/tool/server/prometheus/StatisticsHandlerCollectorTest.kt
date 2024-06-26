package br.com.acmattos.hdc.common.tool.server.prometheus

import io.kotest.core.spec.style.FreeSpec
import io.mockk.mockk
import io.prometheus.client.Collector
import org.assertj.core.api.Assertions.assertThat
import org.eclipse.jetty.server.handler.StatisticsHandler

/**
 * @author ACMattos
 * @since 09/06/2020.
 */
class StatisticsHandlerCollectorTest : FreeSpec({
    "Feature: ${StatisticsHandlerCollector::class.java} usage" - {
        "Scenario: statistics handler collector successfully initialized" - {
            lateinit var handler: StatisticsHandler
            lateinit var collector: StatisticsHandlerCollector
            lateinit var samples: List<Collector.MetricFamilySamples>
            "Given: a statistics handler" {
                handler = mockk(relaxed = true)
            }
            "And: a statistics handler collector for a set of metrics" {
                collector = StatisticsHandlerCollector.initialize(
                    statisticsHandler = handler,
                    enableDefaultRegistry = false
                )
            }
            "When: an collect operation is done" {
                samples = collector.collect()
            }
            "Then:  the list of ${Collector.MetricFamilySamples::class.java} has size 18"{
                assertThat(samples).hasSize(18)
            }
            "And:  the type GAUGE has size 9"{
                val samplesByType = samples.groupBy { it.type }
                assertThat(samplesByType[Collector.Type.GAUGE]).hasSize(9)
                StatisticsHandlerCollector.finalize()
            }
        }
        StatisticsHandlerCollector.finalize()
    }
})