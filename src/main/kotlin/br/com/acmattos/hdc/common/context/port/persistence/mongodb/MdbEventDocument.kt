package br.com.acmattos.hdc.common.context.port.persistence.mongodb

import br.com.acmattos.hdc.common.context.domain.cqs.Event
import br.com.acmattos.hdc.common.context.domain.model.Entity
import org.bson.codecs.pojo.annotations.BsonId

/**
 * @author ACMattos
 * @since 30/06/2020.
 */
data class MdbEventDocument(
    @BsonId val eventId: String,
    val event: Event
): MdbDocument() {
    constructor(event: Event): this(
        event.eventId.id,
        event
    )

    override fun toType(): Entity = event
}
