package br.com.acmattos.hdc.procedure.port.rest

import br.com.acmattos.hdc.common.tool.server.javalin.ContextBuilder

/**
 * @author ACMattos
 * @since 20/03/2022.
 */
object ProcedureRequestBuilder {
    fun buildCreateDentalProcedureRequest() = CreateDentalProcedureRequest(
        81000014,
        "Procedure Description",
    )

    fun buildCreateDentalProcedureRequestInvalidCode() = CreateDentalProcedureRequest(
        8100001,
        "Procedure Description",
    )

    fun buildCreateDentalProcedureRequestInvalidDescription() = CreateDentalProcedureRequest(
        81000014,
        "Pr",
    )

    fun buildUpdateDentalProcedureRequest() = UpdateDentalProcedureRequest(
        "12345678901234567890123456",
        81000014,
        "Procedure Description",
        true
    )

    fun buildDeleteDentalProcedureRequest() = DeleteDentalProcedureRequest(
        getContext()
    )

    fun buildFindAllProceduresRequest() = FindAllProceduresRequest(
        getContext()
    )

    fun buildFindTheProcedureRequest() = FindTheProcedureRequest(
        getContext()
    )

    private fun getContext() = ContextBuilder().mockContext(
        "procedure_id"
    )
}
