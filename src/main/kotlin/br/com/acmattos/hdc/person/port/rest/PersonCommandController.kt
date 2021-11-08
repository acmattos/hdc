package br.com.acmattos.hdc.person.port.rest

import br.com.acmattos.hdc.common.context.domain.cqs.CommandHandler
import br.com.acmattos.hdc.common.context.domain.model.AuditLog
import br.com.acmattos.hdc.common.context.port.rest.EndpointLogEnum.ENDPOINT
import br.com.acmattos.hdc.common.context.port.rest.Request
import br.com.acmattos.hdc.common.tool.loggable.Loggable
import br.com.acmattos.hdc.common.tool.server.javalin.Response
import br.com.acmattos.hdc.person.config.PersonLogEnum.DENTIST
import br.com.acmattos.hdc.person.domain.cqs.CreateADentistCommand
import br.com.acmattos.hdc.person.domain.cqs.PersonCommand
import br.com.acmattos.hdc.person.domain.cqs.PersonEvent
import br.com.acmattos.hdc.person.domain.model.Address
import br.com.acmattos.hdc.person.domain.model.Contact
import br.com.acmattos.hdc.person.domain.model.State
import io.javalin.http.Context
import io.javalin.plugin.openapi.annotations.HttpMethod
import io.javalin.plugin.openapi.annotations.OpenApi
import io.javalin.plugin.openapi.annotations.OpenApiContent
import io.javalin.plugin.openapi.annotations.OpenApiRequestBody
import io.javalin.plugin.openapi.annotations.OpenApiResponse
import org.eclipse.jetty.http.HttpStatus

/**
 * @author ACMattos
 * @since 30/06/2019.
 */
class PersonCommandController(
    private val handler: CommandHandler<PersonEvent>
) {
    @OpenApi(
        summary = "Create a dentist",
        operationId = "createADentist",
        tags = ["Dentist"],
        requestBody = OpenApiRequestBody(
            content = [OpenApiContent(from = CreateADentistRequest::class)],
            required = true,
            description = "CreateADentistRequest Sample"
        ),
        responses = [
            OpenApiResponse("201",[
                OpenApiContent(Response::class)
            ]),
            OpenApiResponse("400", [OpenApiContent(Response::class)])
        ],
        method = HttpMethod.POST
    )
    fun createADentist(context: Context) {
        logger.debug(
            "[{} {}] - Creating a dentist...",
            DENTIST.name,
            ENDPOINT.name
        )
        context.bodyValidator<CreateADentistRequest>()
        .get()
        .toType(what = context.fullUrl())
        .also { command ->
            context.status(HttpStatus.CREATED_201).json(
                Response.create(
                    context.status(),
                    handler.handle(command)
                )
            )
        }
        logger.info(
            "[{} {}] - Creating a dentist... -> !DONE! <-",
            DENTIST.name,
            ENDPOINT.name
        )
    }

    companion object: Loggable()
}

/**
 * @author ACMattos
 * @since 28/06/2020.
 */
data class CreateADentistRequest(
    val fullName: String,
    val cpf: String,
    val personalId: String?,
    val addresses: List<AddressRequest>,
    val contacts: List<ContactRequest>,
): Request<PersonCommand>() {
    override fun toType(who: String, what: String): PersonCommand =
        CreateADentistCommand(
            fullName,
            cpf,
            personalId,
            addresses.map { it.toType() },
            contacts.map { it.toType() },
            AuditLog(who = who, what = what)
        )
}

/**
 * @author ACMattos
 * @since 30/09/2021.
 */
data class AddressRequest (
    val street: String,
    val number: String,
    val complement: String? = null,
    val zipCode: String,
    val neighborhood: String,
    val state: String,
    val city: String,
): Request<Address>() {
    override fun toType(who: String, what: String): Address =
        Address(
            street,
            number,
            complement,
            zipCode,
            neighborhood,
            State.convert(state),
            city
        )
}

/**
 * @author ACMattos
 * @since 30/09/2021.
 */
data class ContactRequest(
    val info: String,
    val type: String? = null,
): Request<Contact>() {
    override fun toType(who: String, what: String): Contact =
        Contact(info, type)
}
