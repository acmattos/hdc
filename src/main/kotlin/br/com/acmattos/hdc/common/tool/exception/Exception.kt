package br.com.acmattos.hdc.common.tool.exception

import br.com.acmattos.hdc.common.exception.HdcGenericException
import br.com.acmattos.hdc.common.tool.server.javalin.MessageTrackerId

/**
 * @author ACMattos
 * @since 30/06/2020.
 */
class InternalServerErrorException(
    message: String,
    val code: MessageTrackerId,
    throwable: Throwable
): HdcGenericException(message, code, throwable)
