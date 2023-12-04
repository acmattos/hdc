package br.com.acmattos.hdc.user.port.rest

import br.com.acmattos.hdc.common.context.config.ContextLogEnum.ENDPOINT
import br.com.acmattos.hdc.common.context.domain.cqs.CommandHandler
import br.com.acmattos.hdc.common.context.domain.model.AuditLog
import br.com.acmattos.hdc.common.context.port.rest.Request
import br.com.acmattos.hdc.common.tool.loggable.Loggable
import br.com.acmattos.hdc.common.tool.server.javalin.Response
import br.com.acmattos.hdc.common.tool.server.javalin.getRequest
import br.com.acmattos.hdc.user.config.UserLogEnum.USER
import br.com.acmattos.hdc.user.domain.cqs.UserCommand
import br.com.acmattos.hdc.user.domain.cqs.UserCreateCommand
import br.com.acmattos.hdc.user.domain.cqs.UserDeleteCommand
import br.com.acmattos.hdc.user.domain.cqs.UserEvent
import br.com.acmattos.hdc.user.domain.cqs.UserUpdateCommand
import io.javalin.http.Context
import org.eclipse.jetty.http.HttpStatus

/**
 * @author ACMattos
 * @since 22/11/2023.
 */
class UserCommandController(
    private val handler: CommandHandler<UserEvent>
) {
//    @OpenApi(
//        summary = "Create user",
//        operationId = "createUser",
//        tags = ["User"],
//        requestBody = OpenApiRequestBody(
//            [OpenApiContent(UserCreateRequest::class)],
//            true,
//            "UserCreateRequest Sample"
//        ),
//        responses = [
//            OpenApiResponse("201",[
//                OpenApiContent(Response::class)
//            ]),
//            OpenApiResponse("400", [OpenApiContent(Response::class)])
//        ],
//        method = HttpMethod.POST
//    )
    fun create(context: Context) {
        logger.debug(
            "[{} {}] - Create user...",
            USER.name,
            ENDPOINT.name
        )
        context.bodyValidator<UserCreateRequest>()
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
            "[{} {}] - User created successfully!",
            USER.name,
            ENDPOINT.name
        )
    }

//    @OpenApi(
//        summary = "Update user",
//        operationId = "updateUser",
//        tags = ["User"],
//        requestBody = OpenApiRequestBody(
//            [OpenApiContent(UserUpdateRequest::class)],
//            true,
//            "UserUpdateRequest Sample"
//        ),
//        responses = [
//            OpenApiResponse("200",[
//                OpenApiContent(Response::class)
//            ]),
//            OpenApiResponse("400", [OpenApiContent(Response::class)])
//        ],
//        method = HttpMethod.POST
//    )
    fun update(context: Context) {
        logger.debug(
            "[{} {}] - Update user...",
            USER.name,
            ENDPOINT.name
        )
        context.bodyValidator<UserUpdateRequest>()
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
            "[{} {}] - User updated successfully!",
            USER.name,
            ENDPOINT.name
        )
    }

//    @OpenApi(
//        summary = "Delete user",
//        operationId = "deleteUser",
//        tags = ["User"],
//        requestBody = OpenApiRequestBody(
//            [OpenApiContent(UserDeleteRequest::class)],
//            true,
//            "UserDeleteRequest Sample"
//        ),
//        responses = [
//            OpenApiResponse("200",[
//                OpenApiContent(Response::class)
//            ]),
//            OpenApiResponse("400", [OpenApiContent(Response::class)])
//        ],
//        method = HttpMethod.POST
//    )
    fun delete(context: Context) {
        logger.debug(
            "[{} {}] - Delete user...",
            USER.name,
            ENDPOINT.name
        )
        context.getRequest(::UserDeleteRequest)
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
            "[{} {}] - User deleted successfully!",
            USER.name,
            ENDPOINT.name
        )
    }

    companion object: Loggable()
}

/**
 * @author ACMattos
 * @since 29/11/2023.
 */
data class UserCreateRequest(
    val name: String,
    val username: String,
    val password: String,
    val email: String,
//    val roles: Set<Role>,
    val enabled: Boolean,
): Request<UserCommand>() {
    override fun toType(who: String, what: String): UserCommand =
        UserCreateCommand(
            name,
            username,
            password,
            email,
            //    val roles: Set<Role>,
            enabled,
            AuditLog(who = who, what = what)
        )
}

/**
 * @author ACMattos
 * @since 30/11/2023.
 */
data class UserUpdateRequest(
    val userId: String,
    val name: String?,
    val username: String?,
    val password: String?,
    val email: String?,
//    val roles: Set<Role>,
    val enabled: Boolean?,
): Request<UserCommand>() {
    override fun toType(who: String, what: String): UserCommand =
        UserUpdateCommand(
            userId,
            name,
            username,
            password,
            email,
           //    val roles: Set<Role>,
            enabled,
            AuditLog(who = who, what = what)
        )
}

/**
 * @author ACMattos
 * @since 26/03/2022.
 */
data class UserDeleteRequest(
    val context: Context
): Request<UserCommand>(context) {
    override fun toType(who: String, what: String): UserCommand {
        val userId = context.pathParam("user_id")// TODO ASSERT VALUES
        return UserDeleteCommand(
            userId,
            AuditLog(who = who, what = what)
        )
    }
}
