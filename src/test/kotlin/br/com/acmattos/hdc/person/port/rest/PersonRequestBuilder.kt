package br.com.acmattos.hdc.person.port.rest

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
        listOf(ContactRequest("info", "type",)),
    )
}