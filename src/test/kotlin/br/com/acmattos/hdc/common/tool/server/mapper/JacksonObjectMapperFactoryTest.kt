package br.com.acmattos.hdc.common.tool.server.mapper

import com.fasterxml.jackson.databind.ObjectMapper
import io.kotest.core.spec.style.FreeSpec
import org.assertj.core.api.Assertions.assertThat

/**
 * @author ACMattos
 * @since 26/09/2021.
 */
class JacksonObjectMapperFactoryTest: FreeSpec({
    "Feature: ${JacksonObjectMapperFactory::class.java} usage" - {
        "Scenario: plugin configuration is done successfully" - {
            lateinit var factory: JacksonObjectMapperFactory
            lateinit var mapper: ObjectMapper
            "Given: a ${JacksonObjectMapperFactory::class.java}" {
                factory = JacksonObjectMapperFactory
            }
            "When: #build is executed" {
                mapper =  factory.build()
            }
            "Then: a mapper is returned" {
                assertThat(mapper).isNotNull()
            }
        }
    }
})
