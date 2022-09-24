//package br.com.acmattos.hdc.treatment.port.rest
//
//import br.com.acmattos.hdc.common.context.port.rest.EndpointLogEnum.ENDPOINT
//import br.com.acmattos.hdc.common.tool.loggable.Loggable
//import br.com.acmattos.hdc.common.tool.server.javalin.Response
//import io.javalin.http.Context
//import io.javalin.plugin.openapi.annotations.HttpMethod
//import io.javalin.plugin.openapi.annotations.OpenApi
//import io.javalin.plugin.openapi.annotations.OpenApiContent
//import io.javalin.plugin.openapi.annotations.OpenApiRequestBody
//import io.javalin.plugin.openapi.annotations.OpenApiResponse
//import org.eclipse.jetty.http.HttpStatus
//
///**
// * @author ACMattos
// * @since 10/08/2022.
// */
//class TreatmentCommandController(
//    //private val handler: CommandHandler<TreatmentEvent>
//) {
//    @OpenApi(
//        summary = "Create dental treatment",
//        operationId = "createDentalTreatment",
//        tags = ["Treatment"],
//        requestBody = OpenApiRequestBody(
//            [OpenApiContent(CreateDentalTreatmentRequest::class)],
//            true,
//            "CreateDentalTreatmentRequest Sample"
//        ),
//        responses = [
//            OpenApiResponse("201",[
//                OpenApiContent(Response::class)
//            ]),
//            OpenApiResponse("400", [OpenApiContent(Response::class)])
//        ],
//        method = HttpMethod.POST
//    )
//    fun createDentalTreatmente(context: Context) {
//        logger.debug(
//            "[{} {}] - Create dental treatment...",
//            TREATMENT.name,
//            ENDPOINT.name
//        )
//        context.bodyValidator<CreateDentalTreatmentRequest>()
//            .get()
//            .toType(what = context.fullUrl())
//            .also { command ->
//                context.status(HttpStatus.CREATED_201).json(
//                    Response.create(
//                        context.status(),
//                        handler.handle(command)
//                    )
//                )
//            }
//        logger.info(
//            "[{} {}] - Dental treatment created successfully!",
//            TREATMENT.name,
//            ENDPOINT.name
//        )
//    }
//
////    @OpenApi(
////        summary = "Update dental procedure",
////        operationId = "updateDentalProcedure",
////        tags = ["Procedure"],
////        requestBody = OpenApiRequestBody(
////            [OpenApiContent(UpdateDentalProcedureRequest::class)],
////            true,
////            "UpdateDentalProcedureRequest Sample"
////        ),
////        responses = [
////            OpenApiResponse("200",[
////                OpenApiContent(Response::class)
////            ]),
////            OpenApiResponse("400", [OpenApiContent(Response::class)])
////        ],
////        method = HttpMethod.POST
////    )
////    fun updateDentalProcedure(context: Context) {
////        logger.debug(
////            "[{} {}] - Update dental procedure...",
////            PROCEDURE.name,
////            ENDPOINT.name
////        )
////        context.bodyValidator<UpdateDentalProcedureRequest>()
////            .get()
////            .toType(what = context.fullUrl())
////            .also { command ->
////                context.status(HttpStatus.OK_200).json(
////                    Response.create(
////                        context.status(),
////                        handler.handle(command)
////                    )
////                )
////            }
////        logger.info(
////            "[{} {}] - Dental procedure updated successfully!",
////            PROCEDURE.name,
////            ENDPOINT.name
////        )
////    }
////
////    @OpenApi(
////        summary = "Delete dental procedure",
////        operationId = "deleteDentalProcedure",
////        tags = ["Procedure"],
////        requestBody = OpenApiRequestBody(
////            [OpenApiContent(DeleteDentalProcedureRequest::class)],
////            true,
////            "DeleteDentalProcedureRequest Sample"
////        ),
////        responses = [
////            OpenApiResponse("200",[
////                OpenApiContent(Response::class)
////            ]),
////            OpenApiResponse("400", [OpenApiContent(Response::class)])
////        ],
////        method = HttpMethod.POST
////    )
////    fun deleteDentalProcedure(context: Context) {
////        logger.debug(
////            "[{} {}] - Delete dental procedure...",
////            PROCEDURE.name,
////            ENDPOINT.name
////        )
////        context.getRequest(br.com.acmattos.hdc.procedure.port.rest::DeleteDentalProcedureRequest)
////            .toType(what = context.fullUrl())
////            .also { command ->
////                context.status(HttpStatus.OK_200).json(
////                    Response.create(
////                        context.status(),
////                        handler.handle(command)
////                    )
////                )
////            }
////        logger.info(
////            "[{} {}] - Dental procedure deleted successfully!",
////            PROCEDURE.name,
////            ENDPOINT.name
////        )
////    }
//
//    companion object: Loggable()
//}
//
/////**
//// * @author ACMattos
//// * @since 19/03/2022.
//// */
////data class CreateDentalTreatmentRequest(
////    val code: Int,
////    val description: String
////): Request<ProcedureCommand>() {
////    override fun toType(who: String, what: String): ProcedureCommand =
////        CreateDentalProcedureCommand(
////            code,
////            description,
////            AuditLog(who = who, what = what)
////        )
////}
////
/////**
//// * @author ACMattos
//// * @since 24/03/2022.
//// */
////data class UpdateDentalProcedureRequest(
////    val procedureId: String,
////    val code: Int,
////    val description: String,
////    val enabled: Boolean
////): Request<ProcedureCommand>() {
////    override fun toType(who: String, what: String): ProcedureCommand =
////        UpdateDentalProcedureCommand(
////            procedureId,
////            code,
////            description,
////            enabled,
////            AuditLog(who = who, what = what)
////        )
////}
////
/////**
//// * @author ACMattos
//// * @since 26/03/2022.
//// */
////data class DeleteDentalProcedureRequest(
////    val context: Context
////): Request<ProcedureCommand>(context) {
////    override fun toType(who: String, what: String): ProcedureCommand {
////        val procedureId = context.pathParam("procedure_id")
////        return DeleteDentalProcedureCommand(
////            procedureId,
////            AuditLog(who = who, what = what)
////        )
////    }
////}