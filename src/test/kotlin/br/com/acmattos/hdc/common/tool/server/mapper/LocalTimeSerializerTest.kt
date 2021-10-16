package br.com.acmattos.hdc.common.tool.server.mapper

import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializerProvider
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.io.StringWriter
import java.io.Writer
import java.nio.charset.StandardCharsets
import java.time.LocalDate
import java.time.LocalTime
import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

private val LOCAL_TIME = LocalTime.of(20, 27)
private const val LOCAL_TIME_STRING = "\"20:27\""

/**
 * @author ACMattos
 * @since 27/09/2021.
 */
object LocalTimeSerializerTest: Spek({
    Feature("${LocalTimeSerializer::class.java} usage") {
        Scenario("serialization is done successfully") {
            lateinit var serializer: LocalTimeSerializer
            lateinit var writer: Writer
            lateinit var generator: JsonGenerator
            lateinit var provider: SerializerProvider
            Given("""a ${LocalTimeSerializer::class.java}""") {
                serializer = LocalTimeSerializer()
            }
            And("""a ${StringWriter::class.java}""") {
                writer = StringWriter()
            }
            And("""a ${JsonGenerator::class.java}""") {
                generator = JsonFactory().createGenerator(writer)
            }
            And("""a ${SerializerProvider::class.java}""") {
                provider = JacksonObjectMapperFactory.build().serializerProvider
            }
            When("""#serialize is executed""") {
                serializer.serialize(LOCAL_TIME, generator, provider)
            }
            And("""#flush is executed""") {
                generator.flush()
            }
            Then("""a mapper is returned""") {
                assertThat(writer.toString()).isEqualTo(LOCAL_TIME_STRING)
            }
        }
    }
})

/**
 * @author ACMattos
 * @since 27/09/2021.
 */
object LocalTimeDeserializerTest: Spek({
    Feature("${LocalTimeDeserializer::class.java} usage") {
        Scenario("de-serialization is done successfully") {
            lateinit var deserializer: LocalTimeDeserializer
            lateinit var json: String
            lateinit var stream: InputStream
            lateinit var mapper: ObjectMapper
            lateinit var parser: JsonParser
            lateinit var dsValue: LocalTime
            Given("""a ${LocalTimeDeserializer::class.java}""") {
                deserializer = LocalTimeDeserializer()
            }
            And("""a String representation ($LOCAL_TIME_STRING) of a ${LocalDate::class.java}""") {
                json = String.format(LOCAL_TIME_STRING)
            }
            And("""a ${InputStream::class.java} representation of the json string""") {
                stream = ByteArrayInputStream(
                    json.toByteArray(StandardCharsets.UTF_8)
                )
            }
            And("""a ${ObjectMapper::class.java}""") {
                mapper = JacksonObjectMapperFactory.build()
            }
            And("""a ${JsonParser::class.java} for the ${InputStream::class.java}""") {
                parser = mapper.factory.createParser(stream)
            }
            When("""#deserialize is executed""") {
                dsValue = deserializer.deserialize(parser, mapper.deserializationContext)
            }
            Then("""the deserialized value is equal to $LOCAL_TIME""") {
                assertThat(dsValue).isEqualTo(LOCAL_TIME)
            }
        }
    }
})
