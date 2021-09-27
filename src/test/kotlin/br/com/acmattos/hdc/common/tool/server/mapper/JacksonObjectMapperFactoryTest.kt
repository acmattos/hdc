package br.com.acmattos.hdc.common.tool.server.mapper

import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

/**
 * @author ACMattos
 * @since 26/09/2021.
 */
object JacksonObjectMapperFactoryTest: Spek({
    Feature("${JacksonObjectMapperFactory::class.java} usage") {
        Scenario("plugin configuration is done successfully") {
            lateinit var factory: JacksonObjectMapperFactory
            lateinit var mapper: ObjectMapper
            Given("""a ${JacksonObjectMapperFactory::class.java}""") {
                factory = JacksonObjectMapperFactory
            }
            When("""#build is executed""") {
                mapper =  factory.build()//bbgetConfiguredPlugin()
            }
            Then("""a mapper is returned""") {
                assertThat(mapper).isNotNull()
            }
        }
    }
})
