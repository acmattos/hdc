package br.com.acmattos.hdc.common.exception

import br.com.acmattos.hdc.common.tool.server.javalin.ErrorTrackerCode

/**
 * @author ACMattos
 * @since 26/10/2021.
 */
open class HdcGenericException : RuntimeException {
    constructor(message: String?, code: ErrorTrackerCode): super(message)
    constructor(message: String?, code: ErrorTrackerCode, throwable: Throwable):
        super(message, throwable)
}
