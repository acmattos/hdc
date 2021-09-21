package br.com.acmattos.hdc.common.tool.server.javalin

import br.com.acmattos.hdc.common.tool.logable.Loggable
import br.com.acmattos.hdc.common.tool.uid.ULIDGen

const val UNKNOWN_REASON = "UNKNOWN REASON"

/**
 * @author ACMattos
 * @since 03/08/2021.
 */
data class Response(
    val uid: String,
    val status: Int,
    val data: Any,
) {
    companion object: ULIDGen() {
        fun create(status: Int, data: Any, uid: String = nextULID()) =
            ResponseBuilder.build(uid, status, data)

        fun create(
            status: Int,
            message: Any? = null,
            exception: Throwable?,
            uid: String = nextULID(),
        ) = ResponseBuilder.build(uid, status, message, exception)
    }
}

/**
 * @author ACMattos
 * @since 03/08/2021.
 */
private class ResponseBuilder {
    companion object: Loggable() {
        fun build(
            uid: String,
            status: Int,
            data: Any
        ) = Response(
                uid = uid,
                status = status,
                data = data
            ).also { response ->
                logger.info("Successful response created: [{}]", response.toString())
            }

        fun build(
            uid: String,
            status: Int,
            message: Any? = null,
            exception: Throwable?
        ) = Response(
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