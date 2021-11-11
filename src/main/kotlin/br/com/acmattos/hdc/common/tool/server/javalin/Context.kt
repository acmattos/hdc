package br.com.acmattos.hdc.common.tool.server.javalin

import br.com.acmattos.hdc.common.context.port.rest.Request
import io.javalin.http.Context

/**
 * @author ACMattos
 * @since 08/11/2021.
 */
inline fun <reified T: Request<*>> Context.getRequest(
    createNew: (Context) -> T
): T {
    return createNew(this)
}
