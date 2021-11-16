package br.com.acmattos.hdc.common.tool.assertion

import br.com.acmattos.hdc.common.exception.HdcGenericException
import br.com.acmattos.hdc.common.tool.assertion.AssertionLogEnum.ASSERTION
import br.com.acmattos.hdc.common.tool.assertion.AssertionLogEnum.FAILURE
import br.com.acmattos.hdc.common.tool.loggable.Loggable
import br.com.acmattos.hdc.common.tool.server.javalin.ErrorTrackerCode

/**
 * @author ACMattos
 * @since 12/06/2020.
 */
object Assertion: Loggable() {
    fun assert(
        message: String,
        context: String,
        code: ErrorTrackerCode,
        condition: () -> Boolean
    ) {
        logger.trace(
            "[{} {}] - Assertion to be evaluated...",
            context,
            ASSERTION.name
        )
        if(!condition()) {
            logger.info(
                "[{} {} {}]: -> '{}' <-",
                context,
                ASSERTION.name,
                FAILURE.name,
                message
            )
            throw AssertionFailedException(message, code)
        }
        logger.trace(
            "[{} {}] - Assertion evaluated successfully!",
            context,
            ASSERTION.name
        )
    }
}

/**
 * @author ACMattos
 * @since 31/10/2019.
 */
data class AssertionFailedException(
    override val message: String,
    val code: ErrorTrackerCode,
): HdcGenericException(message, code)
