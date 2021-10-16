package br.com.acmattos.hdc.person.port.rest

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