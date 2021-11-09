package br.com.acmattos.hdc.common.context.port.rest

import br.com.acmattos.hdc.common.context.domain.cqs.Query
import io.javalin.http.Context

/**
 * @author ACMattos
 * @since 29/06/2020.
 */
abstract class Request<T> {
    constructor()
    constructor(context: Context)
    abstract fun toType(who: String = "ANONYMOUS", what: String = "RESOURCE"): T
}
