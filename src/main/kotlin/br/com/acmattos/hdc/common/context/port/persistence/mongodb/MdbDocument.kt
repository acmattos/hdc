package br.com.acmattos.hdc.common.context.port.persistence.mongodb

/**
 * @author ACMattos
 * @since 27/07/2019.
 */
abstract class MdbDocument {
    abstract fun toType(): Any
}
