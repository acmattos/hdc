package br.com.acmattos.hdc.common.tool.server.mapper

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * @author acmattos
 * @since 19/06/2020.
 */
class LocalDateSerializer: StdSerializer<LocalDate>(LocalDate::class.java) {
    override fun serialize(value: LocalDate, gen: JsonGenerator, provider: SerializerProvider?) {
        gen.writeString(value.format(DateTimeFormatter.ISO_LOCAL_DATE))
    }
}

/**
 * @author acmattos
 * @since 19/06/2020.
 */
class LocalDateDeserializer: StdDeserializer<LocalDate>(LocalDate::class.java) {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext?): LocalDate {
        return p.readValueAs(String::class.java).let {
            LocalDate.parse(it, DateTimeFormatter.ISO_LOCAL_DATE)
        }
    }
}
