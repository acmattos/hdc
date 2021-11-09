package br.com.acmattos.hdc.common.context.port.persistence.mongodb

import br.com.acmattos.hdc.common.context.domain.cqs.Query
import org.bson.codecs.pojo.annotations.BsonId

/**
 * @author ACMattos
 * @since 02/11/2021.
 */
data class MdbQueryDocument(
    @BsonId val queryId: String,
    val query: Query
): MdbDocument() {
    constructor(query: Query): this(
        query.queryId.id,
        query
    )

    override fun toType(): Query = query
}
