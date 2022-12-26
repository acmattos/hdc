package br.com.acmattos.hdc.odontogram.port.rest

import br.com.acmattos.hdc.common.tool.server.javalin.ContextBuilder

/**
 * @author ACMattos
 * @since 20/03/2022.
 */
object OdontogramRequestBuilder {
    fun buildGetAnOdontogramRequest() = GetAnOdontogramRequest(
        getContext()
    )

    private fun getContext() = ContextBuilder().mockContext()
}
