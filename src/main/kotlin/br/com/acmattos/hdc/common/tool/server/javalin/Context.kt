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

/**
 * @author ACMattos
 * @since 11/04/2022.
 */
inline fun Context.filterParam(
    name: String
): String? = this.queryParam("f_$name")

/**
 * @author ACMattos
 * @since 25/09/2022.
 */
inline fun Context.sortParam(
    name: String
): String? = this.queryParam("s_$name")

/**
 * @author ACMattos
 * @since 11/04/2022.
 */
inline fun Context.pageNumber(): String? = this.queryParam("pn")

/**
 * @author ACMattos
 * @since 13/04/2022.
 */
inline fun Context.pageSize(): String? = this.queryParam("ps")
