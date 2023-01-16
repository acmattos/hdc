package br.com.acmattos.hdc.person.port.rest

import br.com.acmattos.hdc.common.context.config.ContextLogEnum.ENDPOINT
import br.com.acmattos.hdc.common.context.domain.cqs.CommandHandler
import br.com.acmattos.hdc.common.context.domain.model.AuditLog
import br.com.acmattos.hdc.common.context.port.rest.Request
import br.com.acmattos.hdc.common.tool.loggable.Loggable
import br.com.acmattos.hdc.common.tool.server.javalin.Response
import br.com.acmattos.hdc.person.config.PersonLogEnum.PATIENT
import br.com.acmattos.hdc.person.domain.cqs.CreatePatientCommand
import br.com.acmattos.hdc.person.domain.cqs.PersonCommand
import br.com.acmattos.hdc.person.domain.cqs.PersonEvent
import br.com.acmattos.hdc.person.domain.cqs.UpdatePatientCommand
import br.com.acmattos.hdc.person.domain.model.Address
import br.com.acmattos.hdc.person.domain.model.Contact
import br.com.acmattos.hdc.person.domain.model.ContactType
import br.com.acmattos.hdc.person.domain.model.DentalPlan
import br.com.acmattos.hdc.person.domain.model.PersonId
import br.com.acmattos.hdc.person.domain.model.State
import br.com.acmattos.hdc.procedure.port.rest.ProcedureUpdateRequest
import io.javalin.http.Context
import io.javalin.plugin.openapi.annotations.HttpMethod
import io.javalin.plugin.openapi.annotations.OpenApi
import io.javalin.plugin.openapi.annotations.OpenApiContent
import io.javalin.plugin.openapi.annotations.OpenApiRequestBody
import io.javalin.plugin.openapi.annotations.OpenApiResponse
import java.time.LocalDate
import java.time.LocalDateTime
import org.eclipse.jetty.http.HttpStatus

/**
 * @author ACMattos
 * @since 30/06/2019.
 */
class PersonCommandController(
    private val handler: CommandHandler<PersonEvent>
) {
//    @OpenApi(
//        summary = "Create a dentist",
//        operationId = "createADentist",
//        tags = ["Dentist"],
//        requestBody = OpenApiRequestBody(
//            content = [OpenApiContent(from = CreateADentistRequest::class)],
//            required = true,
//            description = "CreateADentistRequest Sample"
//        ),
//        responses = [
//            OpenApiResponse("201",[
//                OpenApiContent(Response::class)
//            ]),
//            OpenApiResponse("400", [OpenApiContent(Response::class)])
//        ],
//        method = HttpMethod.POST
//    )
//    fun createADentist(context: Context) {
//        logger.debug(
//            "[{} {}] - Creating a dentist...",
//            DENTIST.name,
//            ENDPOINT.name
//        )
//        context.bodyValidator<CreateADentistRequest>()
//        .get()
//        .toType(what = context.fullUrl())
//        .also { command ->
//            context.status(HttpStatus.CREATED_201).json(
//                Response.create(
//                    context.status(),
//                    handler.handle(command)
//                )
//            )
//        }
//        logger.info(
//            "[{} {}] - Creating a dentist... -> !DONE! <-",
//            DENTIST.name,
//            ENDPOINT.name
//        )
//    }

    @OpenApi(
        summary = "Create a patient",
        operationId = "createPatient",
        tags = ["Patient"],
        requestBody = OpenApiRequestBody(
            content = [OpenApiContent(from = CreatePatientRequest::class)],
            required = true,
            description = "CreatePatientRequest Sample"
        ),
        responses = [
            OpenApiResponse("201",[
                OpenApiContent(Response::class)
            ]),
            OpenApiResponse("400", [OpenApiContent(Response::class)])
        ],
        method = HttpMethod.POST
    )
    fun createPatient(context: Context) {
        logger.debug(
            "[{} {}] - Creating patient...",
            PATIENT.name,
            ENDPOINT.name
        )
        context.bodyValidator<CreatePatientRequest>()
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
            "[{} {}] - Creating patient... -> !DONE! <-",
            PATIENT.name,
            ENDPOINT.name
        )
    }

    @OpenApi(
        summary = "Update the patient",
        operationId = "updatePatient",
        tags = ["Patient"],
        requestBody = OpenApiRequestBody(
            [OpenApiContent(ProcedureUpdateRequest::class)],
            true,
            "UpdatePatientRequest Sample"
        ),
        responses = [
            OpenApiResponse("200", [
                OpenApiContent(Response::class)
            ]),
            OpenApiResponse("400", [OpenApiContent(Response::class)])
        ],
        method = HttpMethod.POST
    )
    fun updatePatient(context: Context) { //TODO TEST
        logger.debug(
            "[{} {}] - Update patient...",
            PATIENT.name,
            ENDPOINT.name
        )
        context.bodyValidator<UpdatePatientRequest>()
            .get()
            .toType(what = context.fullUrl())
            .also { command ->
                context.status(HttpStatus.OK_200).json(
                    Response.create(
                        context.status(),
                        handler.handle(command)
                    )
                )
            }
        logger.info(
            "[{} {}] - Patient updated successfully!",
            PATIENT.name,
            ENDPOINT.name
        )
    }

//    @OpenApi(
//        summary = "Delete dental procedure",
//        operationId = "deleteDentalProcedure",
//        tags = ["Procedure"],
//        requestBody = OpenApiRequestBody(
//            [OpenApiContent(DeleteDentalProcedureRequest::class)],
//            true,
//            "DeleteDentalProcedureRequest Sample"
//        ),
//        responses = [
//            OpenApiResponse("200",[
//                OpenApiContent(Response::class)
//            ]),
//            OpenApiResponse("400", [OpenApiContent(Response::class)])
//        ],
//        method = HttpMethod.POST
//    )
//    fun deleteDentalProcedure(context: Context) {
//        logger.debug(
//            "[{} {}] - Delete dental procedure...",
//            PROCEDURE.name,
//            ENDPOINT.name
//        )
//        context.getRequest(br.com.acmattos.hdc.procedure.port.rest::DeleteDentalProcedureRequest)
//            .toType(what = context.fullUrl())
//            .also { command ->
//                context.status(HttpStatus.OK_200).json(
//                    Response.create(
//                        context.status(),
//                        handler.handle(command)
//                    )
//                )
//            }
//        logger.info(
//            "[{} {}] - Dental procedure deleted successfully!",
//            PROCEDURE.name,
//            ENDPOINT.name
//        )
//    }

    companion object: Loggable()
}

///**
// * @author ACMattos
// * @since 28/06/2020.
// */
//data class CreateADentistRequest(
//    val fullName: String,
//    val cpf: String,
//    val personalId: String?,
//    val addresses: List<AddressRequest>,
//    val contacts: List<ContactRequest>,
//): Request<PersonCommand>() {
//    override fun toType(who: String, what: String): PersonCommand =
//        CreateADentistCommand(
//            fullName,
//            cpf,
//            personalId,
//            addresses.map { it.toType() },
//            contacts.map { it.toType() },
//            AuditLog(who = who, what = what)
//        )
//}

/**
 * @author ACMattos
 * @since 21/04/2022.
 */
data class CreatePatientRequest(
    val fullName: String,
    val dob: LocalDate,
    val cpf: String,
    val maritalStatus: String,
    val gender: String,
    val personalId: String?,
    val occupation: String?,
    val addresses: List<AddressRequest>,
    val contacts: List<ContactRequest>,
    val dentalPlan: DentalPlanRequest?,
    val responsibleFor: PersonId?,
    val indicatedBy: String?,
    val familyGroup: List<String>?,
    val status: String,
    val lastAppointment: LocalDate?,
): Request<PersonCommand>() {
    override fun toType(who: String, what: String): PersonCommand =
        CreatePatientCommand(
            fullName,
            dob,
            cpf,
            maritalStatus,
            gender,
            personalId,
            occupation,
            addresses.map { it.toType() },
            contacts.map { it.toType() },
            dentalPlan?.toType(),
            responsibleFor,
            indicatedBy,
            familyGroup,
            status,
            lastAppointment,
            AuditLog(who = who, what = what)
        )
}

/**
 * @author ACMattos
 * @since 23/05/2022.
 */
data class UpdatePatientRequest(
    val personId: String,
    val fullName: String,
    val dob: LocalDate,
    val cpf: String,
    val maritalStatus: String,
    val gender: String,
    val personalId: String?,
    val occupation: String?,
    val addresses: List<AddressRequest>,
    val contacts: List<ContactRequest>,
    val dentalPlan: DentalPlanRequest?,
    val responsibleFor: PersonId?,
    val indicatedBy: String?,
    val familyGroup: List<PersonId>,
    val status: String,
    val lastAppointment: LocalDate?,
    val enabled: Boolean
): Request<PersonCommand>() {
    override fun toType(who: String, what: String): PersonCommand =
        UpdatePatientCommand(
            personId,
            fullName,
            dob,
            cpf,
            maritalStatus,
            gender,
            personalId,
            occupation,
            addresses.map { it.toType() },
            contacts.map { it.toType() },
            dentalPlan?.toType(),
            responsibleFor,
            indicatedBy,
            familyGroup,
            status,
            lastAppointment,
            enabled,
            AuditLog(who = who, what = what)
        )
}
//
///**
// * @author ACMattos
// * @since 26/03/2022.
// */
//data class DeleteDentalProcedureRequest(
//    val context: Context
//): Request<ProcedureCommand>(context) {
//    override fun toType(who: String, what: String): ProcedureCommand {
//        val procedureId = context.pathParam("procedure_id")
//        return DeleteDentalProcedureCommand(
//            procedureId,
//            AuditLog(who = who, what = what)
//        )
//    }
//}

/**
 * @author ACMattos
 * @since 30/09/2021.
 */
data class AddressRequest (
    val street: String,
    val number: String?,
    val complement: String?,
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
    val type: String,
    val obs: String?,
): Request<Contact>() {
    override fun toType(who: String, what: String): Contact =
        Contact(
            info,
            ContactType.convert(type),
            obs
        )
}

/**
 * @author ACMattos
 * @since 21/04/2022.
 */
data class DentalPlanRequest(
    val name: String,
    val number: String,
): Request<DentalPlan>() {
    override fun toType(who: String, what: String): DentalPlan =
        DentalPlan(
            name,
            number, LocalDateTime.now(), null
        )
}