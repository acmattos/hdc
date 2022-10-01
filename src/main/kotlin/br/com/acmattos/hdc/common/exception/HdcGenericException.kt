package br.com.acmattos.hdc.common.exception

import br.com.acmattos.hdc.common.tool.server.javalin.MessageTrackerId

/**
 * @author ACMattos
 * @since 26/10/2021.
 */
open class HdcGenericException : RuntimeException {
    constructor(message: String?, code: MessageTrackerId): super(message)
    constructor(message: String?, code: MessageTrackerId, throwable: Throwable):
        super(message, throwable)
}
