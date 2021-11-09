package br.com.acmattos.hdc.person.port.rest

import io.javalin.http.Context
import io.javalin.http.util.ContextUtil
import io.mockk.mockk
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * @author ACMattos
 * @since 16/10/2021.
 */
object PersonRequestBuilder {
    fun buildCreateADentistRequest() = CreateADentistRequest(
        "fullName",
             "cpf",
        "personalId",
        listOf(AddressRequest(
            "street",
            "number",
            "complement",
            "zipCode",
            "neighborhood",
            "RJ",
            "city",
            )
        ),
        listOf(ContactRequest("info", "type")),
    )

    fun buildFindTheDentistRequest() = FindTheDentistRequest(mockContext())

    private fun mockContext(): Context {
        val req = mockk<HttpServletRequest>()
        val res = mockk<HttpServletResponse>()
        val context: Context = ContextUtil.init(
            req,
            res,
            "dentist_id",
            mapOf("dentist_id" to "01FJJDJKDXN4K558FMCKEMQE6B")
        )

        return context
    }
}
