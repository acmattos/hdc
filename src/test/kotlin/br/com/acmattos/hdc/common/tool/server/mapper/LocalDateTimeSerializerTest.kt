package br.com.acmattos.hdc.common.tool.server.mapper

import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializerProvider
import io.kotest.core.spec.style.FreeSpec
import org.assertj.core.api.Assertions.assertThat
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.io.StringWriter
import java.io.Writer
import java.nio.charset.StandardCharsets
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Month.SEPTEMBER

private val LOCAL_DATE_TIME = LocalDateTime.of(2021, SEPTEMBER, 27, 20, 27, 41)
private const val LOCAL_DATE_TIME_STRING = "\"2021-09-27T20:27:41\""

/**
 * @author ACMattos
 * @since 27/09/2021.
 */
class LocalDateTimeSerializerTest: FreeSpec({
    "Feature: ${LocalDateTimeSerializer::class.java} usage" - {
        "Scenario: serialization is done successfully" - {
            lateinit var serializer: LocalDateTimeSerializer
            lateinit var writer: Writer
            lateinit var generator: JsonGenerator
            lateinit var provider: SerializerProvider
            "Given: a ${LocalDateTimeSerializer::class.java}" {
                serializer = LocalDateTimeSerializer()
            }
            "And: a ${StringWriter::class.java}" {
                writer = StringWriter()
            }
            "And: a ${JsonGenerator::class.java}" {
                generator = JsonFactory().createGenerator(writer)
            }
            "And: a ${SerializerProvider::class.java}" {
                provider = JacksonObjectMapperFactory.build().serializerProvider
            }
            "When: #serialize is executed" {
                serializer.serialize(LOCAL_DATE_TIME, generator, provider)
            }
            "And: #flush is executed" {
                generator.flush()
            }
            "Then: a mapper is returned" {
                assertThat(writer.toString()).isEqualTo(LOCAL_DATE_TIME_STRING)
            }
        }
    }
})

/**
 * @author ACMattos
 * @since 27/09/2021.
 */
class LocalDateTimeDeserializerTest: FreeSpec({
    "Feature: ${LocalDateTimeDeserializer::class.java} usage" - {
        "Scenario: de-serialization is done successfully" - {
            lateinit var deserializer: LocalDateTimeDeserializer
            lateinit var json: String
            lateinit var stream: InputStream
            lateinit var mapper: ObjectMapper
            lateinit var parser: JsonParser
            lateinit var dsValue: LocalDateTime
            "Given: a ${LocalDateTimeDeserializer::class.java}" {
                deserializer = LocalDateTimeDeserializer()
            }
            "And: a String representation ($LOCAL_DATE_TIME_STRING) of a ${LocalDate::class.java}" {
                json = String.format(LOCAL_DATE_TIME_STRING)
            }
            "And: a ${InputStream::class.java} representation of the json string" {
                stream = ByteArrayInputStream(
                    json.toByteArray(StandardCharsets.UTF_8)
                )
            }
            "And: a ${ObjectMapper::class.java}" {
                mapper = JacksonObjectMapperFactory.build()
            }
            "And: a ${JsonParser::class.java} for the ${InputStream::class.java}" {
                parser = mapper.factory.createParser(stream)
            }
            "When: #deserialize is executed" {
                dsValue = deserializer.deserialize(parser, mapper.deserializationContext)
            }
            "Then: the deserialized value is equal to $LOCAL_DATE_TIME" {
                assertThat(dsValue).isEqualTo(LOCAL_DATE_TIME)
            }
        }
    }
})
