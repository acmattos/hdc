package br.com.acmattos.hdc.procedure.port.rest

import br.com.acmattos.hdc.common.tool.server.javalin.ContextBuilder

/**
 * @author ACMattos
 * @since 20/03/2022.
 */
object ProcedureRequestBuilder {

    fun buildFindAllProceduresRequest() = FindAllProceduresRequest(
        getContext(matchedPath = "procedure_id")
    )

    fun buildFindTheProcedureRequest() = FindTheProcedureRequest(
        getContext(matchedPath = "procedure_id")
    )

    private fun getContext(
        matchedPath: String = "procedure_id",
        matchedPathValue: String = "01FJJDJKDXN4K558FMCKEMQE6B"
    ) = ContextBuilder().mockContext(matchedPath, matchedPathValue)
}
