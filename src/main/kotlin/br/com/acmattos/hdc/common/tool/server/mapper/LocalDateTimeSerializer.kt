package br.com.acmattos.hdc.common.tool.server.mapper

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * @author acmattos
 * @since 19/06/2020.
 */
class LocalDateTimeSerializer: StdSerializer<LocalDateTime>(
    LocalDateTime::class.java) {
    override fun serialize(value: LocalDateTime, gen: JsonGenerator, provider: SerializerProvider?) {
        gen.writeString(value.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
    }
}

/**
 * @author acmattos
 * @since 19/06/2020.
 */
class LocalDateTimeDeserializer: StdDeserializer<LocalDateTime>(LocalDateTime::class.java) {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext?): LocalDateTime {
        return p.readValueAs(String::class.java).let {
            LocalDateTime.parse(it, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        }
    }
}
