package br.com.acmattos.hdc.common.tool.server.javalin

import br.com.acmattos.hdc.common.context.domain.model.Id
import br.com.acmattos.hdc.common.tool.loggable.Loggable
import br.com.acmattos.hdc.common.tool.uid.ULIDGen
import io.swagger.v3.oas.annotations.media.Schema

const val UNKNOWN_REASON = "UNKNOWN REASON"

/**
 * @author ACMattos
 * @since 03/08/2021.
 */
@Schema(example = "{id=01FMMZJBC4Q2MJX8PESM4KVKSP, code:01FMMZJBC59FE3F55670TQCPE3, status: 200, data: {}}")//TODO Document response
data class Response(
    val uid: String,
    val code: String,
    val status: Int,
    val data: Any,
) {
    constructor():this("","", 0, Any())

    companion object: ULIDGen() {
        fun create(status: Int, data: Any, uid: String = nextULID()) =
            ResponseBuilder.build(uid = uid, status = status, data = data)

        fun create(
            code: MessageTrackerCode,
            status: Int,
            message: Any? = null,
            exception: Throwable?,
            uid: String = nextULID(),
        ) = ResponseBuilder.build(
            code = code,
            uid = uid,
            status = status,
            message = message,
            exception = exception
        )
    }
}

/**
 * @author ACMattos
 * @since 03/08/2021.
 */
private class ResponseBuilder {
    companion object: Loggable() {
        fun build(
            code: MessageTrackerCode = MessageTrackerCode("01FK6ADSETKTN1BYZW6BRHFZFJ"),//TODO SINGLE OR MULTIPLE OK?
            uid: String,
            status: Int,
            data: Any
        ) = Response(
            code = code.id,
            uid = uid,
            status = status,
            data = data
        ).also { response ->
            logger.info("Successful response created: [{}]", response.toString())
        }

        fun build(
            code: MessageTrackerCode,
            uid: String,
            status: Int,
            message: Any? = null,
            exception: Throwable?
        ) = Response(
            code = code.id,
            uid = uid,
            status = status,
            data = message
                ?: exception!!.localizedMessage
                ?: exception!!.message
                ?: UNKNOWN_REASON
        ).also { response ->
            logger.info("Error response created: [{}]", response.toString())
        }
    }
}

/**
 * @author ACMattos
 * @since 29/10/2021.
 */
class MessageTrackerCode(id: String): Id(id)
