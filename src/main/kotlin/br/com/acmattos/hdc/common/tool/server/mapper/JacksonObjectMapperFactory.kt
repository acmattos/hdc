package br.com.acmattos.hdc.common.tool.server.mapper

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

/**
 * @author ACMattos
 * @since 19/06/2020.
 */
object JacksonObjectMapperFactory {
    private val jacksonObjectMapper: ObjectMapper = jacksonObjectMapper()

    fun build() = jacksonObjectMapper.apply {
        propertyNamingStrategy = PropertyNamingStrategy.SNAKE_CASE
        setSerializationInclusion(JsonInclude.Include.NON_NULL)
        configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        registerModules( buildDateTypeSimpleModule())
        disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
    }

    private fun buildDateTypeSimpleModule() = SimpleModule()
        .apply {
            addSerializer(LocalTime::class.java, LocalTimeSerializer())
            addDeserializer(LocalTime::class.java, LocalTimeDeserializer())

            addSerializer(LocalDate::class.java, LocalDateSerializer())
            addDeserializer(LocalDate::class.java, LocalDateDeserializer())

            addSerializer(LocalDateTime::class.java, LocalDateTimeSerializer())
            addDeserializer(LocalDateTime::class.java, LocalDateTimeDeserializer())
        }
}
