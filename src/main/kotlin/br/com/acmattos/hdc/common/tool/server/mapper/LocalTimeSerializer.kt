package br.com.acmattos.hdc.common.tool.server.mapper

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import java.time.LocalTime
import java.time.format.DateTimeFormatter

/**
 * @author ACMattos
 * @since 19/06/2020.
 */
class LocalTimeSerializer: StdSerializer<LocalTime>(LocalTime::class.java) {
    override fun serialize(value: LocalTime, gen: JsonGenerator, provider: SerializerProvider?) {
        gen.writeString(value.format(DateTimeFormatter.ofPattern("HH:mm")).toString())
    }
}

/**
 * @author ACMattos
 * @since 19/06/2020.
 */
class LocalTimeDeserializer: StdDeserializer<LocalTime>(LocalTime::class.java) {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext?): LocalTime {
        return p.readValueAs(String::class.java).let {
            LocalTime.parse(it, DateTimeFormatter.ISO_LOCAL_TIME)
        }
    }
}
