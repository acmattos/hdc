package br.com.acmattos.hdc.common.context.port.rest

/**
 * @author ACMattos
 * @since 29/06/2020.
 */
interface Request<T> {
    fun toType(who: String = "ANONYMOUS", what: String = "RESOURCE"): T
}
