//package br.com.acmattos.hdc.person.port.rest
//
//import br.com.acmattos.hdc.common.tool.server.javalin.ContextBuilder
//import io.javalin.http.Context
//import io.javalin.http.util.ContextUtil
//import io.mockk.mockk
//import javax.servlet.http.HttpServletRequest
//import javax.servlet.http.HttpServletResponse
//
///**
// * @author ACMattos
// * @since 16/10/2021.
// */
//object PersonRequestBuilder {
//    fun buildCreateADentistRequest() = CreateADentistRequest(
//        "fullName",
//             "cpf",
//        "personalId",
//        listOf(AddressRequest(
//            "street",
//            "number",
//            "complement",
//            "zipCode",
//            "neighborhood",
//            "RJ",
//            "city",
//            )
//        ),
//        listOf(ContactRequest("info", "type")),
//    )
//
//    fun buildFindTheDentistRequest() = FindTheDentistRequest(
//        ContextBuilder().mockContext("dentist_id")
//    )
//}
