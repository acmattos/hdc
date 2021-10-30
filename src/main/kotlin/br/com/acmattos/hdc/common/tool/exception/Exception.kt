package br.com.acmattos.hdc.common.tool.exception

import br.com.acmattos.hdc.common.exception.HdcGenericException
import br.com.acmattos.hdc.common.tool.server.javalin.ErrorTrackerCode

/**
 * @author ACMattos
 * @since 30/06/2020.
 */
class InternalServerErrorException(
    message: String,
    val code: ErrorTrackerCode,
    throwable: Throwable
): HdcGenericException(message, code, throwable)

///**
// * @author ACMattos
// * @since 16/08/2021.
// */
//class NotFoundException(
//    message: String,
//    code: String
//): HdcGenericException(message, code)