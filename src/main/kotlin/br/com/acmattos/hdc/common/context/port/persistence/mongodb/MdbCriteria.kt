package br.com.acmattos.hdc.common.context.port.persistence.mongodb

import org.bson.conversions.Bson

/**
 * @author ACMattos
 * @since 21/11/2021.
 */
interface MdbCriteria {
    val filters: Bson
}
