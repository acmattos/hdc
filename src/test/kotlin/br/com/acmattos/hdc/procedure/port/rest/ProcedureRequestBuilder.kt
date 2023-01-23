package br.com.acmattos.hdc.procedure.port.rest

import br.com.acmattos.hdc.common.tool.server.javalin.ContextBuilder

/**
 * @author ACMattos
 * @since 20/03/2022.
 */
object ProcedureRequestBuilder {
    fun buildCreateDentalProcedureRequest() = ProcedureCreateRequest(
        81000014,
        "Procedure Description",
    )

    fun buildCreateDentalProcedureRequestInvalidCode() = ProcedureCreateRequest(
        8100001,
        "Procedure Description",
    )

    fun buildCreateDentalProcedureRequestInvalidDescription() = ProcedureCreateRequest(
        81000014,
        "Pr",
    )

    fun buildUpdateDentalProcedureRequest() = ProcedureUpdateRequest(
        "12345678901234567890123456",
        81000014,
        "Procedure Description",
        true
    )

    fun buildDeleteDentalProcedureRequest() = ProcedureDeleteRequest(
        getContext(
            matchedPath = "procedure_ids",
            matchedPathValue = "01FJJDJKDXN4K558FMCKEMQE6B;1"
        )
    )

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
