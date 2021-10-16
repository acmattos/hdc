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
import java.time.Month.SEPTEMBER
import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

private val LOCAL_DATE = LocalDate.of(2021,SEPTEMBER, 27)
private const val LOCAL_DATE_STRING = "\"2021-09-27\""

/**
 * @author ACMattos
 * @since 27/09/2021.
 */
object LocalDateSerializerTest: Spek({
    Feature("${LocalDateSerializer::class.java} usage") {
        Scenario("serialization is done successfully") {
            lateinit var serializer: LocalDateSerializer
            lateinit var writer: Writer
            lateinit var generator: JsonGenerator
            lateinit var provider: SerializerProvider
            Given("""a ${LocalDateSerializer::class.java}""") {
                serializer = LocalDateSerializer()
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
                serializer.serialize(LOCAL_DATE, generator, provider)
            }
            And("""#flush is executed""") {
                generator.flush()
            }
            Then("""a mapper is returned""") {
                assertThat(writer.toString()).isEqualTo(LOCAL_DATE_STRING)
            }
        }
    }
})

/**
 * @author ACMattos
 * @since 27/09/2021.
 */
object LocalDateDeserializerTest: Spek({
    Feature("${LocalDateDeserializer::class.java} usage") {
        Scenario("de-serialization is done successfully") {
            lateinit var deserializer: LocalDateDeserializer
            lateinit var json: String
            lateinit var stream: InputStream
            lateinit var mapper: ObjectMapper
            lateinit var parser: JsonParser
            lateinit var dsValue: LocalDate
            Given("""a ${LocalDateDeserializer::class.java}""") {
                deserializer = LocalDateDeserializer()
            }
            And("""a String representation ($LOCAL_DATE_STRING) of a ${LocalDate::class.java}""") {
                json = String.format(LOCAL_DATE_STRING)
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
            Then("""the deserialized value is equal to $LOCAL_DATE""") {
                assertThat(dsValue).isEqualTo(LOCAL_DATE)
            }
        }
    }
})
