package br.com.acmattos.hdc.common.exception

import br.com.acmattos.hdc.common.tool.server.javalin.MessageTrackerCode

/**
 * @author ACMattos
 * @since 26/10/2021.
 */
open class HdcGenericException : RuntimeException {
    constructor(message: String?, code: MessageTrackerCode): super(message)
    constructor(message: String?, code: MessageTrackerCode, throwable: Throwable):
        super(message, throwable)
}
